package com.terraformersmc.campanion.mixin.client;

import com.terraformersmc.campanion.entity.HowlingEntity;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Wolf.class)
public class MixinWolfEntityClient {

	@Inject(method = "handleEntityEvent", at = @At("HEAD"), cancellable = true)
	public void handleEntityEvent(byte status, CallbackInfo callbackInfo) {
		HowlingEntity thiz = (HowlingEntity) this;
		if (status == 64) {
			thiz.setHowling(true);
			callbackInfo.cancel();
		} else {
			thiz.setHowling(false);
		}
	}

}
