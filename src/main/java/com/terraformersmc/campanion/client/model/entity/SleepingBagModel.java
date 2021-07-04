package com.terraformersmc.campanion.client.model.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

import java.util.Collections;

/**
 * SleepingBagModel - Either Mojang or a mod author
 * Created using Tabula 7.0.100
 */
public class SleepingBagModel<T extends LivingEntity> extends BipedEntityModel<T> {

    public SleepingBagModel(ModelPart root) {
        super(root);
    }

	public SleepingBagModel() {
		this(getTexturedModelData().createModel());
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = BipedEntityModel.getModelData(new Dilation(1/16F), 0);
		ModelPartData root = modelData.getRoot();

		ModelPartBuilder head = ModelPartBuilder.create().uv(16, 16);
		ModelPartBuilder base = ModelPartBuilder.create().uv(0, 0).cuboid(-7.0F, 0, -2.8F, 14, 25, 6, new Dilation(0.0F));
		ModelPartBuilder headPiece = ModelPartBuilder.create().uv(37, 28).cuboid(-5.0F, -9.5F, 0.2F, 10, 10, 3, new Dilation(0.0F));
		ModelPartBuilder bump = ModelPartBuilder.create().uv(0, 31).cuboid(-5.0F, 0.0F, -4.3F, 10, 23, 2, new Dilation(0.0F));

		ModelPartData headData = root.addChild("head", head, ModelTransform.NONE);
		headData.addChild("base", base, ModelTransform.NONE);
		headData.addChild("head_piece", headPiece, ModelTransform.NONE);
		headData.addChild("bump", bump, ModelTransform.NONE);

		return TexturedModelData.of(modelData, 64, 64);
	}

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return Collections.emptyList();
    }
}

