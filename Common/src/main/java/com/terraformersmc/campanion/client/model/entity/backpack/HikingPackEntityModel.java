package com.terraformersmc.campanion.client.model.entity.backpack;

import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

/**
 * SleepingBagModel - Either Mojang or a mod author
 * Created using Tabula 7.0.100
 */
public class HikingPackEntityModel<T extends LivingEntity> extends TorsoParentedModel<T> {

	public HikingPackEntityModel(ModelPart root) {
		super(root);
	}

	public HikingPackEntityModel() {
		this(getTexturedModelData().bakeRoot());
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = TorsoParentedModel.getModelData();
		PartDefinition root = modelData.getRoot();

		CubeListBuilder base = CubeListBuilder.create().texOffs(0, 0).addBox(-4.5F, -1.1F, -1.0F, 9, 11, 5, new CubeDeformation(0.0F));
		CubeListBuilder bottomThing = CubeListBuilder.create().texOffs(0, 17).addBox(-4.5F, -1.0F, 0.0F, 9, 4, 4, new CubeDeformation(-0.4F));
		CubeListBuilder leftThing = CubeListBuilder.create().texOffs(14, 26).addBox(0.0F, 0.0F, 0.0F, 3, 8, 3, new CubeDeformation(-0.2F));
		CubeListBuilder rightThing = CubeListBuilder.create().texOffs(0, 26).addBox(-3.0F, 0.0F, 0.0F, 3, 8, 3, new CubeDeformation(-0.2F));

		PartDefinition baseData = root.getChild("body").addOrReplaceChild("base", base, PartPose.offset(0.0F, 0.0F, 3.0F));
		baseData.addOrReplaceChild("bottom_thing", bottomThing, PartPose.offset(0.0F, 10.0F, -0.4F));
		baseData.addOrReplaceChild("left_thing", leftThing, PartPose.offset(4.0F, 0.6F, 0.0F));
		baseData.addOrReplaceChild("right_thing", rightThing, PartPose.offset(-4.0F, 0.6F, 0.0F));

		return LayerDefinition.create(modelData, 32, 48);
	}
}
