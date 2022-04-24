package com.terraformersmc.campanion.tag;

import com.terraformersmc.campanion.Campanion;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CampanionBlockTags {
	public static final TagKey<Block> LAWN_CHAIRS = get("lawn_chairs");
	public static final TagKey<Block> TENT_SIDES = get("tent_sides");
	public static final TagKey<Block> TENT_TOPS = get("tent_tops");
	public static final TagKey<Block> FLAT_TENT_TOPS = get("flat_tent_tops");
	public static final TagKey<Block> TOPPED_TENT_POLES = get("topped_tent_poles");
	public static final TagKey<Block> TENT_POLES = get("tent_poles");

	private static TagKey<Block> get(String id) {
		return TagKey.of(Registry.BLOCK_KEY, new Identifier(Campanion.MOD_ID, id));
	}

	public static void load() {
	}
}
