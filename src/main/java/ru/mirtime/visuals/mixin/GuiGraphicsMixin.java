package ru.mirtime.visuals.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.mirtime.visuals.VisualConfig;

@Mixin(Screen.class)
public class GuiGraphicsMixin {

    @Inject(method = "renderTransparentBackground", at = @At("HEAD"), cancellable = true)
    private void onRenderInvBackground(GuiGraphics graphics, CallbackInfo ci) {
        if (!VisualConfig.inventoryGlass) return;
        int topColor = (0x88 << 24) | (VisualConfig.getSecondaryColor() & 0x00FFFFFF);
        int bottomColor = 0xCC0A0E17;
        graphics.fillGradient(0, 0, graphics.guiWidth(), graphics.guiHeight(), topColor, bottomColor);
        ci.cancel();
    }
}
