package com.terraformersmc.campanion.block;

import com.terraformersmc.campanion.Campanion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.LinkedHashMap;
import java.util.Map;

public class CampanionBlocks {

	private static final Map<ResourceLocation, Item> ITEMS = new LinkedHashMap<>();
	private static final Map<ResourceLocation, Block> BLOCKS = new LinkedHashMap<>();

	public static final Block ROPE_BRIDGE_POST = add("rope_bridge_post", new RopeBridgePostBlock(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(1.0F, 1.0F).dynamicShape().noOcclusion()), Campanion.TAB);
	public static final Block ROPE_BRIDGE_PLANKS = add("rope_bridge_planks", new RopeBridgePlanksBlock(BlockBehaviour.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(0.5F, 1.0F).dynamicShape().noOcclusion().noLootTable()), Campanion.TAB);
	public static final Block ROPE_LADDER = add("rope_ladder", new RopeLadderBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().destroyTime(0.2F).sound(SoundType.LADDER).noLootTable()), Campanion.TAB);

	public static final Block WHITE_LAWN_CHAIR = createLawnChair("white");
	public static final Block ORANGE_LAWN_CHAIR = createLawnChair("orange");
	public static final Block MAGENTA_LAWN_CHAIR = createLawnChair("magenta");
	public static final Block LIGHT_BLUE_LAWN_CHAIR = createLawnChair("light_blue");
	public static final Block YELLOW_LAWN_CHAIR = createLawnChair("yellow");
	public static final Block LIME_LAWN_CHAIR = createLawnChair("lime");
	public static final Block PINK_LAWN_CHAIR = createLawnChair("pink");
	public static final Block GRAY_LAWN_CHAIR = createLawnChair("gray");
	public static final Block LIGHT_GRAY_LAWN_CHAIR = createLawnChair("light_gray");
	public static final Block CYAN_LAWN_CHAIR = createLawnChair("cyan");
	public static final Block PURPLE_LAWN_CHAIR = createLawnChair("purple");
	public static final Block BLUE_LAWN_CHAIR = createLawnChair("blue");
	public static final Block BROWN_LAWN_CHAIR = createLawnChair("brown");
	public static final Block GREEN_LAWN_CHAIR = createLawnChair("green");
	public static final Block RED_LAWN_CHAIR = createLawnChair("red");
	public static final Block BLACK_LAWN_CHAIR = createLawnChair("black");

	public static final Block LEATHER_TANNER = add("leather_tanner", new LeatherTanner(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().sound(SoundType.WOOD)), Campanion.TAB);

	public static final TentSideBlock WHITE_TENT_SIDE = tentSide(DyeColor.WHITE);
	public static final TentSideBlock ORANGE_TENT_SIDE = tentSide(DyeColor.ORANGE);
	public static final TentSideBlock MAGENTA_TENT_SIDE = tentSide(DyeColor.MAGENTA);
	public static final TentSideBlock LIGHT_BLUE_TENT_SIDE = tentSide(DyeColor.LIGHT_BLUE);
	public static final TentSideBlock YELLOW_TENT_SIDE = tentSide(DyeColor.YELLOW);
	public static final TentSideBlock LIME_TENT_SIDE = tentSide(DyeColor.LIME);
	public static final TentSideBlock PINK_TENT_SIDE = tentSide(DyeColor.PINK);
	public static final TentSideBlock GRAY_TENT_SIDE = tentSide(DyeColor.GRAY);
	public static final TentSideBlock LIGHT_GRAY_TENT_SIDE = tentSide(DyeColor.LIGHT_GRAY);
	public static final TentSideBlock CYAN_TENT_SIDE = tentSide(DyeColor.CYAN);
	public static final TentSideBlock PURPLE_TENT_SIDE = tentSide(DyeColor.PURPLE);
	public static final TentSideBlock BLUE_TENT_SIDE = tentSide(DyeColor.BLUE);
	public static final TentSideBlock BROWN_TENT_SIDE = tentSide(DyeColor.BROWN);
	public static final TentSideBlock GREEN_TENT_SIDE = tentSide(DyeColor.GREEN);
	public static final TentSideBlock RED_TENT_SIDE = tentSide(DyeColor.RED);
	public static final TentSideBlock BLACK_TENT_SIDE = tentSide(DyeColor.BLACK);

	public static final TentTopBlock WHITE_TENT_TOP = tentTop(DyeColor.WHITE);
	public static final TentTopBlock ORANGE_TENT_TOP = tentTop(DyeColor.ORANGE);
	public static final TentTopBlock MAGENTA_TENT_TOP = tentTop(DyeColor.MAGENTA);
	public static final TentTopBlock LIGHT_BLUE_TENT_TOP = tentTop(DyeColor.LIGHT_BLUE);
	public static final TentTopBlock YELLOW_TENT_TOP = tentTop(DyeColor.YELLOW);
	public static final TentTopBlock LIME_TENT_TOP = tentTop(DyeColor.LIME);
	public static final TentTopBlock PINK_TENT_TOP = tentTop(DyeColor.PINK);
	public static final TentTopBlock GRAY_TENT_TOP = tentTop(DyeColor.GRAY);
	public static final TentTopBlock LIGHT_GRAY_TENT_TOP = tentTop(DyeColor.LIGHT_GRAY);
	public static final TentTopBlock CYAN_TENT_TOP = tentTop(DyeColor.CYAN);
	public static final TentTopBlock PURPLE_TENT_TOP = tentTop(DyeColor.PURPLE);
	public static final TentTopBlock BLUE_TENT_TOP = tentTop(DyeColor.BLUE);
	public static final TentTopBlock BROWN_TENT_TOP = tentTop(DyeColor.BROWN);
	public static final TentTopBlock GREEN_TENT_TOP = tentTop(DyeColor.GREEN);
	public static final TentTopBlock RED_TENT_TOP = tentTop(DyeColor.RED);
	public static final TentTopBlock BLACK_TENT_TOP = tentTop(DyeColor.BLACK);

	public static final TentTopPoleBlock WHITE_TOPPED_TENT_POLE = toppedTentPole(DyeColor.WHITE);
	public static final TentTopPoleBlock ORANGE_TOPPED_TENT_POLE = toppedTentPole(DyeColor.ORANGE);
	public static final TentTopPoleBlock MAGENTA_TOPPED_TENT_POLE = toppedTentPole(DyeColor.MAGENTA);
	public static final TentTopPoleBlock LIGHT_BLUE_TOPPED_TENT_POLE = toppedTentPole(DyeColor.LIGHT_BLUE);
	public static final TentTopPoleBlock YELLOW_TOPPED_TENT_POLE = toppedTentPole(DyeColor.YELLOW);
	public static final TentTopPoleBlock LIME_TOPPED_TENT_POLE = toppedTentPole(DyeColor.LIME);
	public static final TentTopPoleBlock PINK_TOPPED_TENT_POLE = toppedTentPole(DyeColor.PINK);
	public static final TentTopPoleBlock GRAY_TOPPED_TENT_POLE = toppedTentPole(DyeColor.GRAY);
	public static final TentTopPoleBlock LIGHT_GRAY_TOPPED_TENT_POLE = toppedTentPole(DyeColor.LIGHT_GRAY);
	public static final TentTopPoleBlock CYAN_TOPPED_TENT_POLE = toppedTentPole(DyeColor.CYAN);
	public static final TentTopPoleBlock PURPLE_TOPPED_TENT_POLE = toppedTentPole(DyeColor.PURPLE);
	public static final TentTopPoleBlock BLUE_TOPPED_TENT_POLE = toppedTentPole(DyeColor.BLUE);
	public static final TentTopPoleBlock BROWN_TOPPED_TENT_POLE = toppedTentPole(DyeColor.BROWN);
	public static final TentTopPoleBlock GREEN_TOPPED_TENT_POLE = toppedTentPole(DyeColor.GREEN);
	public static final TentTopPoleBlock RED_TOPPED_TENT_POLE = toppedTentPole(DyeColor.RED);
	public static final TentTopPoleBlock BLACK_TOPPED_TENT_POLE = toppedTentPole(DyeColor.BLACK);

	public static final TentTopFlatBlock WHITE_FLAT_TENT_TOP = tentTopFlat(DyeColor.WHITE);
	public static final TentTopFlatBlock ORANGE_FLAT_TENT_TOP = tentTopFlat(DyeColor.ORANGE);
	public static final TentTopFlatBlock MAGENTA_FLAT_TENT_TOP = tentTopFlat(DyeColor.MAGENTA);
	public static final TentTopFlatBlock LIGHT_BLUE_FLAT_TENT_TOP = tentTopFlat(DyeColor.LIGHT_BLUE);
	public static final TentTopFlatBlock YELLOW_FLAT_TENT_TOP = tentTopFlat(DyeColor.YELLOW);
	public static final TentTopFlatBlock LIME_FLAT_TENT_TOP = tentTopFlat(DyeColor.LIME);
	public static final TentTopFlatBlock PINK_FLAT_TENT_TOP = tentTopFlat(DyeColor.PINK);
	public static final TentTopFlatBlock GRAY_FLAT_TENT_TOP = tentTopFlat(DyeColor.GRAY);
	public static final TentTopFlatBlock LIGHT_GRAY_FLAT_TENT_TOP = tentTopFlat(DyeColor.LIGHT_GRAY);
	public static final TentTopFlatBlock CYAN_FLAT_TENT_TOP = tentTopFlat(DyeColor.CYAN);
	public static final TentTopFlatBlock PURPLE_FLAT_TENT_TOP = tentTopFlat(DyeColor.PURPLE);
	public static final TentTopFlatBlock BLUE_FLAT_TENT_TOP = tentTopFlat(DyeColor.BLUE);
	public static final TentTopFlatBlock BROWN_FLAT_TENT_TOP = tentTopFlat(DyeColor.BROWN);
	public static final TentTopFlatBlock GREEN_FLAT_TENT_TOP = tentTopFlat(DyeColor.GREEN);
	public static final TentTopFlatBlock RED_FLAT_TENT_TOP = tentTopFlat(DyeColor.RED);
	public static final TentTopFlatBlock BLACK_FLAT_TENT_TOP = tentTopFlat(DyeColor.BLACK);

	public static final Block TENT_POLE = add("tent_pole", new TentPoleBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().destroyTime(-1F).explosionResistance(1200F).sound(SoundType.WOOD)));

	public static final FlareBlock FLARE_BLOCK = add("flare_block", new FlareBlock(BlockBehaviour.Properties.of(Material.EXPLOSIVE).lightLevel(state -> 14)));

	private static <B extends Block> B add(String name, B block, CreativeModeTab tab) {
		Item.Properties settings = new Item.Properties();
		if (tab != null) {
			settings.tab(tab);
		}
		return add(name, block, new BlockItem(block, settings));
	}

	private static <B extends Block> B add(String name, B block, BlockItem item) {
		add(name, block);
		if (item != null) {
			item.registerBlocks(Item.BY_BLOCK, item);
			ITEMS.put(new ResourceLocation(Campanion.MOD_ID, name), item);
		}
		return block;
	}

	private static <B extends Block> B add(String name, B block) {
		BLOCKS.put(new ResourceLocation(Campanion.MOD_ID, name), block);
		return block;
	}

	private static <I extends BlockItem> I add(String name, I item) {
		item.registerBlocks(Item.BY_BLOCK, item);
		ITEMS.put(new ResourceLocation(Campanion.MOD_ID, name), item);
		return item;
	}

