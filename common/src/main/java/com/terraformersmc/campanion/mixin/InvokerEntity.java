package com.terraformersmc.campanion.mixin;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public interface InvokerEntity {
	/*
	@Invoker
	float callGetEyeHeight(Pose entityPose, EntityDimensions entityDimensions);
	 */
}
