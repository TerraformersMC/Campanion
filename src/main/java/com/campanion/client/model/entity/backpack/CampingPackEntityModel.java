package com.campanion.client.model.entity.backpack;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;

/**
 * ModelPlayer - Either Mojang or a mod author
 * Created using Tabula 7.0.100
 */
public class CampingPackEntityModel<T extends LivingEntity> extends AnimalModel<T> {
	public ModelPart modelPart1;
	public ModelPart base;
	public ModelPart bottomThing;

	public CampingPackEntityModel() {
		this.textureWidth = 24;
		this.textureHeight = 24;
		this.bottomThing = new ModelPart(this, 0, 16);
		this.bottomThing.setPivot(0.0F, 9.9F, 0.8F);
		this.bottomThing.addCuboid(-3.0F, -0.3F, -1.5F, 6, 3, 3, 0.0F);
		this.modelPart1 = new ModelPart(this, 28, 16);
		this.modelPart1.setPivot(0.0F, 0.0F, 0.0F);
		this.modelPart1.addCuboid(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		this.base = new ModelPart(this, 0, 0);
		this.base.setPivot(0.0F, 0.0F, 3.0F);
		this.base.addCuboid(-3.5F, 0.1F, -1.0F, 8, 10, 4, 0.0F);
		this.base.addChild(this.bottomThing);
		this.modelPart1.addChild(this.base);
	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(modelPart1, base, bottomThing);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {

	}
}
