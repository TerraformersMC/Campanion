package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.item.BackpackItem;
import com.terraformersmc.campanion.network.S2CClearBackpackHeldItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ItemScatterer;
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
		if (!this.player.world.isClient) {
			ItemStack fromItem = this.getStack(slot);
			boolean equal = ItemStack.areItemsEqualIgnoreDamage(fromItem, stack) && ItemStack.areTagsEqual(fromItem, stack);
			if (fromItem.getItem() instanceof BackpackItem && fromItem.hasTag() && fromItem.getOrCreateTag().contains("Inventory", 10) && !equal) {
				ItemScatterer.spawn(this.player.world, this.player.getBlockPos().add(0, ((InvokerEntity) this.player).callGetEyeHeight(this.player.getPose(), this.player.getDimensions(this.player.getPose())), 0), BackpackItem.getItems(fromItem));
				((ServerPlayerEntity) this.player).networkHandler.sendPacket(S2CClearBackpackHeldItem.createPacket());
				fromItem.getTag().remove("Inventory");
			}
		}
	}

	@Shadow
	public ItemStack getStack(int slot) {
		return null;
	}

}

