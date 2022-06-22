package com.terraformersmc.campanion.tag;

import com.terraformersmc.campanion.Campanion;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class CampanionBlockTags {
	public static final TagKey<Block> LAWN_CHAIRS = get("lawn_chairs");
	public static final TagKey<Block> TENT_SIDES = get("tent_sides");
	public static final TagKey<Block> TENT_TOPS = get("tent_tops");
	public static final TagKey<Block> FLAT_TENT_TOPS = get("flat_tent_tops");
	public static final TagKey<Block> TOPPED_TENT_POLES = get("topped_tent_poles");
	public static final TagKey<Block> TENT_POLES = get("tent_poles");

	private static TagKey<Block> get(String id) {
		return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Campanion.MOD_ID, id));
	}

	public static void load() {
	}
}
