package com.campanion.tag;

import com.campanion.Campanion;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;

public class CampanionItemTags {
	public static final Tag<Item> MARSHMALLOWS = get("marshmallows");
	public static final Tag<Item> MELTED_MARSHMALLOWS = get("melted_marshmallows");
	public static final Tag<Item> MARSHMALLOWS_ON_STICKS = get("marshmallows_on_sticks");
	public static final Tag<Item> SPEARS = get("spears");
	public static final Tag<Item> BACKPACKS = get("backpacks");
	public static final Tag<Item> LAWN_CHAIRS = get("lawn_chairs");

	public static final Tag<Item> TENT_SIDES = get("tent_sides");
	public static final Tag<Item> TENT_TOPS = get("tent_tops");
	public static final Tag<Item> FLAT_TENT_TOPS = get("flat_tent_tops");
	public static final Tag<Item> TOPPED_TENT_POLES = get("topped_tent_poles");
	public static final Tag<Item> TENT_POLES = get("tent_poles");

	public static final Tag<Item> FRUITS = get("fruits");
	public static final Tag<Item> GRAINS = get("grains");
	public static final Tag<Item> PROTEINS = get("proteins");
	public static final Tag<Item> VEGETABLES = get("vegetables");
	public static final Tag<Item> MRE_COMPONENTS = get("mre_components");

	private static Tag<Item> get(String id) {
		return TagRegistry.item(new Identifier(Campanion.MOD_ID, id));
	}
}
