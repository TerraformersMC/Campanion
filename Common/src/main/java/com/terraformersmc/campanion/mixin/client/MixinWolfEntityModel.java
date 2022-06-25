package com.terraformersmc.campanion.mixin.client;

import com.terraformersmc.campanion.entity.HowlingEntity;
import net.minecraft.client.model.ColorableAgeableListModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfModel.class)
public abstract class MixinWolfEntityModel<T extends Wolf> extends ColorableAgeableListModel<T> {

	@Shadow @Final private ModelPart head;

	@Shadow @Final private ModelPart upperBody;

	@Shadow @Final private ModelPart body;

	@Inject(method = "prepareMobModel", at = @At("TAIL"), cancellable = true)
	public void prepareMobModel(T wolfEntity, float f, float g, float h, CallbackInfo callbackInfo) {
		float progress = ((HowlingEntity) wolfEntity).getHowlAnimationProgress(h);
		this.head.y = 13.5F - 2*progress; //13.5 -> 11.5
		this.head.xRot = this.head.xRot + (-0.8F - this.head.xRot) * progress;
		this.upperBody.xRot = this.body.xRot + progress*-0.3F;
	}

	@Inject(method = "setupAnim", at = @At("HEAD"), cancellable =  true)
	public void setupAnim(T wolfEntity, float f, float g, float h, float i, float j, CallbackInfo callbackInfo) {
		if (((HowlingEntity) wolfEntity).getHowlAnimationProgress(1F) >= 0.001D) {
			callbackInfo.cancel();
		}
	}

}
