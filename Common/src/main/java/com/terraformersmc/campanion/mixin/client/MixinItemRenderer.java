package com.terraformersmc.campanion.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.terraformersmc.campanion.client.renderer.item.BuiltTentItemRenderer;
import com.terraformersmc.campanion.client.renderer.item.SpearItemRenderer;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.item.SpearItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Inject(method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/renderer/block/model/ItemTransforms$TransformType;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V", at = @At("HEAD"), cancellable = true)
    public void renderStatic(LivingEntity entity, ItemStack stack, ItemTransforms.TransformType renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, Level world, int light, int overlay, int seed, CallbackInfo info) {
        BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, world, entity, seed);
        if(stack.getItem() instanceof SpearItem && SpearItemRenderer.INSTANCE.render(entity, stack, renderMode, leftHanded, matrices, vertexConsumers, light, overlay, model)) {
            info.cancel();
        }
        if(stack.getItem() == CampanionItems.TENT_BAG && renderMode != ItemTransforms.TransformType.GUI) {
            matrices.pushPose();
            matrices.scale(1/4F, 1/4F, 1/4F);
            Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.STONE.defaultBlockState()).getTransforms().getTransform(renderMode).apply(leftHanded, matrices);
            boolean ret = BuiltTentItemRenderer.INSTANCE.render(stack, matrices, BlockPos.ZERO.above(500), vertexConsumers, light);
            matrices.popPose();
            if(ret) {
                info.cancel();
            }
        }
    }

}
