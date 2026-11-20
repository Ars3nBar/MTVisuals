package ru.mirtime.visuals.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.mirtime.visuals.VisualConfig;

@Mixin(FogRenderer.class)
public class FogMixin {

    @Inject(method = "computeFogColor", at = @At("RETURN"), cancellable = true)
    private static void onComputeFogColor(Camera camera, float partialTicks, ClientLevel level, int renderDistance, float darkenWorldAmount, CallbackInfoReturnable<Vector4f> cir) {
        if (!VisualConfig.customFog) return;
        int col = VisualConfig.getPrimaryColor();
        float r = ((col >> 16) & 0xFF) / 255f * 0.45f;
        float g = ((col >> 8) & 0xFF) / 255f * 0.45f;
        float b = (col & 0xFF) / 255f * 0.45f;
        cir.setReturnValue(new Vector4f(r, g, b, 1.0f));
    }
}
