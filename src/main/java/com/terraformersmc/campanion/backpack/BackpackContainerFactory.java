package com.terraformersmc.campanion.backpack;

import com.terraformersmc.campanion.item.BackpackItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

public class BackpackContainerFactory implements NamedScreenHandlerFactory {

	private final BackpackItem.Type type;

	public BackpackContainerFactory(BackpackItem.Type type) {
		this.type = type;
	}

	@Override
	public Text getDisplayName() {
		return new TranslatableText("container.campanion." + this.type.name().toLowerCase());
	}

	@Override
	public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		DefaultedList<ItemStack> stacks = ((BackpackStorePlayer) player).getBackpackStacks();
		SimpleInventory inventory = new SimpleInventory(this.type.getSlots());
		for (int i = 0; i < stacks.size(); i++) {
			inventory.setStack(i, stacks.get(i));
		}
		inventory.addListener(sender -> {
			stacks.clear();
			for (int i = 0; i < sender.size(); i++) {
				stacks.set(i, sender.getStack(i));
			}
		});

		return new GenericContainerScreenHandler(this.type.getContainerType(), syncId, inv, inventory, this.type.getRows());

	}
}
