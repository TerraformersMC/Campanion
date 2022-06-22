package com.terraformersmc.campanion.client.model.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;
import java.util.Collections;

/**
 * SleepingBagModel - Either Mojang or a mod author
 * Created using Tabula 7.0.100
 */
public class SleepingBagModel<T extends LivingEntity> extends HumanoidModel<T> {

    public SleepingBagModel(ModelPart root) {
        super(root);
    }

	public SleepingBagModel() {
		this(getTexturedModelData().bakeRoot());
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = HumanoidModel.createMesh(new CubeDeformation(1/16F), 0);
		PartDefinition root = modelData.getRoot();

		CubeListBuilder head = CubeListBuilder.create().texOffs(16, 16);
		CubeListBuilder base = CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, 0, -2.8F, 14, 25, 6, new CubeDeformation(0.0F));
		CubeListBuilder headPiece = CubeListBuilder.create().texOffs(37, 28).addBox(-5.0F, -9.5F, 0.2F, 10, 10, 3, new CubeDeformation(0.0F));
		CubeListBuilder bump = CubeListBuilder.create().texOffs(0, 31).addBox(-5.0F, 0.0F, -4.3F, 10, 23, 2, new CubeDeformation(0.0F));

		PartDefinition headData = root.addOrReplaceChild("head", head, PartPose.ZERO);
		headData.addOrReplaceChild("base", base, PartPose.ZERO);
		headData.addOrReplaceChild("head_piece", headPiece, PartPose.ZERO);
		headData.addOrReplaceChild("bump", bump, PartPose.ZERO);

		return LayerDefinition.create(modelData, 64, 64);
	}

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return Collections.emptyList();
    }
}

