package com.terraformersmc.campanion.stat;

import com.terraformersmc.campanion.Campanion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.StatFormatter;

import java.util.*;

public class CampanionStats {
	private static final Map<ResourceLocation, ResourceLocation> STATS = new LinkedHashMap<>();
	private static final Map<ResourceLocation, StatFormatter> FORMATTERS = new LinkedHashMap<>();
	public static final ResourceLocation STONE_SKIPS = register("stone_skips", StatFormatter.DEFAULT);
	public static final ResourceLocation SLEEP_IN_SLEEPING_BAG = register("sleep_in_sleeping_bag", StatFormatter.DEFAULT);

	private static ResourceLocation register(String id, StatFormatter formatter) {
		ResourceLocation identifier = ResourceLocation.fromNamespaceAndPath(Campanion.MOD_ID, id);
		STATS.put(identifier, identifier);
		return identifier;
	}

	public static void loadClass() {

	}

	public static Map<ResourceLocation, ResourceLocation> getStats() {
		return STATS;
	}

	public static Map<ResourceLocation, StatFormatter> getFORMATTERS() {
		return FORMATTERS;
	}
}
