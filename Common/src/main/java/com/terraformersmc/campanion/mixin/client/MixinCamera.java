package com.terraformersmc.campanion.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class MixinCamera {
    @Inject(method = "setup", at = @At("TAIL"))
    public void setup(BlockGetter area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo info) {
        if(focusedEntity instanceof LivingEntity && ((LivingEntity) focusedEntity).isSleeping()) {
            this.setRotation(focusedEntity.getViewYRot(tickDelta), 0.0F);
        }
    }

    @Shadow
    protected void setRotation(float yaw, float pitch) {
    }
}

