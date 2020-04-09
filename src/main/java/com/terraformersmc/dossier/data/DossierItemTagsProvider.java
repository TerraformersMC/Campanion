package com.terraformersmc.dossier.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.AbstractTagProvider;
import net.minecraft.data.server.BlockTagsProvider;
import net.minecraft.data.server.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.tag.Tag;

import java.util.function.Consumer;

public class DossierItemTagsProvider extends ItemTagsProvider implements Consumer<Runnable> {

	private Runnable onConfigure;

	public DossierItemTagsProvider(DataGenerator generator, BlockTagsProvider blockTagsProvider) {
		super(generator, blockTagsProvider);
	}

	@Override
	protected void configure() {
		super.configure();
		this.onConfigure.run();
	}

	@Override
	public void accept(Runnable onConfigure) {
		this.onConfigure = onConfigure;
	}

	@Override
	public AbstractTagProvider.ObjectBuilder<Item> getOrCreateTagBuilder(Tag.Identified<Item> tag) {
		return super.getOrCreateTagBuilder(tag);
	}

	@Override
	public void copy(Tag.Identified<Block> block, Tag.Identified<Item> item) {
		super.copy(block, item);
	}

	@Override
	public String getName() {
		return "Dossier Item Tags";
	}
}
