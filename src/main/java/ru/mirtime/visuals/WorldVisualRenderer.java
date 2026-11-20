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
    private record TrailPoint(Vec3 pos, long time) {}

    private static final List<JumpCircle> circles = new ArrayList<>();
    private static final List<TrailPoint> trailPoints = new ArrayList<>();
    private static boolean wasOnGround = true;

    public static void renderWorld(WorldRenderContext context) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;

        long now = System.currentTimeMillis();

        // Отслеживание прыжка
        if (wasOnGround && !player.onGround() && player.getDeltaMovement().y > 0.1) {
            circles.add(new JumpCircle(player.position(), now));
        }
        wasOnGround = player.onGround();

        // Запись точек для шлейфа (Trails)
        if (VisualConfig.trails) {
            if (trailPoints.isEmpty() || trailPoints.get(trailPoints.size() - 1).pos.distanceToSqr(player.position()) > 0.01) {
                trailPoints.add(new TrailPoint(player.position(), now));
            }
            trailPoints.removeIf(tp -> now - tp.time > 900L);
        } else {
            trailPoints.clear();
        }

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

        // 1. TargetESP (сканер-кольцо вокруг цели под прицелом)
        if (VisualConfig.targetEsp && mc.crosshairPickEntity instanceof LivingEntity target) {
            double tx = target.xo + (target.getX() - target.xo) * tickDelta;
            double ty = target.yo + (target.getY() - target.yo) * tickDelta;
            double tz = target.zo + (target.getZ() - target.zo) * tickDelta;
            double wave = (Math.sin(now / 250.0) + 1.0) * 0.5 * target.getBbHeight();
            drawCircle(buffer, mat, tx, ty + wave, tz, 0.65f, c1);
            drawCircle(buffer, mat, tx, ty + wave + 0.05, tz, 0.72f, c2);
        }

        // 2. Неоновые 3D-хитбоксы сущностей
        if (VisualConfig.customHitboxes) {
            for (Entity ent : mc.level.entitiesForRendering()) {
                if (ent == player || !(ent instanceof LivingEntity)) continue;
                if (ent.distanceToSqr(player) > 2500) continue;
                drawBox(buffer, mat, ent.getBoundingBox(), c1);
            }
        }

        // 3. Неоновый шлейф (Trails)
        if (VisualConfig.trails && trailPoints.size() > 1) {
            for (int i = 0; i < trailPoints.size() - 1; i++) {
                TrailPoint p1 = trailPoints.get(i);
                TrailPoint p2 = trailPoints.get(i + 1);
                float ratio = (float) i / trailPoints.size();
                int alpha = (int) (ratio * 220);
                int col = (alpha << 24) | ((i % 2 == 0 ? c1 : c2) & 0x00FFFFFF);
                addLine(buffer, mat, (float) p1.pos.x, (float) (p1.pos.y + 0.2), (float) p1.pos.z, (float) p2.pos.x, (float) (p2.pos.y + 0.2), (float) p2.pos.z, col);
                addLine(buffer, mat, (float) p1.pos.x, (float) (p1.pos.y + 1.1), (float) p1.pos.z, (float) p2.pos.x, (float) (p2.pos.y + 1.1), (float) p2.pos.z, col);
            }
        }

        // 4. Jump Circles (двойные кольца при прыжке)
        if (VisualConfig.jumpCircles) {
            Iterator<JumpCircle> it = circles.iterator();
            while (it.hasNext()) {
                JumpCircle jc = it.next();
                float age = (now - jc.spawnTime) / 1000f;
                if (age > 1.2f) { it.remove(); continue; }
                float radius = 0.3f + age * 1.4f;
                int alpha = (int) ((1.0f - age / 1.2f) * 255);
                drawCircle(buffer, mat, jc.pos.x, jc.pos.y + 0.05, jc.pos.z, radius, (alpha << 24) | (c1 & 0x00FFFFFF));
                drawCircle(buffer, mat, jc.pos.x, jc.pos.y + 0.08, jc.pos.z, radius * 0.75f, (alpha << 24) | (c2 & 0x00FFFFFF));
            }
        }
        // 5. 3D-Аксессуары на игроке (видны от 3-го лица / в инвентаре)
        if (!mc.options.getCameraType().isFirstPerson()) {
            double px = player.xo + (player.getX() - player.xo) * tickDelta;
            double py = player.yo + (player.getY() - player.yo) * tickDelta;
            double pz = player.zo + (player.getZ() - player.zo) * tickDelta;
            float yawRad = (float) Math.toRadians(-player.yBodyRot);

            // ШАПКИ (1 = Нимб, 2 = Кибер-Шляпа ChinaHat, 3 = Корона Императора)
            double headTop = py + player.getBbHeight() + 0.12;
            if (VisualConfig.hatMode == 1) {
                double floatY = headTop + 0.15 + Math.sin(now / 300.0) * 0.04;
                drawCircle(buffer, mat, px, floatY, pz, 0.38f, c1);
                drawCircle(buffer, mat, px, floatY + 0.03, pz, 0.32f, c2);
            } else if (VisualConfig.hatMode == 2) {
                // Конусная азиатская кибер-шляпа (ChinaHat)
                int segs = 24;
                float r = 0.58f;
                for (int i = 0; i < segs; i++) {
                    double a1 = 2 * Math.PI * i / segs;
                    double a2 = 2 * Math.PI * (i + 1) / segs;
                    float x1 = (float) (px + Math.cos(a1) * r), z1 = (float) (pz + Math.sin(a1) * r);
                    float x2 = (float) (px + Math.cos(a2) * r), z2 = (float) (pz + Math.sin(a2) * r);
                    addLine(buffer, mat, x1, (float) headTop, z1, x2, (float) headTop, z2, c1);
                    addLine(buffer, mat, x1, (float) headTop, z1, (float) px, (float) (headTop + 0.28), (float) pz, c2);
                }
            } else if (VisualConfig.hatMode == 3) {
                // Корона с зубцами
                int points = 8;
                float r = 0.34f;
                for (int i = 0; i < points; i++) {
                    double a1 = 2 * Math.PI * i / points + (now / 1500.0);
                    double a2 = 2 * Math.PI * (i + 1) / points + (now / 1500.0);
                    double aMid = (a1 + a2) * 0.5;
                    float x1 = (float) (px + Math.cos(a1) * r), z1 = (float) (pz + Math.sin(a1) * r);
                    float x2 = (float) (px + Math.cos(a2) * r), z2 = (float) (pz + Math.sin(a2) * r);
                    float xm = (float) (px + Math.cos(aMid) * (r + 0.04)), zm = (float) (pz + Math.sin(aMid) * (r + 0.04));
                    addLine(buffer, mat, x1, (float) headTop, z1, x2, (float) headTop, z2, c1);
                    addLine(buffer, mat, x1, (float) headTop, z1, xm, (float) (headTop + 0.22), zm, c2);
                    addLine(buffer, mat, xm, (float) (headTop + 0.22), zm, x2, (float) headTop, z2, c2);
                }
            }

            // СПИНА (1 = Неоновые Крылья, 2 = Физический Плащ, 3 = Две Катаны)
            if (VisualConfig.backMode == 1) {
                double flap = Math.sin(now / 180.0) * 0.25;
                for (int side : new int[]{-1, 1}) {
                    for (int line = 0; line < 6; line++) {
                        double len = 0.75 + line * 0.12;
                        double angle = yawRad + side * (0.65 + flap);
                        double wx = px - Math.sin(angle) * len;
                        double wz = pz - Math.cos(angle) * len;
                        double wy = py + 1.38 + (2 - line) * 0.14;
                        addLine(buffer, mat, (float) px, (float) (py + 1.3), (float) pz, (float) wx, (float) wy, (float) wz, line % 2 == 0 ? c1 : c2);
                    }
                }
            } else if (VisualConfig.backMode == 2) {
                // Физический развевающийся плащ
                double speed = player.getDeltaMovement().horizontalDistance();
                double sway = 0.25 + Math.min(0.8, speed * 3.0) + Math.sin(now / 200.0) * 0.08;
                double backX = -Math.sin(yawRad), backZ = -Math.cos(yawRad);
                double rightX = Math.cos(yawRad), rightZ = -Math.sin(yawRad);
                float topY = (float) (py + 1.42), botY = (float) (py + 0.35);

                float tlX = (float) (px + backX * 0.18 - rightX * 0.28), tlZ = (float) (pz + backZ * 0.18 - rightZ * 0.28);
                float trX = (float) (px + backX * 0.18 + rightX * 0.28), trZ = (float) (pz + backZ * 0.18 + rightZ * 0.28);
                float blX = (float) (px + backX * (0.25 + sway) - rightX * 0.34), blZ = (float) (pz + backZ * (0.25 + sway) - rightZ * 0.34);
                float brX = (float) (px + backX * (0.25 + sway) + rightX * 0.34), brZ = (float) (pz + backZ * (0.25 + sway) + rightZ * 0.34);

                addLine(buffer, mat, tlX, topY, tlZ, trX, topY, trZ, c1);
                addLine(buffer, mat, blX, botY, blZ, brX, botY, brZ, c1);
                addLine(buffer, mat, tlX, topY, tlZ, blX, botY, blZ, c2);
                addLine(buffer, mat, trX, topY, trZ, brX, botY, brZ, c2);
                addLine(buffer, mat, tlX, topY, tlZ, brX, botY, brZ, c1);
                addLine(buffer, mat, trX, topY, trZ, blX, botY, blZ, c1);
            } else if (VisualConfig.backMode == 3) {
                // Две скрещённые катаны за спиной
                double backX = -Math.sin(yawRad) * 0.22, backZ = -Math.cos(yawRad) * 0.22;
                double rightX = Math.cos(yawRad), rightZ = -Math.sin(yawRad);
                for (int dir : new int[]{-1, 1}) {
                    float x1 = (float) (px + backX - rightX * 0.45 * dir), z1 = (float) (pz + backZ - rightZ * 0.45 * dir);
                    float x2 = (float) (px + backX + rightX * 0.45 * dir), z2 = (float) (pz + backZ + rightZ * 0.45 * dir);
                    addLine(buffer, mat, x1, (float) (py + 0.55), z1, x2, (float) (py + 1.75), z2, c1);
                    addLine(buffer, mat, x2 - (float) (rightX * 0.1 * dir), (float) (py + 1.52), z2 - (float) (rightZ * 0.1 * dir),
                            x2 + (float) (rightX * 0.1 * dir), (float) (py + 1.62), z2 + (float) (rightZ * 0.1 * dir), c2);
                }
            }
        }

        poseStack.popPose();
        MeshData mesh = buffer.build();
        if (mesh != null) BufferUploader.drawWithShader(mesh);

        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private static void drawCircle(BufferBuilder buf, Matrix4f mat, double cx, double cy, double cz, float r, int color) {
        int segs = 36;
        for (int i = 0; i < segs; i++) {
            double a1 = 2 * Math.PI * i / segs;
            double a2 = 2 * Math.PI * (i + 1) / segs;
            addLine(buf, mat, (float) (cx + Math.cos(a1) * r), (float) cy, (float) (cz + Math.sin(a1) * r),
                    (float) (cx + Math.cos(a2) * r), (float) cy, (float) (cz + Math.sin(a2) * r), color);
        }
    }

    private static void drawBox(BufferBuilder buf, Matrix4f mat, AABB b, int col) {
        float x1 = (float) b.minX, y1 = (float) b.minY, z1 = (float) b.minZ;
        float x2 = (float) b.maxX, y2 = (float) b.maxY, z2 = (float) b.maxZ;
        addLine(buf, mat, x1, y1, z1, x2, y1, z1, col); addLine(buf, mat, x2, y1, z1, x2, y1, z2, col);
        addLine(buf, mat, x2, y1, z2, x1, y1, z2, col); addLine(buf, mat, x1, y1, z2, x1, y1, z1, col);
        addLine(buf, mat, x1, y2, z1, x2, y2, z1, col); addLine(buf, mat, x2, y2, z1, x2, y2, z2, col);
        addLine(buf, mat, x2, y2, z2, x1, y2, z2, col); addLine(buf, mat, x1, y2, z2, x1, y2, z1, col);
        addLine(buf, mat, x1, y1, z1, x1, y2, z1, col); addLine(buf, mat, x2, y1, z1, x2, y2, z1, col);
        addLine(buf, mat, x2, y1, z2, x2, y2, z2, col); addLine(buf, mat, x1, y1, z2, x1, y2, z2, col);
    }

    private static void addLine(BufferBuilder buf, Matrix4f mat, float x1, float y1, float z1, float x2, float y2, float z2, int col) {
        int a = (col >> 24) & 0xFF;
        if (a == 0) a = 255;
        buf.addVertex(mat, x1, y1, z1).setColor((col >> 16) & 0xFF, (col >> 8) & 0xFF, col & 0xFF, a);
        buf.addVertex(mat, x2, y2, z2).setColor((col >> 16) & 0xFF, (col >> 8) & 0xFF, col & 0xFF, a);
    }
}
