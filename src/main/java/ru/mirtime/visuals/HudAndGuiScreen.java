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
    private static int draggingHud = -1;
    private static float dragOffsetX = 0, dragOffsetY = 0;
    private static float animatedTargetHp = 20f;
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
        rebuildWidgets();
    }

    private void rebuildWidgets() {
        widgets.clear();
        switch (activeTab) {
            case 0 -> { // HUD
                widgets.add(new CustomToggle("Watermark (Логотип)", () -> VisualConfig.watermark, v -> VisualConfig.watermark = v));
                widgets.add(new CustomCycle("Стиль Watermark", () -> switch (VisualConfig.watermarkStyle) {
                    case 1 -> "Minimal Pill"; case 2 -> "Full Stats"; default -> "Cyber Bar";
                }, () -> VisualConfig.watermarkStyle = (VisualConfig.watermarkStyle + 1) % 3));
                widgets.add(new CustomToggle("Keystrokes (Клавиши)", () -> VisualConfig.keystrokes, v -> VisualConfig.keystrokes = v));
                widgets.add(new CustomToggle("Счётчик CPS в Keystrokes", () -> VisualConfig.keystrokesCps, v -> VisualConfig.keystrokesCps = v));
                widgets.add(new CustomToggle("Armor HUD (Броня)", () -> VisualConfig.armorHud, v -> VisualConfig.armorHud = v));
                widgets.add(new CustomToggle("Процент прочности брони", () -> VisualConfig.armorDurability, v -> VisualConfig.armorDurability = v));
                widgets.add(new CustomToggle("Potions HUD (Эффекты)", () -> VisualConfig.potionsHud, v -> VisualConfig.potionsHud = v));
                widgets.add(new CustomToggle("Target HUD (Карточка цели)", () -> VisualConfig.targetHud, v -> VisualConfig.targetHud = v));
                widgets.add(new CustomCycle("Стиль Target HUD", () -> switch (VisualConfig.targetHudStyle) {
                    case 1 -> "Compact Neon"; case 2 -> "Minimalist"; default -> "Modern Card";
                }, () -> VisualConfig.targetHudStyle = (VisualConfig.targetHudStyle + 1) % 3));
                widgets.add(new CustomToggle("ArrayList (Список модулей)", () -> VisualConfig.arrayList, v -> VisualConfig.arrayList = v));
                widgets.add(new CustomToggle("Неоновый хотбар", () -> VisualConfig.hotbarGlow, v -> VisualConfig.hotbarGlow = v));
                widgets.add(new CustomToggle("Кастомный прицел", () -> VisualConfig.customCrosshair, v -> VisualConfig.customCrosshair = v));
                widgets.add(new CustomCycle("Стиль прицела", () -> switch (VisualConfig.crosshairStyle) {
                    case 1 -> "Круг"; case 2 -> "Скобки"; case 3 -> "Крест-вращение"; default -> "Крест";
                }, () -> VisualConfig.crosshairStyle = (VisualConfig.crosshairStyle + 1) % 4));
                widgets.add(new CustomToggle("Динамический прицел", () -> VisualConfig.dynamicCrosshair, v -> VisualConfig.dynamicCrosshair = v));
                widgets.add(new CustomToggle("Стеклянный фон инвентаря", () -> VisualConfig.inventoryGlass, v -> VisualConfig.inventoryGlass = v));
                widgets.add(new CustomToggle("Streamer Mode (Скрыть ник)", () -> VisualConfig.streamerMode, v -> VisualConfig.streamerMode = v));
            }
            case 1 -> { // Косметика
                widgets.add(new CustomCycle("Головной убор", VisualConfig::getHatName, () -> VisualConfig.hatMode = (VisualConfig.hatMode + 1) % 7));
                widgets.add(new CustomCycle("Аксессуар на спину", VisualConfig::getBackName, () -> VisualConfig.backMode = (VisualConfig.backMode + 1) % 6));
                widgets.add(new CustomCycle("Питомец-спутник", VisualConfig::getPetName, () -> VisualConfig.petMode = (VisualConfig.petMode + 1) % 6));
                widgets.add(new CustomSlider("Размер косметики", 0.4f, 2.0f, () -> VisualConfig.accessorySize, v -> VisualConfig.accessorySize = v));
                widgets.add(new CustomToggle("Видеть косметику от 1-го лица", () -> VisualConfig.firstPersonCosmetics, v -> VisualConfig.firstPersonCosmetics = v));
                widgets.add(new CustomToggle("Шлейф (Trails)", () -> VisualConfig.trails, v -> VisualConfig.trails = v));
                widgets.add(new CustomCycle("Стиль шлейфа", () -> switch (VisualConfig.trailStyle) {
                    case 1 -> "Неоновая лента"; case 2 -> "Кристаллы"; default -> "Двойная линия";
                }, () -> VisualConfig.trailStyle = (VisualConfig.trailStyle + 1) % 3));
                widgets.add(new CustomSlider("Длина шлейфа", 0.3f, 2.5f, () -> VisualConfig.trailLength, v -> VisualConfig.trailLength = v));
                widgets.add(new CustomToggle("Круги при прыжке", () -> VisualConfig.jumpCircles, v -> VisualConfig.jumpCircles = v));
                widgets.add(new CustomCycle("Стиль кругов прыжка", () -> switch (VisualConfig.jumpCircleStyle) {
                    case 1 -> "Тройная волна"; case 2 -> "Звезда"; default -> "Двойное кольцо";
                }, () -> VisualConfig.jumpCircleStyle = (VisualConfig.jumpCircleStyle + 1) % 3));
                widgets.add(new CustomToggle("Кольца при шагах", () -> VisualConfig.footstepRings, v -> VisualConfig.footstepRings = v));
                widgets.add(new CustomToggle("Неоновая аура тела", () -> VisualConfig.bodyAura, v -> VisualConfig.bodyAura = v));
            }
            case 2 -> { // PvP & Бой
                widgets.add(new CustomToggle("TargetESP (Подсветка цели)", () -> VisualConfig.targetEsp, v -> VisualConfig.targetEsp = v));
                widgets.add(new CustomCycle("Стиль TargetESP", VisualConfig::getTargetEspName, () -> VisualConfig.targetEspStyle = (VisualConfig.targetEspStyle + 1) % 5));
                widgets.add(new CustomSlider("Скорость TargetESP", 0.3f, 3.0f, () -> VisualConfig.targetEspSpeed, v -> VisualConfig.targetEspSpeed = v));
                widgets.add(new CustomToggle("Неоновые хитбоксы", () -> VisualConfig.customHitboxes, v -> VisualConfig.customHitboxes = v));
                widgets.add(new CustomToggle("Двойной контур хитбоксов", () -> VisualConfig.hitboxFill, v -> VisualConfig.hitboxFill = v));
                widgets.add(new CustomToggle("Частицы при ударе", () -> VisualConfig.hitParticles, v -> VisualConfig.hitParticles = v));
                widgets.add(new CustomCycle("Форма частиц удара", () -> switch (VisualConfig.hitParticleStyle) {
                    case 1 -> "Звёзды"; case 2 -> "Сферы"; default -> "Искры";
                }, () -> VisualConfig.hitParticleStyle = (VisualConfig.hitParticleStyle + 1) % 3));
                widgets.add(new CustomSlider("Кол-во частиц удара", 5f, 50f, () -> (float) VisualConfig.hitParticleCount, v -> VisualConfig.hitParticleCount = Math.round(v)));
                widgets.add(new CustomToggle("Эффект убийства (Молния)", () -> VisualConfig.killEffect, v -> VisualConfig.killEffect = v));
                widgets.add(new CustomToggle("Траектория полёта", () -> VisualConfig.trajectories, v -> VisualConfig.trajectories = v));
                widgets.add(new CustomToggle("Точка приземления", () -> VisualConfig.trajectoryLandingBox, v -> VisualConfig.trajectoryLandingBox = v));
                widgets.add(new CustomToggle("Трекер Эндер-жемчуга", () -> VisualConfig.pearlTracker, v -> VisualConfig.pearlTracker = v));
                widgets.add(new CustomToggle("Подсветка предметов на земле", () -> VisualConfig.itemPhysics, v -> VisualConfig.itemPhysics = v));
                widgets.add(new CustomToggle("NoHurtCam (Без тряски)", () -> VisualConfig.noHurtCam, v -> VisualConfig.noHurtCam = v));
                widgets.add(new CustomToggle("Плавный Zoom (Кнопка C)", () -> VisualConfig.smoothZoom, v -> VisualConfig.smoothZoom = v));
                widgets.add(new CustomSlider("Кратность Zoom", 1.5f, 8.0f, () -> VisualConfig.zoomFactor, v -> VisualConfig.zoomFactor = v));
            }
            case 3 -> { // Рука (ViewModel)
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
            case 4 -> { // Мир
                widgets.add(new CustomCycle("Время суток", VisualConfig::getTimeModeName, () -> VisualConfig.timeMode = (VisualConfig.timeMode + 1) % 5));
                widgets.add(new CustomCycle("Частицы в небе", VisualConfig::getWeatherName, () -> VisualConfig.weatherParticles = (VisualConfig.weatherParticles + 1) % 6));
                widgets.add(new CustomSlider("Плотность частиц", 10f, 150f, () -> (float) VisualConfig.particleDensity, v -> VisualConfig.particleDensity = Math.round(v)));
                widgets.add(new CustomToggle("Fullbright (Полная яркость)", () -> VisualConfig.fullbright, v -> VisualConfig.fullbright = v));
                widgets.add(new CustomToggle("Неоновый туман", () -> VisualConfig.customFog, v -> VisualConfig.customFog = v));
                widgets.add(new CustomToggle("Кастомное небо", () -> VisualConfig.customSkyColor, v -> VisualConfig.customSkyColor = v));
            }
            case 5 -> { // Темы
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
        int winX = (this.width - winW) / 2;
        int winY = (this.height - winH) / 2;

        // Фон окна и неоновая рамка
        g.fill(winX - 1, winY - 1, winX + winW + 1, winY + winH + 1, c1);
        g.fill(winX, winY, winX + winW, winY + winH, 0xF00D0F18);
        g.fill(winX, winY, winX + 115, winY + winH, 0xFF0A0C14);
        g.fill(winX + 115, winY, winX + 116, winY + winH, (0x55 << 24) | (c1 & 0x00FFFFFF));

        // Логотип слева
        g.drawString(this.font, "§lMTVISUALS", winX + 16, winY + 16, c1, false);
        g.drawString(this.font, "v4.0 ULTRA", winX + 16, winY + 28, 0xFF7A8299, false);

        // Вкладки в левой панели
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

        // Подсказка снизу слева
        g.drawString(this.font, "§7Тащи HUD мышкой", winX + 12, winY + winH - 22, 0xFF667085, false);

        // Верхняя полоса контента
        g.drawString(this.font, "§l" + TABS[activeTab], winX + 130, winY + 14, 0xFFFFFFFF, false);
        g.fill(winX + 126, winY + 29, winX + winW - 12, winY + 30, 0x22FFFFFF);

        // Скроллируемая область виджетов
        int listX = winX + 126, listY = winY + 36, listW = winW - 138, listH = winH - 46;
        g.enableScissor(listX, listY, listX + listW, listY + listH);

        int curY = (int) (listY - scrollY);
        for (CustomWidget w : widgets) {
            w.render(g, this.font, listX, curY, listW, 28, mouseX, mouseY, c1, c2);
            curY += 33;
        }
        g.disableScissor();

        // Отрисовка рамок для перетаскивания HUD
        renderHudDragBoxes(g, c1);
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
        int winW = 470, winH = 300;
        int winX = (this.width - winW) / 2, winY = (this.height - winH) / 2;

        // Клик по вкладкам
        for (int i = 0; i < TABS.length; i++) {
            int ty = winY + 52 + i * 32;
            if (mouseX >= winX + 8 && mouseX <= winX + 107 && mouseY >= ty && mouseY <= ty + 24) {
                activeTab = i;
                rebuildWidgets();
                return true;
            }
        }

        // Клик по виджетам внутри окна
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

        // Начало перетаскивания HUD вне окна
        if (mouseX < winX || mouseX > winX + winW || mouseY < winY || mouseY > winY + winH) {
            if (checkHudHit(mouseX, mouseY, VisualConfig.watermarkX, VisualConfig.watermarkY, 150, 18, 0)) return true;
            if (checkHudHit(mouseX, mouseY, VisualConfig.keystrokesX, VisualConfig.keystrokesY, 84, 78, 1)) return true;
            if (checkHudHit(mouseX, mouseY, VisualConfig.potionsX, VisualConfig.potionsY, 110, 45, 3)) return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean checkHudHit(double mx, double my, float hx, float hy, float hw, float hh, int id) {
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
        if (draggingHud != -1) {
            float nx = (float) mouseX - dragOffsetX;
            float ny = (float) mouseY - dragOffsetY;
            if (draggingHud == 0) { VisualConfig.watermarkX = nx; VisualConfig.watermarkY = ny; }
            if (draggingHud == 1) { VisualConfig.keystrokesX = nx; VisualConfig.keystrokesY = ny; }
            if (draggingHud == 3) { VisualConfig.potionsX = nx; VisualConfig.potionsY = ny; }
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dx, dy);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        activeSlider = null;
        draggingHud = -1;
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

    private void renderHudDragBoxes(GuiGraphics g, int c1) {
        int col = (0x66 << 24) | (c1 & 0x00FFFFFF);
        if (VisualConfig.watermark) g.renderOutline((int) VisualConfig.watermarkX - 2, (int) VisualConfig.watermarkY - 2, 154, 22, col);
        if (VisualConfig.keystrokes) g.renderOutline((int) VisualConfig.keystrokesX - 2, (int) VisualConfig.keystrokesY - 2, 88, 82, col);
        if (VisualConfig.potionsHud) g.renderOutline((int) VisualConfig.potionsX - 2, (int) VisualConfig.potionsY - 2, 114, 48, col);
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

    public static void renderHud(GuiGraphics g) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;
        int c1 = VisualConfig.getPrimaryColor(), c2 = VisualConfig.getSecondaryColor();
        long now = System.currentTimeMillis();

        // CPS трекер
        boolean lmb = mc.options.keyAttack.isDown(), rmb = mc.options.keyUse.isDown();
        if (lmb && !wasLmbDown) leftClicks.add(now);
        if (rmb && !wasRmbDown) rightClicks.add(now);
        wasLmbDown = lmb; wasRmbDown = rmb;
        leftClicks.removeIf(t -> now - t > 1000L);
        rightClicks.removeIf(t -> now - t > 1000L);

        // 1. Watermark
        if (VisualConfig.watermark) {
            String nick = VisualConfig.streamerMode ? "MT_User" : mc.player.getName().getString();
            String txt = "⚡ MTVisuals | " + nick + " | " + mc.getFps() + " FPS";
            int wx = (int) VisualConfig.watermarkX, wy = (int) VisualConfig.watermarkY, ww = mc.font.width(txt) + 14;
            g.fill(wx, wy, wx + ww, wy + 18, 0xCC0D0F18);
            g.fill(wx, wy, wx + ww, wy + 2, c1);
            g.drawString(mc.font, txt, wx + 7, wy + 5, 0xFFFFFFFF, true);
        }

        // 2. Keystrokes + CPS
        if (VisualConfig.keystrokes) {
            int kx = (int) VisualConfig.keystrokesX, ky = (int) VisualConfig.keystrokesY;
            drawKey(g, mc, "W", kx + 28, ky, 26, 22, mc.options.keyUp.isDown(), c1);
            drawKey(g, mc, "A", kx, ky + 24, 26, 22, mc.options.keyLeft.isDown(), c1);
            drawKey(g, mc, "S", kx + 28, ky + 24, 26, 22, mc.options.keyDown.isDown(), c1);
            drawKey(g, mc, "D", kx + 56, ky + 24, 26, 22, mc.options.keyRight.isDown(), c1);
            String lTxt = VisualConfig.keystrokesCps ? "L:" + leftClicks.size() : "LMB";
            String rTxt = VisualConfig.keystrokesCps ? "R:" + rightClicks.size() : "RMB";
            drawKey(g, mc, lTxt, kx, ky + 48, 40, 20, lmb, c2);
            drawKey(g, mc, rTxt, kx + 42, ky + 48, 40, 20, rmb, c2);
        }

        // 3. Armor HUD
        if (VisualConfig.armorHud) {
            int ax = mc.getWindow().getGuiScaledWidth() / 2 + 12, ay = mc.getWindow().getGuiScaledHeight() - 56;
            for (ItemStack armor : mc.player.getArmorSlots()) {
                if (!armor.isEmpty()) {
                    g.renderItem(armor, ax, ay);
                    g.renderItemDecorations(mc.font, armor, ax, ay);
                    ax += 18;
                }
            }
        }

        // 4. Potions HUD
        if (VisualConfig.potionsHud) {
            int py = (int) VisualConfig.potionsY, px = (int) VisualConfig.potionsX;
            for (MobEffectInstance eff : mc.player.getActiveEffects()) {
                String lbl = eff.getEffect().value().getDisplayName().getString() + " " + (eff.getDuration() / 20) + "s";
                g.fill(px, py, px + mc.font.width(lbl) + 10, py + 14, 0xBB0D0F18);
                g.fill(px, py, px + 2, py + 14, c2);
                g.drawString(mc.font, lbl, px + 5, py + 3, 0xFFFFFFFF, true);
                py += 16;
            }
        }

        // 5. Target HUD (с плавным HP)
        if (VisualConfig.targetHud && mc.crosshairPickEntity instanceof LivingEntity target) {
            int sx = mc.getWindow().getGuiScaledWidth() / 2 + 16, sy = mc.getWindow().getGuiScaledHeight() / 2 + 12;
            animatedTargetHp = Mth.lerp(0.15f, animatedTargetHp, target.getHealth());
            float pct = Mth.clamp(animatedTargetHp / Math.max(1f, target.getMaxHealth()), 0f, 1f);
            g.fill(sx, sy, sx + 124, sy + 36, 0xDD0D0F18);
            g.fill(sx, sy, sx + 124, sy + 2, c1);
            g.drawString(mc.font, target.getName().getString(), sx + 6, sy + 6, 0xFFFFFFFF, true);
            g.drawString(mc.font, String.format("%.1f ❤", animatedTargetHp), sx + 6, sy + 17, 0xFFFF5577, true);
            g.fill(sx + 6, sy + 28, sx + 118, sy + 32, 0xFF252A3D);
            g.fill(sx + 6, sy + 28, sx + 6 + (int) (112 * pct), sy + 32, c1);
        }

        // 6. ArrayList справа сверху
        if (VisualConfig.arrayList) {
            List<String> active = new ArrayList<>();
            if (VisualConfig.targetEsp) active.add("TargetESP [" + VisualConfig.getTargetEspName() + "]");
            if (VisualConfig.trails) active.add("Trails");
            if (VisualConfig.jumpCircles) active.add("JumpCircles");
            if (VisualConfig.hitParticles) active.add("HitParticles");
            if (VisualConfig.trajectories) active.add("Trajectories");
            if (VisualConfig.handView) active.add("ViewModel [" + VisualConfig.getSwingName() + "]");
            if (VisualConfig.fullbright) active.add("Fullbright");
            active.sort((a, b) -> mc.font.width(b) - mc.font.width(a));
            int ay = 6, sw = mc.getWindow().getGuiScaledWidth();
            for (String m : active) {
                int mw = mc.font.width(m) + 8;
                g.fill(sw - mw - 4, ay, sw - 4, ay + 13, 0xAA0D0F18);
                g.fill(sw - 6, ay, sw - 4, ay + 13, c1);
                g.drawString(mc.font, m, sw - mw, ay + 3, 0xFFFFFFFF, true);
                ay += 14;
            }
        }

        // 7. Кастомный прицел
        if (VisualConfig.customCrosshair) {
            int cx = mc.getWindow().getGuiScaledWidth() / 2, cy = mc.getWindow().getGuiScaledHeight() / 2;
            int gap = VisualConfig.dynamicCrosshair && mc.player.getDeltaMovement().horizontalDistanceSqr() > 0.002 ? 6 : 3;
            g.fill(cx - gap - 5, cy, cx - gap, cy + 1, c1);
            g.fill(cx + gap + 1, cy, cx + gap + 6, cy + 1, c1);
            g.fill(cx, cy - gap - 5, cx + 1, cy - gap, c2);
            g.fill(cx, cy + gap + 1, cx + 1, cy + gap + 6, c2);
        }
    }

    private static void drawKey(GuiGraphics g, Minecraft mc, String t, int x, int y, int w, int h, boolean p, int col) {
        g.fill(x, y, x + w, y + h, p ? col : 0xBB0D0F18);
        g.drawCenteredString(mc.font, t, x + w / 2, y + (h - 8) / 2, p ? 0xFF000000 : 0xFFFFFFFF);
    }
}
