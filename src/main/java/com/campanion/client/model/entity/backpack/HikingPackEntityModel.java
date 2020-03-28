package com.campanion.client.model.entity.backpack;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;

/**
 * ModelPlayer - Either Mojang or a mod author
 * Created using Tabula 7.0.100
 */
public class HikingPackEntityModel<T extends LivingEntity> extends AnimalModel<T> {
	public ModelPart modelPart1;
	public ModelPart base;
	public ModelPart bottomThing;
	public ModelPart leftThing;
	public ModelPart rightThing;

	public HikingPackEntityModel() {
		this.textureWidth = 32;
		this.textureHeight = 48;
		this.leftThing = new ModelPart(this, 14, 26);
		this.leftThing.setPivot(4.0F, 1.6F, 0.0F);
		this.leftThing.addCuboid(0.0F, 0.0F, 0.0F, 3, 8, 3, -0.2F);
		this.bottomThing = new ModelPart(this, 0, 17);
		this.bottomThing.setPivot(0.0F, 10.0F, -0.4F);
		this.bottomThing.addCuboid(-4.5F, 0.0F, 0.0F, 9, 4, 4, -0.4F);
		this.base = new ModelPart(this, 0, 0);
		this.base.setPivot(0.0F, 0.0F, 3.0F);
		this.base.addCuboid(-4.5F, 0.1F, -1.0F, 9, 11, 5, 0.0F);
		this.modelPart1 = new ModelPart(this, 38, 16);
		this.modelPart1.setPivot(0.0F, 0.0F, 0.0F);
		this.modelPart1.addCuboid(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		this.rightThing = new ModelPart(this, 0, 26);
		this.rightThing.setPivot(-4.0F, 1.6F, 0.0F);
		this.rightThing.addCuboid(-3.0F, 0.0F, 0.0F, 3, 8, 3, -0.2F);
		this.base.addChild(this.leftThing);
		this.base.addChild(this.bottomThing);
		this.modelPart1.addChild(this.base);
		this.base.addChild(this.rightThing);
	}

	@Override
	protected Iterable<ModelPart> getHeadParts() {
		return ImmutableList.of();
	}

	@Override
	protected Iterable<ModelPart> getBodyParts() {
		return ImmutableList.of(modelPart1, base, bottomThing, leftThing, rightThing);
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {

	}
}
