package com.terraformersmc.campanion.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Hand;

import com.terraformersmc.campanion.tag.CampanionItemTags;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
	@ModifyConstant(method = "updateTargetedEntity(F)V", constant = @Constant(doubleValue = 9.0D))
	private double modifySquaredEntityReachDistance(double currentValue) {
		ClientPlayerEntity player = MinecraftClient.getInstance().player;
		if (player != null && player.getStackInHand(Hand.MAIN_HAND).getItem().isIn(CampanionItemTags.SPEARS)) {
			return 5.0D * 5.0D;
		}
		return currentValue;
	}
}
