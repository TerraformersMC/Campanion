package com.campanion.client.items;

import com.campanion.client.model.entity.SpearEntityModel;
import com.campanion.client.renderer.entity.SpearEntityRenderer;
import com.campanion.item.SpearItem;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public enum SpearItemRenderer {
    INSTANCE;

    private final SpearEntityModel spearEntityModel = new SpearEntityModel();

    public boolean render(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
        boolean renderNormal = renderMode == ModelTransformation.Mode.GUI || renderMode == ModelTransformation.Mode.GROUND || renderMode == ModelTransformation.Mode.FIXED;

        matrices.push();

        model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
        matrices.translate(-0.5D, -0.5D, -0.5D);

        if (!renderNormal) {
            matrices.scale(1.0F, -1.0F, -1.0F);
            VertexConsumer spear = ItemRenderer.getArmorVertexConsumer(
                vertexConsumers,
                this.spearEntityModel.getLayer(SpearEntityRenderer.getTexture(((SpearItem)stack.getItem()).getType())),
                false,
                stack.hasEnchantmentGlint()
            );
            this.spearEntityModel.render(matrices, spear, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
        }

        matrices.pop();
        return !renderNormal;
    }
}