package ru.mirtime.visuals.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.mirtime.visuals.VisualConfig;

@Mixin(ClientLevel.ClientLevelData.class)
public class ClientLevelMixin {

    @Inject(method = "getDayTime", at = @At("HEAD"), cancellable = true)
    private void onGetDayTime(CallbackInfoReturnable<Long> cir) {
        if (VisualConfig.timeMode == 0) return;

        long time = switch (VisualConfig.timeMode) {
            case 1 -> 6000L;  // Ясный день
            case 2 -> 12800L; // Красивый закат
            case 3 -> 18000L; // Глубокая ночь
            case 4 -> (System.currentTimeMillis() / 10L) % 24000L; // Плавный цикл
            default -> 12800L;
        };
        cir.setReturnValue(time);
    }
}
