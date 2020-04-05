package com.campanion.data;

import com.campanion.item.CampanionItems;
import com.campanion.tag.CampanionBlockTags;
import com.campanion.tag.CampanionItemTags;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.AbstractTagProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.List;

public class CampanionItemTagsProvider extends AbstractTagProvider<Item> {
	private static final Logger LOG = LogManager.getLogger();

	public CampanionItemTagsProvider(DataGenerator dataGenerator) {
		super(dataGenerator, Registry.ITEM);
	}

	protected void configure() {
		this.copy(CampanionBlockTags.LAWN_CHAIRS, CampanionItemTags.LAWN_CHAIRS);
		this.copy(CampanionBlockTags.TENT_SIDES, CampanionItemTags.TENT_SIDES);
		this.copy(CampanionBlockTags.TENT_TOPS, CampanionItemTags.TENT_TOPS);
		this.copy(CampanionBlockTags.TOPPED_TENT_POLES, CampanionItemTags.TOPPED_TENT_POLES);
		this.copy(CampanionBlockTags.FLAT_TENT_TOPS, CampanionItemTags.FLAT_TENT_TOPS);
		this.copy(CampanionBlockTags.TENT_POLES, CampanionItemTags.TENT_POLES);
		this.getOrCreateTagBuilder(CampanionItemTags.MARSHMALLOWS).add(CampanionItems.MARSHMALLOW, CampanionItems.COOKED_MARSHMALLOW, CampanionItems.BLACKENED_MARSHMALLOW);
		this.getOrCreateTagBuilder(CampanionItemTags.MARSHMALLOWS_ON_STICKS).add(CampanionItems.MARSHMALLOW_ON_A_STICK, CampanionItems.COOKED_MARSHMALLOW_ON_A_STICK, CampanionItems.BLACKENED_MARSHMALLOW_ON_A_STICK);
		this.getOrCreateTagBuilder(CampanionItemTags.SPEARS).add(CampanionItems.WOODEN_SPEAR, CampanionItems.STONE_SPEAR, CampanionItems.IRON_SPEAR, CampanionItems.GOLDEN_SPEAR, CampanionItems.DIAMOND_SPEAR);
		this.getOrCreateTagBuilder(CampanionItemTags.BACKPACKS).add(CampanionItems.DAY_PACK, CampanionItems.CAMPING_PACK, CampanionItems.HIKING_PACK);
		this.getOrCreateTagBuilder(CampanionItemTags.FRUITS).add(Items.APPLE, Items.CHORUS_FRUIT, Items.MELON_SLICE, Items.SWEET_BERRIES);
		this.getOrCreateTagBuilder(CampanionItemTags.GRAINS).add(Items.BREAD, Items.CAKE, Items.COOKIE, CampanionItems.CRACKER);
		this.getOrCreateTagBuilder(CampanionItemTags.PROTEINS).add(Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_COD, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_RABBIT, Items.COOKED_SALMON);
		this.getOrCreateTagBuilder(CampanionItemTags.VEGETABLES).add(Items.BEETROOT, Items.CARROT, Items.POTATO, Items.BAKED_POTATO);
		this.getOrCreateTagBuilder(CampanionItemTags.MRE_COMPONENTS).add(CampanionItemTags.FRUITS).add(CampanionItemTags.GRAINS).add(CampanionItemTags.PROTEINS).add(CampanionItemTags.VEGETABLES);
	}

	protected void copy(Tag<Block> tag, Tag<Item> tag2) {
		Tag.Builder<Item> builder = this.getOrCreateTagBuilder(tag2);

		for (Tag.Entry<Block> entry : tag.entries()) {
			Tag.Entry<Item> entry2 = this.convert(entry);
			builder.add(entry2);
		}

	}

	private Tag.Entry<Item> convert(Tag.Entry<Block> entry) {
		if (entry instanceof Tag.TagEntry) {
			return new Tag.TagEntry<>(((Tag.TagEntry<Block>) entry).getId());
		} else if (entry instanceof Tag.CollectionEntry) {
			List<Item> list = Lists.newArrayList();

			for (Block block : ((Tag.CollectionEntry<Block>) entry).getValues()) {
				Item item = block.asItem();
				if (item == Items.AIR) {
					LOG.warn("Itemless block copied to item tag: {}", Registry.BLOCK.getId(block));
				} else {
					list.add(item);
				}
			}

			return new Tag.CollectionEntry<>(list);
		} else {
			throw new UnsupportedOperationException("Unknown tag entry " + entry);
		}
	}

	protected Path getOutput(Identifier identifier) {
		return this.root.getOutput().resolve("data/" + identifier.getNamespace() + "/tags/items/" + identifier.getPath() + ".json");
	}

	public String getName() {
		return "Campanion Item Tags";
	}

	protected void setContainer(TagContainer<Item> tagContainer) {
		ItemTags.setContainer(tagContainer);
	}
}
