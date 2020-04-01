package com.campanion.mixin.client;

import com.campanion.client.items.BuiltTentItemRenderer;
import com.campanion.client.items.SpearItemRenderer;
import com.campanion.item.CampanionItems;
import com.campanion.item.SpearItem;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    public void renderItem(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {
        if(stack.getItem() instanceof SpearItem && SpearItemRenderer.INSTANCE.render(stack, renderMode, leftHanded, matrices, vertexConsumers, light, overlay, model)) {
            info.cancel();
        }
        if(stack.getItem() == CampanionItems.TENT_BAG && BuiltTentItemRenderer.INSTANCE.render(stack, renderMode, leftHanded, matrices, vertexConsumers, light, overlay, model)) {
            info.cancel();
        }
    }
}
