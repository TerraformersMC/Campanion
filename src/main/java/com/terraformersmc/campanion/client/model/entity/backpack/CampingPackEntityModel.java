package com.terraformersmc.campanion.client.model.entity.backpack;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

/**
 * SleepingBagModel - Either Mojang or a mod author
 * Created using Tabula 7.0.100
 */
public class CampingPackEntityModel<T extends LivingEntity> extends TorsoParentedModel<T> {

	public CampingPackEntityModel(ModelPart root) {
		super(root);
	}

	public CampingPackEntityModel() {
		this(getTexturedModelData().createModel());
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = TorsoParentedModel.getModelData();
		ModelPartData root = modelData.getRoot();

		ModelPartBuilder base = ModelPartBuilder.create().uv(0, 0).cuboid(-3.5F, 0.1F, -1.0F, 8, 10, 4, new Dilation(0.0F));
		ModelPartBuilder bottomThing = ModelPartBuilder.create().uv(0, 16).cuboid(-3.0F, -0.3F, -1.5F, 6, 3, 3, new Dilation(0.0F));

		root.getChild("body").addChild("base", base, ModelTransform.pivot(0.0F, 0.0F, 3.0F))
				.addChild("bottom_thing", bottomThing, ModelTransform.pivot(0.5F, 9.9F, 0.8F));

		return TexturedModelData.of(modelData, 24, 24);
	}
}
