package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import net.minecraft.advancements.critereon.PlayerTrigger;
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

	@Inject(method = "setItem", at = @At("HEAD"))
	public void setItem(int slot, ItemStack stack, CallbackInfo info) {
		Player player = ((Inventory) (Object) this).player;
		int capacity = BackpackStorePlayer.getStackCapacity(stack);
		if (slot == 38 && !player.level.isClientSide) {
			((BackpackStorePlayer) player).changeBackpackCapacity(capacity);
		}
	}

	@Inject(method = "dropAll", at = @At("HEAD"))
	public void dropAll(CallbackInfo info) {
		Player player = ((Inventory) (Object) this).player;
		((BackpackStorePlayer) player).changeBackpackCapacity(0);
	}

}

