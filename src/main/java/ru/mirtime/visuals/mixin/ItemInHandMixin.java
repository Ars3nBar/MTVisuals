package ru.mirtime.visuals.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.mirtime.visuals.VisualConfig;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandMixin {

    @Inject(method = "renderArmWithItem", at = @At("HEAD"))
    private void onRenderHand(AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress, ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, CallbackInfo ci) {
        if (!VisualConfig.handView || stack.isEmpty()) return;
        float side = hand == InteractionHand.MAIN_HAND ? 1.0f : -1.0f;
        poseStack.translate(side * VisualConfig.handOffsetX, VisualConfig.handOffsetY, -0.08f);
        poseStack.scale(VisualConfig.handScale, VisualConfig.handScale, VisualConfig.handScale);
    }
}
