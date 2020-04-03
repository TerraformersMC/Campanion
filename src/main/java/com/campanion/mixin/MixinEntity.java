package com.campanion.mixin;

import com.campanion.entity.CollideAxisEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity implements CollideAxisEntity {
    private boolean collidedX;
    private boolean collidedZ;

    @Inject(method = "adjustMovementForCollisions", at = @At("RETURN"))
    public void adjustMovementForCollisions(Vec3d movement, CallbackInfoReturnable<Vec3d> info) {
        Vec3d vec3d = info.getReturnValue();
        this.collidedX = !MathHelper.approximatelyEquals(movement.x, vec3d.x);
        this.collidedZ = !MathHelper.approximatelyEquals(movement.z, vec3d.z);
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
