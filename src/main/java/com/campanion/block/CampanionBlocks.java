package com.campanion.block;

import com.campanion.Campanion;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class CampanionBlocks {

	private static final Map<Identifier, BlockItem> ITEMS = new LinkedHashMap<>();
	private static final Map<Identifier, Block> BLOCKS = new LinkedHashMap<>();

	public static final Block ROPE_BRIDGE_ANCHOR = add("rope_bridge_anchor", new RopeBridgeAnchor(FabricBlockSettings.of(Material.ANVIL).sounds(BlockSoundGroup.ANVIL).build()), ItemGroup.TOOLS);
	public static final Block ROPE_BRIDGE_PLANKS = add("rope_bridge_planks", new RopeBridgePlanks(FabricBlockSettings.of(Material.WOOD).sounds(BlockSoundGroup.WOOD).dynamicBounds().build()));
	public static final Block ROPE_LADDER = add("rope_ladder", new RopeLadderBlock(FabricBlockSettings.of(Material.WOOD).nonOpaque().hardness(0.2F).sounds(BlockSoundGroup.LADDER).build()), ItemGroup.DECORATIONS);

	public static final Block BLACK_SLEEPING_BAG = addBed(DyeColor.BLACK);
	public static final Block BLUE_SLEEPING_BAG = addBed(DyeColor.BLUE);
	public static final Block BROWN_SLEEPING_BAG = addBed(DyeColor.BROWN);
	public static final Block CYAN_SLEEPING_BAG = addBed(DyeColor.CYAN);
	public static final Block GRAY_SLEEPING_BAG = addBed(DyeColor.GRAY);
	public static final Block GREEN_SLEEPING_BAG = addBed(DyeColor.GREEN);
	public static final Block LIGHT_BLUE_SLEEPING_BAG = addBed(DyeColor.LIGHT_BLUE);
	public static final Block LIGHT_GRAY_SLEEPING_BAG = addBed(DyeColor.LIGHT_GRAY);
	public static final Block LIME_SLEEPING_BAG = addBed(DyeColor.LIME);
	public static final Block MAGENTA_SLEEPING_BAG = addBed(DyeColor.MAGENTA);
	public static final Block ORANGE_SLEEPING_BAG = addBed(DyeColor.ORANGE);
	public static final Block PING_SLEEPING_BAG = addBed(DyeColor.PINK);
	public static final Block PURPLE_SLEEPING_BAG = addBed(DyeColor.PURPLE);
	public static final Block RED_SLEEPING_BAG = addBed(DyeColor.RED);
	public static final Block WHITE_SLEEPING_BAG = addBed(DyeColor.WHITE);
	public static final Block YELLOW_SLEEPING_BAG = addBed(DyeColor.YELLOW);

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

	private static void addFuels() {
		FuelRegistry fuelRegistry = FuelRegistry.INSTANCE;

	}

	private static void addFlammables() {
		FlammableBlockRegistry flammableRegistry = FlammableBlockRegistry.getDefaultInstance();

	}

	private static <B extends Block> B addBed(DyeColor color) {
		return (B) add(color.getName() + "_sleeping_bag", new SleepingBagBlock(color, FabricBlockSettings.of(Material.WOOL).sounds(BlockSoundGroup.WOOL).hardness(0.2F).nonOpaque().build()), ItemGroup.MISC);
	}

}
