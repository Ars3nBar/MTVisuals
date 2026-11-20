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
