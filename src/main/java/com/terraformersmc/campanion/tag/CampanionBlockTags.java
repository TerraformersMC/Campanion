package com.terraformersmc.campanion.tag;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.mixin.AccessorBlockTags;
import net.minecraft.block.Block;
import net.minecraft.tag.Tag;

public class CampanionBlockTags {
	public static final Tag.Identified<Block> LAWN_CHAIRS = get("lawn_chairs");
	public static final Tag.Identified<Block> TENT_SIDES = get("tent_sides");
	public static final Tag.Identified<Block> TENT_TOPS = get("tent_tops");
	public static final Tag.Identified<Block> FLAT_TENT_TOPS = get("flat_tent_tops");
	public static final Tag.Identified<Block> TOPPED_TENT_POLES = get("topped_tent_poles");
	public static final Tag.Identified<Block> TENT_POLES = get("tent_poles");

	private static Tag.Identified<Block> get(String id) {
		return AccessorBlockTags.callRegister(Campanion.MOD_ID + ":" + id);
	}
}
