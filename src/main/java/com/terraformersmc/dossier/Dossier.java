package com.terraformersmc.dossier;

import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import com.terraformersmc.dossier.data.DossierBlockTagsProvider;
import com.terraformersmc.dossier.data.DossierItemTagsProvider;
import com.terraformersmc.dossier.data.DossierLootTablesProvider;
import com.terraformersmc.dossier.data.DossierRecipesProvider;
import com.terraformersmc.dossier.util.CommonValues;
import com.terraformersmc.dossier.util.TransformerFunction;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.server.AbstractTagProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.LootConditionConsumingBuilder;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

public abstract class Dossier<P extends DataProvider & Consumer<F>, F> {

	protected P provider;

	/**
	 * Create data from your Dossiers
	 *
	 * @param modId    Your mod's mod id.
	 * @param enabled  Must be true in order to run
	 * @param dossiers Your dossiers describing what data to generate
	 */
	public static void generateData(String modId, boolean enabled, Dossier.Builder dossiers) {
		if (FabricLoader.getInstance().isDevelopmentEnvironment() && enabled) {
			dossiers.blockTagsDossiers.forEach(dossier -> dossier.run(modId));
			dossiers.itemTagsDossiers.forEach(dossier -> dossier.run(modId));
			dossiers.recipesDossiers.forEach(dossier -> dossier.run(modId));
			dossiers.lootTablesDossiers.forEach(dossier -> dossier.run(modId));
			try {
				DATA_GENERATORS.get(modId).run();
			} catch (IOException | NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	private static final Map<String, EnumMap<Type, DataProvider>> DATA_PROVIDERS = new HashMap<>();
	private static final Map<String, DataGenerator> DATA_GENERATORS = new HashMap<>();

	protected void run(String modId) {
		this.setProvider(modId);
		this.getProvider().accept(this.getCustomFunction());
	}

	protected abstract void setProvider(String modId);

	public P getProvider() {
		return this.provider;
	}

	public abstract F getCustomFunction();

	private static DataProvider getProvider(String modId, Type type) {
		if (!DATA_PROVIDERS.containsKey(modId)) {
			Path output = Paths.get("dossier_generated/" + modId);
			DataGenerator generator = new DataGenerator(output, Collections.emptyList());
			EnumMap<Type, DataProvider> providers = Maps.newEnumMap(Type.class);

			DossierBlockTagsProvider blockTagsProvider = new DossierBlockTagsProvider(generator);
			providers.put(Type.BLOCK_TAGS, blockTagsProvider);
			providers.put(Type.ITEM_TAGS, new DossierItemTagsProvider(generator, blockTagsProvider));
			providers.put(Type.RECIPES, new DossierRecipesProvider(generator));
			providers.put(Type.LOOT_TABLES, new DossierLootTablesProvider(generator));

			providers.forEach((t, provider) -> generator.install(provider));
			DATA_GENERATORS.put(modId, generator);
			DATA_PROVIDERS.put(modId, providers);
		}
		return DATA_PROVIDERS.get(modId).get(type);
	}

	public static abstract class BlockTagsDossier extends Dossier<DossierBlockTagsProvider, Runnable> implements CommonValues {

		protected AbstractTagProvider.ObjectBuilder<Block> get(Tag.Identified<Block> tag) {
			return this.provider.getOrCreateTagBuilder(tag);
		}

		protected AbstractTagProvider.ObjectBuilder<Block> add(Tag.Identified<Block> tag, Block... blocks) {
			AbstractTagProvider.ObjectBuilder<Block> builder = this.provider.getOrCreateTagBuilder(tag);
			return builder.add(blocks);
		}

		protected AbstractTagProvider.ObjectBuilder<Block> add(Tag.Identified<Block> tag, Tag.Identified<Block>... tags) {
			AbstractTagProvider.ObjectBuilder<Block> builder = this.get(tag);
			for (Tag.Identified<Block> blockTag : tags) {
				builder.addTag(blockTag);
			}
			return builder;
		}

		protected AbstractTagProvider.ObjectBuilder<Block> addTransformed(Tag.Identified<Block> tag, TransformerFunction<String, String> transformer, String namespace, String pathTemplate, String... args) {
			return this.add(tag, transformer.apply(pathTemplate, args).stream().map(transformeedPath -> Registry.BLOCK.get(new Identifier(namespace, transformeedPath))).toArray(Block[]::new));
		}

		protected AbstractTagProvider.ObjectBuilder<Block> addReplaceTransformed(Tag.Identified<Block> tag, String namespace, String pathTemplate, String key, String... values) {
			return this.addTransformed(tag, TransformerFunction.REPLACE_TRANSFORMER, namespace, pathTemplate, ArrayUtils.add(values, 0, key));
		}

		@Override
		protected void setProvider(String modId) {
			this.provider = (DossierBlockTagsProvider) Dossier.getProvider(modId, Type.BLOCK_TAGS);
		}

		protected abstract void addBlockTags();

		@Override
		public Runnable getCustomFunction() {
			return this::addBlockTags;
		}
	}

	public static abstract class ItemTagsDossier extends Dossier<DossierItemTagsProvider, Runnable> implements CommonValues {

		protected AbstractTagProvider.ObjectBuilder<Item> get(Tag.Identified<Item> tag) {
			return this.provider.getOrCreateTagBuilder(tag);
		}

		protected void copyFromBlock(Tag.Identified<Item> tag, Tag.Identified<Block> blockTag) {
			this.provider.copy(blockTag, tag);
		}

		protected AbstractTagProvider.ObjectBuilder<Item> add(Tag.Identified<Item> tag, Item... items) {
			return this.get(tag).add(items);
		}

		protected AbstractTagProvider.ObjectBuilder<Item> add(Tag.Identified<Item> tag, Tag.Identified<Item>... tags) {
			AbstractTagProvider.ObjectBuilder<Item> builder = this.get(tag);
			for (Tag.Identified<Item> itemTag : tags) {
				builder.addTag(itemTag);
			}
			return builder;
		}

		protected AbstractTagProvider.ObjectBuilder<Item> addTransformed(Tag.Identified<Item> tag, TransformerFunction<String, String> transformer, String namespace, String pathTemplate, String... args) {
			return this.add(tag, transformer.apply(pathTemplate, args).stream().map(transformeedPath -> Registry.ITEM.get(new Identifier(namespace, transformeedPath))).toArray(Item[]::new));
		}

		protected AbstractTagProvider.ObjectBuilder<Item> addReplaceTransformed(Tag.Identified<Item> tag, String namespace, String pathTemplate, String key, String... values) {
			return this.addTransformed(tag, TransformerFunction.REPLACE_TRANSFORMER, namespace, pathTemplate, ArrayUtils.add(values, 0, key));
		}

		@Override
		protected void setProvider(String modId) {
			this.provider = (DossierItemTagsProvider) Dossier.getProvider(modId, Type.ITEM_TAGS);
		}

		protected abstract void addItemTags();

		@Override
		public Runnable getCustomFunction() {
			return this::addItemTags;
		}
	}

	public static abstract class RecipesDossier extends Dossier<DossierRecipesProvider, Consumer<Consumer<RecipeJsonProvider>>> {

		protected InventoryChangedCriterion.Conditions conditionsFrom(ItemConvertible item) {
			return this.conditionsFrom(ItemPredicate.Builder.create().item(item).build());
		}

		protected InventoryChangedCriterion.Conditions conditionsFrom(Tag<Item> tag) {
			return this.conditionsFrom(this.itemPredicateOf(tag));
		}

		protected ItemPredicate itemPredicateOf(Tag<Item> tag) {
			return ItemPredicate.Builder.create().tag(tag).build();
		}

		protected InventoryChangedCriterion.Conditions conditionsFrom(ItemPredicate... items) {
			return new InventoryChangedCriterion.Conditions(NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, items);
		}

		@Override
		protected void setProvider(String modId) {
			this.provider = (DossierRecipesProvider) Dossier.getProvider(modId, Type.RECIPES);
		}

		protected abstract void addRecipes(Consumer<RecipeJsonProvider> provider);

		@Override
		public Consumer<Consumer<RecipeJsonProvider>> getCustomFunction() {
			return this::addRecipes;
		}
	}

	public static abstract class LootTablesDossier extends Dossier<DossierLootTablesProvider, Runnable> {

		@Override
		protected void setProvider(String modId) {
			this.provider = (DossierLootTablesProvider) Dossier.getProvider(modId, Type.LOOT_TABLES);
		}

		protected abstract void addLootTables();

		protected void addDrop(Block block, ItemConvertible loot) {
			this.addDrop(block, createBlockLootTable(loot));
		}

		protected void addSelfDrop(Block block) {
			this.addDrop(block, block);
		}

		protected void addDrop(Block block, LootTable.Builder builder) {
			List<Pair<Identifier, LootTable.Builder>> lootTables = this.provider.lootTables.get(LootContextTypes.BLOCK);
			lootTables.add(new Pair<>(block.getDropTableID(), builder));
			this.provider.lootTables.put(LootContextTypes.BLOCK, lootTables);
		}

		protected static <T> T addDefaultConditions(LootConditionConsumingBuilder<T> conditions) {
			return conditions.withCondition(SurvivesExplosionLootCondition.builder());
		}

		protected static LootTable.Builder createBlockLootTable(ItemConvertible item) {
			return LootTable.builder().withPool(addDefaultConditions(LootPool.builder().withRolls(ConstantLootTableRange.create(1)).withEntry(ItemEntry.builder(item))));
		}

		@Override
		public Runnable getCustomFunction() {
			return this::addLootTables;
		}
	}

	public static class Builder {
		public List<ItemTagsDossier> itemTagsDossiers = new LinkedList<>();
		public List<BlockTagsDossier> blockTagsDossiers = new LinkedList<>();
		public List<RecipesDossier> recipesDossiers = new LinkedList<>();
		public List<LootTablesDossier> lootTablesDossiers = new LinkedList<>();

		private Builder() {
		}

		public static Builder create() {
			return new Builder();
		}

		public Builder add(ItemTagsDossier dossier) {
			this.itemTagsDossiers.add(dossier);
			return this;
		}

		public Builder add(BlockTagsDossier dossier) {
			this.blockTagsDossiers.add(dossier);
			return this;
		}

		public Builder add(RecipesDossier dossier) {
			this.recipesDossiers.add(dossier);
			return this;
		}

		public Builder add(LootTablesDossier dossier) {
			this.lootTablesDossiers.add(dossier);
			return this;
		}
	}

	private enum Type {
		BLOCK_TAGS,
		ITEM_TAGS,
		RECIPES,
		LOOT_TABLES
	}

}
