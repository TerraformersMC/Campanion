package com.terraformersmc.campanion.mixin.client;

import com.terraformersmc.campanion.item.PlaceableTentItem;
import com.terraformersmc.campanion.network.C2SRotateHeldItem;
import com.terraformersmc.campanion.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraftClient {

	@Inject(method = "handleKeybinds", at = @At("HEAD"), cancellable = true)
	public void handleKeybinds(CallbackInfo info) {
		Minecraft self = Minecraft.getInstance();
		if (self.player != null && self.options.keyAttack.isDown()) {
			ItemStack stack = self.player.getMainHandItem();
			if (stack.getItem() instanceof PlaceableTentItem && ((PlaceableTentItem) stack.getItem()).hasBlocks(stack)) {
				if (self.options.keyAttack.consumeClick()) {
					Services.NETWORK.sendToServer(new C2SRotateHeldItem());
				}
				info.cancel();
			}
		}
	}

}
