package ru.mirtime.visuals.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
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
        if (hand == InteractionHand.OFF_HAND && !VisualConfig.leftHandView) return;

        int side = (hand == InteractionHand.MAIN_HAND) ? 1 : -1;
        poseStack.translate(VisualConfig.handOffsetX * side, VisualConfig.handOffsetY, VisualConfig.handOffsetZ);
        poseStack.mulPose(Axis.XP.rotationDegrees(VisualConfig.handRotX));
        poseStack.mulPose(Axis.YP.rotationDegrees(VisualConfig.handRotY * side));
        poseStack.mulPose(Axis.ZP.rotationDegrees(VisualConfig.handRotZ * side));

        if (swingProgress > 0 && VisualConfig.swingAnimation > 0) {
            float sp = Mth.sin(Mth.sqrt(swingProgress) * (float) Math.PI);
            switch (VisualConfig.swingAnimation) {
                case 1 -> poseStack.mulPose(Axis.ZP.rotationDegrees(sp * -45f * side));
                case 2 -> {
                    poseStack.translate(-0.12f * side, 0.08f, 0f);
                    poseStack.mulPose(Axis.XP.rotationDegrees(-sp * 55f));
                    poseStack.mulPose(Axis.YP.rotationDegrees(sp * 30f * side));
                }
                case 3 -> poseStack.mulPose(Axis.YP.rotationDegrees(sp * 70f * side));
                case 4 -> {
                    poseStack.translate(0f, -sp * 0.18f, 0f);
                    poseStack.mulPose(Axis.XP.rotationDegrees(-sp * 65f));
                }
            }
        }
        poseStack.scale(VisualConfig.handScale, VisualConfig.handScale, VisualConfig.handScale);
    }
}
