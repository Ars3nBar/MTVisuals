package ru.mirtime.visuals.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.mirtime.visuals.MTVisualsClient;
import ru.mirtime.visuals.VisualConfig;

@Mixin(GameRenderer.class)
public class ZoomMixin {
    @Unique
    private static float currentZoom = 1.0f;

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void onGetFov(Camera camera, float partialTicks, boolean useFovSetting, CallbackInfoReturnable<Float> cir) {
        float target = MTVisualsClient.isZooming() ? VisualConfig.zoomFactor : 1.0f;
        if (VisualConfig.smoothZoom) {
            currentZoom = Mth.lerp(0.18f, currentZoom, target);
        } else {
            currentZoom = target;
        }
        if (currentZoom > 1.01f) {
            cir.setReturnValue(cir.getReturnValue() / currentZoom);
        }
    }
}
