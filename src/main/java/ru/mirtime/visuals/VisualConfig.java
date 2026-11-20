package ru.mirtime.visuals;

import net.fabricmc.loader.api.FabricLoader;
import java.awt.Color;
import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class VisualConfig {
    // === 1. HUD & ИНТЕРФЕЙС ===
    public static boolean watermark = true;
    public static int watermarkStyle = 0; // 0=Cyber Bar, 1=Minimal Pill, 2=Full Stats
    public static float watermarkX = 10f, watermarkY = 10f;

    public static boolean keystrokes = true;
    public static boolean keystrokesCps = true;
    public static float keystrokesX = 10f, keystrokesY = 40f;

    public static boolean armorHud = true;
    public static boolean armorDurability = true;
    public static float armorX = -1f, armorY = -1f;

    public static boolean potionsHud = true;
    public static float potionsX = 10f, potionsY = 150f;

    public static boolean targetHud = true;
    public static int targetHudStyle = 0; // 0=Modern Card, 1=Compact Neon, 2=Minimalist
    public static float targetHudX = -1f, targetHudY = -1f;

    public static boolean arrayList = true;
    public static boolean coordinatesHud = true;
    public static boolean fpsPingHud = true;
    public static boolean hotbarGlow = true;
    public static boolean customCrosshair = true;
    public static int crosshairStyle = 0; // 0=Крест, 1=Круг, 2=Скобки, 3=Вращение
    public static boolean dynamicCrosshair = true;
    public static boolean inventoryGlass = true;
    public static boolean streamerMode = false;

    // === 2. КОСМЕТИКА (3D) ===
    public static int hatMode = 1; // 0=Выкл, 1=Нимб, 2=Шляпа, 3=Корона, 4=Рога, 5=Ушки, 6=Орбита
    public static int backMode = 1; // 0=Выкл, 1=Крылья, 2=Плащ, 3=Катаны, 4=Джетпак, 5=Щит
    public static int petMode = 1; // 0=Выкл, 1=Кристалл, 2=Сфера, 3=Куб, 4=Дрон, 5=Дух
    public static boolean trails = true;
    public static int trailStyle = 0; // 0=Линии, 1=Лента, 2=Кристаллы
    public static float trailLength = 1.0f;
    public static boolean jumpCircles = true;
    public static int jumpCircleStyle = 0; // 0=Двойное, 1=Волна, 2=Звезда
    public static boolean footstepRings = false;
    public static boolean bodyAura = false;
    public static float accessorySize = 1.0f;
    public static boolean firstPersonCosmetics = false;
    // === 3. PVP & БОЙ ===
    public static boolean targetEsp = true;
    public static int targetEspStyle = 0; // 0=Волна, 1=Спираль, 2=Ромб, 3=Призраки, 4=Сканер
    public static float targetEspSpeed = 1.0f;
    public static boolean customHitboxes = false;
    public static boolean hitboxFill = false;
    public static boolean hitParticles = true;
    public static int hitParticleStyle = 0; // 0=Искры, 1=Звёзды, 2=Сферы
    public static int hitParticleCount = 20;
    public static boolean hitSoundEffect = false;
    public static boolean killEffect = true;
    public static boolean trajectories = true;
    public static boolean trajectoryLandingBox = true;
    public static boolean pearlTracker = true;
    public static boolean itemPhysics = true;
    public static boolean noHurtCam = true;
    public static boolean smoothZoom = true;
    public static float zoomFactor = 3.5f;
    public static float zoomSpeed = 0.15f;

    // === 4. РУКА (VIEWMODEL) ===
    public static boolean handView = true;
    public static float handScale = 0.85f;
    public static float handOffsetX = 0.05f;
    public static float handOffsetY = -0.04f;
    public static float handOffsetZ = -0.10f;
    public static float handRotX = 0.0f;
    public static float handRotY = 0.0f;
    public static float handRotZ = 0.0f;
    public static int swingAnimation = 1; // 0=Ваниль, 1=Smooth Spin, 2=Block Hit, 3=Slap, 4=Power Down
    public static float swingSpeed = 1.0f;
    public static boolean leftHandView = false;

    // === 5. МИР & АТМОСФЕРА ===
    public static int timeMode = 0; // 0=Сервер, 1=День, 2=Закат, 3=Ночь, 4=Ускоренный
    public static int weatherParticles = 1; // 0=Выкл, 1=Звёзды, 2=Снег, 3=Искры, 4=Дождь, 5=Светлячки
    public static int particleDensity = 60;
    public static boolean customFog = false;
    public static float fogStart = 10f;
    public static float fogEnd = 80f;
    public static boolean fullbright = true;
    public static boolean aspectRatio = false;
    public static float aspectRatioValue = 1.77f;
    public static boolean customSkyColor = false;
    public static boolean chinaHatOnFriends = false;

    // === 6. ТЕМА & ЦВЕТА ===
    public static int themeMode = 0; // 0..7
    public static float rgbSpeed = 2.0f;
    public static float guiBlurAlpha = 0.65f;
    public static float guiScale = 1.0f;
    public static int getPrimaryColor() {
        float speedMs = Math.max(500f, 6000f / Math.max(0.2f, rgbSpeed));
        return switch (themeMode) {
            case 1 -> 0xFF00FF88; // Matrix Green
            case 2 -> 0xFFFF1E56; // Blood Crimson
            case 3 -> 0xFFFFD700; // Imperial Gold
            case 4 -> 0xFF9D00FF; // Void Purple
            case 5 -> Color.HSBtoRGB((System.currentTimeMillis() % (long) speedMs) / speedMs, 0.85f, 1.0f); // RGB
            case 6 -> 0xFF00E5FF; // MIR-TIME Cyan
            case 7 -> 0xFFFF6B00; // Solar Orange
            default -> 0xFFFF007F; // Cyber Pink
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
            case 1 -> "Неоновый Нимб";
            case 2 -> "Кибер-Шляпа";
            case 3 -> "Корона Императора";
            case 4 -> "Демон-Рога";
            case 5 -> "Кибер-Ушки";
            case 6 -> "Орбитальные Кольца";
            default -> "Выкл";
        };
    }

    public static String getBackName() {
        return switch (backMode) {
            case 1 -> "Крылья Дракона";
            case 2 -> "Физический Плащ";
            case 3 -> "Две Катаны";
            case 4 -> "Кибер-Джетпак";
            case 5 -> "Энерго-Щит";
            default -> "Выкл";
        };
    }

    public static String getPetName() {
        return switch (petMode) {
            case 1 -> "Кристалл-Спутник";
            case 2 -> "Огненная Сфера";
            case 3 -> "Куб Пустоты";
            case 4 -> "Мини-Дрон";
            case 5 -> "Дух-Огонёк";
            default -> "Выкл";
        };
    }

    public static String getThemeName() {
        return switch (themeMode) {
            case 1 -> "Matrix Green";
            case 2 -> "Blood Crimson";
            case 3 -> "Imperial Gold";
            case 4 -> "Void Purple";
            case 5 -> "RGB Перелив";
            case 6 -> "MIR-TIME Cyan";
            case 7 -> "Solar Orange";
            default -> "Cyber Pink";
        };
    }

    public static String getTimeModeName() {
        return switch (timeMode) {
            case 1 -> "Вечный День";
            case 2 -> "Кибер-Закат";
            case 3 -> "Неоновая Ночь";
            case 4 -> "Ускоренный Цикл";
            default -> "Как на сервере";
        };
    }

    public static String getWeatherName() {
        return switch (weatherParticles) {
            case 1 -> "Неоновые Звёзды";
            case 2 -> "Снежинки";
            case 3 -> "Искры Пламени";
            case 4 -> "Кибер-Дождь";
            case 5 -> "Светлячки";
            default -> "Выкл";
        };
    }

    public static String getSwingName() {
        return switch (swingAnimation) {
            case 1 -> "Smooth Spin";
            case 2 -> "Block Hit";
            case 3 -> "Cyber Slap";
            case 4 -> "Power Down";
            default -> "Ваниль";
        };
    }

    public static String getTargetEspName() {
        return switch (targetEspStyle) {
            case 1 -> "Спираль";
            case 2 -> "Ромб";
            case 3 -> "Призраки";
            case 4 -> "Сканер";
            default -> "Волна";
        };
    }
    public static void save() {
        try {
            Path file = FabricLoader.getInstance().getConfigDir().resolve("mtvisuals.properties");
            Properties p = new Properties();
            // HUD
            p.setProperty("watermark", String.valueOf(watermark));
            p.setProperty("watermarkStyle", String.valueOf(watermarkStyle));
            p.setProperty("watermarkX", String.valueOf(watermarkX));
            p.setProperty("watermarkY", String.valueOf(watermarkY));
            p.setProperty("keystrokes", String.valueOf(keystrokes));
            p.setProperty("keystrokesCps", String.valueOf(keystrokesCps));
            p.setProperty("keystrokesX", String.valueOf(keystrokesX));
            p.setProperty("keystrokesY", String.valueOf(keystrokesY));
            p.setProperty("armorHud", String.valueOf(armorHud));
            p.setProperty("armorDurability", String.valueOf(armorDurability));
            p.setProperty("armorX", String.valueOf(armorX));
            p.setProperty("armorY", String.valueOf(armorY));
            p.setProperty("potionsHud", String.valueOf(potionsHud));
            p.setProperty("potionsX", String.valueOf(potionsX));
            p.setProperty("potionsY", String.valueOf(potionsY));
            p.setProperty("targetHud", String.valueOf(targetHud));
            p.setProperty("targetHudStyle", String.valueOf(targetHudStyle));
            p.setProperty("targetHudX", String.valueOf(targetHudX));
            p.setProperty("targetHudY", String.valueOf(targetHudY));
            p.setProperty("arrayList", String.valueOf(arrayList));
            p.setProperty("coordinatesHud", String.valueOf(coordinatesHud));
            p.setProperty("fpsPingHud", String.valueOf(fpsPingHud));
            p.setProperty("hotbarGlow", String.valueOf(hotbarGlow));
            p.setProperty("customCrosshair", String.valueOf(customCrosshair));
            p.setProperty("crosshairStyle", String.valueOf(crosshairStyle));
            p.setProperty("dynamicCrosshair", String.valueOf(dynamicCrosshair));
            p.setProperty("inventoryGlass", String.valueOf(inventoryGlass));
            p.setProperty("streamerMode", String.valueOf(streamerMode));
            // Косметика
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
            // PvP
            p.setProperty("targetEsp", String.valueOf(targetEsp));
            p.setProperty("targetEspStyle", String.valueOf(targetEspStyle));
            p.setProperty("targetEspSpeed", String.valueOf(targetEspSpeed));
            p.setProperty("customHitboxes", String.valueOf(customHitboxes));
            p.setProperty("hitboxFill", String.valueOf(hitboxFill));
            p.setProperty("hitParticles", String.valueOf(hitParticles));
            p.setProperty("hitParticleStyle", String.valueOf(hitParticleStyle));
            p.setProperty("hitParticleCount", String.valueOf(hitParticleCount));
            p.setProperty("hitSoundEffect", String.valueOf(hitSoundEffect));
            p.setProperty("killEffect", String.valueOf(killEffect));
            p.setProperty("trajectories", String.valueOf(trajectories));
            p.setProperty("trajectoryLandingBox", String.valueOf(trajectoryLandingBox));
            p.setProperty("pearlTracker", String.valueOf(pearlTracker));
            p.setProperty("itemPhysics", String.valueOf(itemPhysics));
            p.setProperty("noHurtCam", String.valueOf(noHurtCam));
            p.setProperty("smoothZoom", String.valueOf(smoothZoom));
            p.setProperty("zoomFactor", String.valueOf(zoomFactor));
            p.setProperty("zoomSpeed", String.valueOf(zoomSpeed));
            // Рука
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
            // Мир & Тема
            p.setProperty("timeMode", String.valueOf(timeMode));
            p.setProperty("weatherParticles", String.valueOf(weatherParticles));
            p.setProperty("particleDensity", String.valueOf(particleDensity));
            p.setProperty("customFog", String.valueOf(customFog));
            p.setProperty("fogStart", String.valueOf(fogStart));
            p.setProperty("fogEnd", String.valueOf(fogEnd));
            p.setProperty("fullbright", String.valueOf(fullbright));
            p.setProperty("aspectRatio", String.valueOf(aspectRatio));
            p.setProperty("aspectRatioValue", String.valueOf(aspectRatioValue));
            p.setProperty("customSkyColor", String.valueOf(customSkyColor));
            p.setProperty("themeMode", String.valueOf(themeMode));
            p.setProperty("rgbSpeed", String.valueOf(rgbSpeed));
            p.setProperty("guiBlurAlpha", String.valueOf(guiBlurAlpha));
            p.setProperty("guiScale", String.valueOf(guiScale));

            try (OutputStream out = new FileOutputStream(file.toFile())) {
                p.store(out, "MTVisuals v4.0 ULTRA Config");
            }
        } catch (Exception ignored) {}
    }

    public static void load() {
        try {
            Path file = FabricLoader.getInstance().getConfigDir().resolve("mtvisuals.properties");
            if (!file.toFile().exists()) return;
            Properties p = new Properties();
            try (InputStream in = new FileInputStream(file.toFile())) {
                p.load(in);
            }
            watermark = Boolean.parseBoolean(p.getProperty("watermark", "true"));
            watermarkStyle = Integer.parseInt(p.getProperty("watermarkStyle", "0"));
            watermarkX = Float.parseFloat(p.getProperty("watermarkX", "10.0"));
            watermarkY = Float.parseFloat(p.getProperty("watermarkY", "10.0"));
            keystrokes = Boolean.parseBoolean(p.getProperty("keystrokes", "true"));
            keystrokesCps = Boolean.parseBoolean(p.getProperty("keystrokesCps", "true"));
            keystrokesX = Float.parseFloat(p.getProperty("keystrokesX", "10.0"));
            keystrokesY = Float.parseFloat(p.getProperty("keystrokesY", "40.0"));
            armorHud = Boolean.parseBoolean(p.getProperty("armorHud", "true"));
            armorDurability = Boolean.parseBoolean(p.getProperty("armorDurability", "true"));
            armorX = Float.parseFloat(p.getProperty("armorX", "-1.0"));
            armorY = Float.parseFloat(p.getProperty("armorY", "-1.0"));
            potionsHud = Boolean.parseBoolean(p.getProperty("potionsHud", "true"));
            potionsX = Float.parseFloat(p.getProperty("potionsX", "10.0"));
            potionsY = Float.parseFloat(p.getProperty("potionsY", "150.0"));
            targetHud = Boolean.parseBoolean(p.getProperty("targetHud", "true"));
            targetHudStyle = Integer.parseInt(p.getProperty("targetHudStyle", "0"));
            targetHudX = Float.parseFloat(p.getProperty("targetHudX", "-1.0"));
            targetHudY = Float.parseFloat(p.getProperty("targetHudY", "-1.0"));
            arrayList = Boolean.parseBoolean(p.getProperty("arrayList", "true"));
            coordinatesHud = Boolean.parseBoolean(p.getProperty("coordinatesHud", "true"));
            fpsPingHud = Boolean.parseBoolean(p.getProperty("fpsPingHud", "true"));
            hotbarGlow = Boolean.parseBoolean(p.getProperty("hotbarGlow", "true"));
            customCrosshair = Boolean.parseBoolean(p.getProperty("customCrosshair", "true"));
            crosshairStyle = Integer.parseInt(p.getProperty("crosshairStyle", "0"));
            dynamicCrosshair = Boolean.parseBoolean(p.getProperty("dynamicCrosshair", "true"));
            inventoryGlass = Boolean.parseBoolean(p.getProperty("inventoryGlass", "true"));
            streamerMode = Boolean.parseBoolean(p.getProperty("streamerMode", "false"));
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
            hitParticleCount = Integer.parseInt(p.getProperty("hitParticleCount", "20"));
            hitSoundEffect = Boolean.parseBoolean(p.getProperty("hitSoundEffect", "false"));
            killEffect = Boolean.parseBoolean(p.getProperty("killEffect", "true"));
            trajectories = Boolean.parseBoolean(p.getProperty("trajectories", "true"));
            trajectoryLandingBox = Boolean.parseBoolean(p.getProperty("trajectoryLandingBox", "true"));
            pearlTracker = Boolean.parseBoolean(p.getProperty("pearlTracker", "true"));
            itemPhysics = Boolean.parseBoolean(p.getProperty("itemPhysics", "true"));
            noHurtCam = Boolean.parseBoolean(p.getProperty("noHurtCam", "true"));
            smoothZoom = Boolean.parseBoolean(p.getProperty("smoothZoom", "true"));
            zoomFactor = Float.parseFloat(p.getProperty("zoomFactor", "3.5"));
            zoomSpeed = Float.parseFloat(p.getProperty("zoomSpeed", "0.15"));

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
            fogStart = Float.parseFloat(p.getProperty("fogStart", "10.0"));
            fogEnd = Float.parseFloat(p.getProperty("fogEnd", "80.0"));
            fullbright = Boolean.parseBoolean(p.getProperty("fullbright", "true"));
            aspectRatio = Boolean.parseBoolean(p.getProperty("aspectRatio", "false"));
            aspectRatioValue = Float.parseFloat(p.getProperty("aspectRatioValue", "1.77"));
            customSkyColor = Boolean.parseBoolean(p.getProperty("customSkyColor", "false"));
            themeMode = Integer.parseInt(p.getProperty("themeMode", "0"));
            rgbSpeed = Float.parseFloat(p.getProperty("rgbSpeed", "2.0"));
            guiBlurAlpha = Float.parseFloat(p.getProperty("guiBlurAlpha", "0.65"));
            guiScale = Float.parseFloat(p.getProperty("guiScale", "1.0"));
        } catch (Exception ignored) {}
    }
}
