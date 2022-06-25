package com.terraformersmc.campanion.data;

import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.tag.CampanionBlockTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.world.level.block.Block;

public class CampanionBlockTagsGenerator extends BlockTagsProvider {
	public CampanionBlockTagsGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void addTags() {
		this.tag(CampanionBlockTags.LAWN_CHAIRS).add(CampanionBlocks.LAWN_CHAIRS.toArray(Block[]::new));
		this.tag(CampanionBlockTags.TENT_SIDES).add(CampanionBlocks.TENT_SIDES.toArray(Block[]::new));
		this.tag(CampanionBlockTags.TENT_TOPS).add(CampanionBlocks.TENT_TOPS.toArray(Block[]::new));
		this.tag(CampanionBlockTags.TOPPED_TENT_POLES).add(CampanionBlocks.TOPPED_TENT_POLES.toArray(Block[]::new));
		this.tag(CampanionBlockTags.FLAT_TENT_TOPS).add(CampanionBlocks.FLAT_TENT_TOPS.toArray(Block[]::new));

		this.tag(CampanionBlockTags.TENT_POLES).add(CampanionBlocks.TENT_POLE).addTag(CampanionBlockTags.TOPPED_TENT_POLES);
	}
}
