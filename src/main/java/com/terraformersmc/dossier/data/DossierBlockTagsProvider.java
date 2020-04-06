package com.terraformersmc.dossier.data;

import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.server.BlockTagsProvider;
import net.minecraft.tag.Tag;

import java.util.function.Consumer;

public class DossierBlockTagsProvider extends BlockTagsProvider implements Consumer<Runnable> {

	private Runnable onConfigure;

	public DossierBlockTagsProvider(DataGenerator generator) {
		super(generator);
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
	public Tag.Builder<Block> getOrCreateTagBuilder(Tag<Block> tag) {
		return super.getOrCreateTagBuilder(tag);
	}

	@Override
	public String getName() {
		return "Dossier Block Tags";
	}
}
