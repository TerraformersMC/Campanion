package com.terraformersmc.campanion.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class SpearEntityModel extends Model {
    private final ModelPart base;

    public SpearEntityModel(ModelPart root) {
        super(RenderType::entitySolid);
        this.base = root.getChild("base");
    }

	public SpearEntityModel() {
		this(getTexturedModelData().bakeRoot());
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = new MeshDefinition();
		PartDefinition root = modelData.getRoot();

		CubeListBuilder base = CubeListBuilder.create().texOffs(0, 6).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 27.0F, 1.0F, new CubeDeformation(0.0F));
		CubeListBuilder head = CubeListBuilder.create().texOffs(4, 0).addBox(-1F, 1F, -1F, 2F, 1F, 2F);

		root.addOrReplaceChild("base", base, PartPose.ZERO)
			.addOrReplaceChild("head", head, PartPose.ZERO);

		return LayerDefinition.create(modelData, 32, 32);
	}

    @Override
	public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.base.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
