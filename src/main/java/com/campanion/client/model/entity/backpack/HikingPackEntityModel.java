package com.campanion.client.model.entity.backpack;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;

/**
 * ModelPlayer - Either Mojang or a mod author
 * Created using Tabula 7.0.100
 */
public class HikingPackEntityModel<T extends LivingEntity> extends BaseBackpackModel<T> {

	public HikingPackEntityModel() {
		super(32, 48);

		ModelPart leftThing = new ModelPart(this, 14, 26);
		leftThing.setPivot(4.0F, 0.6F, 0.0F);
		leftThing.addCuboid(0.0F, 0.0F, 0.0F, 3, 8, 3, -0.2F);

		ModelPart bottomThing = new ModelPart(this, 0, 17);
		bottomThing.setPivot(0.0F, 10.0F, -0.4F);
		bottomThing.addCuboid(-4.5F, -1.0F, 0.0F, 9, 4, 4, -0.4F);

		ModelPart base = new ModelPart(this, 0, 0);
		base.setPivot(0.0F, 0.0F, 3.0F);
		base.addCuboid(-4.5F, -1.1F, -1.0F, 9, 11, 5, 0.0F);

		ModelPart rightThing = new ModelPart(this, 0, 26);
		rightThing.setPivot(-4.0F, 0.6F, 0.0F);
		rightThing.addCuboid(-3.0F, 0.0F, 0.0F, 3, 8, 3, -0.2F);

		this.torso.addChild(base);

		base.addChild(leftThing);
		base.addChild(bottomThing);
		base.addChild(rightThing);
	}
}
