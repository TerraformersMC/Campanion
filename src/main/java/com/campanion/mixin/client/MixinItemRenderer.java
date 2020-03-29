package com.campanion.mixin.client;

import com.campanion.client.model.entity.SpearEntityModel;
import com.campanion.client.renderer.entity.SpearEntityRenderer;
import com.campanion.item.SpearItem;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    private final SpearEntityModel spearEntityModel = new SpearEntityModel();

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    public void renderItem(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model, CallbackInfo info) {
        if(stack.getItem() instanceof SpearItem) {
            boolean renderNormal = renderMode == ModelTransformation.Mode.GUI || renderMode == ModelTransformation.Mode.GROUND || renderMode == ModelTransformation.Mode.FIXED;

            matrices.push();

            model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
            matrices.translate(-0.5D, -0.5D, -0.5D);

            if(renderNormal) {
                RenderLayer renderLayer = RenderLayers.getItemLayer(stack);
                RenderLayer renderLayer3;
                if (Objects.equals(renderLayer, TexturedRenderLayers.getEntityTranslucent())) {
                    renderLayer3 = TexturedRenderLayers.getEntityTranslucentCull();
                } else {
                    renderLayer3 = renderLayer;
                }

                VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexConsumers, renderLayer3, true, stack.hasEnchantmentGlint());
                this.renderBakedItemModel(model, stack, light, overlay, matrices, vertexConsumer);
            } else {
                matrices.push();
                matrices.scale(1.0F, -1.0F, -1.0F);
                VertexConsumer spear = ItemRenderer.getArmorVertexConsumer(
                    vertexConsumers,
                    this.spearEntityModel.getLayer(SpearEntityRenderer.getTexture(((SpearItem)stack.getItem()).getType())),
                    false,
                    stack.hasEnchantmentGlint()
                );
                this.spearEntityModel.render(matrices, spear, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
                matrices.pop();
            }

            matrices.pop();

            info.cancel();
        }
    }

    @Shadow
    private void renderBakedItemModel(BakedModel model, ItemStack stack, int light, int overlay, MatrixStack matrices, VertexConsumer vertices) {
    }
}
