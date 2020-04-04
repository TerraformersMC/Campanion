package com.campanion.block;

import com.campanion.Campanion;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CampanionBlocks {

	private static final Map<Identifier, BlockItem> ITEMS = new LinkedHashMap<>();
	private static final Map<Identifier, Block> BLOCKS = new LinkedHashMap<>();

	public static final Block ROPE_BRIDGE_ANCHOR = add("rope_bridge_post", new RopeBridgePostBlock(FabricBlockSettings.of(Material.WOOD).sounds(BlockSoundGroup.WOOD).strength(1.0F, 1.0F).dynamicBounds().nonOpaque().build()), ItemGroup.TOOLS);
	public static final Block ROPE_BRIDGE_PLANKS = add("rope_bridge_planks", new RopeBridgePlanksBlock(FabricBlockSettings.of(Material.WOOD).sounds(BlockSoundGroup.WOOD).strength(0.5F, 1.0F).dynamicBounds().nonOpaque().build()));
	public static final Block ROPE_LADDER = add("rope_ladder", new RopeLadderBlock(FabricBlockSettings.of(Material.WOOD).nonOpaque().hardness(0.2F).sounds(BlockSoundGroup.LADDER).build()), ItemGroup.DECORATIONS);

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

	public static final Block LEATHER_TANNER = add("leather_tanner", new LeatherTanner(FabricBlockSettings.of(Material.WOOD).nonOpaque().sounds(BlockSoundGroup.WOOD).build()), ItemGroup.DECORATIONS);

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

	public static final TentTopPoleBlock WHITE_TENT_TOP_POLE = tentTopPole(DyeColor.WHITE);
	public static final TentTopPoleBlock ORANGE_TENT_TOP_POLE = tentTopPole(DyeColor.ORANGE);
	public static final TentTopPoleBlock MAGENTA_TENT_TOP_POLE = tentTopPole(DyeColor.MAGENTA);
	public static final TentTopPoleBlock LIGHT_BLUE_TENT_TOP_POLE = tentTopPole(DyeColor.LIGHT_BLUE);
	public static final TentTopPoleBlock YELLOW_TENT_TOP_POLE = tentTopPole(DyeColor.YELLOW);
	public static final TentTopPoleBlock LIME_TENT_TOP_POLE = tentTopPole(DyeColor.LIME);
	public static final TentTopPoleBlock PINK_TENT_TOP_POLE = tentTopPole(DyeColor.PINK);
	public static final TentTopPoleBlock GRAY_TENT_TOP_POLE = tentTopPole(DyeColor.GRAY);
	public static final TentTopPoleBlock LIGHT_GRAY_TENT_TOP_POLE = tentTopPole(DyeColor.LIGHT_GRAY);
	public static final TentTopPoleBlock CYAN_TENT_TOP_POLE = tentTopPole(DyeColor.CYAN);
	public static final TentTopPoleBlock PURPLE_TENT_TOP_POLE = tentTopPole(DyeColor.PURPLE);
	public static final TentTopPoleBlock BLUE_TENT_TOP_POLE = tentTopPole(DyeColor.BLUE);
	public static final TentTopPoleBlock BROWN_TENT_TOP_POLE = tentTopPole(DyeColor.BROWN);
	public static final TentTopPoleBlock GREEN_TENT_TOP_POLE = tentTopPole(DyeColor.GREEN);
	public static final TentTopPoleBlock RED_TENT_TOP_POLE = tentTopPole(DyeColor.RED);
	public static final TentTopPoleBlock BLACK_TENT_TOP_POLE = tentTopPole(DyeColor.BLACK);

	public static final TentTopFlatBlock WHITE_TENT_TOP_FLAT = tentTopFlat(DyeColor.WHITE);
	public static final TentTopFlatBlock ORANGE_TENT_TOP_FLAT = tentTopFlat(DyeColor.ORANGE);
	public static final TentTopFlatBlock MAGENTA_TENT_TOP_FLAT = tentTopFlat(DyeColor.MAGENTA);
	public static final TentTopFlatBlock LIGHT_BLUE_TENT_TOP_FLAT = tentTopFlat(DyeColor.LIGHT_BLUE);
	public static final TentTopFlatBlock YELLOW_TENT_TOP_FLAT = tentTopFlat(DyeColor.YELLOW);
	public static final TentTopFlatBlock LIME_TENT_TOP_FLAT = tentTopFlat(DyeColor.LIME);
	public static final TentTopFlatBlock PINK_TENT_TOP_FLAT = tentTopFlat(DyeColor.PINK);
	public static final TentTopFlatBlock GRAY_TENT_TOP_FLAT = tentTopFlat(DyeColor.GRAY);
	public static final TentTopFlatBlock LIGHT_GRAY_TENT_TOP_FLAT = tentTopFlat(DyeColor.LIGHT_GRAY);
	public static final TentTopFlatBlock CYAN_TENT_TOP_FLAT = tentTopFlat(DyeColor.CYAN);
	public static final TentTopFlatBlock PURPLE_TENT_TOP_FLAT = tentTopFlat(DyeColor.PURPLE);
	public static final TentTopFlatBlock BLUE_TENT_TOP_FLAT = tentTopFlat(DyeColor.BLUE);
	public static final TentTopFlatBlock BROWN_TENT_TOP_FLAT = tentTopFlat(DyeColor.BROWN);
	public static final TentTopFlatBlock GREEN_TENT_TOP_FLAT = tentTopFlat(DyeColor.GREEN);
	public static final TentTopFlatBlock RED_TENT_TOP_FLAT = tentTopFlat(DyeColor.RED);
	public static final TentTopFlatBlock BLACK_TENT_TOP_FLAT = tentTopFlat(DyeColor.BLACK);

	public static final Block TENT_POLE = add("tent_pole", new TentPoleBlock(FabricBlockSettings.of(Material.WOOD).nonOpaque().hardness(-1F).sounds(BlockSoundGroup.WOOD).build()), (ItemGroup) null);

	private static <B extends Block> B add(String name, B block, ItemGroup tab) {
		Item.Settings settings = new Item.Settings();
		if(tab != null) {
			settings.group(tab);
		}
		return add(name, block, new BlockItem(block, settings));
	}

	private static <B extends Block> B add(String name, B block, BlockItem item) {
		add(name, block);
		if (item != null) {
			item.appendBlocks(Item.BLOCK_ITEMS, item);
			ITEMS.put(new Identifier(Campanion.MOD_ID, name), item);
		}
		return block;
	}

	private static <B extends Block> B add(String name, B block) {
		BLOCKS.put(new Identifier(Campanion.MOD_ID, name), block);
		return block;
	}

	private static <I extends BlockItem> I add(String name, I item) {
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		ITEMS.put(new Identifier(Campanion.MOD_ID, name), item);
		return item;
	}

	public static void register() {
		for (Identifier id : ITEMS.keySet()) {
			Registry.register(Registry.ITEM, id, ITEMS.get(id));
		}
		for (Identifier id : BLOCKS.keySet()) {
			Registry.register(Registry.BLOCK, id, BLOCKS.get(id));
		}
		addFuels();
		addFlammables();
	}

	private static TentSideBlock tentSide(DyeColor color) {
		return add(color.getName() + "_tent_side", new TentSideBlock(FabricBlockSettings.of(Material.WOOL).nonOpaque().hardness(-1).sounds(BlockSoundGroup.WOOL).build(), color), (ItemGroup) null);
	}

	private static TentTopBlock tentTop(DyeColor color) {
		return add(color.getName() + "_tent_top", new TentTopBlock(FabricBlockSettings.of(Material.WOOL).nonOpaque().hardness(-1).sounds(BlockSoundGroup.WOOL).build(), color), (ItemGroup) null);
	}

	private static TentTopPoleBlock tentTopPole(DyeColor color) {
		return add(color.getName() + "_tent_top_pole", new TentTopPoleBlock(FabricBlockSettings.of(Material.WOOL).nonOpaque().hardness(-1).sounds(BlockSoundGroup.WOOL).build(), color), (ItemGroup) null);
	}

	private static TentTopFlatBlock tentTopFlat(DyeColor color) {
		return add(color.getName() + "_tent_top_flat", new TentTopFlatBlock(FabricBlockSettings.of(Material.WOOL).nonOpaque().hardness(-1).sounds(BlockSoundGroup.WOOL).build(), color), (ItemGroup) null);
	}


	private static LawnChairBlock createLawnChair(String color) {
		return add(color + "_lawn_chair", new LawnChairBlock(FabricBlockSettings.of(Material.WOOD).nonOpaque().sounds(BlockSoundGroup.WOOD).build()), ItemGroup.DECORATIONS);
	}

	private static void addFuels() {
		FuelRegistry fuelRegistry = FuelRegistry.INSTANCE;

	}

	private static void addFlammables() {
		FlammableBlockRegistry flammableRegistry = FlammableBlockRegistry.getDefaultInstance();

	}

}
