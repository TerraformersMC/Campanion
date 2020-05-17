package com.terraformersmc.campanion.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.Hand;

import com.terraformersmc.campanion.tag.CampanionItemTags;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager {

	@Inject(method = "getReachDistance()F", at = @At("HEAD"), cancellable = true)
	private void onGetReachDistance(CallbackInfoReturnable<Float> info) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null && player.getStackInHand(Hand.MAIN_HAND).getItem().isIn(CampanionItemTags.SPEARS)) {
			info.setReturnValue(6.0F);
		}
	}

}
