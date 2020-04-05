package com.campanion.mixin.client;

import com.campanion.client.renderer.item.BuiltTentItemRenderer;
import com.campanion.client.renderer.item.SpearItemRenderer;
import com.campanion.item.CampanionItems;
import com.campanion.item.SpearItem;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Inject(method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;II)V", at = @At("HEAD"), cancellable = true)
    public void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, World world, int light, int overlay, CallbackInfo info) {
        BakedModel model = MinecraftClient.getInstance().getItemRenderer().getHeldItemModel(stack, world, entity);
        if(stack.getItem() instanceof SpearItem && SpearItemRenderer.INSTANCE.render(entity, stack, renderMode, leftHanded, matrices, vertexConsumers, light, overlay, model)) {
            info.cancel();
        }
        if(stack.getItem() == CampanionItems.TENT_BAG && renderMode != ModelTransformation.Mode.GUI) {
            matrices.push();
            matrices.scale(1/4F, 1/4F, 1/4F);
            MinecraftClient.getInstance().getBlockRenderManager().getModel(Blocks.STONE.getDefaultState()).getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
            boolean ret = BuiltTentItemRenderer.INSTANCE.render(stack, matrices, BlockPos.ORIGIN.up(500), vertexConsumers, light);
            matrices.pop();
            if(ret) {
                info.cancel();
            }
        }
    }

}
