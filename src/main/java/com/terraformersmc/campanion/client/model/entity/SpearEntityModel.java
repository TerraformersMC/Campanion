package com.terraformersmc.campanion.client.model.entity;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class SpearEntityModel extends Model {
    private final ModelPart base = new ModelPart(32, 32, 0, 6);

    public SpearEntityModel() {
        super(RenderLayer::getEntitySolid);
        this.base.addCuboid(-0.5F, 0.0F, -0.5F, 1.0F, 27.0F, 1.0F, 0.0F);

        ModelPart head = new ModelPart(32, 32, 4, 0);
        head.addCuboid(-1F, 1F, -1F, 2F, 1F, 2F);
        this.base.addChild(head);
    }

    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
