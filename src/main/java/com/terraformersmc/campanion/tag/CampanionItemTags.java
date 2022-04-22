package com.terraformersmc.campanion.tag;

import com.terraformersmc.campanion.Campanion;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CampanionItemTags {
	public static final TagKey<Item> MARSHMALLOWS = get("marshmallows");
	public static final TagKey<Item> MELTED_MARSHMALLOWS = get("melted_marshmallows");
	public static final TagKey<Item> MARSHMALLOWS_ON_STICKS = get("marshmallows_on_sticks");
	public static final TagKey<Item> SPEARS = get("spears");
	public static final TagKey<Item> BACKPACKS = get("backpacks");
	public static final TagKey<Item> LAWN_CHAIRS = get("lawn_chairs");

	public static final TagKey<Item> TENT_SIDES = get("tent_sides");
	public static final TagKey<Item> TENT_TOPS = get("tent_tops");
	public static final TagKey<Item> FLAT_TENT_TOPS = get("flat_tent_tops");
	public static final TagKey<Item> TOPPED_TENT_POLES = get("topped_tent_poles");
	public static final TagKey<Item> TENT_POLES = get("tent_poles");

	public static final TagKey<Item> FRUITS = get("fruits");
	public static final TagKey<Item> GRAINS = get("grains");
	public static final TagKey<Item> PROTEINS = get("proteins");
	public static final TagKey<Item> VEGETABLES = get("vegetables");
	public static final TagKey<Item> MRE_COMPONENTS = get("mre_components");

	private static TagKey<Item> get(String id) {
		return TagKey.of(Registry.ITEM_KEY, new Identifier(Campanion.MOD_ID, id));
	}

	public static void load() {
	}
}
