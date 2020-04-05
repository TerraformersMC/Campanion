package com.campanion.data;

import com.campanion.block.CampanionBlocks;
import com.campanion.tag.CampanionBlockTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.BlockTagsProvider;

public class CampanionBlockTagsProvider extends BlockTagsProvider {
	public CampanionBlockTagsProvider(DataGenerator dataGenerator) {
		super(dataGenerator);
	}

	@Override
	protected void configure() {
		super.configure();
		this.getOrCreateTagBuilder(CampanionBlockTags.LAWN_CHAIRS).add(CampanionBlocks.WHITE_LAWN_CHAIR, CampanionBlocks.ORANGE_LAWN_CHAIR, CampanionBlocks.MAGENTA_LAWN_CHAIR, CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR, CampanionBlocks.YELLOW_LAWN_CHAIR, CampanionBlocks.LIME_LAWN_CHAIR, CampanionBlocks.PINK_LAWN_CHAIR, CampanionBlocks.GRAY_LAWN_CHAIR, CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR, CampanionBlocks.CYAN_LAWN_CHAIR, CampanionBlocks.PURPLE_LAWN_CHAIR, CampanionBlocks.BLUE_LAWN_CHAIR, CampanionBlocks.BROWN_LAWN_CHAIR, CampanionBlocks.GREEN_LAWN_CHAIR, CampanionBlocks.RED_LAWN_CHAIR, CampanionBlocks.BLACK_LAWN_CHAIR);
		this.getOrCreateTagBuilder(CampanionBlockTags.TENT_SIDES).add(CampanionBlocks.WHITE_TENT_SIDE, CampanionBlocks.ORANGE_TENT_SIDE, CampanionBlocks.MAGENTA_TENT_SIDE, CampanionBlocks.LIGHT_BLUE_TENT_SIDE, CampanionBlocks.YELLOW_TENT_SIDE, CampanionBlocks.LIME_TENT_SIDE, CampanionBlocks.PINK_TENT_SIDE, CampanionBlocks.GRAY_TENT_SIDE, CampanionBlocks.LIGHT_GRAY_TENT_SIDE, CampanionBlocks.CYAN_TENT_SIDE, CampanionBlocks.PURPLE_TENT_SIDE, CampanionBlocks.BLUE_TENT_SIDE, CampanionBlocks.BROWN_TENT_SIDE, CampanionBlocks.GREEN_TENT_SIDE, CampanionBlocks.RED_TENT_SIDE, CampanionBlocks.BLACK_TENT_SIDE);
		this.getOrCreateTagBuilder(CampanionBlockTags.TENT_TOPS).add(CampanionBlocks.WHITE_TENT_TOP, CampanionBlocks.ORANGE_TENT_TOP, CampanionBlocks.MAGENTA_TENT_TOP, CampanionBlocks.LIGHT_BLUE_TENT_TOP, CampanionBlocks.YELLOW_TENT_TOP, CampanionBlocks.LIME_TENT_TOP, CampanionBlocks.PINK_TENT_TOP, CampanionBlocks.GRAY_TENT_TOP, CampanionBlocks.LIGHT_GRAY_TENT_TOP, CampanionBlocks.CYAN_TENT_TOP, CampanionBlocks.PURPLE_TENT_TOP, CampanionBlocks.BLUE_TENT_TOP, CampanionBlocks.BROWN_TENT_TOP, CampanionBlocks.GREEN_TENT_TOP, CampanionBlocks.RED_TENT_TOP, CampanionBlocks.BLACK_TENT_TOP);
		this.getOrCreateTagBuilder(CampanionBlockTags.TOPPED_TENT_POLES).add(CampanionBlocks.WHITE_TOPPED_TENT_POLE, CampanionBlocks.ORANGE_TOPPED_TENT_POLE, CampanionBlocks.MAGENTA_TOPPED_TENT_POLE, CampanionBlocks.LIGHT_BLUE_TOPPED_TENT_POLE, CampanionBlocks.YELLOW_TOPPED_TENT_POLE, CampanionBlocks.LIME_TOPPED_TENT_POLE, CampanionBlocks.PINK_TOPPED_TENT_POLE, CampanionBlocks.GRAY_TOPPED_TENT_POLE, CampanionBlocks.LIGHT_GRAY_TOPPED_TENT_POLE, CampanionBlocks.CYAN_TOPPED_TENT_POLE, CampanionBlocks.PURPLE_TOPPED_TENT_POLE, CampanionBlocks.BLUE_TOPPED_TENT_POLE, CampanionBlocks.BROWN_TOPPED_TENT_POLE, CampanionBlocks.GREEN_TOPPED_TENT_POLE, CampanionBlocks.RED_TOPPED_TENT_POLE, CampanionBlocks.BLACK_TOPPED_TENT_POLE);
		this.getOrCreateTagBuilder(CampanionBlockTags.FLAT_TENT_TOPS).add(CampanionBlocks.WHITE_FLAT_TENT_TOP, CampanionBlocks.ORANGE_FLAT_TENT_TOP, CampanionBlocks.MAGENTA_FLAT_TENT_TOP, CampanionBlocks.LIGHT_BLUE_FLAT_TENT_TOP, CampanionBlocks.YELLOW_FLAT_TENT_TOP, CampanionBlocks.LIME_FLAT_TENT_TOP, CampanionBlocks.PINK_FLAT_TENT_TOP, CampanionBlocks.GRAY_FLAT_TENT_TOP, CampanionBlocks.LIGHT_GRAY_FLAT_TENT_TOP, CampanionBlocks.CYAN_FLAT_TENT_TOP, CampanionBlocks.PURPLE_FLAT_TENT_TOP, CampanionBlocks.BLUE_FLAT_TENT_TOP, CampanionBlocks.BROWN_FLAT_TENT_TOP, CampanionBlocks.GREEN_FLAT_TENT_TOP, CampanionBlocks.RED_FLAT_TENT_TOP, CampanionBlocks.BLACK_FLAT_TENT_TOP);
		this.getOrCreateTagBuilder(CampanionBlockTags.FLAT_TENT_TOPS).add(CampanionBlocks.WHITE_FLAT_TENT_TOP, CampanionBlocks.ORANGE_FLAT_TENT_TOP, CampanionBlocks.MAGENTA_FLAT_TENT_TOP, CampanionBlocks.LIGHT_BLUE_FLAT_TENT_TOP, CampanionBlocks.YELLOW_FLAT_TENT_TOP, CampanionBlocks.LIME_FLAT_TENT_TOP, CampanionBlocks.PINK_FLAT_TENT_TOP, CampanionBlocks.GRAY_FLAT_TENT_TOP, CampanionBlocks.LIGHT_GRAY_FLAT_TENT_TOP, CampanionBlocks.CYAN_FLAT_TENT_TOP, CampanionBlocks.PURPLE_FLAT_TENT_TOP, CampanionBlocks.BLUE_FLAT_TENT_TOP, CampanionBlocks.BROWN_FLAT_TENT_TOP, CampanionBlocks.GREEN_FLAT_TENT_TOP, CampanionBlocks.RED_FLAT_TENT_TOP, CampanionBlocks.BLACK_FLAT_TENT_TOP);
	}

	@Override
	public String getName() {
		return "Campanion Block Tags";
	}
}
