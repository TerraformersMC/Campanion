package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Inventory.class)
public abstract class MixinPlayerInventory {

	@Shadow
	@Final
	public Player player;

	@Shadow
	public abstract ItemStack getItem(int slot);

	@Inject(method = "setItem", at = @At("HEAD"))
	public void setItem(int slot, ItemStack stack, CallbackInfo info) {
		int capacity = BackpackStorePlayer.getStackCapacity(stack);
		if (slot == 38 && !this.player.level.isClientSide) {
			((BackpackStorePlayer) this.player).changeBackpackCapacity(capacity);
		}
	}

	@Inject(method = "dropAll", at = @At("HEAD"))
	public void dropAll(CallbackInfo info) {
		((BackpackStorePlayer) this.player).changeBackpackCapacity(0);
	}

}

