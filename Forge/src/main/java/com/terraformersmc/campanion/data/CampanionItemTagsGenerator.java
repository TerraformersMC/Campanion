package com.terraformersmc.campanion.data;

import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.tag.CampanionBlockTags;
import com.terraformersmc.campanion.tag.CampanionItemTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;

public class CampanionItemTagsGenerator extends ItemTagsProvider {
	public CampanionItemTagsGenerator(DataGenerator generator, BlockTagsProvider blockTagsProvider) {
		super(generator, blockTagsProvider);
	}

	@Override
	protected void addTags() {
		this.copy(CampanionBlockTags.LAWN_CHAIRS, CampanionItemTags.LAWN_CHAIRS);
		this.copy(CampanionBlockTags.TENT_SIDES, CampanionItemTags.TENT_SIDES);
		this.copy(CampanionBlockTags.TENT_TOPS, CampanionItemTags.TENT_TOPS);
		this.copy(CampanionBlockTags.TOPPED_TENT_POLES, CampanionItemTags.TOPPED_TENT_POLES);
		this.copy(CampanionBlockTags.FLAT_TENT_TOPS, CampanionItemTags.FLAT_TENT_TOPS);
		this.copy(CampanionBlockTags.TENT_POLES, CampanionItemTags.TENT_POLES);

		this.tag(CampanionItemTags.MELTED_MARSHMALLOWS).add(CampanionItems.COOKED_MARSHMALLOW, CampanionItems.BLACKENED_MARSHMALLOW);
		this.tag(CampanionItemTags.MARSHMALLOWS).add(CampanionItems.MARSHMALLOW).addTag(CampanionItemTags.MELTED_MARSHMALLOWS);
		this.tag(CampanionItemTags.MARSHMALLOWS_ON_STICKS).add(CampanionItems.MARSHMALLOW_ON_A_STICK, CampanionItems.COOKED_MARSHMALLOW_ON_A_STICK, CampanionItems.BLACKENED_MARSHMALLOW_ON_A_STICK);
		this.tag(CampanionItemTags.SPEARS).add(CampanionItems.WOODEN_SPEAR, CampanionItems.STONE_SPEAR, CampanionItems.IRON_SPEAR, CampanionItems.GOLDEN_SPEAR, CampanionItems.DIAMOND_SPEAR, CampanionItems.NETHERITE_SPEAR);
		this.tag(CampanionItemTags.BACKPACKS).add(CampanionItems.DAY_PACK, CampanionItems.CAMPING_PACK, CampanionItems.HIKING_PACK);
		this.tag(CampanionItemTags.FRUITS).add(Items.APPLE, Items.CHORUS_FRUIT, Items.MELON_SLICE, Items.SWEET_BERRIES);
		this.tag(CampanionItemTags.GRAINS).add(Items.BREAD, Items.CAKE, Items.COOKIE, CampanionItems.CRACKER);
		this.tag(CampanionItemTags.PROTEINS).add(Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_COD, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_RABBIT, Items.COOKED_SALMON);
		this.tag(CampanionItemTags.VEGETABLES).add(Items.BEETROOT, Items.CARROT, Items.POTATO, Items.BAKED_POTATO);
		this.tag(CampanionItemTags.MRE_COMPONENTS).addTag(CampanionItemTags.FRUITS).addTag(CampanionItemTags.GRAINS).addTag(CampanionItemTags.PROTEINS).addTag(CampanionItemTags.VEGETABLES);

	}
}
