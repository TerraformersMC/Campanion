package com.terraformersmc.campanion.client.model.entity.backpack;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;

public class TorsoParentedModel<T extends LivingEntity> extends HumanoidModel<T> {
    protected TorsoParentedModel(ModelPart root) {
        super(root);
    }

    protected static MeshDefinition getModelData() {
    	MeshDefinition modelData = HumanoidModel.createMesh(new CubeDeformation(1/16F), 0);
		PartDefinition root = modelData.getRoot();
    	CubeListBuilder body = CubeListBuilder.create().texOffs(16, 16);

		root.addOrReplaceChild("body", body, PartPose.ZERO);

		return modelData;
	}

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.body);
    }
}
