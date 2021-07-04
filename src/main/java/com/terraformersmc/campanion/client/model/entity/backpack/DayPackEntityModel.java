package com.terraformersmc.campanion.client.model.entity.backpack;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

/**
 * SleepingBagModel - Either Mojang or a mod author
 * Created using Tabula 7.0.100
 */
public class DayPackEntityModel<T extends LivingEntity> extends TorsoParentedModel<T> {

	public DayPackEntityModel(ModelPart root) {
		super(root);
	}

	public DayPackEntityModel() {
		this(getTexturedModelData().createModel());
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = TorsoParentedModel.getModelData();
		ModelPartData root = modelData.getRoot();

		ModelPartBuilder base = ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, 0.1F, -1.0F, 6, 10, 4, new Dilation(0.0F));

		root.getChild("body").addChild("base", base, ModelTransform.pivot(0.0F, 0.0F, 3.0F));

		return TexturedModelData.of(modelData, 24, 24);
	}
}
