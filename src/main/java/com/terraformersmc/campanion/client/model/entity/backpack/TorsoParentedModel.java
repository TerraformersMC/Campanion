package com.terraformersmc.campanion.client.model.entity.backpack;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

public class TorsoParentedModel<T extends LivingEntity> extends BipedEntityModel<T> {
    protected TorsoParentedModel(ModelPart root) {
        super(root);
    }

    protected static ModelData getModelData() {
    	ModelData modelData = BipedEntityModel.getModelData(new Dilation(1/16F), 0);
		ModelPartData root = modelData.getRoot();
    	ModelPartBuilder body = ModelPartBuilder.create().uv(16, 16);

		root.addChild("body", body, ModelTransform.NONE);

		return modelData;
	}

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body);
    }
}
