package com.campanion.item;

import com.campanion.Campanion;
import com.campanion.entity.CampanionEntities;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class CampanionItems {

	private static final Map<Identifier, Item> ITEMS = new LinkedHashMap<>();

	public static final Item MRE = add("mre", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(20).saturationModifier(24.8F).build())));
	public static final Item ROPE = add("rope", new Item(new Item.Settings().group(ItemGroup.MATERIALS)));
	public static final Item LEATHER_POUCH = add("leather_pouch", new Item(new Item.Settings().group(ItemGroup.MATERIALS)));
	public static final Item TANNED_LEATHER_POUCH = add("tanned_leather_pouch", new Item(new Item.Settings().group(ItemGroup.MATERIALS)));
	public static final Item WOODEN_ROD = add("wooden_rod", new Item(new Item.Settings().group(ItemGroup.MATERIALS)));
	public static final Item WOOL_TARP = add("wool_tarp", new Item(new Item.Settings().group(ItemGroup.MATERIALS)));

	public static final Item TANNED_LEATHER = add("tanned_leather", new Item(new Item.Settings().group(ItemGroup.MATERIALS)));

	public static final Item CRACKER = add("cracker", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).build())));
	public static final Item MARSHMALLOW = add("marshmallow", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).build())));
	public static final Item COOKED_MARSHMALLOW = add("cooked_marshmallow", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).build())));
	public static final Item BLACKENED_MARSHMALLOW = add("blackened_marshmallow", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(1).build())));
	public static final Item MARSHMALLOW_ON_A_STICK = add("marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.MISC), MARSHMALLOW));
	public static final Item COOKED_MARSHMALLOW_ON_A_STICK = add("cooked_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.MISC), COOKED_MARSHMALLOW));
	public static final Item BLACKENED_MARSHMALLOW_ON_A_STICK = add("blackened_marshmallow_on_a_stick", new MarshmallowOnAStickItem(new Item.Settings().group(ItemGroup.MISC), BLACKENED_MARSHMALLOW));
	public static final Item SMORE = add("smore", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(3).build())));

	public static final BackpackItem DAY_PACK = add("day_pack", new BackpackItem(BackpackItem.Type.DAY_PACK, new Item.Settings().group(ItemGroup.TOOLS)));
	public static final BackpackItem CAMPING_PACK = add("camping_pack", new BackpackItem(BackpackItem.Type.CAMPING_PACK, new Item.Settings().group(ItemGroup.TOOLS)));
	public static final BackpackItem HIKING_PACK = add("hiking_pack", new BackpackItem(BackpackItem.Type.HIKING_PACK, new Item.Settings().group(ItemGroup.TOOLS)));

	public static final SpearItem WOODEN_SPEAR = add("wooden_spear", new SpearItem(ToolMaterials.WOOD, 1, -3.2F, () -> CampanionEntities.WOODEN_SPEAR, new Item.Settings().group(ItemGroup.COMBAT)));
	public static final SpearItem STONE_SPEAR = add("stone_spear", new SpearItem(ToolMaterials.STONE, 1, -3.2F, () -> CampanionEntities.STONE_SPEAR, new Item.Settings().group(ItemGroup.COMBAT)));
	public static final SpearItem IRON_SPEAR = add("iron_spear", new SpearItem(ToolMaterials.IRON, 1, -3.1F, () -> CampanionEntities.IRON_SPEAR, new Item.Settings().group(ItemGroup.COMBAT)));
	public static final SpearItem GOLDEN_SPEAR = add("golden_spear", new SpearItem(ToolMaterials.GOLD, 1, -3.0F, () -> CampanionEntities.GOLDEN_SPEAR, new Item.Settings().group(ItemGroup.COMBAT)));
	public static final SpearItem DIAMOND_SPEAR = add("diamond_spear", new SpearItem(ToolMaterials.DIAMOND, 1, -3.0F, () -> CampanionEntities.DIAMOND_SPEAR, new Item.Settings().group(ItemGroup.COMBAT)));

	public static final SkippingStoneItem SKIPPING_STONE = add("skipping_stone", new SkippingStoneItem(new Item.Settings().group(ItemGroup.MISC)));

	public static final GrapplingHookItem GRAPPLING_HOOK = add("grappling_hook", new GrapplingHookItem(new Item.Settings().maxDamage(250).group(ItemGroup.TOOLS)));
	public static final SleepingBagItem SLEEPING_BAG = add("sleeping_bag", new SleepingBagItem(new Item.Settings().maxDamage(250).group(ItemGroup.TOOLS)));
	public static final TentBagItem TENT_BAG = add("tent_bag", new TentBagItem(new Item.Settings().group(ItemGroup.TOOLS)));
	public static final UnbuiltTent SMALL_UNBUILT_TENT = add("small_unbuilt_tent", new UnbuiltTent(new Item.Settings().group(ItemGroup.TOOLS), "small"));
	public static final UnbuiltTent LARGE_UNBUILT_TENT = add("large_unbuilt_tent", new UnbuiltTent(new Item.Settings().group(ItemGroup.TOOLS), "large"));

	private static <I extends Item> I add(String name, I item) {
		ITEMS.put(new Identifier(Campanion.MOD_ID, name), item);
		return item;
	}

	public static void register() {
		for (Identifier id : ITEMS.keySet()) {
			Registry.register(Registry.ITEM, id, ITEMS.get(id));
		}
	}

}
