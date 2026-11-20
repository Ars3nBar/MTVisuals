package ru.mirtime.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.*;

public class WorldVisualRenderer {

    private record JumpCircle(Vec3 pos, long spawnTime) {}
    private record TrailPoint(Vec3 pos, long time) {}
    private record HitSpark(Vec3 pos, Vec3 vel, long spawnTime) {}

    private static final List<JumpCircle> circles = new ArrayList<>();
    private static final List<TrailPoint> trailPoints = new ArrayList<>();
    private static final List<HitSpark> hitSparks = new ArrayList<>();
    private static boolean wasOnGround = true;
    private static int lastAttackTime = 0;

    public static void renderWorld(WorldRenderContext context) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;

        long now = System.currentTimeMillis();

        if (wasOnGround && !player.onGround() && player.getDeltaMovement().y > 0.1) {
            circles.add(new JumpCircle(player.position(), now));
        }
        wasOnGround = player.onGround();

        if (VisualConfig.hitParticles && player.attackAnim > 0 && player.tickCount - lastAttackTime > 4 && mc.crosshairPickEntity instanceof LivingEntity target) {
            lastAttackTime = player.tickCount;
            Random rnd = new Random();
            for (int i = 0; i < 18; i++) {
                Vec3 vel = new Vec3((rnd.nextDouble() - 0.5) * 0.25, rnd.nextDouble() * 0.2, (rnd.nextDouble() - 0.5) * 0.25);
                hitSparks.add(new HitSpark(target.position().add(0, target.getBbHeight() * 0.6, 0), vel, now));
            }
        }

