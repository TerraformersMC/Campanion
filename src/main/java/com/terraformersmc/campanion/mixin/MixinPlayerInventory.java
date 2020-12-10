package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import com.terraformersmc.campanion.item.BackpackItem;
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
public class MixinPlayerInventory {

	@Shadow
	@Final
	public PlayerEntity player;

	@Inject(method = "setStack", at = @At("HEAD"))
	public void removeInvStack(int slot, ItemStack stack, CallbackInfo info) {
		int capacity = this.getStackCapacity(stack);
		if (slot == 38 && !this.player.world.isClient && this.getStackCapacity(this.getStack(slot)) > this.getStackCapacity(stack)) {
			((BackpackStorePlayer) this.player).changeBackpackCapacity(capacity);
		}
	}

	private int getStackCapacity(ItemStack stack) {
		return stack.getItem() instanceof BackpackItem ? ((BackpackItem) stack.getItem()).type.getSlots() : 0;
	}

	@Inject(method = "dropAll", at = @At("HEAD"))
	public void dropAll(CallbackInfo info) {
		((BackpackStorePlayer) this.player).changeBackpackCapacity(0);
	}

	@Shadow
	public ItemStack getStack(int slot) {
		return null;
	}

}

