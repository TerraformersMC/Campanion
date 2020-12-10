package com.terraformersmc.campanion.backpack;

import com.terraformersmc.campanion.mixin.InvokerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;

public interface BackpackStorePlayer {
	DefaultedList<ItemStack> getBackpackStacks();

	default void dropAllStacks() {
		PlayerEntity player = (PlayerEntity) this;
		DefaultedList<ItemStack> stacks = this.getBackpackStacks();
		ItemScatterer.spawn(player.world, player.getBlockPos().add(0, ((InvokerEntity) player).callGetEyeHeight(player.getPose(), player.getDimensions(player.getPose())), 0), stacks);
		stacks.clear();
	}
}