//	public static void register() {
//		for (ResourceLocation id : ITEMS.keySet()) {
//			Registry.register(Registry.ITEM, id, ITEMS.get(id));
//		}
//		for (ResourceLocation id : BLOCKS.keySet()) {
//			Registry.register(Registry.BLOCK, id, BLOCKS.get(id));
//		}
//		addFuels();
//		addFlammables();
//	}


	private static TentSideBlock tentSide(DyeColor color) {
		return add(color.getName() + "_tent_side", new TentSideBlock(BlockBehaviour.Properties.of(Material.WOOL).noOcclusion().destroyTime(1F).explosionResistance(1200F).sound(SoundType.WOOL), color));
	}

	private static TentTopBlock tentTop(DyeColor color) {
		return add(color.getName() + "_tent_top", new TentTopBlock(BlockBehaviour.Properties.of(Material.WOOL).noOcclusion().destroyTime(1F).explosionResistance(1200F).sound(SoundType.WOOL), color));
	}

	private static TentTopPoleBlock toppedTentPole(DyeColor color) {
		return add(color.getName() + "_topped_tent_pole", new TentTopPoleBlock(BlockBehaviour.Properties.of(Material.WOOL).noOcclusion().destroyTime(1F).explosionResistance(1200F).sound(SoundType.WOOL), color));
	}

	private static TentTopFlatBlock tentTopFlat(DyeColor color) {
		return add(color.getName() + "_flat_tent_top", new TentTopFlatBlock(BlockBehaviour.Properties.of(Material.WOOL).noOcclusion().destroyTime(1F).explosionResistance(1200F).sound(SoundType.WOOL), color));
	}


	private static LawnChairBlock createLawnChair(String color) {
		return add(color + "_lawn_chair", new LawnChairBlock(BlockBehaviour.Properties.of(Material.WOOD).noOcclusion().sound(SoundType.WOOD)), Campanion.TAB);
	}

	public static Map<ResourceLocation, Block> getBlocks() {
		return BLOCKS;
	}

	public static Map<ResourceLocation, Item> getItemBlocks() {
		return ITEMS;
	}

}
