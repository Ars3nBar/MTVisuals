package ru.mirtime.visuals;

import java.awt.Color;

public class VisualConfig {

    // Включённые визуальные модули
    public static boolean watermark = true;
    public static boolean keystrokes = true;
    public static boolean targetHud = true;
    public static boolean inventoryGlass = true;

    // 3D Косметика и мир
    public static boolean haloHat = true;         // Неоновый нимб над головой
    public static boolean cyberWings = true;      // Крылья за спиной
    public static boolean jumpCircles = true;     // Кольца под ногами при прыжке
    public static boolean customHitboxes = true;  // Неоновые 3D-хитбоксы сущностей
    public static boolean handView = true;        // Красивое положение меча/руки (ViewModel)

    // Управление временем (0 = Серверное, 1 = День, 2 = Закат, 3 = Ночь, 4 = Плавный цикл)
    public static int timeMode = 2;
    public static long customTimeTick = 12800L; // Закат по умолчанию

    // Цветовая тема (0 = MIR-TIME Blue, 1 = Cyberpunk Pink, 2 = Toxic Green, 3 = RGB Перелив)
    public static int themeMode = 0;

    public static int getPrimaryColor() {
        return switch (themeMode) {
            case 1 -> 0xFFD900FF;
            case 2 -> 0xFF00FF66;
            case 3 -> Color.HSBtoRGB((System.currentTimeMillis() % 4000L) / 4000f, 0.85f, 1.0f);
            default -> 0xFF00E5FF;
        };
    }

    public static int getSecondaryColor() {
        return switch (themeMode) {
            case 1 -> 0xFF6A00FF;
            case 2 -> 0xFF008844;
            case 3 -> Color.HSBtoRGB(((System.currentTimeMillis() + 1000L) % 4000L) / 4000f, 0.85f, 1.0f);
            default -> 0xFF0055FF;
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
            case 2 -> "Matrix Green";
            case 3 -> "RGB Перелив";
            default -> "MIR-TIME Cyan";
        };
    }
}
