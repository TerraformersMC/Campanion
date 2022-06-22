package com.terraformersmc.campanion.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.terraformersmc.campanion.client.model.entity.SpearEntityModel;
import com.terraformersmc.campanion.client.renderer.entity.SpearEntityRenderer;
import com.terraformersmc.campanion.item.SpearItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public enum SpearItemRenderer {
    INSTANCE;

    private final SpearEntityModel spearEntityModel = new SpearEntityModel();

    public boolean render(LivingEntity entity, ItemStack stack, ItemTransforms.TransformType renderMode, boolean leftHanded, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, BakedModel model) {
        if(renderMode == ItemTransforms.TransformType.GUI || renderMode == ItemTransforms.TransformType.GROUND || renderMode == ItemTransforms.TransformType.FIXED) {
            return false;
        }

        matrices.pushPose();

        model.getTransforms().getTransform(renderMode).apply(leftHanded, matrices);

        if(entity != null && entity.isUsingItem() && entity.getUseItem() == stack && (renderMode == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND || renderMode == ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)) {
            matrices.mulPose(Vector3f.XP.rotationDegrees(180));
            matrices.translate(0, 2.1, 0);
        } else {
            matrices.translate(0, 0.85, 0);
        }

        matrices.scale(2.0F, -2.0F, -2.0F);
        VertexConsumer spear = ItemRenderer.getArmorFoilBuffer(
            vertexConsumers,
            this.spearEntityModel.renderType(SpearEntityRenderer.getTexture(((SpearItem)stack.getItem()).getType())),
            false,
            stack.hasFoil()
        );
        this.spearEntityModel.renderToBuffer(matrices, spear, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);

        matrices.popPose();
        return true;
    }
}
