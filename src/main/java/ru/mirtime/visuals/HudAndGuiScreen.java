package ru.mirtime.visuals;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class HudAndGuiScreen extends Screen {

    private static int activeTab = 0;
    private static float scrollY = 0f, targetScrollY = 0f, openAnim = 0f;

    // 0=Watermark, 1=Keystrokes, 2=Armor, 3=Potions, 4=TargetHUD
    private static int draggingHud = -1;
    private static int resizingHud = -1;
    private static float dragOffsetX = 0, dragOffsetY = 0;
    private static float resizeStartX = 0, resizeStartScale = 1.0f;
    private static Float snapLineX = null, snapLineY = null;

    // Анимации и PvP-счётчики
    private static float animatedTargetHp = 20f;
    private static int comboCount = 0;
    private static float lastReach = 0f;
    private static long lastHitTime = 0L;
    private static int lastHurtTime = 0;
    private static final List<Long> leftClicks = new ArrayList<>();
    private static final List<Long> rightClicks = new ArrayList<>();
    private static boolean wasLmbDown = false, wasRmbDown = false;

    private final List<CustomWidget> widgets = new ArrayList<>();
    private CustomSlider activeSlider = null;
    private static final String[] TABS = {"HUD", "Косметика", "PvP & Бой", "Рука", "Мир", "Темы"};

    public HudAndGuiScreen() {
        super(Component.literal("MTVisuals Custom GUI"));
    }

    @Override
    protected void init() {
        openAnim = 0f;
        buildCustomWidgets();
    }

    private void buildCustomWidgets() {
        widgets.clear();
        switch (activeTab) {
            case 0 -> {
                widgets.add(new CustomToggle("Watermark (Логотип)", () -> VisualConfig.watermark, v -> VisualConfig.watermark = v));
                widgets.add(new CustomToggle("Keystrokes (Клавиши)", () -> VisualConfig.keystrokes, v -> VisualConfig.keystrokes = v));
                widgets.add(new CustomToggle("Счётчик CPS в Keystrokes", () -> VisualConfig.keystrokesCps, v -> VisualConfig.keystrokesCps = v));
                widgets.add(new CustomToggle("Armor HUD (Броня)", () -> VisualConfig.armorHud, v -> VisualConfig.armorHud = v));
                widgets.add(new CustomToggle("Potions HUD (Эффекты)", () -> VisualConfig.potionsHud, v -> VisualConfig.potionsHud = v));
                widgets.add(new CustomToggle("Target HUD (Карточка цели)", () -> VisualConfig.targetHud, v -> VisualConfig.targetHud = v));
                widgets.add(new CustomToggle("Магнитные линии HUD (Snap)", () -> VisualConfig.hudSnapLines, v -> VisualConfig.hudSnapLines = v));
                widgets.add(new CustomToggle("ArrayList (Список модулей)", () -> VisualConfig.arrayList, v -> VisualConfig.arrayList = v));
                widgets.add(new CustomToggle("Неоновый хотбар", () -> VisualConfig.hotbarGlow, v -> VisualConfig.hotbarGlow = v));
                widgets.add(new CustomToggle("Кастомный прицел", () -> VisualConfig.customCrosshair, v -> VisualConfig.customCrosshair = v));
                widgets.add(new CustomCycle("Стиль прицела", () -> switch (VisualConfig.crosshairStyle) {
                    case 1 -> "Круг"; case 2 -> "Скобки"; default -> "Крест";
                }, () -> VisualConfig.crosshairStyle = (VisualConfig.crosshairStyle + 1) % 3));
                widgets.add(new CustomToggle("Динамический прицел", () -> VisualConfig.dynamicCrosshair, v -> VisualConfig.dynamicCrosshair = v));
                widgets.add(new CustomToggle("Стеклянный фон инвентаря", () -> VisualConfig.inventoryGlass, v -> VisualConfig.inventoryGlass = v));
                widgets.add(new CustomToggle("Streamer Mode (Скрыть ник)", () -> VisualConfig.streamerMode, v -> VisualConfig.streamerMode = v));
            }
            case 1 -> {
                widgets.add(new CustomCycle("Головной убор", VisualConfig::getHatName, () -> VisualConfig.hatMode = (VisualConfig.hatMode + 1) % 9));
                widgets.add(new CustomCycle("Аксессуар на спину", VisualConfig::getBackName, () -> VisualConfig.backMode = (VisualConfig.backMode + 1) % 8));
                widgets.add(new CustomCycle("Питомец-спутник", VisualConfig::getPetName, () -> VisualConfig.petMode = (VisualConfig.petMode + 1) % 8));
                widgets.add(new CustomSlider("Размер косметики", 0.4f, 2.0f, () -> VisualConfig.accessorySize, v -> VisualConfig.accessorySize = v));
                widgets.add(new CustomToggle("Видеть косметику от 1-го лица", () -> VisualConfig.firstPersonCosmetics, v -> VisualConfig.firstPersonCosmetics = v));
                widgets.add(new CustomToggle("Шлейф (Trails)", () -> VisualConfig.trails, v -> VisualConfig.trails = v));
                widgets.add(new CustomSlider("Длина шлейфа", 0.3f, 2.5f, () -> VisualConfig.trailLength, v -> VisualConfig.trailLength = v));
                widgets.add(new CustomToggle("Круги при прыжке", () -> VisualConfig.jumpCircles, v -> VisualConfig.jumpCircles = v));
                widgets.add(new CustomToggle("Кольца при шагах", () -> VisualConfig.footstepRings, v -> VisualConfig.footstepRings = v));
                widgets.add(new CustomToggle("Неоновая аура тела", () -> VisualConfig.bodyAura, v -> VisualConfig.bodyAura = v));
            }
            case 2 -> {
                widgets.add(new CustomToggle("TargetESP (Подсветка цели)", () -> VisualConfig.targetEsp, v -> VisualConfig.targetEsp = v));
                widgets.add(new CustomCycle("Стиль TargetESP", VisualConfig::getTargetEspName, () -> VisualConfig.targetEspStyle = (VisualConfig.targetEspStyle + 1) % 5));
                widgets.add(new CustomSlider("Скорость TargetESP", 0.3f, 3.0f, () -> VisualConfig.targetEspSpeed, v -> VisualConfig.targetEspSpeed = v));
                widgets.add(new CustomToggle("Подсветка блока (BlockOverlay)", () -> VisualConfig.blockOverlay, v -> VisualConfig.blockOverlay = v));
                widgets.add(new CustomToggle("Радиус кристаллов и TNT", () -> VisualConfig.crystalRadius, v -> VisualConfig.crystalRadius = v));
                widgets.add(new CustomToggle("Линии до игроков (Tracers)", () -> VisualConfig.playerTracers, v -> VisualConfig.playerTracers = v));
                widgets.add(new CustomToggle("Виньетка при низком HP", () -> VisualConfig.lowHpVignette, v -> VisualConfig.lowHpVignette = v));
                widgets.add(new CustomToggle("Предупреждение о броне", () -> VisualConfig.armorBreakWarning, v -> VisualConfig.armorBreakWarning = v));
                widgets.add(new CustomToggle("Счётчик Комбо и Reach", () -> VisualConfig.comboCounter, v -> VisualConfig.comboCounter = v));
                widgets.add(new CustomToggle("Неоновые хитбоксы", () -> VisualConfig.customHitboxes, v -> VisualConfig.customHitboxes = v));
                widgets.add(new CustomToggle("Заливка хитбоксов", () -> VisualConfig.hitboxFill, v -> VisualConfig.hitboxFill = v));
                widgets.add(new CustomToggle("Частицы при ударе", () -> VisualConfig.hitParticles, v -> VisualConfig.hitParticles = v));
                widgets.add(new CustomCycle("Форма частиц удара", () -> switch (VisualConfig.hitParticleStyle) {
                    case 1 -> "Звёзды"; case 2 -> "Сферы"; default -> "Искры";
                }, () -> VisualConfig.hitParticleStyle = (VisualConfig.hitParticleStyle + 1) % 3));
                widgets.add(new CustomSlider("Кол-во частиц удара", 5f, 50f, () -> (float) VisualConfig.hitParticleCount, v -> VisualConfig.hitParticleCount = Math.round(v)));
                widgets.add(new CustomToggle("Эффект убийства (Молния)", () -> VisualConfig.killEffect, v -> VisualConfig.killEffect = v));
                widgets.add(new CustomToggle("Траектория полёта", () -> VisualConfig.trajectories, v -> VisualConfig.trajectories = v));
                widgets.add(new CustomToggle("Точка приземления", () -> VisualConfig.trajectoryLandingBox, v -> VisualConfig.trajectoryLandingBox = v));
                widgets.add(new CustomToggle("Трекер Эндер-жемчуга", () -> VisualConfig.pearlTracker, v -> VisualConfig.pearlTracker = v));
                widgets.add(new CustomToggle("Подсветка предметов", () -> VisualConfig.itemPhysics, v -> VisualConfig.itemPhysics = v));
                widgets.add(new CustomToggle("NoHurtCam (Без тряски)", () -> VisualConfig.noHurtCam, v -> VisualConfig.noHurtCam = v));
                widgets.add(new CustomToggle("Плавный Zoom (Кнопка C)", () -> VisualConfig.smoothZoom, v -> VisualConfig.smoothZoom = v));
                widgets.add(new CustomSlider("Кратность Zoom", 1.5f, 8.0f, () -> VisualConfig.zoomFactor, v -> VisualConfig.zoomFactor = v));
            }
            case 3 -> {
                widgets.add(new CustomToggle("Кастомная рука (ViewModel)", () -> VisualConfig.handView, v -> VisualConfig.handView = v));
                widgets.add(new CustomCycle("Анимация удара (Swing)", VisualConfig::getSwingName, () -> VisualConfig.swingAnimation = (VisualConfig.swingAnimation + 1) % 5));
                widgets.add(new CustomSlider("Скорость взмаха", 0.3f, 2.5f, () -> VisualConfig.swingSpeed, v -> VisualConfig.swingSpeed = v));
                widgets.add(new CustomSlider("Размер предметов", 0.3f, 1.6f, () -> VisualConfig.handScale, v -> VisualConfig.handScale = v));
                widgets.add(new CustomSlider("Сдвиг по X (Право/Лево)", -0.6f, 0.6f, () -> VisualConfig.handOffsetX, v -> VisualConfig.handOffsetX = v));
                widgets.add(new CustomSlider("Сдвиг по Y (Вверх/Вниз)", -0.6f, 0.6f, () -> VisualConfig.handOffsetY, v -> VisualConfig.handOffsetY = v));
                widgets.add(new CustomSlider("Сдвиг по Z (Вперёд/Назад)", -0.6f, 0.6f, () -> VisualConfig.handOffsetZ, v -> VisualConfig.handOffsetZ = v));
                widgets.add(new CustomSlider("Поворот X (Наклон)", -90f, 90f, () -> VisualConfig.handRotX, v -> VisualConfig.handRotX = v));
                widgets.add(new CustomSlider("Поворот Y (Разворот)", -90f, 90f, () -> VisualConfig.handRotY, v -> VisualConfig.handRotY = v));
                widgets.add(new CustomSlider("Поворот Z (Крен)", -90f, 90f, () -> VisualConfig.handRotZ, v -> VisualConfig.handRotZ = v));
                widgets.add(new CustomToggle("Применять к левой руке", () -> VisualConfig.leftHandView, v -> VisualConfig.leftHandView = v));
            }
            case 4 -> {
                widgets.add(new CustomCycle("Время суток", VisualConfig::getTimeModeName, () -> VisualConfig.timeMode = (VisualConfig.timeMode + 1) % 5));
                widgets.add(new CustomCycle("Частицы в небе", VisualConfig::getWeatherName, () -> VisualConfig.weatherParticles = (VisualConfig.weatherParticles + 1) % 6));
                widgets.add(new CustomSlider("Плотность частиц", 10f, 150f, () -> (float) VisualConfig.particleDensity, v -> VisualConfig.particleDensity = Math.round(v)));
                widgets.add(new CustomToggle("Fullbright (Полная яркость)", () -> VisualConfig.fullbright, v -> VisualConfig.fullbright = v));
                widgets.add(new CustomToggle("Неоновый туман", () -> VisualConfig.customFog, v -> VisualConfig.customFog = v));
            }
            case 5 -> {
                widgets.add(new CustomCycle("Цветовая палитра", VisualConfig::getThemeName, () -> VisualConfig.themeMode = (VisualConfig.themeMode + 1) % 8));
                widgets.add(new CustomSlider("Скорость RGB перелива", 0.3f, 6.0f, () -> VisualConfig.rgbSpeed, v -> VisualConfig.rgbSpeed = v));
                widgets.add(new CustomSlider("Затемнение фона меню", 0.1f, 0.95f, () -> VisualConfig.guiBlurAlpha, v -> VisualConfig.guiBlurAlpha = v));
            }
        }
        scrollY = 0f;
        targetScrollY = 0f;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        openAnim = Mth.lerp(0.22f, openAnim, 1.0f);
        scrollY = Mth.lerp(0.25f, scrollY, targetScrollY);

        int blurAlpha = (int) (VisualConfig.guiBlurAlpha * 255 * openAnim);
        g.fill(0, 0, this.width, this.height, (blurAlpha << 24) | 0x07080F);

        int c1 = VisualConfig.getPrimaryColor();
        int c2 = VisualConfig.getSecondaryColor();
        int winW = 470, winH = 300;
        int winX = (this.width - winW) / 2, winY = (this.height - winH) / 2;

        g.fill(winX - 1, winY - 1, winX + winW + 1, winY + winH + 1, c1);
        g.fill(winX, winY, winX + winW, winY + winH, 0xF00D0F18);
        g.fill(winX, winY, winX + 115, winY + winH, 0xFF0A0C14);
        g.fill(winX + 115, winY, winX + 116, winY + winH, (0x55 << 24) | (c1 & 0x00FFFFFF));

        g.drawString(this.font, "§lMTVISUALS", winX + 16, winY + 16, c1, false);
        g.drawString(this.font, "v5.0 ULTRA", winX + 16, winY + 28, 0xFF7A8299, false);

        for (int i = 0; i < TABS.length; i++) {
            int ty = winY + 52 + i * 32;
            boolean sel = (i == activeTab);
            boolean hov = mouseX >= winX + 8 && mouseX <= winX + 107 && mouseY >= ty && mouseY <= ty + 24;
            if (sel) {
                g.fill(winX + 8, ty, winX + 107, ty + 24, (0x33 << 24) | (c1 & 0x00FFFFFF));
                g.fill(winX + 8, ty + 3, winX + 11, ty + 21, c1);
            } else if (hov) {
                g.fill(winX + 8, ty, winX + 107, ty + 24, 0x18FFFFFF);
            }
            g.drawString(this.font, TABS[i], winX + 18, ty + 8, sel ? 0xFFFFFFFF : 0xFFA0A8C0, false);
        }

        g.drawString(this.font, "§7Тащи HUD / уголок=размер", winX + 8, winY + winH - 20, 0xFF667085, false);
        g.drawString(this.font, "§l" + TABS[activeTab], winX + 130, winY + 14, 0xFFFFFFFF, false);
        g.fill(winX + 126, winY + 29, winX + winW - 12, winY + 30, 0x22FFFFFF);

        int listX = winX + 126, listY = winY + 36, listW = winW - 138, listH = winH - 46;
        g.enableScissor(listX, listY, listX + listW, listY + listH);
        int curY = (int) (listY - scrollY);
        for (CustomWidget w : widgets) {
            w.render(g, this.font, listX, curY, listW, 28, mouseX, mouseY, c1, c2);
            curY += 33;
        }
        g.disableScissor();

        // Магнитные направляющие при перетаскивании
        if (draggingHud != -1 && VisualConfig.hudSnapLines) {
            if (snapLineX != null) g.fill(Math.round(snapLineX), 0, Math.round(snapLineX) + 1, this.height, c2);
            if (snapLineY != null) g.fill(0, Math.round(snapLineY), this.width, Math.round(snapLineY) + 1, c2);
        }

        // Рамки всех 5 элементов HUD + квадратный маркер изменения размера в правом нижнем углу
        if (VisualConfig.watermark) drawHudBox(g, VisualConfig.watermarkX, VisualConfig.watermarkY, 155 * VisualConfig.watermarkScale, 18 * VisualConfig.watermarkScale, c1);
        if (VisualConfig.keystrokes) drawHudBox(g, VisualConfig.keystrokesX, VisualConfig.keystrokesY, 82 * VisualConfig.keystrokesScale, 68 * VisualConfig.keystrokesScale, c1);
        if (VisualConfig.armorHud) drawHudBox(g, VisualConfig.armorX, VisualConfig.armorY, 74 * VisualConfig.armorScale, 18 * VisualConfig.armorScale, c1);
        if (VisualConfig.potionsHud) drawHudBox(g, VisualConfig.potionsX, VisualConfig.potionsY, 105 * VisualConfig.potionsScale, 42 * VisualConfig.potionsScale, c1);
        if (VisualConfig.targetHud) drawHudBox(g, VisualConfig.targetHudX, VisualConfig.targetHudY, 124 * VisualConfig.targetHudScale, 36 * VisualConfig.targetHudScale, c1);
    }

    private void drawHudBox(GuiGraphics g, float x, float y, float w, float h, int c1) {
        int ix = Math.round(x), iy = Math.round(y), iw = Math.round(w), ih = Math.round(h);
        g.renderOutline(ix - 2, iy - 2, iw + 4, ih + 4, (0x77 << 24) | (c1 & 0x00FFFFFF));
        // Маркер изменения размера (правый нижний угол)
        g.fill(ix + iw - 3, iy + ih - 3, ix + iw + 3, iy + ih + 3, c1);
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
        int winW = 470, winH = 300;
        int winX = (this.width - winW) / 2, winY = (this.height - winH) / 2;

        for (int i = 0; i < TABS.length; i++) {
            int ty = winY + 52 + i * 32;
            if (mouseX >= winX + 8 && mouseX <= winX + 107 && mouseY >= ty && mouseY <= ty + 24) {
                activeTab = i;
                buildCustomWidgets();
                return true;
            }
        }

        int listX = winX + 126, listY = winY + 36, listW = winW - 138, listH = winH - 46;
        if (mouseX >= listX && mouseX <= listX + listW && mouseY >= listY && mouseY <= listY + listH) {
            int curY = (int) (listY - scrollY);
            for (CustomWidget w : widgets) {
                if (mouseY >= curY && mouseY <= curY + 28) {
                    w.onClick(mouseX, listX, listW);
                    if (w instanceof CustomSlider sl) activeSlider = sl;
                    return true;
                }
                curY += 33;
            }
        }

        // Проверка клика по HUD вне окна меню (сначала уголок изменения размера, затем само тело HUD)
        if (mouseX < winX || mouseX > winX + winW || mouseY < winY || mouseY > winY + winH) {
            if (VisualConfig.watermark && checkHudAction(mouseX, mouseY, VisualConfig.watermarkX, VisualConfig.watermarkY, 155 * VisualConfig.watermarkScale, 18 * VisualConfig.watermarkScale, 0, VisualConfig.watermarkScale)) return true;
            if (VisualConfig.keystrokes && checkHudAction(mouseX, mouseY, VisualConfig.keystrokesX, VisualConfig.keystrokesY, 82 * VisualConfig.keystrokesScale, 68 * VisualConfig.keystrokesScale, 1, VisualConfig.keystrokesScale)) return true;
            if (VisualConfig.armorHud && checkHudAction(mouseX, mouseY, VisualConfig.armorX, VisualConfig.armorY, 74 * VisualConfig.armorScale, 18 * VisualConfig.armorScale, 2, VisualConfig.armorScale)) return true;
            if (VisualConfig.potionsHud && checkHudAction(mouseX, mouseY, VisualConfig.potionsX, VisualConfig.potionsY, 105 * VisualConfig.potionsScale, 42 * VisualConfig.potionsScale, 3, VisualConfig.potionsScale)) return true;
            if (VisualConfig.targetHud && checkHudAction(mouseX, mouseY, VisualConfig.targetHudX, VisualConfig.targetHudY, 124 * VisualConfig.targetHudScale, 36 * VisualConfig.targetHudScale, 4, VisualConfig.targetHudScale)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean checkHudAction(double mx, double my, float hx, float hy, float hw, float hh, int id, float curScale) {
        // Клик по правому нижнему маркеру = Resize
        if (Math.abs(mx - (hx + hw)) <= 6 && Math.abs(my - (hy + hh)) <= 6) {
            resizingHud = id;
            resizeStartX = (float) mx;
            resizeStartScale = curScale;
            return true;
        }
        // Клик по телу блока = Drag
        if (mx >= hx && mx <= hx + hw && my >= hy && my <= hy + hh) {
            draggingHud = id;
            dragOffsetX = (float) mx - hx;
            dragOffsetY = (float) my - hy;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        if (activeSlider != null) {
            int winX = (this.width - 470) / 2;
            activeSlider.updateValue(mouseX, winX + 126, 470 - 138);
            return true;
        }
        if (resizingHud != -1) {
            float newScale = Mth.clamp(resizeStartScale + ((float) mouseX - resizeStartX) / 100f, 0.5f, 2.2f);
            switch (resizingHud) {
                case 0 -> VisualConfig.watermarkScale = newScale;
                case 1 -> VisualConfig.keystrokesScale = newScale;
                case 2 -> VisualConfig.armorScale = newScale;
                case 3 -> VisualConfig.potionsScale = newScale;
                case 4 -> VisualConfig.targetHudScale = newScale;
            }
            return true;
        }
        if (draggingHud != -1) {
            float nx = (float) mouseX - dragOffsetX;
            float ny = (float) mouseY - dragOffsetY;
            snapLineX = null;
            snapLineY = null;

            if (VisualConfig.hudSnapLines) {
                float[] otherX = {10f, this.width / 2f, VisualConfig.watermarkX, VisualConfig.keystrokesX, VisualConfig.armorX, VisualConfig.potionsX, VisualConfig.targetHudX};
                float[] otherY = {10f, this.height / 2f, VisualConfig.watermarkY, VisualConfig.keystrokesY, VisualConfig.armorY, VisualConfig.potionsY, VisualConfig.targetHudY};
                for (int i = 0; i < otherX.length; i++) {
                    if (i - 2 == draggingHud) continue;
                    if (Math.abs(nx - otherX[i]) < 6f) { nx = otherX[i]; snapLineX = otherX[i]; break; }
                }
                for (int i = 0; i < otherY.length; i++) {
                    if (i - 2 == draggingHud) continue;
                    if (Math.abs(ny - otherY[i]) < 6f) { ny = otherY[i]; snapLineY = otherY[i]; break; }
                }
            }

            switch (draggingHud) {
                case 0 -> { VisualConfig.watermarkX = nx; VisualConfig.watermarkY = ny; }
                case 1 -> { VisualConfig.keystrokesX = nx; VisualConfig.keystrokesY = ny; }
                case 2 -> { VisualConfig.armorX = nx; VisualConfig.armorY = ny; }
                case 3 -> { VisualConfig.potionsX = nx; VisualConfig.potionsY = ny; }
                case 4 -> { VisualConfig.targetHudX = nx; VisualConfig.targetHudY = ny; }
            }
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        activeSlider = null;
        draggingHud = -1;
        resizingHud = -1;
        snapLineX = null;
        snapLineY = null;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double hAmount, double vAmount) {
        float maxScroll = Math.max(0, widgets.size() * 33 - 250);
        targetScrollY = Mth.clamp(targetScrollY - (float) vAmount * 22f, 0f, maxScroll);
        return true;
    }

    @Override
    public void onClose() {
        VisualConfig.save();
        super.onClose();
    }
    private interface CustomWidget {
        void render(GuiGraphics g, net.minecraft.client.gui.Font f, int x, int y, int w, int h, int mx, int my, int c1, int c2);
        void onClick(double mx, int x, int w);
    }

    private static class CustomToggle implements CustomWidget {
        final String name; final BooleanSupplier get; final Consumer<Boolean> set;
        CustomToggle(String n, BooleanSupplier g, Consumer<Boolean> s) { name = n; get = g; set = s; }
        public void render(GuiGraphics g, net.minecraft.client.gui.Font f, int x, int y, int w, int h, int mx, int my, int c1, int c2) {
            boolean on = get.getAsBoolean();
            g.fill(x, y, x + w, y + h, 0xCC141724);
            g.drawString(f, name, x + 10, y + 10, on ? 0xFFFFFFFF : 0xFFB0B8D0, false);
            int sw = 30, sh = 14, sx = x + w - sw - 10, sy = y + 7;
            g.fill(sx, sy, sx + sw, sy + sh, on ? c1 : 0xFF2A2F42);
            int knobX = on ? sx + sw - 12 : sx + 2;
            g.fill(knobX, sy + 2, knobX + 10, sy + sh - 2, 0xFFFFFFFF);
        }
        public void onClick(double mx, int x, int w) { set.accept(!get.getAsBoolean()); }
    }

    private static class CustomCycle implements CustomWidget {
        final String name; final Supplier<String> val; final Runnable next;
        CustomCycle(String n, Supplier<String> v, Runnable r) { name = n; val = v; next = r; }
        public void render(GuiGraphics g, net.minecraft.client.gui.Font f, int x, int y, int w, int h, int mx, int my, int c1, int c2) {
            g.fill(x, y, x + w, y + h, 0xCC141724);
            g.drawString(f, name, x + 10, y + 10, 0xFFE0E6F5, false);
            String v = val.get();
            int vw = f.width(v) + 12;
            g.fill(x + w - vw - 8, y + 5, x + w - 8, y + h - 5, (0x33 << 24) | (c1 & 0x00FFFFFF));
            g.drawString(f, v, x + w - vw - 2, y + 10, c1, false);
        }
        public void onClick(double mx, int x, int w) { next.run(); }
    }

    private static class CustomSlider implements CustomWidget {
        final String name; final float min, max; final Supplier<Float> get; final Consumer<Float> set;
        CustomSlider(String n, float mn, float mx, Supplier<Float> g, Consumer<Float> s) { name = n; min = mn; max = mx; get = g; set = s; }
        public void render(GuiGraphics g, net.minecraft.client.gui.Font f, int x, int y, int w, int h, int mx, int my, int c1, int c2) {
            g.fill(x, y, x + w, y + h, 0xCC141724);
            float val = get.get();
            g.drawString(f, name, x + 10, y + 6, 0xFFE0E6F5, false);
            String vs = String.format("%.2f", val);
            g.drawString(f, vs, x + w - f.width(vs) - 10, y + 6, c1, false);
            int bx = x + 10, by = y + 19, bw = w - 20;
            g.fill(bx, by, bx + bw, by + 4, 0xFF252A3D);
            float pct = Mth.clamp((val - min) / (max - min), 0f, 1f);
            g.fill(bx, by, bx + (int) (bw * pct), by + 4, c1);
        }
        public void onClick(double mx, int x, int w) { updateValue(mx, x, w); }
        void updateValue(double mx, int x, int w) {
            float pct = Mth.clamp((float) (mx - (x + 10)) / (w - 20), 0f, 1f);
            set.accept(min + pct * (max - min));
        }
    }

    public static void tickHud() {
    }

    public static void renderHud(GuiGraphics g) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;
        int c1 = VisualConfig.getPrimaryColor(), c2 = VisualConfig.getSecondaryColor();
        long now = System.currentTimeMillis();
        int sw = mc.getWindow().getGuiScaledWidth(), sh = mc.getWindow().getGuiScaledHeight();

        // Трекинг комбо, дистанции удара и CPS
        if (mc.player.hurtTime > lastHurtTime) comboCount = 0;
        lastHurtTime = mc.player.hurtTime;

        if (mc.player.attackAnim > 0 && mc.crosshairPickEntity instanceof LivingEntity target && now - lastHitTime > 250L) {
            lastHitTime = now;
            comboCount++;
            lastReach = (float) mc.player.getEyePosition().distanceTo(target.getBoundingBox().getCenter());
        }
        if (now - lastHitTime > 4000L) comboCount = 0;

        boolean lmb = mc.options.keyAttack.isDown(), rmb = mc.options.keyUse.isDown();
        if (lmb && !wasLmbDown) leftClicks.add(now);
        if (rmb && !wasRmbDown) rightClicks.add(now);
        wasLmbDown = lmb; wasRmbDown = rmb;
        leftClicks.removeIf(t -> now - t > 1000L);
        rightClicks.removeIf(t -> now - t > 1000L);

        // 0. Красная виньетка при малом HP
        if (VisualConfig.lowHpVignette && mc.player.getHealth() < 7.0f) {
            int pulse = (int) ((Math.sin(now / 150.0) + 1.0) * 45 + 30);
            int vigCol = (pulse << 24) | 0xFF0022;
            g.fill(0, 0, sw, 18, vigCol);
            g.fill(0, sh - 18, sw, sh, vigCol);
            g.fill(0, 18, 18, sh - 18, vigCol);
            g.fill(sw - 18, 18, sw, sh - 18, vigCol);
        }

        // 1. Watermark
        if (VisualConfig.watermark) {
            g.pose().pushPose();
            g.pose().translate(VisualConfig.watermarkX, VisualConfig.watermarkY, 0);
            g.pose().scale(VisualConfig.watermarkScale, VisualConfig.watermarkScale, 1f);
            String nick = VisualConfig.streamerMode ? "MT_User" : mc.player.getName().getString();
            String txt = "⚡ MTVisuals | " + nick + " | " + mc.getFps() + " FPS";
            int ww = mc.font.width(txt) + 14;
            g.fill(0, 0, ww, 18, 0xCC0D0F18);
            g.fill(0, 0, ww, 2, c1);
            g.drawString(mc.font, txt, 7, 5, 0xFFFFFFFF, true);
            g.pose().popPose();
        }

        // 2. Keystrokes + CPS
        if (VisualConfig.keystrokes) {
            g.pose().pushPose();
            g.pose().translate(VisualConfig.keystrokesX, VisualConfig.keystrokesY, 0);
            g.pose().scale(VisualConfig.keystrokesScale, VisualConfig.keystrokesScale, 1f);
            drawKey(g, mc, "W", 28, 0, 26, 22, mc.options.keyUp.isDown(), c1);
            drawKey(g, mc, "A", 0, 24, 26, 22, mc.options.keyLeft.isDown(), c1);
            drawKey(g, mc, "S", 28, 24, 26, 22, mc.options.keyDown.isDown(), c1);
            drawKey(g, mc, "D", 56, 24, 26, 22, mc.options.keyRight.isDown(), c1);
            String lTxt = VisualConfig.keystrokesCps ? "L:" + leftClicks.size() : "LMB";
            String rTxt = VisualConfig.keystrokesCps ? "R:" + rightClicks.size() : "RMB";
            drawKey(g, mc, lTxt, 0, 48, 40, 20, lmb, c2);
            drawKey(g, mc, rTxt, 42, 48, 40, 20, rmb, c2);
            g.pose().popPose();
        }
        // 3. Armor HUD + предупреждение о поломке
        boolean lowArmor = false;
        if (VisualConfig.armorHud) {
            g.pose().pushPose();
            g.pose().translate(VisualConfig.armorX, VisualConfig.armorY, 0);
            g.pose().scale(VisualConfig.armorScale, VisualConfig.armorScale, 1f);
            int ax = 0;
            for (ItemStack armor : mc.player.getArmorSlots()) {
                if (!armor.isEmpty()) {
                    g.renderItem(armor, ax, 0);
                    g.renderItemDecorations(mc.font, armor, ax, 0);
                    if (armor.isDamageableItem() && (float) (armor.getMaxDamage() - armor.getDamageValue()) / armor.getMaxDamage() < 0.2f) {
                        lowArmor = true;
                    }
                    ax += 18;
                }
            }
            g.pose().popPose();
        }
        if (VisualConfig.armorBreakWarning && lowArmor) {
            g.drawCenteredString(mc.font, "§c§l⚠ БРОНЯ ПОЧТИ СЛОМАНА!", sw / 2, sh / 2 - 42, 0xFFFF3355);
        }

        // 4. Potions HUD
        if (VisualConfig.potionsHud) {
            g.pose().pushPose();
            g.pose().translate(VisualConfig.potionsX, VisualConfig.potionsY, 0);
            g.pose().scale(VisualConfig.potionsScale, VisualConfig.potionsScale, 1f);
            int py = 0;
            for (MobEffectInstance eff : mc.player.getActiveEffects()) {
                String lbl = eff.getEffect().value().getDisplayName().getString() + " " + (eff.getDuration() / 20) + "s";
                g.fill(0, py, mc.font.width(lbl) + 10, py + 14, 0xBB0D0F18);
                g.fill(0, py, 2, py + 14, c2);
                g.drawString(mc.font, lbl, 5, py + 3, 0xFFFFFFFF, true);
                py += 16;
            }
            g.pose().popPose();
        }

        // 5. Target HUD (перетаскиваемый и масштабируемый)
        if (VisualConfig.targetHud && (mc.crosshairPickEntity instanceof LivingEntity || mc.currentScreen instanceof HudAndGuiScreen)) {
            LivingEntity target = (mc.crosshairPickEntity instanceof LivingEntity le) ? le : mc.player;
            g.pose().pushPose();
            g.pose().translate(VisualConfig.targetHudX, VisualConfig.targetHudY, 0);
            g.pose().scale(VisualConfig.targetHudScale, VisualConfig.targetHudScale, 1f);
            animatedTargetHp = Mth.lerp(0.15f, animatedTargetHp, target.getHealth());
            float pct = Mth.clamp(animatedTargetHp / Math.max(1f, target.getMaxHealth()), 0f, 1f);
            g.fill(0, 0, 124, 36, 0xDD0D0F18);
            g.fill(0, 0, 124, 2, c1);
            g.drawString(mc.font, target.getName().getString(), 6, 6, 0xFFFFFFFF, true);
            g.drawString(mc.font, String.format("%.1f ❤", animatedTargetHp), 6, 17, 0xFFFF5577, true);
            g.fill(6, 28, 118, 32, 0xFF252A3D);
            g.fill(6, 28, 6 + (int) (112 * pct), 32, c1);
            g.pose().popPose();
        }

        // 6. Счётчик Комбо и дистанции удара
        if (VisualConfig.comboCounter && comboCount > 0) {
            String cmb = String.format("COMBO: %dx | REACH: %.2fm", comboCount, lastReach);
            g.drawCenteredString(mc.font, cmb, sw / 2, sh / 2 + 24, c1);
        }

        // 7. Неоновая подсветка выбранного слота хотбара
        if (VisualConfig.hotbarGlow) {
            int slotX = sw / 2 - 91 + mc.player.getInventory().selected * 20;
            g.renderOutline(slotX, sh - 22, 22, 22, c1);
        }

        // 8. ArrayList справа сверху
        if (VisualConfig.arrayList) {
            List<String> active = new ArrayList<>();
            if (VisualConfig.targetEsp) active.add("TargetESP [" + VisualConfig.getTargetEspName() + "]");
            if (VisualConfig.blockOverlay) active.add("BlockOverlay");
            if (VisualConfig.crystalRadius) active.add("CrystalRadius");
            if (VisualConfig.playerTracers) active.add("Tracers");
            if (VisualConfig.trails) active.add("Trails");
            if (VisualConfig.jumpCircles) active.add("JumpCircles");
            if (VisualConfig.hitParticles) active.add("HitParticles");
            if (VisualConfig.trajectories) active.add("Trajectories");
            if (VisualConfig.handView) active.add("ViewModel");
            active.sort((a, b) -> mc.font.width(b) - mc.font.width(a));
            int ay = 6;
            for (String m : active) {
                int mw = mc.font.width(m) + 8;
                g.fill(sw - mw - 4, ay, sw - 4, ay + 13, 0xAA0D0F18);
                g.fill(sw - 6, ay, sw - 4, ay + 13, c1);
                g.drawString(mc.font, m, sw - mw, ay + 3, 0xFFFFFFFF, true);
                ay += 14;
            }
        }

        // 9. Кастомный прицел (3 стиля)
        if (VisualConfig.customCrosshair) {
            int cx = sw / 2, cy = sh / 2;
            int gap = VisualConfig.dynamicCrosshair && mc.player.getDeltaMovement().horizontalDistanceSqr() > 0.002 ? 6 : 3;
            if (VisualConfig.crosshairStyle == 1) {
                g.renderOutline(cx - gap - 2, cy - gap - 2, (gap + 2) * 2 + 1, (gap + 2) * 2 + 1, c1);
            } else if (VisualConfig.crosshairStyle == 2) {
                g.fill(cx - gap - 3, cy - 4, cx - gap - 2, cy + 5, c1);
                g.fill(cx + gap + 3, cy - 4, cx + gap + 4, cy + 5, c1);
            } else {
                g.fill(cx - gap - 5, cy, cx - gap, cy + 1, c1);
                g.fill(cx + gap + 1, cy, cx + gap + 6, cy + 1, c1);
                g.fill(cx, cy - gap - 5, cx + 1, cy - gap, c2);
                g.fill(cx, cy + gap + 1, cx + 1, cy + gap + 6, c2);
            }
        }
    }

    private static void drawKey(GuiGraphics g, Minecraft mc, String t, int x, int y, int w, int h, boolean p, int col) {
        g.fill(x, y, x + w, y + h, p ? col : 0xBB0D0F18);
        g.drawCenteredString(mc.font, t, x + w / 2, y + (h - 8) / 2, p ? 0xFF000000 : 0xFFFFFFFF);
    }
}
