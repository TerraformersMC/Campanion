package com.campanion.client.model.entity.backpack;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.entity.LivingEntity;

/**
 * ModelPlayer - Either Mojang or a mod author
 * Created using Tabula 7.0.100
 */
public class CampingPackEntityModel<T extends LivingEntity> extends BaseBackpackModel<T> {

	public CampingPackEntityModel() {
		super(24, 24);

		ModelPart bottomThing = new ModelPart(this, 0, 16);
		bottomThing.setPivot(0.0F, 9.9F, 0.8F);
		bottomThing.addCuboid(-3.0F, -0.3F, -1.5F, 6, 3, 3, 0.0F);

		ModelPart base = new ModelPart(this, 0, 0);
		base.setPivot(0.0F, 0.0F, 3.0F);
		base.addCuboid(-3.5F, 0.1F, -1.0F, 8, 10, 4, 0.0F);

		base.addChild(bottomThing);
		this.torso.addChild(base);
	}

}
