package com.campanion.item;

import com.campanion.Campanion;
import com.campanion.entity.CampanionEntities;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class CampanionItems {

	private static final Map<Identifier, Item> ITEMS = new HashMap<>();

	public static final Item ROPE = add("rope", new Item(new Item.Settings().group(ItemGroup.MATERIALS)));
	public static final Item MRE = add("mre", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(8).saturationModifier(0.8F).build())));

	public static final BackpackItem DAY_PACK = add("day_pack", new BackpackItem(BackpackItem.Type.DAY_PACK, new Item.Settings().group(ItemGroup.TOOLS)));
	public static final BackpackItem CAMPING_PACK = add("camping_pack", new BackpackItem(BackpackItem.Type.CAMPING_PACK, new Item.Settings().group(ItemGroup.TOOLS)));
	public static final BackpackItem HIKING_PACK = add("hiking_pack", new BackpackItem(BackpackItem.Type.HIKING_PACK, new Item.Settings().group(ItemGroup.TOOLS)));

	public static final SpearItem WOODEN_SPEAR = add("wooden_spear", new SpearItem(ToolMaterials.WOOD, 3, -3.2F, () -> CampanionEntities.WOODEN_SPEAR, new Item.Settings().group(ItemGroup.COMBAT)));
	public static final SpearItem STONE_SPEAR = add("stone_spear", new SpearItem(ToolMaterials.STONE, 3, -3.2F, () -> CampanionEntities.STONE_SPEAR, new Item.Settings().group(ItemGroup.COMBAT)));
	public static final SpearItem IRON_SPEAR = add("iron_spear", new SpearItem(ToolMaterials.IRON, 3, -3.1F, () -> CampanionEntities.IRON_SPEAR, new Item.Settings().group(ItemGroup.COMBAT)));
	public static final SpearItem GOLDEN_SPEAR = add("golden_spear", new SpearItem(ToolMaterials.GOLD, 3, -3.0F, () -> CampanionEntities.GOLDEN_SPEAR, new Item.Settings().group(ItemGroup.COMBAT)));
	public static final SpearItem DIAMOND_SPEAR = add("diamond_spear", new SpearItem(ToolMaterials.DIAMOND, 3, -3.0F, () -> CampanionEntities.DIAMOND_SPEAR, new Item.Settings().group(ItemGroup.COMBAT)));

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
