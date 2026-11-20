package ru.mirtime.visuals;

import net.fabricmc.loader.api.FabricLoader;
import java.awt.Color;
import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class VisualConfig {
    // === 1. HUD & ПОЗИЦИИ / МАСШТАБЫ ===
    public static boolean watermark = true;
    public static int watermarkStyle = 0;
    public static float watermarkX = 10f, watermarkY = 10f, watermarkScale = 1.0f;

    public static boolean keystrokes = true;
    public static boolean keystrokesCps = true;
    public static float keystrokesX = 10f, keystrokesY = 40f, keystrokesScale = 1.0f;

    public static boolean armorHud = true;
    public static boolean armorDurability = true;
    public static float armorX = 10f, armorY = 125f, armorScale = 1.0f;

    public static boolean potionsHud = true;
    public static float potionsX = 10f, potionsY = 155f, potionsScale = 1.0f;

    public static boolean targetHud = true;
    public static int targetHudStyle = 0;
    public static float targetHudX = 220f, targetHudY = 150f, targetHudScale = 1.0f;

    public static boolean arrayList = true;
    public static boolean hotbarGlow = true;
    public static boolean customCrosshair = true;
    public static int crosshairStyle = 0;
    public static boolean dynamicCrosshair = true;
    public static boolean inventoryGlass = true;
    public static boolean streamerMode = false;
    public static boolean hudSnapLines = true; // Магнитные направляющие

    // === 2. КОСМЕТИКА (3D + ОБЪЁМНАЯ ЗАЛИВКА) ===
    public static int hatMode = 1; // 0..8
    public static int backMode = 1; // 0..7
    public static int petMode = 1; // 0..7
    public static boolean trails = true;
    public static int trailStyle = 0;
    public static float trailLength = 1.0f;
    public static boolean jumpCircles = true;
    public static int jumpCircleStyle = 0;
    public static boolean footstepRings = false;
    public static boolean bodyAura = false;
    public static float accessorySize = 1.0f;
    public static boolean firstPersonCosmetics = false;

    // === 3. PVP & БОЙ (РАСШИРЕННЫЙ ПАКЕТ) ===
    public static boolean targetEsp = true;
    public static int targetEspStyle = 0;
    public static float targetEspSpeed = 1.0f;
    public static boolean customHitboxes = false;
    public static boolean hitboxFill = false;
    public static boolean hitParticles = true;
    public static int hitParticleStyle = 0;
    public static int hitParticleCount = 22;
    public static boolean killEffect = true;
    public static boolean totemPopEffect = true; // Эффект срабатывания тотема
    public static boolean blockOverlay = true; // Неоновая подсветка блока под прицелом
    public static boolean crystalRadius = true; // Радиус взрыва кристаллов и TNT
    public static boolean playerTracers = false; // Линии до игроков
    public static boolean lowHpVignette = true; // Красная виньетка при малом HP
    public static boolean armorBreakWarning = true; // Предупреждение о поломке брони
    public static boolean comboCounter = true; // Счётчик комбо и дистанции удара
    public static boolean trajectories = true;
    public static boolean trajectoryLandingBox = true;
    public static boolean pearlTracker = true;
    public static boolean itemPhysics = true;
    public static boolean noHurtCam = true;
    public static boolean smoothZoom = true;
    public static float zoomFactor = 3.5f;

    // === 4. РУКА (VIEWMODEL) ===
    public static boolean handView = true;
    public static float handScale = 0.85f;
    public static float handOffsetX = 0.05f, handOffsetY = -0.04f, handOffsetZ = -0.10f;
    public static float handRotX = 0f, handRotY = 0f, handRotZ = 0f;
    public static int swingAnimation = 1;
    public static float swingSpeed = 1.0f;
    public static boolean leftHandView = false;

    // === 5. МИР & ТЕМЫ ===
    public static int timeMode = 0;
    public static int weatherParticles = 1;
    public static int particleDensity = 60;
    public static boolean customFog = false;
    public static boolean fullbright = true;
    public static boolean customSkyColor = false;
    public static int themeMode = 0;
    public static float rgbSpeed = 2.0f;
    public static float guiBlurAlpha = 0.65f;
    public static int getPrimaryColor() {
        float speedMs = Math.max(500f, 6000f / Math.max(0.2f, rgbSpeed));
        return switch (themeMode) {
            case 1 -> 0xFF00FF88;
            case 2 -> 0xFFFF1E56;
            case 3 -> 0xFFFFD700;
            case 4 -> 0xFF9D00FF;
            case 5 -> Color.HSBtoRGB((System.currentTimeMillis() % (long) speedMs) / speedMs, 0.85f, 1.0f);
            case 6 -> 0xFF00E5FF;
            case 7 -> 0xFFFF6B00;
            default -> 0xFFFF007F;
        };
    }

    public static int getSecondaryColor() {
        float speedMs = Math.max(500f, 6000f / Math.max(0.2f, rgbSpeed));
        return switch (themeMode) {
            case 1 -> 0xFF00AAFF;
            case 2 -> 0xFFFF8800;
            case 3 -> 0xFFFF5500;
            case 4 -> 0xFF00FFFF;
            case 5 -> Color.HSBtoRGB(((System.currentTimeMillis() + (long) (speedMs / 3)) % (long) speedMs) / speedMs, 0.85f, 1.0f);
            case 6 -> 0xFF7000FF;
            case 7 -> 0xFFFF0055;
            default -> 0xFF00F0FF;
        };
    }

    public static String getHatName() {
        return switch (hatMode) {
            case 1 -> "Неоновый Нимб"; case 2 -> "Самурайская Каса (3D)"; case 3 -> "Корона Императора";
            case 4 -> "Демон-Рога"; case 5 -> "Кибер-Ушки (3D)"; case 6 -> "Орбитальные Кольца";
            case 7 -> "Кибер-Визор"; case 8 -> "Кристалл-Диадема"; default -> "Выкл";
        };
    }

    public static String getBackName() {
        return switch (backMode) {
            case 1 -> "Крылья Дракона (3D)"; case 2 -> "Физический Плащ (3D)"; case 3 -> "Две Катаны";
            case 4 -> "Кибер-Джетпак"; case 5 -> "Энерго-Щит (3D)"; case 6 -> "Кольцо-Портал";
            case 7 -> "Ангельские Крылья (3D)"; default -> "Выкл";
        };
    }

    public static String getPetName() {
        return switch (petMode) {
            case 1 -> "Кристалл-Спутник (3D)"; case 2 -> "Огненная Сфера"; case 3 -> "Куб Пустоты (3D)";
            case 4 -> "Мини-Дрон"; case 5 -> "Дух-Огонёк"; case 6 -> "Неоновый Дракончик";
            case 7 -> "Ядро Энергии"; default -> "Выкл";
        };
    }

    public static String getThemeName() {
        return switch (themeMode) {
            case 1 -> "Matrix Green"; case 2 -> "Blood Crimson"; case 3 -> "Imperial Gold";
            case 4 -> "Void Purple"; case 5 -> "RGB Перелив"; case 6 -> "MIR-TIME Cyan";
            case 7 -> "Solar Orange"; default -> "Cyber Pink";
        };
    }

    public static String getTimeModeName() {
        return switch (timeMode) {
            case 1 -> "Вечный День"; case 2 -> "Кибер-Закат"; case 3 -> "Неоновая Ночь";
            case 4 -> "Ускоренный Цикл"; default -> "Как на сервере";
        };
    }

    public static String getWeatherName() {
        return switch (weatherParticles) {
            case 1 -> "Неоновые Звёзды"; case 2 -> "Снежинки"; case 3 -> "Искры Пламени";
            case 4 -> "Кибер-Дождь"; case 5 -> "Светлячки"; default -> "Выкл";
        };
    }

    public static String getSwingName() {
        return switch (swingAnimation) {
            case 1 -> "Smooth Spin"; case 2 -> "Block Hit"; case 3 -> "Cyber Slap";
            case 4 -> "Power Down"; default -> "Ваниль";
        };
    }

    public static String getTargetEspName() {
        return switch (targetEspStyle) {
            case 1 -> "Спираль"; case 2 -> "Ромб"; case 3 -> "Призраки";
            case 4 -> "Сканер"; default -> "Волна";
        };
    }

    public static void save() {
        try {
            Path file = FabricLoader.getInstance().getConfigDir().resolve("mtvisuals.properties");
            Properties p = new Properties();
            p.setProperty("watermark", String.valueOf(watermark));
            p.setProperty("watermarkX", String.valueOf(watermarkX));
            p.setProperty("watermarkY", String.valueOf(watermarkY));
            p.setProperty("watermarkScale", String.valueOf(watermarkScale));
            p.setProperty("keystrokes", String.valueOf(keystrokes));
            p.setProperty("keystrokesCps", String.valueOf(keystrokesCps));
            p.setProperty("keystrokesX", String.valueOf(keystrokesX));
            p.setProperty("keystrokesY", String.valueOf(keystrokesY));
            p.setProperty("keystrokesScale", String.valueOf(keystrokesScale));
            p.setProperty("armorHud", String.valueOf(armorHud));
            p.setProperty("armorX", String.valueOf(armorX));
            p.setProperty("armorY", String.valueOf(armorY));
            p.setProperty("armorScale", String.valueOf(armorScale));
            p.setProperty("potionsHud", String.valueOf(potionsHud));
            p.setProperty("potionsX", String.valueOf(potionsX));
            p.setProperty("potionsY", String.valueOf(potionsY));
            p.setProperty("potionsScale", String.valueOf(potionsScale));
            p.setProperty("targetHud", String.valueOf(targetHud));
            p.setProperty("targetHudX", String.valueOf(targetHudX));
            p.setProperty("targetHudY", String.valueOf(targetHudY));
            p.setProperty("targetHudScale", String.valueOf(targetHudScale));
            p.setProperty("arrayList", String.valueOf(arrayList));
            p.setProperty("hotbarGlow", String.valueOf(hotbarGlow));
            p.setProperty("customCrosshair", String.valueOf(customCrosshair));
            p.setProperty("crosshairStyle", String.valueOf(crosshairStyle));
            p.setProperty("dynamicCrosshair", String.valueOf(dynamicCrosshair));
            p.setProperty("inventoryGlass", String.valueOf(inventoryGlass));
            p.setProperty("streamerMode", String.valueOf(streamerMode));
            p.setProperty("hudSnapLines", String.valueOf(hudSnapLines));
            p.setProperty("hatMode", String.valueOf(hatMode));
            p.setProperty("backMode", String.valueOf(backMode));
            p.setProperty("petMode", String.valueOf(petMode));
            p.setProperty("trails", String.valueOf(trails));
            p.setProperty("trailStyle", String.valueOf(trailStyle));
            p.setProperty("trailLength", String.valueOf(trailLength));
            p.setProperty("jumpCircles", String.valueOf(jumpCircles));
            p.setProperty("jumpCircleStyle", String.valueOf(jumpCircleStyle));
            p.setProperty("footstepRings", String.valueOf(footstepRings));
            p.setProperty("bodyAura", String.valueOf(bodyAura));
            p.setProperty("accessorySize", String.valueOf(accessorySize));
            p.setProperty("firstPersonCosmetics", String.valueOf(firstPersonCosmetics));
            p.setProperty("targetEsp", String.valueOf(targetEsp));
            p.setProperty("targetEspStyle", String.valueOf(targetEspStyle));
            p.setProperty("targetEspSpeed", String.valueOf(targetEspSpeed));
            p.setProperty("customHitboxes", String.valueOf(customHitboxes));
            p.setProperty("hitboxFill", String.valueOf(hitboxFill));
            p.setProperty("hitParticles", String.valueOf(hitParticles));
            p.setProperty("hitParticleStyle", String.valueOf(hitParticleStyle));
            p.setProperty("hitParticleCount", String.valueOf(hitParticleCount));
            p.setProperty("killEffect", String.valueOf(killEffect));
            p.setProperty("totemPopEffect", String.valueOf(totemPopEffect));
            p.setProperty("blockOverlay", String.valueOf(blockOverlay));
            p.setProperty("crystalRadius", String.valueOf(crystalRadius));
            p.setProperty("playerTracers", String.valueOf(playerTracers));
            p.setProperty("lowHpVignette", String.valueOf(lowHpVignette));
            p.setProperty("armorBreakWarning", String.valueOf(armorBreakWarning));
            p.setProperty("comboCounter", String.valueOf(comboCounter));
            p.setProperty("trajectories", String.valueOf(trajectories));
            p.setProperty("trajectoryLandingBox", String.valueOf(trajectoryLandingBox));
            p.setProperty("pearlTracker", String.valueOf(pearlTracker));
            p.setProperty("itemPhysics", String.valueOf(itemPhysics));
            p.setProperty("noHurtCam", String.valueOf(noHurtCam));
            p.setProperty("smoothZoom", String.valueOf(smoothZoom));
            p.setProperty("zoomFactor", String.valueOf(zoomFactor));
            p.setProperty("handView", String.valueOf(handView));
            p.setProperty("handScale", String.valueOf(handScale));
            p.setProperty("handOffsetX", String.valueOf(handOffsetX));
            p.setProperty("handOffsetY", String.valueOf(handOffsetY));
            p.setProperty("handOffsetZ", String.valueOf(handOffsetZ));
            p.setProperty("handRotX", String.valueOf(handRotX));
            p.setProperty("handRotY", String.valueOf(handRotY));
            p.setProperty("handRotZ", String.valueOf(handRotZ));
            p.setProperty("swingAnimation", String.valueOf(swingAnimation));
            p.setProperty("swingSpeed", String.valueOf(swingSpeed));
            p.setProperty("leftHandView", String.valueOf(leftHandView));
            p.setProperty("timeMode", String.valueOf(timeMode));
            p.setProperty("weatherParticles", String.valueOf(weatherParticles));
            p.setProperty("particleDensity", String.valueOf(particleDensity));
            p.setProperty("customFog", String.valueOf(customFog));
            p.setProperty("fullbright", String.valueOf(fullbright));
            p.setProperty("themeMode", String.valueOf(themeMode));
            p.setProperty("rgbSpeed", String.valueOf(rgbSpeed));
            p.setProperty("guiBlurAlpha", String.valueOf(guiBlurAlpha));
            try (OutputStream out = new FileOutputStream(file.toFile())) {
                p.store(out, "MTVisuals v5.0 ULTRA Config");
            }
        } catch (Exception ignored) {}
    }
    public static void load() {
        try {
            Path file = FabricLoader.getInstance().getConfigDir().resolve("mtvisuals.properties");
            if (!file.toFile().exists()) return;
            Properties p = new Properties();
            try (InputStream in = new FileInputStream(file.toFile())) { p.load(in); }
            watermark = Boolean.parseBoolean(p.getProperty("watermark", "true"));
            watermarkX = Float.parseFloat(p.getProperty("watermarkX", "10.0"));
            watermarkY = Float.parseFloat(p.getProperty("watermarkY", "10.0"));
            watermarkScale = Float.parseFloat(p.getProperty("watermarkScale", "1.0"));
            keystrokes = Boolean.parseBoolean(p.getProperty("keystrokes", "true"));
            keystrokesCps = Boolean.parseBoolean(p.getProperty("keystrokesCps", "true"));
            keystrokesX = Float.parseFloat(p.getProperty("keystrokesX", "10.0"));
            keystrokesY = Float.parseFloat(p.getProperty("keystrokesY", "40.0"));
            keystrokesScale = Float.parseFloat(p.getProperty("keystrokesScale", "1.0"));
            armorHud = Boolean.parseBoolean(p.getProperty("armorHud", "true"));
            armorX = Float.parseFloat(p.getProperty("armorX", "10.0"));
            armorY = Float.parseFloat(p.getProperty("armorY", "125.0"));
            armorScale = Float.parseFloat(p.getProperty("armorScale", "1.0"));
            potionsHud = Boolean.parseBoolean(p.getProperty("potionsHud", "true"));
            potionsX = Float.parseFloat(p.getProperty("potionsX", "10.0"));
            potionsY = Float.parseFloat(p.getProperty("potionsY", "155.0"));
            potionsScale = Float.parseFloat(p.getProperty("potionsScale", "1.0"));
            targetHud = Boolean.parseBoolean(p.getProperty("targetHud", "true"));
            targetHudX = Float.parseFloat(p.getProperty("targetHudX", "220.0"));
            targetHudY = Float.parseFloat(p.getProperty("targetHudY", "150.0"));
            targetHudScale = Float.parseFloat(p.getProperty("targetHudScale", "1.0"));
            arrayList = Boolean.parseBoolean(p.getProperty("arrayList", "true"));
            hotbarGlow = Boolean.parseBoolean(p.getProperty("hotbarGlow", "true"));
            customCrosshair = Boolean.parseBoolean(p.getProperty("customCrosshair", "true"));
            crosshairStyle = Integer.parseInt(p.getProperty("crosshairStyle", "0"));
            dynamicCrosshair = Boolean.parseBoolean(p.getProperty("dynamicCrosshair", "true"));
            inventoryGlass = Boolean.parseBoolean(p.getProperty("inventoryGlass", "true"));
            streamerMode = Boolean.parseBoolean(p.getProperty("streamerMode", "false"));
            hudSnapLines = Boolean.parseBoolean(p.getProperty("hudSnapLines", "true"));
            hatMode = Integer.parseInt(p.getProperty("hatMode", "1"));
            backMode = Integer.parseInt(p.getProperty("backMode", "1"));
            petMode = Integer.parseInt(p.getProperty("petMode", "1"));
            trails = Boolean.parseBoolean(p.getProperty("trails", "true"));
            trailStyle = Integer.parseInt(p.getProperty("trailStyle", "0"));
            trailLength = Float.parseFloat(p.getProperty("trailLength", "1.0"));
            jumpCircles = Boolean.parseBoolean(p.getProperty("jumpCircles", "true"));
            jumpCircleStyle = Integer.parseInt(p.getProperty("jumpCircleStyle", "0"));
            footstepRings = Boolean.parseBoolean(p.getProperty("footstepRings", "false"));
            bodyAura = Boolean.parseBoolean(p.getProperty("bodyAura", "false"));
            accessorySize = Float.parseFloat(p.getProperty("accessorySize", "1.0"));
            firstPersonCosmetics = Boolean.parseBoolean(p.getProperty("firstPersonCosmetics", "false"));
            targetEsp = Boolean.parseBoolean(p.getProperty("targetEsp", "true"));
            targetEspStyle = Integer.parseInt(p.getProperty("targetEspStyle", "0"));
            targetEspSpeed = Float.parseFloat(p.getProperty("targetEspSpeed", "1.0"));
            customHitboxes = Boolean.parseBoolean(p.getProperty("customHitboxes", "false"));
            hitboxFill = Boolean.parseBoolean(p.getProperty("hitboxFill", "false"));
            hitParticles = Boolean.parseBoolean(p.getProperty("hitParticles", "true"));
            hitParticleStyle = Integer.parseInt(p.getProperty("hitParticleStyle", "0"));
            hitParticleCount = Integer.parseInt(p.getProperty("hitParticleCount", "22"));
            killEffect = Boolean.parseBoolean(p.getProperty("killEffect", "true"));
            totemPopEffect = Boolean.parseBoolean(p.getProperty("totemPopEffect", "true"));
            blockOverlay = Boolean.parseBoolean(p.getProperty("blockOverlay", "true"));
            crystalRadius = Boolean.parseBoolean(p.getProperty("crystalRadius", "true"));
            playerTracers = Boolean.parseBoolean(p.getProperty("playerTracers", "false"));
            lowHpVignette = Boolean.parseBoolean(p.getProperty("lowHpVignette", "true"));
            armorBreakWarning = Boolean.parseBoolean(p.getProperty("armorBreakWarning", "true"));
            comboCounter = Boolean.parseBoolean(p.getProperty("comboCounter", "true"));
            trajectories = Boolean.parseBoolean(p.getProperty("trajectories", "true"));
            trajectoryLandingBox = Boolean.parseBoolean(p.getProperty("trajectoryLandingBox", "true"));
            pearlTracker = Boolean.parseBoolean(p.getProperty("pearlTracker", "true"));
            itemPhysics = Boolean.parseBoolean(p.getProperty("itemPhysics", "true"));
            noHurtCam = Boolean.parseBoolean(p.getProperty("noHurtCam", "true"));
            smoothZoom = Boolean.parseBoolean(p.getProperty("smoothZoom", "true"));
            zoomFactor = Float.parseFloat(p.getProperty("zoomFactor", "3.5"));
            handView = Boolean.parseBoolean(p.getProperty("handView", "true"));
            handScale = Float.parseFloat(p.getProperty("handScale", "0.85"));
            handOffsetX = Float.parseFloat(p.getProperty("handOffsetX", "0.05"));
            handOffsetY = Float.parseFloat(p.getProperty("handOffsetY", "-0.04"));
            handOffsetZ = Float.parseFloat(p.getProperty("handOffsetZ", "-0.10"));
            handRotX = Float.parseFloat(p.getProperty("handRotX", "0.0"));
            handRotY = Float.parseFloat(p.getProperty("handRotY", "0.0"));
            handRotZ = Float.parseFloat(p.getProperty("handRotZ", "0.0"));
            swingAnimation = Integer.parseInt(p.getProperty("swingAnimation", "1"));
            swingSpeed = Float.parseFloat(p.getProperty("swingSpeed", "1.0"));
            leftHandView = Boolean.parseBoolean(p.getProperty("leftHandView", "false"));
            timeMode = Integer.parseInt(p.getProperty("timeMode", "0"));
            weatherParticles = Integer.parseInt(p.getProperty("weatherParticles", "1"));
            particleDensity = Integer.parseInt(p.getProperty("particleDensity", "60"));
            customFog = Boolean.parseBoolean(p.getProperty("customFog", "false"));
            fullbright = Boolean.parseBoolean(p.getProperty("fullbright", "true"));
            themeMode = Integer.parseInt(p.getProperty("themeMode", "0"));
            rgbSpeed = Float.parseFloat(p.getProperty("rgbSpeed", "2.0"));
            guiBlurAlpha = Float.parseFloat(p.getProperty("guiBlurAlpha", "0.65"));
        } catch (Exception ignored) {}
    }
}
package ru.mirtime.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.*;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.*;

