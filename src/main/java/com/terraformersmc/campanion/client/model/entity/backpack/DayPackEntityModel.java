package com.terraformersmc.campanion.client.model.entity.backpack;

import net.minecraft.client.model.ModelPart;
import net.minecraft.entity.LivingEntity;

/**
 * SleepingBagModel - Either Mojang or a mod author
 * Created using Tabula 7.0.100
 */
public class DayPackEntityModel<T extends LivingEntity> extends TorsoParentedModel<T> {

	public DayPackEntityModel() {
		super(24, 24);
		ModelPart base = new ModelPart(this, 0, 0);
		base.setPivot(0.0F, 0.0F, 3.0F);
		base.addCuboid(-3.0F, 0.1F, -1.0F, 6, 10, 4, 0.0F);
		this.torso.addChild(base);
	}
}
