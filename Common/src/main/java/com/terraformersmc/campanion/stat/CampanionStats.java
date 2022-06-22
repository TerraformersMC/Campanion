package com.terraformersmc.campanion.stat;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;
import net.minecraft.stats.Stats;

public class CampanionStats {
	public static final ResourceLocation STONE_SKIPS = register("stone_skips", StatFormatter.DEFAULT);
	public static final ResourceLocation SLEEP_IN_SLEEPING_BAG = register("sleep_in_sleeping_bag", StatFormatter.DEFAULT);

	private static ResourceLocation register(String id, StatFormatter formatter) {
		ResourceLocation identifier = new ResourceLocation(Campanion.MOD_ID, id);
		Registry.register(Registry.CUSTOM_STAT, id, identifier);
		Stats.CUSTOM.get(identifier, formatter);
		return identifier;
	}

	public static void loadClass() {

	}
}
