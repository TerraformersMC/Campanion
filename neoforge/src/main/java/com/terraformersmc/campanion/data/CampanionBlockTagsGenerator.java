package com.terraformersmc.campanion.data;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.tag.CampanionBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class CampanionBlockTagsGenerator extends BlockTagsProvider {
	public CampanionBlockTagsGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper existingFileHelper) {
		super(packOutput, provider, Campanion.MOD_ID, existingFileHelper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(CampanionBlockTags.LAWN_CHAIRS).add(CampanionBlocks.LAWN_CHAIRS.toArray(Block[]::new));
		this.tag(CampanionBlockTags.TENT_SIDES).add(CampanionBlocks.TENT_SIDES.toArray(Block[]::new));
		this.tag(CampanionBlockTags.TENT_TOPS).add(CampanionBlocks.TENT_TOPS.toArray(Block[]::new));
		this.tag(CampanionBlockTags.TOPPED_TENT_POLES).add(CampanionBlocks.TOPPED_TENT_POLES.toArray(Block[]::new));
		this.tag(CampanionBlockTags.FLAT_TENT_TOPS).add(CampanionBlocks.FLAT_TENT_TOPS.toArray(Block[]::new));

		this.tag(CampanionBlockTags.TENT_POLES).add(CampanionBlocks.TENT_POLE).addTag(CampanionBlockTags.TOPPED_TENT_POLES);

		this.tag(BlockTags.CLIMBABLE).add(CampanionBlocks.ROPE_LADDER);
	}
}
