package com.terraformersmc.campanion.stat;

import com.terraformersmc.campanion.Campanion;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CampanionStats {
	public static final Identifier STONE_SKIPS = register("stone_skips", StatFormatter.DEFAULT);
	public static final Identifier SLEEP_IN_SLEEPING_BAG = register("sleep_in_sleeping_bag", StatFormatter.DEFAULT);

	private static Identifier register(String id, StatFormatter formatter) {
		Identifier identifier = new Identifier(Campanion.MOD_ID, id);
		Registry.register(Registry.CUSTOM_STAT, id, identifier);
		Stats.CUSTOM.getOrCreateStat(identifier, formatter);
		return identifier;
	}

	public static void loadClass() {

	}
}
