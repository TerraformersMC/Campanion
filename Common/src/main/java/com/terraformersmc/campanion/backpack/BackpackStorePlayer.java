package com.terraformersmc.campanion.backpack;

import com.terraformersmc.campanion.item.BackpackItem;
import com.terraformersmc.campanion.mixin.InvokerEntity;
import com.terraformersmc.campanion.network.S2CSyncBackpackContents;
import com.terraformersmc.campanion.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface BackpackStorePlayer {
	static int getStackCapacity(ItemStack stack) {
		return stack.getItem() instanceof BackpackItem ? ((BackpackItem) stack.getItem()).type.getSlots() : 0;
	}

	NonNullList<ItemStack> getBackpackStacks();

	void setBackpackStacks(NonNullList<ItemStack> stacks);

	default void syncChanges() {
		Player player = (Player) this;
		if (!player.level().isClientSide) {
			Services.NETWORK.sendToPlayer(new S2CSyncBackpackContents(this.getBackpackStacks()), (ServerPlayer) player);
		}
	}

	default void changeBackpackCapacity(int newCapacity) {
		Player player = (Player) this;
		BlockPos pos = player.blockPosition();
		float eyeHeight = ((InvokerEntity) player).callGetEyeHeight(player.getPose(), player.getDimensions(player.getPose()));
		NonNullList<ItemStack> stacks = this.getBackpackStacks();

		while (stacks.size() > newCapacity) {
			Containers.dropItemStack(player.level(), pos.getX(), pos.getY() + eyeHeight, pos.getZ(), stacks.remove(newCapacity));
		}

		syncChanges();
	}
}
