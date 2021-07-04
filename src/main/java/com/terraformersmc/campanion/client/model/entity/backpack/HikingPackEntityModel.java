package com.terraformersmc.campanion.client.model.entity.backpack;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

/**
 * SleepingBagModel - Either Mojang or a mod author
 * Created using Tabula 7.0.100
 */
public class HikingPackEntityModel<T extends LivingEntity> extends TorsoParentedModel<T> {

	public HikingPackEntityModel(ModelPart root) {
		super(root);
	}

	public HikingPackEntityModel() {
		this(getTexturedModelData().createModel());
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = TorsoParentedModel.getModelData();
		ModelPartData root = modelData.getRoot();

		ModelPartBuilder base = ModelPartBuilder.create().uv(0, 0).cuboid(-4.5F, -1.1F, -1.0F, 9, 11, 5, new Dilation(0.0F));
		ModelPartBuilder bottomThing = ModelPartBuilder.create().uv(0, 17).cuboid(-4.5F, -1.0F, 0.0F, 9, 4, 4, new Dilation(-0.4F));
		ModelPartBuilder leftThing = ModelPartBuilder.create().uv(14, 26).cuboid(0.0F, 0.0F, 0.0F, 3, 8, 3, new Dilation(-0.2F));
		ModelPartBuilder rightThing = ModelPartBuilder.create().uv(0, 26).cuboid(-3.0F, 0.0F, 0.0F, 3, 8, 3, new Dilation(-0.2F));

		ModelPartData baseData = root.getChild("body").addChild("base", base, ModelTransform.pivot(0.0F, 0.0F, 3.0F));
		baseData.addChild("bottom_thing", bottomThing, ModelTransform.pivot(0.0F, 10.0F, -0.4F));
		baseData.addChild("left_thing", leftThing, ModelTransform.pivot(4.0F, 0.6F, 0.0F));
		baseData.addChild("right_thing", rightThing, ModelTransform.pivot(-4.0F, 0.6F, 0.0F));

		return TexturedModelData.of(modelData, 32, 48);
	}
}
