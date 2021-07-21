package com.terraformersmc.campanion.item;

import net.minecraft.item.ItemStack;

public class TentBagItem extends PlaceableTentItem {
	public TentBagItem(Settings settings) {
		super(settings.maxCount(1));
	}

	@Override
	public void onPlaceTent(ItemStack stack) {
		stack.getOrCreateNbt().remove("Blocks");
	}

	public static boolean isEmpty(ItemStack stack) {
		return !(stack.getItem() == CampanionItems.TENT_BAG && stack.hasNbt() && stack.getOrCreateNbt().contains("Blocks", 9));
	}
}
