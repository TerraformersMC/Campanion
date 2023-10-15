package com.terraformersmc.campanion.backpack;

import com.terraformersmc.campanion.item.BackpackItem;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BackpackContainerFactory implements MenuProvider {

	private final BackpackItem.Type type;

	public BackpackContainerFactory(BackpackItem.Type type) {
		this.type = type;
	}

	@Override
	public @NotNull Component getDisplayName() {
		return Component.translatable("container.campanion." + this.type.name().toLowerCase());
	}

	@Override
	public @Nullable AbstractContainerMenu createMenu(int syncId, @NotNull Inventory inv, @NotNull Player player) {
		BackpackStorePlayer storePlayer = (BackpackStorePlayer) player;
		NonNullList<ItemStack> stacks = storePlayer.getBackpackStacks();
		SimpleContainer inventory = new SimpleContainer(this.type.getSlots());
		for (int i = 0; i < Math.min(inventory.getContainerSize(), stacks.size()); i++) {
			inventory.setItem(i, stacks.get(i));
		}
		inventory.addListener(sender -> {
			stacks.clear();
			for (int i = 0; i < sender.getContainerSize(); i++) {
				stacks.add(sender.getItem(i));
			}
			storePlayer.syncChanges();
		});

		return new ChestMenu(this.type.getContainerType(), syncId, inv, inventory, this.type.getRows());

	}
}
