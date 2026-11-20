package ru.mirtime.visuals;

import net.fabricmc.loader.api.FabricLoader;
import java.awt.Color;
import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class VisualConfig {

    // Вкладка 1: Интерфейс (HUD)
    public static boolean watermark = true;
    public static boolean keystrokes = true;
    public static boolean armorHud = true;
    public static boolean potionsHud = true;
    public static boolean targetHud = true;
    public static boolean customCrosshair = true;
    public static boolean inventoryGlass = true;
    public static boolean streamerMode = false;
    public static int crosshairStyle = 0; // 0=Плюс, 1=Круг, 2=Точка+Уголки

    // Вкладка 2: 3D Косметика
    public static int hatMode = 1;        // 0=Выкл, 1=Нимб, 2=Кибер-Шляпа, 3=Корона, 4=Демон-Рога, 5=Кошачьи Ушки
    public static int backMode = 1;       // 0=Выкл, 1=Крылья, 2=Плащ, 3=Две Катаны, 4=Кибер-Рюкзак
    public static int petMode = 1;        // 0=Выкл, 1=Кристалл-Спутник, 2=Огненная Сфера, 3=Куб Пустоты
    public static boolean trails = true;
    public static boolean jumpCircles = true;
    public static float accessorySize = 1.0f; // 0.7x .. 1.5x

    // Вкладка 3: Бой (PvP)
    public static boolean targetEsp = true;
    public static int targetEspStyle = 0; // 0=Двойное кольцо, 1=Спираль, 2=Прицельный ромб
    public static boolean customHitboxes = true;
    public static boolean hitParticles = true;
    public static boolean trajectories = true; // Траектория полёта стрел и жемчуга
    public static boolean noHurtCam = true;
    public static float zoomFactor = 3.0f;

    // Вкладка 4: Рука (ViewModel)
    public static boolean handView = true;
    public static float handScale = 0.85f;
    public static float handOffsetX = 0.05f;
    public static float handOffsetY = -0.04f;

    // Вкладка 5: Мир и Цвета
    public static int timeMode = 2;       // 0=Сервер, 1=День, 2=Закат, 3=Ночь, 4=Цикл
    public static int weatherParticles = 1; // 0=Выкл, 1=Неон-Звёзды, 2=Снежинки, 3=Искры пламени
    public static boolean customFog = true;
    public static boolean fullbright = true;
    public static int themeMode = 0;      // 0..6 (7 цветовых тем!)
    public static float rgbSpeed = 1.0f;  // 0.5x .. 3.0x
    public static int getPrimaryColor() {
        long speedMs = (long) (3500L / Math.max(0.2f, rgbSpeed));
        return switch (themeMode) {
            case 1 -> 0xFFD900FF; // Cyber Pink
            case 2 -> 0xFF00FF66; // Matrix Green
            case 3 -> 0xFFFF2A55; // Blood Crimson
            case 4 -> 0xFFFFD700; // Imperial Gold
            case 5 -> 0xFF9D4EDD; // Void Purple
            case 6 -> Color.HSBtoRGB((System.currentTimeMillis() % speedMs) / (float) speedMs, 0.85f, 1.0f); // RGB
            default -> 0xFF00E5FF; // MIR-TIME Cyan
        };
    }

    public static int getSecondaryColor() {
        long speedMs = (long) (3500L / Math.max(0.2f, rgbSpeed));
        return switch (themeMode) {
            case 1 -> 0xFF6A00FF;
            case 2 -> 0xFF008844;
            case 3 -> 0xFF880022;
            case 4 -> 0xFFFF8800;
            case 5 -> 0xFF3C096C;
            case 6 -> Color.HSBtoRGB(((System.currentTimeMillis() + speedMs / 4) % speedMs) / (float) speedMs, 0.85f, 1.0f);
            default -> 0xFF0055FF;
        };
    }

    public static String getHatName() {
        return switch (hatMode) {
            case 1 -> "Неоновый Нимб";
            case 2 -> "Кибер-Шляпа";
            case 3 -> "Корона Императора";
            case 4 -> "Демон-Рога";
            case 5 -> "Кибер-Ушки";
            default -> "Выкл";
        };
    }

    public static String getBackName() {
        return switch (backMode) {
            case 1 -> "Крылья Дракона";
            case 2 -> "Физический Плащ";
            case 3 -> "Две Катаны";
            case 4 -> "Кибер-Джетпак";
            default -> "Выкл";
        };
    }

    public static String getPetName() {
        return switch (petMode) {
            case 1 -> "Кристалл-Спутник";
            case 2 -> "Огненная Сфера";
            case 3 -> "Куб Пустоты";
            default -> "Выкл";
        };
    }

    public static String getThemeName() {
        return switch (themeMode) {
            case 1 -> "Cyber Pink";
            case 2 -> "Matrix Green";
            case 3 -> "Blood Crimson";
            case 4 -> "Imperial Gold";
            case 5 -> "Void Purple";
            case 6 -> "RGB Перелив";
            default -> "MIR-TIME Cyan";
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
            default -> "Выкл";
        };
    }

    public static void save() {
        try {
            Path file = FabricLoader.getInstance().getConfigDir().resolve("mtvisuals.properties");
            Properties p = new Properties();
            p.setProperty("watermark", String.valueOf(watermark));
            p.setProperty("keystrokes", String.valueOf(keystrokes));
            p.setProperty("armorHud", String.valueOf(armorHud));
            p.setProperty("potionsHud", String.valueOf(potionsHud));
            p.setProperty("targetHud", String.valueOf(targetHud));
            p.setProperty("customCrosshair", String.valueOf(customCrosshair));
            p.setProperty("inventoryGlass", String.valueOf(inventoryGlass));
            p.setProperty("streamerMode", String.valueOf(streamerMode));
            p.setProperty("crosshairStyle", String.valueOf(crosshairStyle));
            p.setProperty("hatMode", String.valueOf(hatMode));
            p.setProperty("backMode", String.valueOf(backMode));
            p.setProperty("petMode", String.valueOf(petMode));
            p.setProperty("trails", String.valueOf(trails));
            p.setProperty("jumpCircles", String.valueOf(jumpCircles));
            p.setProperty("accessorySize", String.valueOf(accessorySize));
            p.setProperty("targetEsp", String.valueOf(targetEsp));
            p.setProperty("targetEspStyle", String.valueOf(targetEspStyle));
            p.setProperty("customHitboxes", String.valueOf(customHitboxes));
            p.setProperty("hitParticles", String.valueOf(hitParticles));
            p.setProperty("trajectories", String.valueOf(trajectories));
            p.setProperty("noHurtCam", String.valueOf(noHurtCam));
            p.setProperty("zoomFactor", String.valueOf(zoomFactor));
            p.setProperty("handView", String.valueOf(handView));
            p.setProperty("handScale", String.valueOf(handScale));
            p.setProperty("handOffsetX", String.valueOf(handOffsetX));
            p.setProperty("handOffsetY", String.valueOf(handOffsetY));
            p.setProperty("timeMode", String.valueOf(timeMode));
            p.setProperty("weatherParticles", String.valueOf(weatherParticles));
            p.setProperty("customFog", String.valueOf(customFog));
            p.setProperty("fullbright", String.valueOf(fullbright));
            p.setProperty("themeMode", String.valueOf(themeMode));
            p.setProperty("rgbSpeed", String.valueOf(rgbSpeed));
            try (OutputStream out = new FileOutputStream(file.toFile())) { p.store(out, "MTVisuals Config"); }
        } catch (Exception ignored) {}
    }

    public static void load() {
        try {
            Path file = FabricLoader.getInstance().getConfigDir().resolve("mtvisuals.properties");
            if (!file.toFile().exists()) return;
            Properties p = new Properties();
            try (InputStream in = new FileInputStream(file.toFile())) { p.load(in); }
            watermark = Boolean.parseBoolean(p.getProperty("watermark", "true"));
            keystrokes = Boolean.parseBoolean(p.getProperty("keystrokes", "true"));
            armorHud = Boolean.parseBoolean(p.getProperty("armorHud", "true"));
            potionsHud = Boolean.parseBoolean(p.getProperty("potionsHud", "true"));
            targetHud = Boolean.parseBoolean(p.getProperty("targetHud", "true"));
            customCrosshair = Boolean.parseBoolean(p.getProperty("customCrosshair", "true"));
            inventoryGlass = Boolean.parseBoolean(p.getProperty("inventoryGlass", "true"));
            streamerMode = Boolean.parseBoolean(p.getProperty("streamerMode", "false"));
            crosshairStyle = Integer.parseInt(p.getProperty("crosshairStyle", "0"));
            hatMode = Integer.parseInt(p.getProperty("hatMode", "1"));
            backMode = Integer.parseInt(p.getProperty("backMode", "1"));
            petMode = Integer.parseInt(p.getProperty("petMode", "1"));
            trails = Boolean.parseBoolean(p.getProperty("trails", "true"));
            jumpCircles = Boolean.parseBoolean(p.getProperty("jumpCircles", "true"));
            accessorySize = Float.parseFloat(p.getProperty("accessorySize", "1.0"));
            targetEsp = Boolean.parseBoolean(p.getProperty("targetEsp", "true"));
            targetEspStyle = Integer.parseInt(p.getProperty("targetEspStyle", "0"));
            customHitboxes = Boolean.parseBoolean(p.getProperty("customHitboxes", "true"));
            hitParticles = Boolean.parseBoolean(p.getProperty("hitParticles", "true"));
            trajectories = Boolean.parseBoolean(p.getProperty("trajectories", "true"));
            noHurtCam = Boolean.parseBoolean(p.getProperty("noHurtCam", "true"));
            zoomFactor = Float.parseFloat(p.getProperty("zoomFactor", "3.0"));
            handView = Boolean.parseBoolean(p.getProperty("handView", "true"));
            handScale = Float.parseFloat(p.getProperty("handScale", "0.85"));
            handOffsetX = Float.parseFloat(p.getProperty("handOffsetX", "0.05"));
            handOffsetY = Float.parseFloat(p.getProperty("handOffsetY", "-0.04"));
            timeMode = Integer.parseInt(p.getProperty("timeMode", "2"));
            weatherParticles = Integer.parseInt(p.getProperty("weatherParticles", "1"));
            customFog = Boolean.parseBoolean(p.getProperty("customFog", "true"));
            fullbright = Boolean.parseBoolean(p.getProperty("fullbright", "true"));
            themeMode = Integer.parseInt(p.getProperty("themeMode", "0"));
            rgbSpeed = Float.parseFloat(p.getProperty("rgbSpeed", "1.0"));
        } catch (Exception ignored) {}
    }
}
