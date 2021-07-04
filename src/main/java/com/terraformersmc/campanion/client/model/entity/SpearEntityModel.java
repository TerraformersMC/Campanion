package com.terraformersmc.campanion.client.model.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class SpearEntityModel extends Model {
    private final ModelPart base;

    public SpearEntityModel(ModelPart root) {
        super(RenderLayer::getEntitySolid);
        this.base = root.getChild("base");
    }

	public SpearEntityModel() {
		this(getTexturedModelData().createModel());
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData root = modelData.getRoot();

		ModelPartBuilder base = ModelPartBuilder.create().uv(0, 6).cuboid(-0.5F, 0.0F, -0.5F, 1.0F, 27.0F, 1.0F, new Dilation(0.0F));
		ModelPartBuilder head = ModelPartBuilder.create().uv(4, 0).cuboid(-1F, 1F, -1F, 2F, 1F, 2F);

		root.addChild("base", base, ModelTransform.NONE)
			.addChild("head", head, ModelTransform.NONE);

		return TexturedModelData.of(modelData, 32, 32);
	}

    @Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
