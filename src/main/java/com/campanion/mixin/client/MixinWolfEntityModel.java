package com.campanion.mixin.client;

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
		float progress = ((HowlingEntity) wolfEntity).getHowlAnimationProgress(h);
		this.head.pivotY = 13.5F - 2*progress; //13.5 -> 11.5
		this.head.pitch = this.head.pitch + (-0.8F - this.head.pitch) * progress;
		this.neck.pitch = this.torso.pitch + progress*-0.3F;
	}

	@Inject(method = "setAngles", at = @At("HEAD"), cancellable =  true)
	public void setAngles(T wolfEntity, float f, float g, float h, float i, float j, CallbackInfo callbackInfo) {
		if (((HowlingEntity) wolfEntity).getHowlAnimationProgress(1F) >= 0.001D) {
			callbackInfo.cancel();
		}
	}

}
