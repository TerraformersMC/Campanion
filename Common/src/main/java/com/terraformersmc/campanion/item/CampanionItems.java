package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.entity.CampanionEntities;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;

public class CampanionItems {

	private static final Map<ResourceLocation, Item> ITEMS = new LinkedHashMap<>();

	public static final Item MRE = add("mre", new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().nutrition(20).saturationMod(1.24F).build())));
	public static final Item ROPE = add("rope", new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final Item LEATHER_POUCH = add("leather_pouch", new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final Item TANNED_LEATHER_POUCH = add("tanned_leather_pouch", new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final Item WOODEN_ROD = add("wooden_rod", new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));
	public static final Item WOOL_TARP = add("wool_tarp", new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

	public static final Item TANNED_LEATHER = add("tanned_leather", new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS)));

	public static final Item CRACKER = add("cracker", new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().nutrition(1).build())));
	public static final Item MARSHMALLOW = add("marshmallow", new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().nutrition(1).build())));
	public static final Item COOKED_MARSHMALLOW = add("cooked_marshmallow", new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().nutrition(1).build())));
	public static final Item BLACKENED_MARSHMALLOW = add("blackened_marshmallow", new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().nutrition(1).build())));
	public static final Item MARSHMALLOW_ON_A_STICK = add("marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC), MARSHMALLOW));
	public static final Item COOKED_MARSHMALLOW_ON_A_STICK = add("cooked_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC), COOKED_MARSHMALLOW));
	public static final Item BLACKENED_MARSHMALLOW_ON_A_STICK = add("blackened_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC), BLACKENED_MARSHMALLOW));
	public static final Item SMORE = add("smore", new Item(new Item.Properties().tab(CreativeModeTab.TAB_FOOD).food(new FoodProperties.Builder().nutrition(3).build())));

	public static final BackpackItem DAY_PACK = add("day_pack", new BackpackItem(BackpackItem.Type.DAY_PACK, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
	public static final BackpackItem CAMPING_PACK = add("camping_pack", new BackpackItem(BackpackItem.Type.CAMPING_PACK, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
	public static final BackpackItem HIKING_PACK = add("hiking_pack", new BackpackItem(BackpackItem.Type.HIKING_PACK, new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

	public static final SpearItem WOODEN_SPEAR = add("wooden_spear", new SpearItem(Tiers.WOOD, 2.0F, -2.2F, () -> CampanionEntities.WOODEN_SPEAR, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final SpearItem STONE_SPEAR = add("stone_spear", new SpearItem(Tiers.STONE, 2.0F, -2.3F, () -> CampanionEntities.STONE_SPEAR, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final SpearItem IRON_SPEAR = add("iron_spear", new SpearItem(Tiers.IRON, 2.0F, -2.5F, () -> CampanionEntities.IRON_SPEAR, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final SpearItem GOLDEN_SPEAR = add("golden_spear", new SpearItem(Tiers.GOLD, 2.0F, -2.7F, () -> CampanionEntities.GOLDEN_SPEAR, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final SpearItem DIAMOND_SPEAR = add("diamond_spear", new SpearItem(Tiers.DIAMOND, 2.0F, -2.7F, () -> CampanionEntities.DIAMOND_SPEAR, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT)));
	public static final SpearItem NETHERITE_SPEAR = add("netherite_spear", new SpearItem(Tiers.NETHERITE, 2.0F, -2.8F, () -> CampanionEntities.NETHERITE_SPEAR, new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).fireResistant()));

	public static final SkippingStoneItem SKIPPING_STONE = add("skipping_stone", new SkippingStoneItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

	public static final FlareItem FLARE = add("flare", new FlareItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

	public static final GrapplingHookItem GRAPPLING_HOOK = add("grappling_hook", new GrapplingHookItem(new Item.Properties().durability(120).tab(CreativeModeTab.TAB_TOOLS)));
	public static final SleepingBagItem SLEEPING_BAG = add("sleeping_bag", new SleepingBagItem(new Item.Properties().durability(250).tab(CreativeModeTab.TAB_TOOLS)));
	public static final TentBagItem TENT_BAG = add("tent_bag", new TentBagItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));
	public static final TentItem SMALL_UNBUILT_TENT = add("small_unbuilt_tent", new TentItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1), "small"));
	public static final TentItem LARGE_UNBUILT_TENT = add("large_unbuilt_tent", new TentItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1), "large"));

	private static <I extends Item> I add(String name, I item) {
		ITEMS.put(new ResourceLocation(Campanion.MOD_ID, name), item);
		return item;
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
