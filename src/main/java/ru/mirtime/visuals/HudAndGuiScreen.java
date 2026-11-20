package ru.mirtime.visuals;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class HudAndGuiScreen extends Screen {

    public HudAndGuiScreen() {
        super(Component.literal("MTVisuals ClickGUI"));
    }

    public static void renderHud(GuiGraphics g) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        int c1 = VisualConfig.getPrimaryColor();
        int c2 = VisualConfig.getSecondaryColor();

        // 1. Водяной знак (Watermark)
        if (VisualConfig.watermark) {
            String text = "⚡ MTVisuals | " + mc.getFps() + " FPS | XYZ: " + (int) mc.player.getX() + " " + (int) mc.player.getY() + " " + (int) mc.player.getZ();
            int w = mc.font.width(text) + 12;
            g.fillGradient(6, 6, 6 + w, 22, 0xCC0B101B, 0xCC11192B);
            g.fill(6, 6, 6 + w, 8, c1);
            g.drawString(mc.font, text, 12, 10, 0xFFFFFFFF, true);
        }

        // 2. Keystrokes (WASD + Space)
        if (VisualConfig.keystrokes) {
            int bx = 10, by = 32;
            drawKey(g, mc, "W", bx + 22, by, mc.options.keyUp.isDown(), c1);
            drawKey(g, mc, "A", bx, by + 22, mc.options.keyLeft.isDown(), c1);
            drawKey(g, mc, "S", bx + 22, by + 22, mc.options.keyDown.isDown(), c1);
            drawKey(g, mc, "D", bx + 44, by + 22, mc.options.keyRight.isDown(), c1);
            drawKeyWide(g, mc, "SPACE", bx, by + 44, 64, mc.options.keyJump.isDown(), c2);
        }

        // 3. Target HUD (когда наводишь прицел на игрока или моба)
        if (VisualConfig.targetHud && mc.crosshairPickEntity instanceof LivingEntity target) {
            int sx = g.guiWidth() / 2 + 14;
            int sy = g.guiHeight() / 2 - 14;
            g.fillGradient(sx, sy, sx + 125, sy + 32, 0xCC0B101B, 0xCC141E30);
            g.fill(sx, sy, sx + 125, sy + 2, c1);
            g.drawString(mc.font, target.getName().getString(), sx + 6, sy + 6, 0xFFFFFFFF, true);

            float hpPct = Math.max(0f, Math.min(1f, target.getHealth() / target.getMaxHealth()));
            g.fill(sx + 6, sy + 20, sx + 119, sy + 26, 0xFF1E293B);
            g.fill(sx + 6, sy + 20, sx + 6 + (int) (113 * hpPct), sy + 26, c1);
        }
    }

    private static void drawKey(GuiGraphics g, Minecraft mc, String label, int x, int y, boolean pressed, int activeCol) {
        g.fill(x, y, x + 20, y + 20, pressed ? activeCol : 0xAA0B101B);
        g.drawCenteredString(mc.font, label, x + 10, y + 6, 0xFFFFFFFF);
    }

    private static void drawKeyWide(GuiGraphics g, Minecraft mc, String label, int x, int y, int w, boolean pressed, int activeCol) {
        g.fill(x, y, x + w, y + 16, pressed ? activeCol : 0xAA0B101B);
        g.drawCenteredString(mc.font, label, x + w / 2, y + 4, 0xFFFFFFFF);
    }
    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2 - 85;

        addToggle(cx - 155, cy, 150, "Водяной знак HUD", () -> VisualConfig.watermark, v -> VisualConfig.watermark = v);
        addToggle(cx + 5, cy, 150, "Keystrokes (WASD)", () -> VisualConfig.keystrokes, v -> VisualConfig.keystrokes = v);

        addToggle(cx - 155, cy + 25, 150, "Target HUD (Прицел)", () -> VisualConfig.targetHud, v -> VisualConfig.targetHud = v);
        addToggle(cx + 5, cy + 25, 150, "Неон фон инвентаря", () -> VisualConfig.inventoryGlass, v -> VisualConfig.inventoryGlass = v);

        addToggle(cx - 155, cy + 50, 150, "3D Нимб на голову", () -> VisualConfig.haloHat, v -> VisualConfig.haloHat = v);
        addToggle(cx + 5, cy + 50, 150, "3D Крылья за спиной", () -> VisualConfig.cyberWings, v -> VisualConfig.cyberWings = v);

        addToggle(cx - 155, cy + 75, 150, "Jump Circles (Прыжки)", () -> VisualConfig.jumpCircles, v -> VisualConfig.jumpCircles = v);
        addToggle(cx + 5, cy + 75, 150, "Неоновые 3D-хитбоксы", () -> VisualConfig.customHitboxes, v -> VisualConfig.customHitboxes = v);

        addToggle(cx - 155, cy + 100, 310, "Компактная рука и меч (ViewModel)", () -> VisualConfig.handView, v -> VisualConfig.handView = v);

        this.addRenderableWidget(Button.builder(Component.literal("Время мира: " + VisualConfig.getTimeModeName()), btn -> {
            VisualConfig.timeMode = (VisualConfig.timeMode + 1) % 5;
            btn.setMessage(Component.literal("Время мира: " + VisualConfig.getTimeModeName()));
        }).bounds(cx - 155, cy + 130, 150, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Тема: " + VisualConfig.getThemeName()), btn -> {
            VisualConfig.themeMode = (VisualConfig.themeMode + 1) % 4;
            btn.setMessage(Component.literal("Тема: " + VisualConfig.getThemeName()));
        }).bounds(cx + 5, cy + 130, 150, 20).build());
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
        int cx = this.width / 2;
        int cy = this.height / 2 - 115;
        g.fillGradient(cx - 165, cy, cx + 165, cy + 195, 0xEE090D16, 0xEE101829);
        g.fill(cx - 165, cy, cx + 165, cy + 3, VisualConfig.getPrimaryColor());
        g.drawCenteredString(this.font, "⚡ MTVisuals v1.0 | Настройка локальных визуалов", cx, cy + 10, 0xFFFFFFFF);
        super.render(g, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
