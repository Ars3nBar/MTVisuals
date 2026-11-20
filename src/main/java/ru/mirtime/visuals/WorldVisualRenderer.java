package ru.mirtime.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorldVisualRenderer {

    private record JumpCircle(Vec3 pos, long spawnTime) {}
    private static final List<JumpCircle> circles = new ArrayList<>();
    private static boolean wasOnGround = true;

    public static void renderWorld(WorldRenderContext context) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;

        // Отслеживание прыжка для Jump Circles
        if (wasOnGround && !player.onGround() && player.getDeltaMovement().y > 0.1) {
            circles.add(new JumpCircle(player.position(), System.currentTimeMillis()));
        }
        wasOnGround = player.onGround();

        PoseStack poseStack = context.matrixStack();
        if (poseStack == null) return;
        Vec3 cam = context.camera().getPosition();
        float tickDelta = context.tickCounter().getGameTimeDeltaPartialTick(true);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.disableDepthTest();
        RenderSystem.setShader(CoreShaders.POSITION_COLOR);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);

        poseStack.pushPose();
        poseStack.translate(-cam.x, -cam.y, -cam.z);
        Matrix4f mat = poseStack.last().pose();

        int c1 = VisualConfig.getPrimaryColor();
        int c2 = VisualConfig.getSecondaryColor();

        // 1. Неоновые 3D-хитбоксы сущностей
        if (VisualConfig.customHitboxes) {
            for (Entity ent : mc.level.entitiesForRendering()) {
                if (ent == player || !(ent instanceof LivingEntity)) continue;
                if (ent.distanceToSqr(player) > 2500) continue;
                AABB box = ent.getBoundingBox();
                drawBox(buffer, mat, box, c1);
            }
        }

        // 2. Jump Circles (кольца при прыжке)
        if (VisualConfig.jumpCircles) {
            long now = System.currentTimeMillis();
            Iterator<JumpCircle> it = circles.iterator();
            while (it.hasNext()) {
                JumpCircle jc = it.next();
                float age = (now - jc.spawnTime) / 1000f;
                if (age > 1.2f) { it.remove(); continue; }
                float radius = 0.3f + age * 1.4f;
                int alpha = (int) ((1.0f - age / 1.2f) * 255);
                int col = (alpha << 24) | (c1 & 0x00FFFFFF);
                drawCircle(buffer, mat, jc.pos.x, jc.pos.y + 0.05, jc.pos.z, radius, col);
            }
        }
        // 3. 3D-аксессуары на игроке (видны от 3-го лица / в инвентаре)
        if (!mc.options.getCameraType().isFirstPerson()) {
            double px = player.xo + (player.getX() - player.xo) * tickDelta;
            double py = player.yo + (player.getY() - player.yo) * tickDelta;
            double pz = player.zo + (player.getZ() - player.zo) * tickDelta;

            // Вращающийся неоновый Нимб над головой
            if (VisualConfig.haloHat) {
                double headY = py + player.getBbHeight() + 0.25 + Math.sin(System.currentTimeMillis() / 300.0) * 0.04;
                drawCircle(buffer, mat, px, headY, pz, 0.38f, c1);
                drawCircle(buffer, mat, px, headY + 0.03, pz, 0.32f, c2);
            }

            // Анимированные 3D-крылья за спиной
            if (VisualConfig.cyberWings) {
                float yawRad = (float) Math.toRadians(-player.yBodyRot);
                double flap = Math.sin(System.currentTimeMillis() / 180.0) * 0.25;
                for (int side : new int[]{-1, 1}) {
                    for (int line = 0; line < 5; line++) {
                        double len = 0.7 + line * 0.12;
                        double angle = yawRad + side * (0.6 + flap);
                        double wx = px - Math.sin(angle) * len;
                        double wz = pz - Math.cos(angle) * len;
                        double wy = py + 1.35 + (2 - line) * 0.14;

                        addLine(buffer, mat, (float) px, (float) (py + 1.3), (float) pz, (float) wx, (float) wy, (float) wz, line % 2 == 0 ? c1 : c2);
                    }
                }
            }
        }

        poseStack.popPose();
        MeshData mesh = buffer.build();
        if (mesh != null) {
            BufferUploader.drawWithShader(mesh);
        }

        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private static void drawCircle(BufferBuilder buf, Matrix4f mat, double cx, double cy, double cz, float r, int color) {
        int segs = 36;
        for (int i = 0; i < segs; i++) {
            double a1 = 2 * Math.PI * i / segs;
            double a2 = 2 * Math.PI * (i + 1) / segs;
            addLine(buf, mat,
                    (float) (cx + Math.cos(a1) * r), (float) cy, (float) (cz + Math.sin(a1) * r),
                    (float) (cx + Math.cos(a2) * r), (float) cy, (float) (cz + Math.sin(a2) * r), color);
        }
    }

    private static void drawBox(BufferBuilder buf, Matrix4f mat, AABB b, int col) {
        float x1 = (float) b.minX, y1 = (float) b.minY, z1 = (float) b.minZ;
        float x2 = (float) b.maxX, y2 = (float) b.maxY, z2 = (float) b.maxZ;
        addLine(buf, mat, x1, y1, z1, x2, y1, z1, col);
        addLine(buf, mat, x2, y1, z1, x2, y1, z2, col);
        addLine(buf, mat, x2, y1, z2, x1, y1, z2, col);
        addLine(buf, mat, x1, y1, z2, x1, y1, z1, col);
        addLine(buf, mat, x1, y2, z1, x2, y2, z1, col);
        addLine(buf, mat, x2, y2, z1, x2, y2, z2, col);
        addLine(buf, mat, x2, y2, z2, x1, y2, z2, col);
        addLine(buf, mat, x1, y2, z2, x1, y2, z1, col);
        addLine(buf, mat, x1, y1, z1, x1, y2, z1, col);
        addLine(buf, mat, x2, y1, z1, x2, y2, z1, col);
        addLine(buf, mat, x2, y1, z2, x2, y2, z2, col);
        addLine(buf, mat, x1, y1, z2, x1, y2, z2, col);
    }

    private static void addLine(BufferBuilder buf, Matrix4f mat, float x1, float y1, float z1, float x2, float y2, float z2, int col) {
        int a = (col >> 24) & 0xFF;
        if (a == 0) a = 255;
        int r = (col >> 16) & 0xFF, g = (col >> 8) & 0xFF, b = col & 0xFF;
        buf.addVertex(mat, x1, y1, z1).setColor(r, g, b, a);
        buf.addVertex(mat, x2, y2, z2).setColor(r, g, b, a);
    }
}
