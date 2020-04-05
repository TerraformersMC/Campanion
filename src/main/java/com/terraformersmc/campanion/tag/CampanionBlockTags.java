package com.terraformersmc.campanion.tag;

import com.terraformersmc.campanion.Campanion;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class CampanionBlockTags {
	public static final Tag<Block> LAWN_CHAIRS = get("lawn_chairs");
	public static final Tag<Block> TENT_SIDES = get("tent_sides");
	public static final Tag<Block> TENT_TOPS = get("tent_tops");
	public static final Tag<Block> FLAT_TENT_TOPS = get("flat_tent_tops");
	public static final Tag<Block> TOPPED_TENT_POLES = get("topped_tent_poles");
	public static final Tag<Block> TENT_POLES = get("tent_poles");

	private static Tag<Block> get(String id) {
		return TagRegistry.block(new Identifier(Campanion.MOD_ID, id));
	}
}
