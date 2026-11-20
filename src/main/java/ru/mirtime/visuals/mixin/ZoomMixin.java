package ru.mirtime.visuals.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.mirtime.visuals.MTVisualsClient;
import ru.mirtime.visuals.VisualConfig;

@Mixin(GameRenderer.class)
public class ZoomMixin {

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void onGetFov(Camera camera, float partialTicks, boolean useFovSetting, CallbackInfoReturnable<Float> cir) {
        if (MTVisualsClient.isZooming()) {
            cir.setReturnValue(cir.getReturnValue() / VisualConfig.zoomFactor);
        }
    }
}
