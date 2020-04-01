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
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

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

	private static <B extends Block> B add(String name, B block, ItemGroup tab) {
		return add(name, block, new BlockItem(block, new Item.Settings().group(tab)));
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
