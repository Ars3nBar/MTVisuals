package ru.mirtime.visuals;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

public class HudAndGuiScreen extends Screen {

    private static int activeTab = 0; // 0 = HUD, 1 = 3D Косметика, 2 = PvP и Мир

    public HudAndGuiScreen() {
        super(Component.literal("MTVisuals ClickGUI"));
    }

    public static void renderHud(GuiGraphics g) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) return;

        int c1 = VisualConfig.getPrimaryColor();
        int c2 = VisualConfig.getSecondaryColor();

        // 1. Watermark (с учётом Streamer Mode)
        if (VisualConfig.watermark) {
            String nick = VisualConfig.streamerMode ? "MTVisuals_User" : mc.player.getName().getString();
            String text = "⚡ MTVisuals | " + nick + " | " + mc.getFps() + " FPS | XYZ: " + (int) mc.player.getX() + " " + (int) mc.player.getY() + " " + (int) mc.player.getZ();
            int w = mc.font.width(text) + 12;
            g.fillGradient(6, 6, 6 + w, 22, 0xCC0B101B, 0xCC11192B);
            g.fill(6, 6, 6 + w, 8, c1);
            g.drawString(mc.font, text, 12, 10, 0xFFFFFFFF, true);
        }

        // 2. Keystrokes (WASD + LMB/RMB + Space)
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

        // 3. Кастомный динамический неоновый прицел
        if (VisualConfig.customCrosshair) {
            int cx = g.guiWidth() / 2;
            int cy = g.guiHeight() / 2;
            int gap = mc.player.isSprinting() ? 5 : 3;
            int len = 4;
            g.fill(cx - 1, cy - 1, cx + 1, cy + 1, 0xFFFFFFFF);
            g.fill(cx - gap - len, cy - 1, cx - gap, cy + 1, c1);
            g.fill(cx + gap, cy - 1, cx + gap + len, cy + 1, c1);
            g.fill(cx - 1, cy - gap - len, cx + 1, cy - gap, c1);
            g.fill(cx - 1, cy + gap, cx + 1, cy + gap + len, c1);
        }

        // 4. Target HUD (здоровье и имя цели)
        if (VisualConfig.targetHud && mc.crosshairPickEntity instanceof LivingEntity target) {
            int sx = g.guiWidth() / 2 + 16;
            int sy = g.guiHeight() / 2 - 16;
            g.fillGradient(sx, sy, sx + 130, sy + 34, 0xDD0B101B, 0xDD141E30);
            g.fill(sx, sy, sx + 130, sy + 2, c1);
            String tName = VisualConfig.streamerMode ? "Игрок" : target.getName().getString();
            g.drawString(mc.font, tName + " §c" + (int) Math.ceil(target.getHealth()) + "❤", sx + 6, sy + 6, 0xFFFFFFFF, true);

            float hpPct = Math.max(0f, Math.min(1f, target.getHealth() / target.getMaxHealth()));
            g.fill(sx + 6, sy + 21, sx + 124, sy + 27, 0xFF1E293B);
            g.fill(sx + 6, sy + 21, sx + 6 + (int) (118 * hpPct), sy + 27, c1);
        }
    }

    private static void drawKey(GuiGraphics g, Minecraft mc, String label, int x, int y, int w, int h, boolean pressed, int activeCol) {
        g.fill(x, y, x + w, y + h, pressed ? activeCol : 0xAA0B101B);
        g.drawCenteredString(mc.font, label, x + w / 2, y + (h - 8) / 2, 0xFFFFFFFF);
    }
    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2 - 90;

        // Вкладки сверху
        this.addRenderableWidget(Button.builder(Component.literal(activeTab == 0 ? "§b§l[ Интерфейс ]" : "Интерфейс"), b -> { activeTab = 0; this.rebuildWidgets(); }).bounds(cx - 160, cy, 102, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal(activeTab == 1 ? "§b§l[ 3D Косметика ]" : "3D Косметика"), b -> { activeTab = 1; this.rebuildWidgets(); }).bounds(cx - 51, cy, 102, 20).build());
        this.addRenderableWidget(Button.builder(Component.literal(activeTab == 2 ? "§b§l[ PvP и Мир ]" : "PvP и Мир"), b -> { activeTab = 2; this.rebuildWidgets(); }).bounds(cx + 58, cy, 102, 20).build());

        int y = cy + 30;
        if (activeTab == 0) {
            addToggle(cx - 160, y, 155, "Watermark HUD", () -> VisualConfig.watermark, v -> VisualConfig.watermark = v);
            addToggle(cx + 5, y, 155, "Keystrokes + CPS", () -> VisualConfig.keystrokes, v -> VisualConfig.keystrokes = v);
            addToggle(cx - 160, y + 26, 155, "Target HUD", () -> VisualConfig.targetHud, v -> VisualConfig.targetHud = v);
            addToggle(cx + 5, y + 26, 155, "Кастом Прицел", () -> VisualConfig.customCrosshair, v -> VisualConfig.customCrosshair = v);
            addToggle(cx - 160, y + 52, 155, "Неон Инвентарь", () -> VisualConfig.inventoryGlass, v -> VisualConfig.inventoryGlass = v);
            addToggle(cx + 5, y + 52, 155, "Streamer Mode", () -> VisualConfig.streamerMode, v -> VisualConfig.streamerMode = v);
        } else if (activeTab == 1) {
            this.addRenderableWidget(Button.builder(Component.literal("Шапка: §b" + VisualConfig.getHatName()), btn -> {
                VisualConfig.hatMode = (VisualConfig.hatMode + 1) % 4;
                btn.setMessage(Component.literal("Шапка: §b" + VisualConfig.getHatName()));
            }).bounds(cx - 160, y, 320, 20).build());

            this.addRenderableWidget(Button.builder(Component.literal("Спина: §b" + VisualConfig.getBackName()), btn -> {
                VisualConfig.backMode = (VisualConfig.backMode + 1) % 4;
                btn.setMessage(Component.literal("Спина: §b" + VisualConfig.getBackName()));
            }).bounds(cx - 160, y + 26, 320, 20).build());

            addToggle(cx - 160, y + 52, 155, "Шлейф (Trails)", () -> VisualConfig.trails, v -> VisualConfig.trails = v);
            addToggle(cx + 5, y + 52, 155, "Jump Circles", () -> VisualConfig.jumpCircles, v -> VisualConfig.jumpCircles = v);
        } else {
            addToggle(cx - 160, y, 155, "TargetESP (Сканер)", () -> VisualConfig.targetEsp, v -> VisualConfig.targetEsp = v);
            addToggle(cx + 5, y, 155, "Неон Хитбоксы", () -> VisualConfig.customHitboxes, v -> VisualConfig.customHitboxes = v);
            addToggle(cx - 160, y + 26, 155, "Компактная рука", () -> VisualConfig.handView, v -> VisualConfig.handView = v);
            addToggle(cx + 5, y + 26, 155, "NoHurtCam", () -> VisualConfig.noHurtCam, v -> VisualConfig.noHurtCam = v);
            addToggle(cx - 160, y + 52, 320, "Неоновый туман мира (Custom Fog)", () -> VisualConfig.customFog, v -> VisualConfig.customFog = v);
        }

        // Общие кнопки снизу (Время и Тема)
        this.addRenderableWidget(Button.builder(Component.literal("Время: §e" + VisualConfig.getTimeModeName()), btn -> {
            VisualConfig.timeMode = (VisualConfig.timeMode + 1) % 5;
            btn.setMessage(Component.literal("Время: §e" + VisualConfig.getTimeModeName()));
        }).bounds(cx - 160, cy + 120, 155, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Тема: §d" + VisualConfig.getThemeName()), btn -> {
            VisualConfig.themeMode = (VisualConfig.themeMode + 1) % 5;
            btn.setMessage(Component.literal("Тема: §d" + VisualConfig.getThemeName()));
        }).bounds(cx + 5, cy + 120, 155, 20).build());
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
        int cy = this.height / 2 - 118;
        g.fillGradient(cx - 170, cy, cx + 170, cy + 180, 0xEE090D16, 0xEE101829);
        g.fill(cx - 170, cy, cx + 170, cy + 3, VisualConfig.getPrimaryColor());
        g.drawCenteredString(this.font, "⚡ MTVisuals v2.0 | Кастомизация клиента", cx, cy + 10, 0xFFFFFFFF);
        super.render(g, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
