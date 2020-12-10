package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import com.terraformersmc.campanion.item.BackpackItem;
import com.terraformersmc.campanion.network.S2CClearBackpackHeldItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerInventory.class)
public class MixinPlayerInventory {

	@Shadow
	@Final
	public PlayerEntity player;

	@Inject(method = "setStack", at = @At("HEAD"))
	public void removeInvStack(int slot, ItemStack stack, CallbackInfo info) {
		ItemStack fromItem = this.getStack(slot);
		boolean equal = ItemStack.areItemsEqualIgnoreDamage(fromItem, stack) && ItemStack.areTagsEqual(fromItem, stack);
		if (slot == 38 && !this.player.world.isClient && !equal) {
			((BackpackStorePlayer) this.player).dropAllStacks();
		}
	}

	@Inject(method = "dropAll", at = @At("HEAD"))
	public void dropAll(CallbackInfo info) {
		((BackpackStorePlayer) this.player).dropAllStacks();
	}

	@Shadow
	public ItemStack getStack(int slot) {
		return null;
	}

}

