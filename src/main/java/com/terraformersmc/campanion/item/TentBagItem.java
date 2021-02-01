package com.terraformersmc.campanion.item;

import net.minecraft.item.ItemStack;

public class TentBagItem extends PlaceableTentItem {
	public TentBagItem(Settings settings) {
		super(settings.maxCount(1));
	}

	@Override
	public void onPlaceTent(ItemStack stack) {
		stack.getOrCreateTag().remove("Blocks");
	}

	public static boolean isEmpty(ItemStack stack) {
		return !(stack.getItem() == CampanionItems.TENT_BAG && stack.hasTag() && stack.getOrCreateTag().contains("Blocks", 9));
	}
}
