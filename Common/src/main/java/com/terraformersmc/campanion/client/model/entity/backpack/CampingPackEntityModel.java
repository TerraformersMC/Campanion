package com.terraformersmc.campanion.client.model.entity.backpack;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;

/**
 * SleepingBagModel - Either Mojang or a mod author
 * Created using Tabula 7.0.100
 */
public class CampingPackEntityModel<T extends LivingEntity> extends TorsoParentedModel<T> {

	public CampingPackEntityModel(ModelPart root) {
		super(root);
	}

	public CampingPackEntityModel() {
		this(getTexturedModelData().bakeRoot());
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = TorsoParentedModel.getModelData();
		PartDefinition root = modelData.getRoot();

		CubeListBuilder base = CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, 0.1F, -1.0F, 8, 10, 4, new CubeDeformation(0.0F));
		CubeListBuilder bottomThing = CubeListBuilder.create().texOffs(0, 16).addBox(-3.0F, -0.3F, -1.5F, 6, 3, 3, new CubeDeformation(0.0F));

		root.getChild("body").addOrReplaceChild("base", base, PartPose.offset(0.0F, 0.0F, 3.0F))
			.addOrReplaceChild("bottom_thing", bottomThing, PartPose.offset(0.5F, 9.9F, 0.8F));

		return LayerDefinition.create(modelData, 24, 24);
	}
}
