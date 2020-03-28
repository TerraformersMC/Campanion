package com.campanion.item;

import com.campanion.Campanion;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class CampanionItems {

	private static final Map<Identifier, Item> ITEMS = new HashMap<>();

	public static final Item ROPE = add("rope", new Item(new Item.Settings().group(ItemGroup.MATERIALS)));
	public static final Item MRE = add("mre", new Item(new Item.Settings().group(ItemGroup.FOOD).food(new FoodComponent.Builder().hunger(8).saturationModifier(0.8F).build())));
	public static final Item DAY_PACK = add("day_pack", new BackpackItem(BackpackItem.Type.DAY_PACK, new Item.Settings().group(ItemGroup.TOOLS)));
	public static final Item CAMPING_PACK = add("camping_pack", new BackpackItem(BackpackItem.Type.CAMPING_PACK, new Item.Settings().group(ItemGroup.TOOLS)));
	public static final Item HIKING_PACK = add("hiking_pack", new BackpackItem(BackpackItem.Type.HIKING_PACK, new Item.Settings().group(ItemGroup.TOOLS)));

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
