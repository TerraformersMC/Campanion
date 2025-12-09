package com.terraformersmc.campanion.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class TentBagItem extends PlaceableTentItem {
	public TentBagItem(Properties settings) {
		super(settings.stacksTo(1));
	}

	@Override
	public void onPlaceTent(ItemStack stack) {
		CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> {
			tag.remove("Blocks");
		});
	}

	public static boolean isEmpty(ItemStack stack) {
		return !(stack.getItem() == CampanionItems.TENT_BAG && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).contains("Blocks"));
	}
}
