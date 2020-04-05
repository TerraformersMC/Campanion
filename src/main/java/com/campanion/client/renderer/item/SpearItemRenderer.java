package com.campanion.client.renderer.item;

import com.campanion.client.model.entity.SpearEntityModel;
import com.campanion.client.renderer.entity.SpearEntityRenderer;
import com.campanion.item.SpearItem;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public enum SpearItemRenderer {
    INSTANCE;

    private final SpearEntityModel spearEntityModel = new SpearEntityModel();

    public boolean render(LivingEntity entity, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
        if(renderMode == ModelTransformation.Mode.GUI || renderMode == ModelTransformation.Mode.GROUND || renderMode == ModelTransformation.Mode.FIXED) {
            return false;
        }

        matrices.push();

        model.getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);

        if(entity != null && entity.isUsingItem() && entity.getActiveItem() == stack && (renderMode == ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND || renderMode == ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND)) {
            matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(180));
            matrices.translate(0, 1.6, 0);
        } else {
            matrices.translate(0, 0.35, 0);
        }

        matrices.scale(1.0F, -1.0F, -1.0F);
        VertexConsumer spear = ItemRenderer.getArmorVertexConsumer(
            vertexConsumers,
            this.spearEntityModel.getLayer(SpearEntityRenderer.getTexture(((SpearItem)stack.getItem()).getType())),
            false,
            stack.hasEnchantmentGlint()
        );
        this.spearEntityModel.render(matrices, spear, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrices.pop();
        return true;
    }
}
