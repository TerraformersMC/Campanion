package com.terraformersmc.campanion.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface InvokerEntity {
	@Invoker
	float callGetEyeHeight(EntityPose entityPose, EntityDimensions entityDimensions);
}
