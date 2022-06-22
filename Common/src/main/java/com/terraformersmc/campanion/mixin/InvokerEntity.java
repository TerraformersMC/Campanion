package com.terraformersmc.campanion.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface InvokerEntity {
	@Invoker
	float callGetEyeHeight(Pose entityPose, EntityDimensions entityDimensions);
}