        if (VisualConfig.trails) {
            if (trailPoints.isEmpty() || trailPoints.get(trailPoints.size() - 1).pos.distanceToSqr(player.position()) > 0.01) {
                trailPoints.add(new TrailPoint(player.position(), now));
            }
            trailPoints.removeIf(tp -> now - tp.time > 950L);
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

        // 1. TargetESP (3 стиля)
        if (VisualConfig.targetEsp && mc.crosshairPickEntity instanceof LivingEntity target) {
            double tx = target.xo + (target.getX() - target.xo) * tickDelta;
            double ty = target.yo + (target.getY() - target.yo) * tickDelta;
            double tz = target.zo + (target.getZ() - target.zo) * tickDelta;
            double h = target.getBbHeight();

            if (VisualConfig.targetEspStyle == 0) {
                double wave = (Math.sin(now / 240.0) + 1.0) * 0.5 * h;
                drawCircle(buffer, mat, tx, ty + wave, tz, 0.65f, c1);
                drawCircle(buffer, mat, tx, ty + wave + 0.05, tz, 0.72f, c2);
            } else if (VisualConfig.targetEspStyle == 1) {
                for (int i = 0; i < 36; i++) {
                    double a1 = (now / 200.0) + i * 0.35;
                    double a2 = (now / 200.0) + (i + 1) * 0.35;
                    double y1 = ty + (i / 36.0) * h;
                    double y2 = ty + ((i + 1) / 36.0) * h;
                    addLine(buffer, mat, (float) (tx + Math.cos(a1) * 0.65), (float) y1, (float) (tz + Math.sin(a1) * 0.65),
                            (float) (tx + Math.cos(a2) * 0.65), (float) y2, (float) (tz + Math.sin(a2) * 0.65), i % 2 == 0 ? c1 : c2);
                }
            } else {
                drawDiamond(buffer, mat, tx, ty + h * 0.5, tz, 0.75f, (now / 400.0), c1);
            }
        }

        // 2. Неоновые 3D-хитбоксы
        if (VisualConfig.customHitboxes) {
            for (Entity ent : mc.level.entitiesForRendering()) {
                if (ent == player || !(ent instanceof LivingEntity)) continue;
                if (ent.distanceToSqr(player) > 2500) continue;
                drawBox(buffer, mat, ent.getBoundingBox(), c1);
            }
        }

        // 3. 3D-Траектория полёта Лука / Эндер-жемчуга / Снежков
        if (VisualConfig.trajectories) {
            Item held = player.getMainHandItem().getItem();
            if (held instanceof BowItem || held instanceof CrossbowItem || held instanceof EnderpearlItem || held instanceof SnowballItem || held instanceof WindChargeItem) {
                Vec3 pos = player.getEyePosition(tickDelta).add(0, -0.1, 0);
                Vec3 look = player.getViewVector(tickDelta);
                double power = (held instanceof BowItem || held instanceof CrossbowItem) ? 2.8 : 1.5;
                Vec3 vel = look.scale(power);
                double grav = (held instanceof BowItem || held instanceof CrossbowItem) ? 0.05 : 0.03;

                for (int step = 0; step < 60; step++) {
                    Vec3 next = pos.add(vel);
                    addLine(buffer, mat, (float) pos.x, (float) pos.y, (float) pos.z, (float) next.x, (float) next.y, (float) next.z, step % 2 == 0 ? c1 : c2);
                    pos = next;
                    vel = vel.scale(0.99).subtract(0, grav, 0);
                }
            }
        }
        // 4. Вспышки ударов (Hit Particles)
        hitSparks.removeIf(s -> now - s.spawnTime > 600L);
        for (HitSpark s : hitSparks) {
            float age = (now - s.spawnTime) / 600f;
            Vec3 p = s.pos.add(s.vel.scale(age * 12));
            int alpha = (int) ((1f - age) * 255);
            int col = (alpha << 24) | (c1 & 0x00FFFFFF);
            addLine(buffer, mat, (float) p.x - 0.06f, (float) p.y, (float) p.z, (float) p.x + 0.06f, (float) p.y, (float) p.z, col);
            addLine(buffer, mat, (float) p.x, (float) p.y - 0.06f, (float) p.z, (float) p.x, (float) p.y + 0.06f, (float) p.z, col);
        }

        // 5. Атмосферные неоновые частицы вокруг игрока
        if (VisualConfig.weatherParticles > 0) {
            for (int i = 0; i < 45; i++) {
                double seed = i * 137.5;
                double ox = Math.sin(seed + now / 1800.0) * 10.0;
                double oz = Math.cos(seed + now / 1800.0) * 10.0;
                double oy = ((seed * 3.1 + (VisualConfig.weatherParticles == 3 ? now / 150.0 : -now / 220.0)) % 8.0 + 8.0) % 8.0 - 2.0;
                float px = (float) (player.getX() + ox), py = (float) (player.getY() + oy), pz = (float) (player.getZ() + oz);
                int col = i % 2 == 0 ? c1 : c2;
                if (VisualConfig.weatherParticles == 1) {
                    addLine(buffer, mat, px - 0.08f, py, pz, px + 0.08f, py, pz, col);
                    addLine(buffer, mat, px, py - 0.08f, pz, px, py + 0.08f, pz, col);
                } else {
                    addLine(buffer, mat, px, py, pz, px, py + 0.14f, pz, col);
                }
            }
        }

        // 6. Шлейф (Trails) и Jump Circles
        if (VisualConfig.trails && trailPoints.size() > 1) {
            for (int i = 0; i < trailPoints.size() - 1; i++) {
                TrailPoint p1 = trailPoints.get(i), p2 = trailPoints.get(i + 1);
                int alpha = (int) (((float) i / trailPoints.size()) * 220);
                int col = (alpha << 24) | ((i % 2 == 0 ? c1 : c2) & 0x00FFFFFF);
                addLine(buffer, mat, (float) p1.pos.x, (float) (p1.pos.y + 0.2), (float) p1.pos.z, (float) p2.pos.x, (float) (p2.pos.y + 0.2), (float) p2.pos.z, col);
                addLine(buffer, mat, (float) p1.pos.x, (float) (p1.pos.y + 1.1), (float) p1.pos.z, (float) p2.pos.x, (float) (p2.pos.y + 1.1), (float) p2.pos.z, col);
            }
        }
        if (VisualConfig.jumpCircles) {
            Iterator<JumpCircle> it = circles.iterator();
            while (it.hasNext()) {
                JumpCircle jc = it.next();
                float age = (now - jc.spawnTime) / 1000f;
                if (age > 1.2f) { it.remove(); continue; }
                float radius = (0.3f + age * 1.4f) * VisualConfig.accessorySize;
                int alpha = (int) ((1.0f - age / 1.2f) * 255);
                drawCircle(buffer, mat, jc.pos.x, jc.pos.y + 0.05, jc.pos.z, radius, (alpha << 24) | (c1 & 0x00FFFFFF));
                drawCircle(buffer, mat, jc.pos.x, jc.pos.y + 0.08, jc.pos.z, radius * 0.75f, (alpha << 24) | (c2 & 0x00FFFFFF));
            }
        }

        // 7. 3D-Косметика на игроке: Шапки (1..5)
        if (!mc.options.getCameraType().isFirstPerson()) {
            double px = player.xo + (player.getX() - player.xo) * tickDelta;
            double py = player.yo + (player.getY() - player.yo) * tickDelta;
            double pz = player.zo + (player.getZ() - player.zo) * tickDelta;
            float yawRad = (float) Math.toRadians(-player.yBodyRot);
            float sz = VisualConfig.accessorySize;

            double headTop = py + player.getBbHeight() + 0.1;
            if (VisualConfig.hatMode == 1) {
                double floatY = headTop + 0.15 + Math.sin(now / 300.0) * 0.04;
                drawCircle(buffer, mat, px, floatY, pz, 0.38f * sz, c1);
                drawCircle(buffer, mat, px, floatY + 0.03, pz, 0.32f * sz, c2);
            } else if (VisualConfig.hatMode == 2) {
                int segs = 24;
                float r = 0.58f * sz;
                for (int i = 0; i < segs; i++) {
                    double a1 = 2 * Math.PI * i / segs, a2 = 2 * Math.PI * (i + 1) / segs;
                    float x1 = (float) (px + Math.cos(a1) * r), z1 = (float) (pz + Math.sin(a1) * r);
                    float x2 = (float) (px + Math.cos(a2) * r), z2 = (float) (pz + Math.sin(a2) * r);
                    addLine(buffer, mat, x1, (float) headTop, z1, x2, (float) headTop, z2, c1);
                    addLine(buffer, mat, x1, (float) headTop, z1, (float) px, (float) (headTop + 0.28 * sz), (float) pz, c2);
                }
            } else if (VisualConfig.hatMode == 3) {
                int pts = 8;
                float r = 0.34f * sz;
                for (int i = 0; i < pts; i++) {
                    double a1 = 2 * Math.PI * i / pts + (now / 1500.0), a2 = 2 * Math.PI * (i + 1) / pts + (now / 1500.0), am = (a1 + a2) * 0.5;
                    float x1 = (float) (px + Math.cos(a1) * r), z1 = (float) (pz + Math.sin(a1) * r);
                    float x2 = (float) (px + Math.cos(a2) * r), z2 = (float) (pz + Math.sin(a2) * r);
                    float xm = (float) (px + Math.cos(am) * (r + 0.04)), zm = (float) (pz + Math.sin(am) * (r + 0.04));
                    addLine(buffer, mat, x1, (float) headTop, z1, x2, (float) headTop, z2, c1);
                    addLine(buffer, mat, x1, (float) headTop, z1, xm, (float) (headTop + 0.22 * sz), zm, c2);
                    addLine(buffer, mat, xm, (float) (headTop + 0.22 * sz), zm, x2, (float) headTop, z2, c2);
                }
            } else if (VisualConfig.hatMode == 4) {
                double rightX = Math.cos(yawRad), rightZ = -Math.sin(yawRad);
                for (int s : new int[]{-1, 1}) {
                    float bx = (float) (px + rightX * 0.22 * s * sz), bz = (float) (pz + rightZ * 0.22 * s * sz);
                    float mx = (float) (px + rightX * 0.42 * s * sz), mz = (float) (pz + rightZ * 0.42 * s * sz);
                    float tx = (float) (px + rightX * 0.34 * s * sz), tz = (float) (pz + rightZ * 0.34 * s * sz);
                    addLine(buffer, mat, bx, (float) (headTop - 0.05), bz, mx, (float) (headTop + 0.18 * sz), mz, c1);
                    addLine(buffer, mat, mx, (float) (headTop + 0.18 * sz), mz, tx, (float) (headTop + 0.38 * sz), tz, c2);
                }
            } else if (VisualConfig.hatMode == 5) {
                double rightX = Math.cos(yawRad), rightZ = -Math.sin(yawRad);
                for (int s : new int[]{-1, 1}) {
                    float xIn = (float) (px + rightX * 0.12 * s * sz), zIn = (float) (pz + rightZ * 0.12 * s * sz);
                    float xOut = (float) (px + rightX * 0.32 * s * sz), zOut = (float) (pz + rightZ * 0.32 * s * sz);
                    float xTip = (float) (px + rightX * 0.24 * s * sz), zTip = (float) (pz + rightZ * 0.24 * s * sz);
                    addLine(buffer, mat, xIn, (float) headTop, zIn, xTip, (float) (headTop + 0.24 * sz), zTip, c1);
                    addLine(buffer, mat, xOut, (float) headTop, zOut, xTip, (float) (headTop + 0.24 * sz), zTip, c2);
                }
            }
            // СПИНА (1=Крылья, 2=Плащ, 3=Катаны, 4=Кибер-Джетпак)
            if (VisualConfig.backMode == 1) {
                double flap = Math.sin(now / 180.0) * 0.25;
                for (int side : new int[]{-1, 1}) {
                    for (int line = 0; line < 6; line++) {
                        double len = (0.75 + line * 0.12) * sz;
                        double angle = yawRad + side * (0.65 + flap);
                        addLine(buffer, mat, (float) px, (float) (py + 1.3), (float) pz,
                                (float) (px - Math.sin(angle) * len), (float) (py + 1.38 + (2 - line) * 0.14 * sz), (float) (pz - Math.cos(angle) * len), line % 2 == 0 ? c1 : c2);
                    }
                }
            } else if (VisualConfig.backMode == 2) {
                double sway = 0.25 + Math.min(0.8, player.getDeltaMovement().horizontalDistance() * 3.0) + Math.sin(now / 200.0) * 0.08;
                double backX = -Math.sin(yawRad), backZ = -Math.cos(yawRad), rightX = Math.cos(yawRad), rightZ = -Math.sin(yawRad);
                float topY = (float) (py + 1.42), botY = (float) (py + 0.35);
                float tlX = (float) (px + backX * 0.18 - rightX * 0.28 * sz), tlZ = (float) (pz + backZ * 0.18 - rightZ * 0.28 * sz);
                float trX = (float) (px + backX * 0.18 + rightX * 0.28 * sz), trZ = (float) (pz + backZ * 0.18 + rightZ * 0.28 * sz);
                float blX = (float) (px + backX * (0.25 + sway) - rightX * 0.34 * sz), blZ = (float) (pz + backZ * (0.25 + sway) - rightZ * 0.34 * sz);
                float brX = (float) (px + backX * (0.25 + sway) + rightX * 0.34 * sz), brZ = (float) (pz + backZ * (0.25 + sway) + rightZ * 0.34 * sz);
                addLine(buffer, mat, tlX, topY, tlZ, trX, topY, trZ, c1); addLine(buffer, mat, blX, botY, blZ, brX, botY, brZ, c1);
                addLine(buffer, mat, tlX, topY, tlZ, blX, botY, blZ, c2); addLine(buffer, mat, trX, topY, trZ, brX, botY, brZ, c2);
            } else if (VisualConfig.backMode == 3) {
                double backX = -Math.sin(yawRad) * 0.22, backZ = -Math.cos(yawRad) * 0.22, rightX = Math.cos(yawRad), rightZ = -Math.sin(yawRad);
                for (int dir : new int[]{-1, 1}) {
                    addLine(buffer, mat, (float) (px + backX - rightX * 0.45 * dir * sz), (float) (py + 0.55), (float) (pz + backZ - rightZ * 0.45 * dir * sz),
                            (float) (px + backX + rightX * 0.45 * dir * sz), (float) (py + 1.75 * sz), (float) (pz + backZ + rightZ * 0.45 * dir * sz), c1);
                }
            } else if (VisualConfig.backMode == 4) {
                double backX = -Math.sin(yawRad) * 0.25, backZ = -Math.cos(yawRad) * 0.25, rightX = Math.cos(yawRad), rightZ = -Math.sin(yawRad);
                for (int s : new int[]{-1, 1}) {
                    float cx = (float) (px + backX + rightX * 0.16 * s * sz), cz = (float) (pz + backZ + rightZ * 0.16 * s * sz);
                    drawBox(buffer, mat, new AABB(cx - 0.08, py + 0.85, cz - 0.08, cx + 0.08, py + 1.4, cz + 0.08), c1);
                    float flameLen = (float) (0.25 + (now % 150) / 400.0);
                    addLine(buffer, mat, cx, (float) (py + 0.85), cz, cx, (float) (py + 0.85 - flameLen), cz, c2);
                }
            }

            // ЛЕТАЮЩИЕ ПИТОМЦЫ-СПУТНИКИ (1=Кристалл, 2=Сфера, 3=Куб Пустоты)
            if (VisualConfig.petMode > 0) {
                double orbit = now / 500.0;
                double petX = px + Math.cos(orbit) * 0.95 * sz;
                double petZ = pz + Math.sin(orbit) * 0.95 * sz;
                double petY = py + 1.35 + Math.sin(now / 250.0) * 0.15;
                if (VisualConfig.petMode == 1) drawDiamond(buffer, mat, petX, petY, petZ, 0.22f * sz, orbit * 2, c1);
                else if (VisualConfig.petMode == 2) { drawCircle(buffer, mat, petX, petY, petZ, 0.2f * sz, c1); drawCircle(buffer, mat, petX, petY + 0.04, petZ, 0.14f * sz, c2); }
                else drawBox(buffer, mat, new AABB(petX - 0.15 * sz, petY - 0.15 * sz, petZ - 0.15 * sz, petX + 0.15 * sz, petY + 0.15 * sz, petZ + 0.15 * sz), c1);
            }
        }

        poseStack.popPose();
        MeshData mesh = buffer.build();
        if (mesh != null) BufferUploader.drawWithShader(mesh);

        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    private static void drawDiamond(BufferBuilder buf, Matrix4f mat, double cx, double cy, double cz, float r, double rot, int col) {
        for (int i = 0; i < 4; i++) {
            double a1 = rot + i * Math.PI / 2, a2 = rot + (i + 1) * Math.PI / 2;
            float x1 = (float) (cx + Math.cos(a1) * r), z1 = (float) (cz + Math.sin(a1) * r);
            float x2 = (float) (cx + Math.cos(a2) * r), z2 = (float) (cz + Math.sin(a2) * r);
            addLine(buf, mat, x1, (float) cy, z1, x2, (float) cy, z2, col);
            addLine(buf, mat, x1, (float) cy, z1, (float) cx, (float) (cy + r * 1.4), (float) cz, col);
            addLine(buf, mat, x1, (float) cy, z1, (float) cx, (float) (cy - r * 1.4), (float) cz, col);
        }
    }

    private static void drawCircle(BufferBuilder buf, Matrix4f mat, double cx, double cy, double cz, float r, int color) {
        int segs = 36;
        for (int i = 0; i < segs; i++) {
            double a1 = 2 * Math.PI * i / segs, a2 = 2 * Math.PI * (i + 1) / segs;
            addLine(buf, mat, (float) (cx + Math.cos(a1) * r), (float) cy, (float) (cz + Math.sin(a1) * r), (float) (cx + Math.cos(a2) * r), (float) cy, (float) (cz + Math.sin(a2) * r), color);
        }
    }

    private static void drawBox(BufferBuilder buf, Matrix4f mat, AABB b, int col) {
        float x1 = (float) b.minX, y1 = (float) b.minY, z1 = (float) b.minZ, x2 = (float) b.maxX, y2 = (float) b.maxY, z2 = (float) b.maxZ;
        addLine(buf, mat, x1, y1, z1, x2, y1, z1, col); addLine(buf, mat, x2, y1, z1, x2, y1, z2, col); addLine(buf, mat, x2, y1, z2, x1, y1, z2, col); addLine(buf, mat, x1, y1, z2, x1, y1, z1, col);
        addLine(buf, mat, x1, y2, z1, x2, y2, z1, col); addLine(buf, mat, x2, y2, z1, x2, y2, z2, col); addLine(buf, mat, x2, y2, z2, x1, y2, z2, col); addLine(buf, mat, x1, y2, z2, x1, y2, z1, col);
        addLine(buf, mat, x1, y1, z1, x1, y2, z1, col); addLine(buf, mat, x2, y1, z1, x2, y2, z1, col); addLine(buf, mat, x2, y1, z2, x2, y2, z2, col); addLine(buf, mat, x1, y1, z2, x1, y2, z2, col);
    }

    private static void addLine(BufferBuilder buf, Matrix4f mat, float x1, float y1, float z1, float x2, float y2, float z2, int col) {
        int a = (col >> 24) & 0xFF; if (a == 0) a = 255;
        buf.addVertex(mat, x1, y1, z1).setColor((col >> 16) & 0xFF, (col >> 8) & 0xFF, col & 0xFF, a);
        buf.addVertex(mat, x2, y2, z2).setColor((col >> 16) & 0xFF, (col >> 8) & 0xFF, col & 0xFF, a);
    }
}
