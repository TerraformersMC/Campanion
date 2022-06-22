package com.terraformersmc.campanion.mixin.client;

import com.terraformersmc.campanion.item.PlaceableTentItem;
import com.terraformersmc.campanion.network.C2SRotateHeldItem;
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

	@Shadow
	public LocalPlayer player;

	@Shadow
	@Final
	public Options options;

	@Inject(method = "handleInputEvents", at = @At("HEAD"), cancellable = true)
	public void handleInputEvents(CallbackInfo info) {
		if (this.player != null && this.options.keyAttack.isDown()) {
			ItemStack stack = this.player.getMainHandItem();
			if (stack.getItem() instanceof PlaceableTentItem && ((PlaceableTentItem) stack.getItem()).hasBlocks(stack)) {
				if (this.options.keyAttack.consumeClick()) {
					this.player.connection.send(C2SRotateHeldItem.createPacket());
				}
				info.cancel();
			}
		}
	}

}
