package com.campanion.mixin;

import com.campanion.entity.HowlingEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.TintableAnimalModel;
import net.minecraft.client.render.entity.model.WolfEntityModel;
import net.minecraft.entity.passive.WolfEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfEntityModel.class)
public abstract class MixinWolfEntityModel<T extends WolfEntity> extends TintableAnimalModel<T> {

	@Shadow @Final private ModelPart head;

	@Shadow @Final private ModelPart neck;

	@Shadow @Final private ModelPart torso;

	@Inject(method = "animateModel", at = @At("TAIL"), cancellable = true)
	public void animateModel(T wolfEntity, float f, float g, float h, CallbackInfo callbackInfo) {
		if (((HowlingEntity)wolfEntity).isHowling()) {
			System.out.println("Howling Anim " + ((HowlingEntity) wolfEntity).isHowling());
			this.neck.pitch = this.torso.pitch - 0.3F;
			this.head.pitch = -0.8F;
			this.head.setPivot(-1.0F, 11.5F, -7.0F);
		} else {
			this.neck.pitch = this.torso.pitch;
			this.head.setPivot(-1.0F, 13.5F, -7.0F);
		}
	}

	@Inject(method = "setAngles", at = @At("HEAD"), cancellable =  true)
	public void setAngles(T wolfEntity, float f, float g, float h, float i, float j, CallbackInfo callbackInfo) {
		if (((HowlingEntity)wolfEntity).isHowling()) {
			callbackInfo.cancel();
		}
	}

}
