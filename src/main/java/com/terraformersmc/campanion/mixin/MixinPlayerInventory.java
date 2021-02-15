package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public abstract class MixinPlayerInventory {

	@Shadow
	@Final
	public PlayerEntity player;

	@Shadow
	public abstract ItemStack getStack(int slot);

	@Inject(method = "setStack", at = @At("HEAD"))
	public void onSetStack(int slot, ItemStack stack, CallbackInfo info) {
		int capacity = BackpackStorePlayer.getStackCapacity(stack);
		if (slot == 38 && !this.player.world.isClient) {
			((BackpackStorePlayer) this.player).changeBackpackCapacity(capacity);
		}
	}

	@Inject(method = "dropAll", at = @At("HEAD"))
	public void onDropAll(CallbackInfo info) {
		((BackpackStorePlayer) this.player).changeBackpackCapacity(0);
	}

}

