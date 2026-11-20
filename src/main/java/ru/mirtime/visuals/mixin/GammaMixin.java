package ru.mirtime.visuals.mixin;

import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.mirtime.visuals.VisualConfig;

@Mixin(LightTexture.class)
public class GammaMixin {

    @Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
    private static void onGetBrightness(CallbackInfoReturnable<Float> cir) {
        if (VisualConfig.fullbright) {
            cir.setReturnValue(1.0f);
        }
    }
}
