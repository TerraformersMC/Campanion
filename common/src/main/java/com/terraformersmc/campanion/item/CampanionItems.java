package com.terraformersmc.campanion.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.entity.CampanionEntities;
import java.util.LinkedHashMap;
import java.util.Map;

import com.terraformersmc.campanion.mixin.AccessorCreativeModeTabs;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;

public class CampanionItems {

	private static final Map<ResourceLocation, Item> ITEMS = new LinkedHashMap<>();
	private static final Multimap<ResourceKey<CreativeModeTab>, Item> TABS_TO_ITEMS = ArrayListMultimap.create();

	public static final Item MRE = add("mre", AccessorCreativeModeTabs.getFOOD_AND_DRINKS(), new Item(createProperties().food(new FoodProperties.Builder().nutrition(20).saturationModifier(1.24F).build())));

	private static Item.Properties createProperties() {
		return new Item.Properties();
	}

	public static final Item ROPE = add("rope", AccessorCreativeModeTabs.getINGREDIENTS(), new Item(createProperties()));
	public static final Item LEATHER_POUCH = add("leather_pouch", AccessorCreativeModeTabs.getINGREDIENTS(), new Item(createProperties()));
	public static final Item TANNED_LEATHER_POUCH = add("tanned_leather_pouch", AccessorCreativeModeTabs.getINGREDIENTS(), new Item(createProperties()));
	public static final Item WOODEN_ROD = add("wooden_rod", AccessorCreativeModeTabs.getINGREDIENTS(), new Item(createProperties()));
	public static final Item WOOL_TARP = add("wool_tarp", AccessorCreativeModeTabs.getINGREDIENTS(), new Item(createProperties()));

	public static final Item TANNED_LEATHER = add("tanned_leather", AccessorCreativeModeTabs.getINGREDIENTS(), new Item(createProperties()));

	public static final Item CRACKER = add("cracker", AccessorCreativeModeTabs.getFOOD_AND_DRINKS(), new Item(createProperties().food(new FoodProperties.Builder().nutrition(1).build())));
	public static final Item MARSHMALLOW = add("marshmallow", AccessorCreativeModeTabs.getFOOD_AND_DRINKS(), new Item(createProperties().food(new FoodProperties.Builder().nutrition(1).build())));
	public static final Item COOKED_MARSHMALLOW = add("cooked_marshmallow", AccessorCreativeModeTabs.getFOOD_AND_DRINKS(), new Item(createProperties().food(new FoodProperties.Builder().nutrition(1).build())));
	public static final Item BLACKENED_MARSHMALLOW = add("blackened_marshmallow", AccessorCreativeModeTabs.getFOOD_AND_DRINKS(), new Item(createProperties().food(new FoodProperties.Builder().nutrition(1).build())));
	public static final Item MARSHMALLOW_ON_A_STICK = add("marshmallow_on_a_stick", AccessorCreativeModeTabs.getTOOLS_AND_UTILITIES(), new MarshmallowOnAStickItem(createProperties(), MARSHMALLOW));
	public static final Item COOKED_MARSHMALLOW_ON_A_STICK = add("cooked_marshmallow_on_a_stick", AccessorCreativeModeTabs.getTOOLS_AND_UTILITIES(), new MarshmallowOnAStickItem(createProperties(), COOKED_MARSHMALLOW));
	public static final Item BLACKENED_MARSHMALLOW_ON_A_STICK = add("blackened_marshmallow_on_a_stick", AccessorCreativeModeTabs.getTOOLS_AND_UTILITIES(), new MarshmallowOnAStickItem(createProperties(), BLACKENED_MARSHMALLOW));
	public static final Item SMORE = add("smore", AccessorCreativeModeTabs.getFOOD_AND_DRINKS(), new Item(createProperties().food(new FoodProperties.Builder().nutrition(3).build())));

	public static final BackpackItem DAY_PACK = add("day_pack", AccessorCreativeModeTabs.getTOOLS_AND_UTILITIES(), new BackpackItem(BackpackItem.Type.DAY_PACK, createProperties()));
	public static final BackpackItem CAMPING_PACK = add("camping_pack", AccessorCreativeModeTabs.getTOOLS_AND_UTILITIES(), new BackpackItem(BackpackItem.Type.CAMPING_PACK, createProperties()));
	public static final BackpackItem HIKING_PACK = add("hiking_pack", AccessorCreativeModeTabs.getTOOLS_AND_UTILITIES(), new BackpackItem(BackpackItem.Type.HIKING_PACK, createProperties()));

