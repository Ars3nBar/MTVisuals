package ru.mirtime.visuals;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class HudAndGuiScreen extends Screen {

    private static int activeTab = 0; // 0=HUD, 1=Косметика, 2=PvP, 3=Рука, 4=Мир и Цвета

    public HudAndGuiScreen() {
        super(Component.literal("MTVisuals ClickGUI"));
    }

    public static void renderHud(GuiGraphics g) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        int c1 = VisualConfig.getPrimaryColor();
        int c2 = VisualConfig.getSecondaryColor();

        // 1. Watermark
        if (VisualConfig.watermark) {
            String nick = VisualConfig.streamerMode ? "MTVisuals_User" : mc.player.getName().getString();
            String text = "⚡ MTVisuals v3.0 | " + nick + " | " + mc.getFps() + " FPS | XYZ: " + (int) mc.player.getX() + " " + (int) mc.player.getY() + " " + (int) mc.player.getZ();
            int w = mc.font.width(text) + 12;
            g.fillGradient(6, 6, 6 + w, 22, 0xCC0B101B, 0xCC11192B);
            g.fill(6, 6, 6 + w, 8, c1);
            g.drawString(mc.font, text, 12, 10, 0xFFFFFFFF, true);
        }

        // 2. Keystrokes + LMB/RMB
        if (VisualConfig.keystrokes) {
            int bx = 10, by = 32;
            drawKey(g, mc, "W", bx + 22, by, 20, 20, mc.options.keyUp.isDown(), c1);
            drawKey(g, mc, "A", bx, by + 22, 20, 20, mc.options.keyLeft.isDown(), c1);
            drawKey(g, mc, "S", bx + 22, by + 22, 20, 20, mc.options.keyDown.isDown(), c1);
            drawKey(g, mc, "D", bx + 44, by + 22, 20, 20, mc.options.keyRight.isDown(), c1);
            drawKey(g, mc, "LMB", bx, by + 44, 31, 18, mc.options.keyAttack.isDown(), c2);
            drawKey(g, mc, "RMB", bx + 33, by + 44, 31, 18, mc.options.keyUse.isDown(), c2);
            drawKey(g, mc, "SPACE", bx, by + 64, 64, 15, mc.options.keyJump.isDown(), c1);
        }

        // 3. Armor HUD (броня и прочность справа от хотбара)
        if (VisualConfig.armorHud) {
            int ax = g.guiWidth() / 2 + 100;
            int ay = g.guiHeight() - 22;
            for (ItemStack armor : mc.player.getArmorSlots()) {
                if (!armor.isEmpty()) {
                    g.renderItem(armor, ax, ay);
                    g.renderItemDecorations(mc.font, armor, ax, ay);
                    ax += 18;
                }
            }
        }
        // 4. Potions HUD (активные зелья слева снизу)
        if (VisualConfig.potionsHud) {
            int py = g.guiHeight() - 24;
            for (MobEffectInstance eff : mc.player.getActiveEffects()) {
                int sec = eff.getDuration() / 20;
                String label = eff.getEffect().value().getDisplayName().getString() + " §b" + (sec / 60) + ":" + String.format("%02d", sec % 60);
                int w = mc.font.width(label) + 10;
                g.fillGradient(6, py, 6 + w, py + 15, 0xCC0B101B, 0xCC141E30);
                g.fill(6, py, 8, py + 15, c1);
                g.drawString(mc.font, label, 11, py + 4, 0xFFFFFFFF, true);
                py -= 18;
            }
        }

        // 5. Кастомный прицел (3 стиля)
        if (VisualConfig.customCrosshair) {
            int cx = g.guiWidth() / 2, cy = g.guiHeight() / 2;
            int gap = mc.player.isSprinting() ? 5 : 3;
            g.fill(cx - 1, cy - 1, cx + 1, cy + 1, 0xFFFFFFFF);
            if (VisualConfig.crosshairStyle == 0) {
                g.fill(cx - gap - 4, cy - 1, cx - gap, cy + 1, c1);
                g.fill(cx + gap, cy - 1, cx + gap + 4, cy + 1, c1);
                g.fill(cx - 1, cy - gap - 4, cx + 1, cy - gap, c1);
                g.fill(cx - 1, cy + gap, cx + 1, cy + gap + 4, c1);
            } else if (VisualConfig.crosshairStyle == 1) {
                g.fill(cx - 6, cy - 6, cx - 2, cy - 5, c1); g.fill(cx - 6, cy - 6, cx - 5, cy - 2, c1);
                g.fill(cx + 2, cy - 6, cx + 6, cy - 5, c1); g.fill(cx + 5, cy - 6, cx + 6, cy - 2, c1);
                g.fill(cx - 6, cy + 5, cx - 2, cy + 6, c1); g.fill(cx - 6, cy + 2, cx - 5, cy + 6, c1);
                g.fill(cx + 2, cy + 5, cx + 6, cy + 6, c1); g.fill(cx + 5, cy + 2, cx + 6, cy + 6, c1);
            } else {
                g.fill(cx - gap - 5, cy - 1, cx - gap, cy + 1, c2);
                g.fill(cx + gap, cy - 1, cx + gap + 5, cy + 1, c2);
            }
        }

        // 6. Target HUD
        if (VisualConfig.targetHud && mc.crosshairPickEntity instanceof LivingEntity target) {
            int sx = g.guiWidth() / 2 + 16, sy = g.guiHeight() / 2 - 16;
            g.fillGradient(sx, sy, sx + 135, sy + 36, 0xDD0B101B, 0xDD141E30);
            g.fill(sx, sy, sx + 135, sy + 2, c1);
            String tName = VisualConfig.streamerMode ? "Игрок" : target.getName().getString();
            g.drawString(mc.font, tName + " §c" + (int) Math.ceil(target.getHealth()) + "❤", sx + 6, sy + 6, 0xFFFFFFFF, true);
            float hpPct = Math.max(0f, Math.min(1f, target.getHealth() / target.getMaxHealth()));
            g.fill(sx + 6, sy + 22, sx + 129, sy + 28, 0xFF1E293B);
            g.fill(sx + 6, sy + 22, sx + 6 + (int) (123 * hpPct), sy + 28, c1);
        }
    }

    private static void drawKey(GuiGraphics g, Minecraft mc, String label, int x, int y, int w, int h, boolean pressed, int activeCol) {
        g.fill(x, y, x + w, y + h, pressed ? activeCol : 0xAA0B101B);
        g.drawCenteredString(mc.font, label, x + w / 2, y + (h - 8) / 2, 0xFFFFFFFF);
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2 - 102;

        String[] tabs = {"HUD", "Косметика", "PvP", "Рука", "Мир"};
        for (int i = 0; i < tabs.length; i++) {
            final int idx = i;
            this.addRenderableWidget(Button.builder(Component.literal(activeTab == i ? "§b§l" + tabs[i] : tabs[i]), b -> {
                activeTab = idx;
                this.rebuildWidgets();
            }).bounds(cx - 175 + i * 71, cy, 68, 20).build());
        }

        int y = cy + 28;
        if (activeTab == 0) {
            addToggle(cx - 175, y, 170, "Watermark HUD", () -> VisualConfig.watermark, v -> VisualConfig.watermark = v);
            addToggle(cx + 5, y, 170, "Keystrokes + LMB/RMB", () -> VisualConfig.keystrokes, v -> VisualConfig.keystrokes = v);
            addToggle(cx - 175, y + 25, 170, "Armor HUD (Броня)", () -> VisualConfig.armorHud, v -> VisualConfig.armorHud = v);
            addToggle(cx + 5, y + 25, 170, "Potions HUD (Зелья)", () -> VisualConfig.potionsHud, v -> VisualConfig.potionsHud = v);
            addToggle(cx - 175, y + 50, 170, "Target HUD (Цель)", () -> VisualConfig.targetHud, v -> VisualConfig.targetHud = v);
            addToggle(cx + 5, y + 50, 170, "Неон Инвентарь", () -> VisualConfig.inventoryGlass, v -> VisualConfig.inventoryGlass = v);
            addToggle(cx - 175, y + 75, 170, "Кастом Прицел", () -> VisualConfig.customCrosshair, v -> VisualConfig.customCrosshair = v);
            this.addRenderableWidget(Button.builder(Component.literal("Стиль прицела: #" + (VisualConfig.crosshairStyle + 1)), b -> {
                VisualConfig.crosshairStyle = (VisualConfig.crosshairStyle + 1) % 3;
                b.setMessage(Component.literal("Стиль прицела: #" + (VisualConfig.crosshairStyle + 1)));
            }).bounds(cx + 5, y + 75, 170, 20).build());
            addToggle(cx - 175, y + 100, 350, "Streamer Mode (Скрытие ника)", () -> VisualConfig.streamerMode, v -> VisualConfig.streamerMode = v);
        } else if (activeTab == 1) {
            this.addRenderableWidget(Button.builder(Component.literal("Шапка: §b" + VisualConfig.getHatName()), b -> {
                VisualConfig.hatMode = (VisualConfig.hatMode + 1) % 6;
                b.setMessage(Component.literal("Шапка: §b" + VisualConfig.getHatName()));
            }).bounds(cx - 175, y, 350, 20).build());
            this.addRenderableWidget(Button.builder(Component.literal("Спина: §b" + VisualConfig.getBackName()), b -> {
                VisualConfig.backMode = (VisualConfig.backMode + 1) % 5;
                b.setMessage(Component.literal("Спина: §b" + VisualConfig.getBackName()));
            }).bounds(cx - 175, y + 25, 350, 20).build());
            this.addRenderableWidget(Button.builder(Component.literal("Питомец-спутник: §b" + VisualConfig.getPetName()), b -> {
                VisualConfig.petMode = (VisualConfig.petMode + 1) % 4;
                b.setMessage(Component.literal("Питомец-спутник: §b" + VisualConfig.getPetName()));
            }).bounds(cx - 175, y + 50, 350, 20).build());
            addToggle(cx - 175, y + 75, 170, "Шлейф (Trails)", () -> VisualConfig.trails, v -> VisualConfig.trails = v);
            addToggle(cx + 5, y + 75, 170, "Jump Circles", () -> VisualConfig.jumpCircles, v -> VisualConfig.jumpCircles = v);
            this.addRenderableWidget(Button.builder(Component.literal("Размер аксессуаров: §e" + String.format("%.1fx", VisualConfig.accessorySize)), b -> {
                VisualConfig.accessorySize = VisualConfig.accessorySize >= 1.5f ? 0.7f : Math.round((VisualConfig.accessorySize + 0.2f) * 10f) / 10f;
                b.setMessage(Component.literal("Размер аксессуаров: §e" + String.format("%.1fx", VisualConfig.accessorySize)));
            }).bounds(cx - 175, y + 100, 350, 20).build());
        }
        else if (activeTab == 2) {
            addToggle(cx - 175, y, 170, "TargetESP (Сканер)", () -> VisualConfig.targetEsp, v -> VisualConfig.targetEsp = v);
            this.addRenderableWidget(Button.builder(Component.literal("Стиль TargetESP: #" + (VisualConfig.targetEspStyle + 1)), b -> {
                VisualConfig.targetEspStyle = (VisualConfig.targetEspStyle + 1) % 3;
                b.setMessage(Component.literal("Стиль TargetESP: #" + (VisualConfig.targetEspStyle + 1)));
            }).bounds(cx + 5, y, 170, 20).build());
            addToggle(cx - 175, y + 25, 170, "Неон Хитбоксы", () -> VisualConfig.customHitboxes, v -> VisualConfig.customHitboxes = v);
            addToggle(cx + 5, y + 25, 170, "Вспышки ударов", () -> VisualConfig.hitParticles, v -> VisualConfig.hitParticles = v);
            addToggle(cx - 175, y + 50, 170, "Траектория стрел", () -> VisualConfig.trajectories, v -> VisualConfig.trajectories = v);
            addToggle(cx + 5, y + 50, 170, "NoHurtCam", () -> VisualConfig.noHurtCam, v -> VisualConfig.noHurtCam = v);
            this.addRenderableWidget(Button.builder(Component.literal("Кратность Zoom (Кнопка C): §b" + (int) VisualConfig.zoomFactor + "x"), b -> {
                VisualConfig.zoomFactor = VisualConfig.zoomFactor >= 6.0f ? 2.0f : VisualConfig.zoomFactor + 1.0f;
                b.setMessage(Component.literal("Кратность Zoom (Кнопка C): §b" + (int) VisualConfig.zoomFactor + "x"));
            }).bounds(cx - 175, y + 75, 350, 20).build());
        } else if (activeTab == 3) {
            addToggle(cx - 175, y, 350, "Включить кастомную руку (ViewModel)", () -> VisualConfig.handView, v -> VisualConfig.handView = v);
            this.addRenderableWidget(Button.builder(Component.literal("Размер предметов в руке: §b" + String.format("%.2f", VisualConfig.handScale)), b -> {
                VisualConfig.handScale = VisualConfig.handScale >= 1.15f ? 0.60f : Math.round((VisualConfig.handScale + 0.05f) * 100f) / 100f;
                b.setMessage(Component.literal("Размер предметов в руке: §b" + String.format("%.2f", VisualConfig.handScale)));
            }).bounds(cx - 175, y + 28, 350, 20).build());
            this.addRenderableWidget(Button.builder(Component.literal("Сдвиг по горизонтали (X): §e" + String.format("%.2f", VisualConfig.handOffsetX)), b -> {
                VisualConfig.handOffsetX = VisualConfig.handOffsetX >= 0.20f ? -0.10f : Math.round((VisualConfig.handOffsetX + 0.03f) * 100f) / 100f;
                b.setMessage(Component.literal("Сдвиг по горизонтали (X): §e" + String.format("%.2f", VisualConfig.handOffsetX)));
            }).bounds(cx - 175, y + 56, 350, 20).build());
            this.addRenderableWidget(Button.builder(Component.literal("Сдвиг по высоте (Y): §e" + String.format("%.2f", VisualConfig.handOffsetY)), b -> {
                VisualConfig.handOffsetY = VisualConfig.handOffsetY >= 0.10f ? -0.15f : Math.round((VisualConfig.handOffsetY + 0.03f) * 100f) / 100f;
                b.setMessage(Component.literal("Сдвиг по высоте (Y): §e" + String.format("%.2f", VisualConfig.handOffsetY)));
            }).bounds(cx - 175, y + 84, 350, 20).build());
        } else {
            addToggle(cx - 175, y, 170, "Fullbright (Яркость)", () -> VisualConfig.fullbright, v -> VisualConfig.fullbright = v);
            addToggle(cx + 5, y, 170, "Неоновый Туман", () -> VisualConfig.customFog, v -> VisualConfig.customFog = v);
            this.addRenderableWidget(Button.builder(Component.literal("Частицы в небе: §b" + VisualConfig.getWeatherName()), b -> {
                VisualConfig.weatherParticles = (VisualConfig.weatherParticles + 1) % 4;
                b.setMessage(Component.literal("Частицы в небе: §b" + VisualConfig.getWeatherName()));
            }).bounds(cx - 175, y + 28, 350, 20).build());
            this.addRenderableWidget(Button.builder(Component.literal("Время суток: §e" + VisualConfig.getTimeModeName()), b -> {
                VisualConfig.timeMode = (VisualConfig.timeMode + 1) % 5;
                b.setMessage(Component.literal("Время суток: §e" + VisualConfig.getTimeModeName()));
            }).bounds(cx - 175, y + 56, 350, 20).build());
            this.addRenderableWidget(Button.builder(Component.literal("Цветовая тема: §d" + VisualConfig.getThemeName()), b -> {
                VisualConfig.themeMode = (VisualConfig.themeMode + 1) % 7;
                b.setMessage(Component.literal("Цветовая тема: §d" + VisualConfig.getThemeName()));
            }).bounds(cx - 175, y + 84, 230, 20).build());
            this.addRenderableWidget(Button.builder(Component.literal("RGB: " + String.format("%.1fx", VisualConfig.rgbSpeed)), b -> {
                VisualConfig.rgbSpeed = VisualConfig.rgbSpeed >= 3.0f ? 0.5f : VisualConfig.rgbSpeed + 0.5f;
                b.setMessage(Component.literal("RGB: " + String.format("%.1fx", VisualConfig.rgbSpeed)));
            }).bounds(cx + 60, y + 84, 115, 20).build());
        }
    }

    private void addToggle(int x, int y, int w, String name, java.util.function.BooleanSupplier getter, java.util.function.Consumer<Boolean> setter) {
        this.addRenderableWidget(Button.builder(Component.literal(name + ": " + (getter.getAsBoolean() ? "§aВКЛ" : "§cВЫКЛ")), btn -> {
            boolean next = !getter.getAsBoolean();
            setter.accept(next);
            btn.setMessage(Component.literal(name + ": " + (next ? "§aВКЛ" : "§cВЫКЛ")));
        }).bounds(x, y, w, 20).build());
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta) {
        int cx = this.width / 2, cy = this.height / 2 - 128;
        g.fillGradient(cx - 185, cy, cx + 185, cy + 195, 0xEE090D16, 0xEE101829);
        g.fill(cx - 185, cy, cx + 185, cy + 3, VisualConfig.getPrimaryColor());
        g.drawCenteredString(this.font, "⚡ MTVisuals v3.0 MEGA | Кастомизация клиента", cx, cy + 10, 0xFFFFFFFF);
        super.render(g, mouseX, mouseY, delta);
    }

    @Override
    public void removed() {
        VisualConfig.save();
        super.removed();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
