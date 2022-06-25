package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.entity.CollideAxisEntity;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity implements CollideAxisEntity {
	private boolean collidedX;
	private boolean collidedZ;

	@Inject(method = "collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", at = @At("RETURN"))
	public void adjustMovementForCollisions(Vec3 movement, CallbackInfoReturnable<Vec3> info) {
		Vec3 vec3d = info.getReturnValue();
		this.collidedX = !Mth.equal(movement.x, vec3d.x);
		this.collidedZ = !Mth.equal(movement.z, vec3d.z);
	}

	@Override
	public boolean isCollidesX() {
		return this.collidedX;
	}

	@Override
	public boolean isCollidesZ() {
		return this.collidedZ;
	}
}
