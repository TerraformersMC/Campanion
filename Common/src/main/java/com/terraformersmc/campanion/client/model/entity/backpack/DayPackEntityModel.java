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
public class DayPackEntityModel<T extends LivingEntity> extends TorsoParentedModel<T> {

	public DayPackEntityModel(ModelPart root) {
		super(root);
	}

	public DayPackEntityModel() {
		this(getTexturedModelData().bakeRoot());
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = TorsoParentedModel.getModelData();
		PartDefinition root = modelData.getRoot();

		CubeListBuilder base = CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.1F, -1.0F, 6, 10, 4, new CubeDeformation(0.0F));

		root.getChild("body").addOrReplaceChild("base", base, PartPose.offset(0.0F, 0.0F, 3.0F));

		return LayerDefinition.create(modelData, 24, 24);
	}
}
