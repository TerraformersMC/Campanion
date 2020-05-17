package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.item.CampanionItems;
import net.minecraft.item.Item;
import net.minecraft.screen.SmithingScreenHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

@Mixin(SmithingScreenHandler.class)
public class MixinSmithingScreenHandler {
	@Shadow
	@Final
	@Mutable
	private static Map<Item, Item> RECIPES;

	static {
		RECIPES = new HashMap<>(RECIPES);
		RECIPES.put(CampanionItems.DIAMOND_SPEAR, CampanionItems.NETHERITE_SPEAR);
	}
}