public class WorldVisualRenderer {

    private record JumpCircle(Vec3 pos, long spawnTime, boolean isStep) {}
    private record TrailPoint(Vec3 pos, long time) {}
    private record HitSpark(Vec3 pos, Vec3 vel, long spawnTime, int style) {}
    private record KillBolt(Vec3 pos, long spawnTime) {}

    private static final List<JumpCircle> circles = new ArrayList<>();
    private static final List<TrailPoint> trailPoints = new ArrayList<>();
    private static final List<HitSpark> hitSparks = new ArrayList<>();
    private static final List<KillBolt> killBolts = new ArrayList<>();

    private static boolean wasOnGround = true;
    private static int lastAttackTime = 0, lastStepTick = 0;
    private static LivingEntity lastTarget = null;

    public static void renderWorld(WorldRenderContext context) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || mc.level == null) return;

        long now = System.currentTimeMillis();

        if (wasOnGround && !player.onGround() && player.getDeltaMovement().y > 0.1) {
            circles.add(new JumpCircle(player.position(), now, false));
        }
        wasOnGround = player.onGround();

        if (VisualConfig.footstepRings && player.onGround() && player.getDeltaMovement().horizontalDistanceSqr() > 0.002 && player.tickCount - lastStepTick > 8) {
            lastStepTick = player.tickCount;
            circles.add(new JumpCircle(player.position(), now, true));
        }

        if (mc.crosshairPickEntity instanceof LivingEntity target) {
            lastTarget = target;
            if (VisualConfig.hitParticles && player.attackAnim > 0 && player.tickCount - lastAttackTime > 4) {
                lastAttackTime = player.tickCount;
                Random rnd = new Random();
                for (int i = 0; i < VisualConfig.hitParticleCount; i++) {
                    Vec3 vel = new Vec3((rnd.nextDouble() - 0.5) * 0.3, rnd.nextDouble() * 0.25, (rnd.nextDouble() - 0.5) * 0.3);
                    hitSparks.add(new HitSpark(target.position().add(0, target.getBbHeight() * 0.6, 0), vel, now, VisualConfig.hitParticleStyle));
                }
            }
        }
        if (VisualConfig.killEffect && lastTarget != null && (!lastTarget.isAlive() || lastTarget.getHealth() <= 0)) {
            killBolts.add(new KillBolt(lastTarget.position(), now));
            lastTarget = null;
        }

        if (VisualConfig.trails) {
            if (trailPoints.isEmpty() || trailPoints.get(trailPoints.size() - 1).pos.distanceToSqr(player.position()) > 0.01) {
                trailPoints.add(new TrailPoint(player.position(), now));
            }
            trailPoints.removeIf(tp -> now - tp.time > (long) (950L * VisualConfig.trailLength));
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
        BufferBuilder lines = tesselator.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
        BufferBuilder quads = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        poseStack.pushPose();
        poseStack.translate(-cam.x, -cam.y, -cam.z);
        Matrix4f mat = poseStack.last().pose();

        int c1 = VisualConfig.getPrimaryColor();
        int c2 = VisualConfig.getSecondaryColor();
        int fillC1 = (0x44 << 24) | (c1 & 0x00FFFFFF);
        int fillC2 = (0x44 << 24) | (c2 & 0x00FFFFFF);

        // 1. Подсветка блока под прицелом (Block Overlay с заливкой)
        if (VisualConfig.blockOverlay && mc.hitResult instanceof BlockHitResult bhr && bhr.getType() == HitResult.Type.BLOCK) {
            AABB box = new AABB(bhr.getBlockPos()).inflate(0.003);
            drawBox(lines, mat, box, c1);
            drawFilledBox(quads, mat, box, fillC1);
        }

        // 2. TargetESP (5 стилей)
        if (VisualConfig.targetEsp && mc.crosshairPickEntity instanceof LivingEntity target && target.isAlive()) {
            double tx = target.xo + (target.getX() - target.xo) * tickDelta;
            double ty = target.yo + (target.getY() - target.yo) * tickDelta;
            double tz = target.zo + (target.getZ() - target.zo) * tickDelta;
            double h = target.getBbHeight();
            double spd = VisualConfig.targetEspSpeed;

            switch (VisualConfig.targetEspStyle) {
                case 1 -> {
                    for (int i = 0; i < 40; i++) {
                        double a1 = (now * spd / 200.0) + i * 0.35, a2 = (now * spd / 200.0) + (i + 1) * 0.35;
                        addLine(lines, mat, (float) (tx + Math.cos(a1) * 0.65), (float) (ty + (i / 40.0) * h), (float) (tz + Math.sin(a1) * 0.65),
                                (float) (tx + Math.cos(a2) * 0.65), (float) (ty + ((i + 1) / 40.0) * h), (float) (tz + Math.sin(a2) * 0.65), i % 2 == 0 ? c1 : c2);
                    }
                }
                case 2 -> drawDiamond(lines, mat, tx, ty + h * 0.5, tz, 0.75f, (now * spd / 400.0), c1);
                case 3 -> {
                    for (int g = 0; g < 3; g++) {
                        double baseA = (now * spd / 350.0) + g * (Math.PI * 2 / 3);
                        for (int t = 0; t < 12; t++) {
                            double a = baseA - t * 0.14;
                            double gy = ty + h * 0.5 + Math.sin(a * 2 + g) * (h * 0.38);
                            int alpha = (int) ((1f - t / 12f) * 255);
                            int col = (alpha << 24) | ((g % 2 == 0 ? c1 : c2) & 0x00FFFFFF);
                            drawCircle(lines, mat, tx + Math.cos(a) * 0.7, gy, tz + Math.sin(a) * 0.7, 0.14f * (1f - t / 13f), col);
                        }
                    }
                }
                case 4 -> {
                    double scan = (Math.sin(now * spd / 260.0) + 1.0) * 0.5 * h;
                    drawCircle(lines, mat, tx, ty + scan, tz, 0.72f, c1);
                    drawCircle(lines, mat, tx, ty + h - scan, tz, 0.72f, c2);
                }
                default -> {
                    double wave = (Math.sin(now * spd / 240.0) + 1.0) * 0.5 * h;
                    drawCircle(lines, mat, tx, ty + wave, tz, 0.65f, c1);
                    drawCircle(lines, mat, tx, ty + wave + 0.05, tz, 0.72f, c2);
                }
            }
        }
        // 3. Сущности: Хитбоксы, Кристаллы/TNT, Перлы, Предметы, Трейсеры до игроков
        Vec3 eyePos = player.getEyePosition(tickDelta).add(player.getViewVector(tickDelta).scale(0.5));
        for (Entity ent : mc.level.entitiesForRendering()) {
            if (ent == player || ent.distanceToSqr(player) > 4000) continue;

            if (VisualConfig.customHitboxes && ent instanceof LivingEntity) {
                drawBox(lines, mat, ent.getBoundingBox(), c1);
                if (VisualConfig.hitboxFill) {
                    drawFilledBox(quads, mat, ent.getBoundingBox(), fillC1);
                }
            }
            if (VisualConfig.crystalRadius && (ent instanceof EndCrystal || ent instanceof PrimedTnt)) {
                drawCircle(lines, mat, ent.getX(), ent.getY() + 0.1, ent.getZ(), 6.0f, 0xFFFF2255);
                drawCircle(lines, mat, ent.getX(), ent.getY() + 0.12, ent.getZ(), 3.0f, 0xFFFFAA00);
            }
            if (VisualConfig.playerTracers && ent instanceof Player) {
                addLine(lines, mat, (float) eyePos.x, (float) eyePos.y, (float) eyePos.z,
                        (float) ent.getX(), (float) (ent.getY() + ent.getBbHeight() * 0.5), (float) ent.getZ(), c1);
            }
            if (VisualConfig.pearlTracker && ent instanceof ThrownEnderpearl) {
                drawDiamond(lines, mat, ent.getX(), ent.getY() + 0.15, ent.getZ(), 0.35f, now / 150.0, c2);
            }
            if (VisualConfig.itemPhysics && ent instanceof ItemEntity) {
                drawCircle(lines, mat, ent.getX(), ent.getY() + 0.05, ent.getZ(), 0.28f, c1);
            }
        }

        // 4. Траектории полёта + точка приземления
        if (VisualConfig.trajectories) {
            Item held = player.getMainHandItem().getItem();
            if (held instanceof BowItem || held instanceof CrossbowItem || held instanceof EnderpearlItem || held instanceof SnowballItem || held instanceof WindChargeItem) {
                Vec3 pos = player.getEyePosition(tickDelta).add(0, -0.1, 0);
                Vec3 look = player.getViewVector(tickDelta);
                double power = (held instanceof BowItem || held instanceof CrossbowItem) ? 2.8 : 1.5;
                Vec3 vel = look.scale(power);
                double grav = (held instanceof BowItem || held instanceof CrossbowItem) ? 0.05 : 0.03;

                for (int step = 0; step < 65; step++) {
                    Vec3 next = pos.add(vel);
                    addLine(lines, mat, (float) pos.x, (float) pos.y, (float) pos.z, (float) next.x, (float) next.y, (float) next.z, step % 2 == 0 ? c1 : c2);
                    if (!mc.level.getBlockState(BlockPos.containing(next)).isAir()) {
                        if (VisualConfig.trajectoryLandingBox) {
                            drawCircle(lines, mat, next.x, next.y + 0.05, next.z, 0.45f, c1);
                            drawDiamond(lines, mat, next.x, next.y + 0.35, next.z, 0.25f, now / 200.0, c2);
                        }
                        break;
                    }
                    pos = next;
                    vel = vel.scale(0.99).subtract(0, grav, 0);
                }
            }
        }

        // 5. Частицы ударов и эффект убийства
        hitSparks.removeIf(s -> now - s.spawnTime > 650L);
        for (HitSpark s : hitSparks) {
            float age = (now - s.spawnTime) / 650f;
            Vec3 p = s.pos.add(s.vel.scale(age * 13));
            int alpha = (int) ((1f - age) * 255);
            int col = (alpha << 24) | ((s.style == 1 ? c2 : c1) & 0x00FFFFFF);
            if (s.style == 1) drawDiamond(lines, mat, p.x, p.y, p.z, 0.08f * (1f - age * 0.5f), now / 120.0, col);
            else if (s.style == 2) drawCircle(lines, mat, p.x, p.y, p.z, 0.07f, col);
            else {
                addLine(lines, mat, (float) p.x - 0.07f, (float) p.y, (float) p.z, (float) p.x + 0.07f, (float) p.y, (float) p.z, col);
                addLine(lines, mat, (float) p.x, (float) p.y - 0.07f, (float) p.z, (float) p.x, (float) p.y + 0.07f, (float) p.z, col);
            }
        }

        killBolts.removeIf(b -> now - b.spawnTime > 800L);
        for (KillBolt b : killBolts) {
            float age = (now - b.spawnTime) / 800f;
            int alpha = (int) ((1f - age) * 255);
            int col1 = (alpha << 24) | (c1 & 0x00FFFFFF), col2 = (alpha << 24) | (c2 & 0x00FFFFFF);
            drawCircle(lines, mat, b.pos.x, b.pos.y + 0.1, b.pos.z, 0.5f + age * 2.2f, col1);
            addLine(lines, mat, (float) b.pos.x, (float) b.pos.y, (float) b.pos.z, (float) b.pos.x, (float) (b.pos.y + 4.5), (float) b.pos.z, col2);
        }

        // 6. Погодные частицы + Шлейф + Jump Circles
        if (VisualConfig.weatherParticles > 0) {
            int density = Math.max(10, Math.min(150, VisualConfig.particleDensity));
            for (int i = 0; i < density; i++) {
                double seed = i * 137.5;
                double ox = Math.sin(seed + now / 1800.0) * 10.5, oz = Math.cos(seed + now / 1800.0) * 10.5;
                double dir = (VisualConfig.weatherParticles == 3 || VisualConfig.weatherParticles == 5) ? 1.0 : -1.0;
                double oy = ((seed * 3.1 + dir * (now / 200.0)) % 8.5 + 8.5) % 8.5 - 2.0;
                float px = (float) (player.getX() + ox), py = (float) (player.getY() + oy), pz = (float) (player.getZ() + oz);
                int col = i % 2 == 0 ? c1 : c2;
                addLine(lines, mat, px - 0.07f, py, pz, px + 0.07f, py, pz, col);
                addLine(lines, mat, px, py - 0.07f, pz, px, py + 0.07f, pz, col);
            }
        }

        if (VisualConfig.trails && trailPoints.size() > 1) {
            for (int i = 0; i < trailPoints.size() - 1; i++) {
                TrailPoint p1 = trailPoints.get(i), p2 = trailPoints.get(i + 1);
                int alpha = (int) (((float) i / trailPoints.size()) * 230);
                int col = (alpha << 24) | ((i % 2 == 0 ? c1 : c2) & 0x00FFFFFF);
                addLine(lines, mat, (float) p1.pos.x, (float) (p1.pos.y + 0.2), (float) p1.pos.z, (float) p2.pos.x, (float) (p2.pos.y + 0.2), (float) p2.pos.z, col);
                addLine(lines, mat, (float) p1.pos.x, (float) (p1.pos.y + 1.1), (float) p1.pos.z, (float) p2.pos.x, (float) (p2.pos.y + 1.1), (float) p2.pos.z, col);
            }
        }

        if (VisualConfig.jumpCircles || VisualConfig.footstepRings) {
            Iterator<JumpCircle> it = circles.iterator();
            while (it.hasNext()) {
                JumpCircle jc = it.next();
                float maxAge = jc.isStep ? 0.65f : 1.2f;
                float age = (now - jc.spawnTime) / 1000f;
                if (age > maxAge) { it.remove(); continue; }
                float radius = ((jc.isStep ? 0.12f : 0.3f) + age * (jc.isStep ? 0.55f : 1.45f)) * VisualConfig.accessorySize;
                int alpha = (int) ((1.0f - age / maxAge) * 255);
                drawCircle(lines, mat, jc.pos.x, jc.pos.y + 0.05, jc.pos.z, radius, (alpha << 24) | (c1 & 0x00FFFFFF));
                if (!jc.isStep) drawCircle(lines, mat, jc.pos.x, jc.pos.y + 0.08, jc.pos.z, radius * 0.75f, (alpha << 24) | (c2 & 0x00FFFFFF));
            }
        }
        // 7. Объёмные 3D-Аксессуары с заливкой QUADS
        if (!mc.options.getCameraType().isFirstPerson() || VisualConfig.firstPersonCosmetics) {
            double px = player.xo + (player.getX() - player.xo) * tickDelta;
            double py = player.yo + (player.getY() - player.yo) * tickDelta;
            double pz = player.zo + (player.getZ() - player.zo) * tickDelta;
            float yawRad = (float) Math.toRadians(-player.yBodyRot);
            float sz = VisualConfig.accessorySize;
            double headTop = py + player.getBbHeight() + 0.08;
            double rightX = Math.cos(yawRad), rightZ = -Math.sin(yawRad);
            double fwdX = Math.sin(yawRad), fwdZ = Math.cos(yawRad);

            // Шапки (1..8)
            switch (VisualConfig.hatMode) {
                case 1 -> { // Нимб
                    double fy = headTop + 0.16 + Math.sin(now / 300.0) * 0.04;
                    drawCircle(lines, mat, px, fy, pz, 0.38f * sz, c1);
                    drawCircle(lines, mat, px, fy + 0.03, pz, 0.32f * sz, c2);
                }
                case 2 -> { // Самурайская Каса (3D конус с заливкой)
                    int segs = 24;
                    float r = 0.62f * sz, topH = (float) (headTop + 0.28 * sz);
                    for (int i = 0; i < segs; i++) {
                        double a1 = 2 * Math.PI * i / segs, a2 = 2 * Math.PI * (i + 1) / segs;
                        float x1 = (float) (px + Math.cos(a1) * r), z1 = (float) (pz + Math.sin(a1) * r);
                        float x2 = (float) (px + Math.cos(a2) * r), z2 = (float) (pz + Math.sin(a2) * r);
                        addLine(lines, mat, x1, (float) headTop, z1, x2, (float) headTop, z2, c1);
                        addQuad(quads, mat, x1, (float) headTop, z1, x2, (float) headTop, z2, (float) px, topH, (float) pz, (float) px, topH, (float) pz, fillC1);
                    }
                }
                case 3 -> { // Корона с заливкой
                    int pts = 8;
                    float r = 0.34f * sz;
                    for (int i = 0; i < pts; i++) {
                        double a1 = 2 * Math.PI * i / pts + (now / 1500.0), a2 = 2 * Math.PI * (i + 1) / pts + (now / 1500.0), am = (a1 + a2) * 0.5;
                        float x1 = (float) (px + Math.cos(a1) * r), z1 = (float) (pz + Math.sin(a1) * r);
                        float x2 = (float) (px + Math.cos(a2) * r), z2 = (float) (pz + Math.sin(a2) * r);
                        float xm = (float) (px + Math.cos(am) * (r + 0.04)), zm = (float) (pz + Math.sin(am) * (r + 0.04));
                        addLine(lines, mat, x1, (float) headTop, z1, x2, (float) headTop, z2, c1);
                        addQuad(quads, mat, x1, (float) headTop, z1, x2, (float) headTop, z2, xm, (float) (headTop + 0.22 * sz), zm, xm, (float) (headTop + 0.22 * sz), zm, fillC2);
                    }
                }
                case 4 -> { // Рога
                    for (int s : new int[]{-1, 1}) {
                        float bx = (float) (px + rightX * 0.22 * s * sz), bz = (float) (pz + rightZ * 0.22 * s * sz);
                        float mx = (float) (px + rightX * 0.42 * s * sz), mz = (float) (pz + rightZ * 0.42 * s * sz);
                        float tx = (float) (px + rightX * 0.34 * s * sz), tz = (float) (pz + rightZ * 0.34 * s * sz);
                        addLine(lines, mat, bx, (float) (headTop - 0.05), bz, mx, (float) (headTop + 0.18 * sz), mz, c1);
                        addLine(lines, mat, mx, (float) (headTop + 0.18 * sz), mz, tx, (float) (headTop + 0.38 * sz), tz, c2);
                    }
                }
                case 5 -> { // Объёмные кибер-ушки (3D)
                    for (int s : new int[]{-1, 1}) {
                        float xIn = (float) (px + rightX * 0.10 * s * sz), zIn = (float) (pz + rightZ * 0.10 * s * sz);
                        float xOut = (float) (px + rightX * 0.32 * s * sz), zOut = (float) (pz + rightZ * 0.32 * s * sz);
                        float xTip = (float) (px + rightX * 0.24 * s * sz), zTip = (float) (pz + rightZ * 0.24 * s * sz);
                        addLine(lines, mat, xIn, (float) headTop, zIn, xTip, (float) (headTop + 0.26 * sz), zTip, c1);
                        addLine(lines, mat, xOut, (float) headTop, zOut, xTip, (float) (headTop + 0.26 * sz), zTip, c2);
                        addQuad(quads, mat, xIn, (float) headTop, zIn, xOut, (float) headTop, zOut, xTip, (float) (headTop + 0.26 * sz), zTip, xTip, (float) (headTop + 0.26 * sz), zTip, fillC1);
                    }
                }
                case 6 -> {
                    drawCircle(lines, mat, px, headTop + 0.18, pz, 0.45f * sz, c1);
                    drawDiamond(lines, mat, px, headTop + 0.22, pz, 0.25f * sz, now / 250.0, c2);
                }
                case 7 -> { // Кибер-визор (очки с заливкой перед глазами)
                    float vy1 = (float) (py + player.getBbHeight() - 0.28), vy2 = vy1 + 0.12f * sz;
                    float vx1 = (float) (px + fwdX * 0.28 - rightX * 0.26 * sz), vz1 = (float) (pz + fwdZ * 0.28 - rightZ * 0.26 * sz);
                    float vx2 = (float) (px + fwdX * 0.28 + rightX * 0.26 * sz), vz2 = (float) (pz + fwdZ * 0.28 + rightZ * 0.26 * sz);
                    addQuad(quads, mat, vx1, vy1, vz1, vx2, vy1, vz2, vx2, vy2, vz2, vx1, vy2, vz1, fillC2);
                    addLine(lines, mat, vx1, vy1, vz1, vx2, vy1, vz2, c1);
                    addLine(lines, mat, vx1, vy2, vz1, vx2, vy2, vz2, c1);
                }
                case 8 -> drawDiamond(lines, mat, px, headTop + 0.22, pz, 0.22f * sz, now / 200.0, c1);
            }
            // Спина (1..7) с 3D заливкой
            switch (VisualConfig.backMode) {
                case 1, 7 -> { // Крылья Дракона (1) или Ангельские Крылья (7) с перепонками
                    int wingCol = (VisualConfig.backMode == 7) ? 0xFFFFFFFF : c1;
                    int wingFill = (VisualConfig.backMode == 7) ? 0x55FFFFFF : fillC1;
                    double flap = Math.sin(now / 180.0) * 0.28;
                    for (int side : new int[]{-1, 1}) {
                        float[] tipX = new float[5], tipY = new float[5], tipZ = new float[5];
                        for (int line = 0; line < 5; line++) {
                            double len = (0.78 + line * 0.14) * sz;
                            double angle = yawRad + side * (0.68 + flap);
                            tipX[line] = (float) (px - Math.sin(angle) * len);
                            tipY[line] = (float) (py + 1.42 + (2 - line) * 0.16 * sz);
                            tipZ[line] = (float) (pz - Math.cos(angle) * len);
                            addLine(lines, mat, (float) px, (float) (py + 1.3), (float) pz, tipX[line], tipY[line], tipZ[line], wingCol);
                        }
                        for (int k = 0; k < 4; k++) {
                            addQuad(quads, mat, (float) px, (float) (py + 1.3), (float) pz, tipX[k], tipY[k], tipZ[k], tipX[k + 1], tipY[k + 1], tipZ[k + 1], (float) px, (float) (py + 1.15), (float) pz, wingFill);
                        }
                    }
                }
                case 2 -> { // Физический плащ с заливкой
                    double sway = 0.25 + Math.min(0.8, player.getDeltaMovement().horizontalDistance() * 3.0) + Math.sin(now / 200.0) * 0.08;
                    double backX = -fwdX, backZ = -fwdZ;
                    float topY = (float) (py + 1.42), botY = (float) (py + 0.35);
                    float tlX = (float) (px + backX * 0.18 - rightX * 0.28 * sz), tlZ = (float) (pz + backZ * 0.18 - rightZ * 0.28 * sz);
                    float trX = (float) (px + backX * 0.18 + rightX * 0.28 * sz), trZ = (float) (pz + backZ * 0.18 + rightZ * 0.28 * sz);
                    float blX = (float) (px + backX * (0.25 + sway) - rightX * 0.34 * sz), blZ = (float) (pz + backZ * (0.25 + sway) - rightZ * 0.34 * sz);
                    float brX = (float) (px + backX * (0.25 + sway) + rightX * 0.34 * sz), brZ = (float) (pz + backZ * (0.25 + sway) + rightZ * 0.34 * sz);
                    addQuad(quads, mat, tlX, topY, tlZ, trX, topY, trZ, brX, botY, brZ, blX, botY, blZ, fillC1);
                    addLine(lines, mat, tlX, topY, tlZ, trX, topY, trZ, c1); addLine(lines, mat, blX, botY, blZ, brX, botY, brZ, c1);
                    addLine(lines, mat, tlX, topY, tlZ, blX, botY, blZ, c2); addLine(lines, mat, trX, topY, trZ, brX, botY, brZ, c2);
                }
                case 3 -> {
                    for (int dir : new int[]{-1, 1}) {
                        addLine(lines, mat, (float) (px - fwdX * 0.22 - rightX * 0.45 * dir * sz), (float) (py + 0.55), (float) (pz - fwdZ * 0.22 - rightZ * 0.45 * dir * sz),
                                (float) (px - fwdX * 0.22 + rightX * 0.45 * dir * sz), (float) (py + 1.75 * sz), (float) (pz - fwdZ * 0.22 + rightZ * 0.45 * dir * sz), c1);
                    }
                }
                case 4 -> {
                    for (int s : new int[]{-1, 1}) {
                        float cx = (float) (px - fwdX * 0.25 + rightX * 0.16 * s * sz), cz = (float) (pz - fwdZ * 0.25 + rightZ * 0.16 * s * sz);
                        AABB box = new AABB(cx - 0.08, py + 0.85, cz - 0.08, cx + 0.08, py + 1.4, cz + 0.08);
                        drawBox(lines, mat, box, c1); drawFilledBox(quads, mat, box, fillC1);
                    }
                }
                case 5 -> {
                    drawDiamond(lines, mat, px - fwdX * 0.28, py + 1.15, pz - fwdZ * 0.28, 0.45f * sz, yawRad, c1);
                    drawCircle(lines, mat, px - fwdX * 0.28, py + 1.15, pz - fwdZ * 0.28, 0.35f * sz, c2);
                }
                case 6 -> drawCircle(lines, mat, px - fwdX * 0.3, py + 1.15, pz - fwdZ * 0.3, 0.55f * sz, c1);
            }
            // Питомцы (1..7)
            if (VisualConfig.petMode > 0) {
                double orbit = now / 500.0;
                double petX = px + Math.cos(orbit) * 0.95 * sz, petZ = pz + Math.sin(orbit) * 0.95 * sz;
                double petY = py + 1.35 + Math.sin(now / 250.0) * 0.15;
                switch (VisualConfig.petMode) {
                    case 1 -> drawDiamond(lines, mat, petX, petY, petZ, 0.22f * sz, orbit * 2, c1);
                    case 2 -> { drawCircle(lines, mat, petX, petY, petZ, 0.2f * sz, c1); drawCircle(lines, mat, petX, petY + 0.04, petZ, 0.14f * sz, c2); }
                    case 3 -> {
                        AABB pb = new AABB(petX - 0.15 * sz, petY - 0.15 * sz, petZ - 0.15 * sz, petX + 0.15 * sz, petY + 0.15 * sz, petZ + 0.15 * sz);
                        drawBox(lines, mat, pb, c1); drawFilledBox(quads, mat, pb, fillC1);
                    }
                    case 4 -> {
                        drawBox(lines, mat, new AABB(petX - 0.12 * sz, petY - 0.06 * sz, petZ - 0.12 * sz, petX + 0.12 * sz, petY + 0.06 * sz, petZ + 0.12 * sz), c1);
                        drawCircle(lines, mat, petX, petY + 0.09 * sz, petZ, 0.24f * sz, c2);
                    }
                    case 5 -> drawDiamond(lines, mat, petX, petY, petZ, 0.15f * sz, -orbit * 3, c2);
                    case 6 -> {
                        for (int seg = 0; seg < 8; seg++) {
                            double sa = orbit - seg * 0.22;
                            double sx = px + Math.cos(sa) * 0.95 * sz, szz = pz + Math.sin(sa) * 0.95 * sz;
                            double sy = py + 1.35 + Math.sin(sa * 2.5) * 0.2;
                            drawDiamond(lines, mat, sx, sy, szz, (0.16f - seg * 0.015f) * sz, sa * 2, seg % 2 == 0 ? c1 : c2);
                        }
                    }
                    case 7 -> {
                        drawDiamond(lines, mat, petX, petY, petZ, 0.16f * sz, orbit * 4, c2);
                        drawCircle(lines, mat, petX, petY, petZ, 0.26f * sz, c1);
                    }
                }
            }
        }

        poseStack.popPose();
        MeshData quadMesh = quads.build();
        if (quadMesh != null) BufferUploader.drawWithShader(quadMesh);
        MeshData lineMesh = lines.build();
        if (lineMesh != null) BufferUploader.drawWithShader(lineMesh);

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

    private static void drawFilledBox(BufferBuilder buf, Matrix4f mat, AABB b, int col) {
        float x1 = (float) b.minX, y1 = (float) b.minY, z1 = (float) b.minZ, x2 = (float) b.maxX, y2 = (float) b.maxY, z2 = (float) b.maxZ;
        addQuad(buf, mat, x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2, col);
        addQuad(buf, mat, x1, y2, z1, x1, y2, z2, x2, y2, z2, x2, y2, z1, col);
        addQuad(buf, mat, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1, col);
        addQuad(buf, mat, x2, y1, z2, x2, y2, z2, x1, y2, z2, x1, y1, z2, col);
        addQuad(buf, mat, x1, y1, z2, x1, y2, z2, x1, y2, z1, x1, y1, z1, col);
        addQuad(buf, mat, x2, y1, z1, x2, y2, z1, x2, y2, z2, x2, y1, z2, col);
    }

    private static void addQuad(BufferBuilder buf, Matrix4f mat, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, float x4, float y4, float z4, int col) {
        int a = (col >> 24) & 0xFF, r = (col >> 16) & 0xFF, g = (col >> 8) & 0xFF, b = col & 0xFF;
        buf.addVertex(mat, x1, y1, z1).setColor(r, g, b, a);
        buf.addVertex(mat, x2, y2, z2).setColor(r, g, b, a);
        buf.addVertex(mat, x3, y3, z3).setColor(r, g, b, a);
        buf.addVertex(mat, x4, y4, z4).setColor(r, g, b, a);
    }

    private static void addLine(BufferBuilder buf, Matrix4f mat, float x1, float y1, float z1, float x2, float y2, float z2, int col) {
        int a = (col >> 24) & 0xFF; if (a == 0) a = 255;
        buf.addVertex(mat, x1, y1, z1).setColor((col >> 16) & 0xFF, (col >> 8) & 0xFF, col & 0xFF, a);
        buf.addVertex(mat, x2, y2, z2).setColor((col >> 16) & 0xFF, (col >> 8) & 0xFF, col & 0xFF, a);
    }
}
