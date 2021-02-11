package com.terraformersmc.campanion.backpack;

import com.terraformersmc.campanion.mixin.InvokerEntity;
import com.terraformersmc.campanion.network.C2SOpenBackpack;
import com.terraformersmc.campanion.network.S2CSyncBackpackContents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

public interface BackpackStorePlayer {
	DefaultedList<ItemStack> getBackpackStacks();

	void setBackpackStacks(DefaultedList<ItemStack> stacks);

	default void syncChanges() {
		PlayerEntity player = (PlayerEntity) this;
		if(!player.world.isClient) {
			((ServerPlayerEntity) player).networkHandler.sendPacket(S2CSyncBackpackContents.createPacket(this.getBackpackStacks()));
		}
	}

	default void changeBackpackCapacity(int newCapacity) {
		PlayerEntity player = (PlayerEntity) this;
		BlockPos pos = player.getBlockPos();
		float eyeHeight = ((InvokerEntity) player).callGetEyeHeight(player.getPose(), player.getDimensions(player.getPose()));
		DefaultedList<ItemStack> stacks = this.getBackpackStacks();

		while (stacks.size() > newCapacity) {
			ItemScatterer.spawn(player.world, pos.getX(), pos.getY()+eyeHeight, pos.getZ(), stacks.remove(newCapacity));
		}

		syncChanges();
	}
}