	public static final SpearItem WOODEN_SPEAR = add("wooden_spear", AccessorCreativeModeTabs.getCOMBAT(), new SpearItem(Tiers.WOOD, 2.0F, -2.2F, () -> CampanionEntities.WOODEN_SPEAR, createProperties()));
	public static final SpearItem STONE_SPEAR = add("stone_spear", AccessorCreativeModeTabs.getCOMBAT(), new SpearItem(Tiers.STONE, 2.0F, -2.3F, () -> CampanionEntities.STONE_SPEAR, createProperties()));
	public static final SpearItem IRON_SPEAR = add("iron_spear", AccessorCreativeModeTabs.getCOMBAT(), new SpearItem(Tiers.IRON, 2.0F, -2.5F, () -> CampanionEntities.IRON_SPEAR, createProperties()));
	public static final SpearItem GOLDEN_SPEAR = add("golden_spear", AccessorCreativeModeTabs.getCOMBAT(), new SpearItem(Tiers.GOLD, 2.0F, -2.7F, () -> CampanionEntities.GOLDEN_SPEAR, createProperties()));
	public static final SpearItem DIAMOND_SPEAR = add("diamond_spear", AccessorCreativeModeTabs.getCOMBAT(), new SpearItem(Tiers.DIAMOND, 2.0F, -2.7F, () -> CampanionEntities.DIAMOND_SPEAR, createProperties()));
	public static final SpearItem NETHERITE_SPEAR = add("netherite_spear", AccessorCreativeModeTabs.getCOMBAT(), new SpearItem(Tiers.NETHERITE, 2.0F, -2.8F, () -> CampanionEntities.NETHERITE_SPEAR, createProperties().fireResistant()));

	public static final SkippingStoneItem SKIPPING_STONE = add("skipping_stone", AccessorCreativeModeTabs.getTOOLS_AND_UTILITIES(), new SkippingStoneItem(createProperties()));

	public static final FlareItem FLARE = add("flare", AccessorCreativeModeTabs.getTOOLS_AND_UTILITIES(), new FlareItem(createProperties()));

	public static final GrapplingHookItem GRAPPLING_HOOK = add("grappling_hook", AccessorCreativeModeTabs.getTOOLS_AND_UTILITIES(), new GrapplingHookItem(createProperties().durability(120)));
	public static final SleepingBagItem SLEEPING_BAG = add("sleeping_bag", AccessorCreativeModeTabs.getTOOLS_AND_UTILITIES(), new SleepingBagItem(createProperties().durability(250)));
	public static final TentBagItem TENT_BAG = add("tent_bag", AccessorCreativeModeTabs.getTOOLS_AND_UTILITIES(), new TentBagItem(createProperties()));
	public static final TentItem SMALL_UNBUILT_TENT = add("small_unbuilt_tent", AccessorCreativeModeTabs.getTOOLS_AND_UTILITIES(), new TentItem(createProperties().stacksTo(1), "small"));
	public static final TentItem LARGE_UNBUILT_TENT = add("large_unbuilt_tent", AccessorCreativeModeTabs.getTOOLS_AND_UTILITIES(), new TentItem(createProperties().stacksTo(1), "large"));

	private static <I extends Item> I add(String name, ResourceKey<CreativeModeTab> tab, I item) {
		ITEMS.put(ResourceLocation.fromNamespaceAndPath(Campanion.MOD_ID, name), item);
		TABS_TO_ITEMS.put(tab, item);
		return item;
	}

	public static Multimap<ResourceKey<CreativeModeTab>, Item> getTabsToItems() {
		return TABS_TO_ITEMS;
	}

	//	public static void register() {
//		for (ResourceLocation id : ITEMS.keySet()) {
//			Registry.register(Registry.ITEM, id, ITEMS.get(id));
//		}
//	}


	public static Map<ResourceLocation, Item> getItems() {
		return ITEMS;
	}
}
