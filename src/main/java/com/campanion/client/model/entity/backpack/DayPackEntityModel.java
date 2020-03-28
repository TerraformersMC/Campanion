package com.campanion.client.model.entity.backpack;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;

/**
 * ModelPlayer - Either Mojang or a mod author
 * Created using Tabula 7.0.100
 */
public class DayPackEntityModel<T extends LivingEntity> extends AnimalModel<T> {
	public ModelPart modelPart1;
	public ModelPart base;

	public DayPackEntityModel() {
		this.textureWidth = 24;
		this.textureHeight = 24;
		this.modelPart1 = new ModelPart(this, 27, 16);
		this.modelPart1.setPivot(0.0F, 0.0F, 0.0F);
		this.modelPart1.addCuboid(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		this.base = new ModelPart(this, 0, 0);
		this.base.setPivot(0.0F, 0.0F, 3.0F);
		this.base.addCuboid(-3.0F, 0.1F, -1.0F, 6, 10, 4, 0.0F);
		this.modelPart1.addChild(this.base);
	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(modelPart1, base);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {

	}
}
