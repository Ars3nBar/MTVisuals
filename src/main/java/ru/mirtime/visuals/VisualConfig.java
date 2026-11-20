package ru.mirtime.visuals;

import java.awt.Color;

public class VisualConfig {

    // Вкладка 1: Интерфейс (HUD)
    public static boolean watermark = true;
    public static boolean keystrokes = true;
    public static boolean targetHud = true;
    public static boolean customCrosshair = true;
    public static boolean inventoryGlass = true;
    public static boolean streamerMode = false;

    // Вкладка 2: 3D Косметика (Аксессуары на игроке)
    public static int hatMode = 1;       // 0=Выкл, 1=Неоновый Нимб, 2=Кибер-Шляпа, 3=Корона
    public static int backMode = 1;      // 0=Выкл, 1=Крылья, 2=Физический Плащ, 3=Катаны
    public static boolean trails = true; // Неоновый шлейф при беге
    public static boolean jumpCircles = true;

    // Вкладка 3: Бой и Мир (PvP & World)
    public static boolean targetEsp = true;      // Сканер-кольцо вокруг цели
    public static boolean customHitboxes = true; // Неоновые рамки сущностей
    public static boolean handView = true;       // Компактная рука и меч
    public static boolean noHurtCam = true;      // Убрать тряску экрана при ударе
    public static boolean customFog = true;      // Неоновый цвет тумана

    // Режимы времени (0=Сервер, 1=День, 2=Закат, 3=Ночь, 4=Цикл) и Цвета (0..4)
    public static int timeMode = 2;
    public static int themeMode = 0;

    public static int getPrimaryColor() {
        return switch (themeMode) {
            case 1 -> 0xFFD900FF; // Cyber Pink
            case 2 -> 0xFF00FF66; // Toxic Green
            case 3 -> 0xFFFF2A55; // Blood Crimson
            case 4 -> Color.HSBtoRGB((System.currentTimeMillis() % 3500L) / 3500f, 0.85f, 1.0f); // RGB
            default -> 0xFF00E5FF; // MIR-TIME Cyan
        };
    }

    public static int getSecondaryColor() {
        return switch (themeMode) {
            case 1 -> 0xFF6A00FF;
            case 2 -> 0xFF008844;
            case 3 -> 0xFF880022;
            case 4 -> Color.HSBtoRGB(((System.currentTimeMillis() + 900L) % 3500L) / 3500f, 0.85f, 1.0f);
            default -> 0xFF0055FF;
        };
    }

    public static String getHatName() {
        return switch (hatMode) {
            case 1 -> "Неоновый Нимб";
            case 2 -> "Кибер-Шляпа";
            case 3 -> "Корона Императора";
            default -> "Выкл";
        };
    }

    public static String getBackName() {
        return switch (backMode) {
            case 1 -> "Неоновые Крылья";
            case 2 -> "Физический Плащ";
            case 3 -> "Две Катаны";
            default -> "Выкл";
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

    public static String getThemeName() {
        return switch (themeMode) {
            case 1 -> "Cyber Pink";
            case 2 -> "Toxic Green";
            case 3 -> "Blood Crimson";
            case 4 -> "RGB Перелив";
            default -> "MIR-TIME Cyan";
        };
    }
}
