package com.campanion.data;

import com.campanion.block.CampanionBlocks;
import com.campanion.item.CampanionItems;
import com.campanion.recipe.CampanionRecipeSerializers;
import com.campanion.tag.CampanionItemTags;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.server.recipe.ComplexRecipeJsonFactory;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class CampanionRecipesProvider implements DataProvider {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	private final DataGenerator root;

	public CampanionRecipesProvider(DataGenerator dataGenerator) {
		this.root = dataGenerator;
	}

	@Override
	public void run(DataCache dataCache) {
		Path path = this.root.getOutput();
		Set<Identifier> set = Sets.newHashSet();
		this.generate((provider) -> {
			if (!set.add(provider.getRecipeId())) {
				throw new IllegalStateException("Duplicate recipe " + provider.getRecipeId());
			} else {
				this.saveRecipe(dataCache, provider.toJson(), path.resolve("data/" + provider.getRecipeId().getNamespace() + "/recipes/" + provider.getRecipeId().getPath() + ".json"));
				JsonObject advancementJson = provider.toAdvancementJson();
				if (advancementJson != null) {
					this.saveRecipeAdvancement(dataCache, advancementJson, path.resolve("data/" + provider.getRecipeId().getNamespace() + "/advancements/" + provider.getAdvancementId().getPath() + ".json"));
				}

			}
		});
		this.saveRecipeAdvancement(dataCache, Advancement.Task.create().criterion("impossible", new ImpossibleCriterion.Conditions()).toJson(), path.resolve("data/minecraft/advancements/recipes/root.json"));
	}

	@SuppressWarnings("UnstableApiUsage")
	private void saveRecipe(DataCache dataCache, JsonObject jsonObject, Path path) {
		try {
			String json = GSON.toJson(jsonObject);
			String string2 = SHA1.hashUnencodedChars(json).toString();
			if (!Objects.equals(dataCache.getOldSha1(path), string2) || !Files.exists(path)) {
				Files.createDirectories(path.getParent());
				BufferedWriter bufferedWriter = Files.newBufferedWriter(path);
				Throwable throwable = null;

				try {
					bufferedWriter.write(json);
				} catch (Throwable var17) {
					throwable = var17;
					throw var17;
				} finally {
					if (throwable != null) {
						try {
							bufferedWriter.close();
						} catch (Throwable var16) {
							throwable.addSuppressed(var16);
						}
					} else {
						bufferedWriter.close();
					}

				}
			}

			dataCache.updateSha1(path, string2);
		} catch (IOException var19) {
			LOGGER.error("Couldn't save recipe {}", path, var19);
		}

	}

	@SuppressWarnings("UnstableApiUsage")
	private void saveRecipeAdvancement(DataCache dataCache, JsonObject jsonObject, Path path) {
		try {
			String json = GSON.toJson(jsonObject);
			String hash = SHA1.hashUnencodedChars(json).toString();
			if (!Objects.equals(dataCache.getOldSha1(path), hash) || !Files.exists(path)) {
				Files.createDirectories(path.getParent());
				BufferedWriter writer = Files.newBufferedWriter(path);
				Throwable throwable = null;

				try {
					writer.write(json);
				} catch (Throwable var17) {
					throwable = var17;
					throw var17;
				} finally {
					if (throwable != null) {
						try {
							writer.close();
						} catch (Throwable var16) {
							throwable.addSuppressed(var16);
						}
					} else {
						writer.close();
					}

				}
			}

			dataCache.updateSha1(path, hash);
		} catch (IOException e) {
			LOGGER.error("Couldn't save recipe advancement {}", path, e);
		}

	}

	private void generate(Consumer<RecipeJsonProvider> consumer) {
		ShapedRecipeJsonFactory.create(CampanionItems.MARSHMALLOW, 2).input('S', Items.SUGAR).pattern("SS").pattern("SS").criterion("has_sugar", this.conditionsFrom(Items.SUGAR)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionItems.MARSHMALLOW_ON_A_STICK).input(CampanionItems.MARSHMALLOW).input(Items.STICK).criterion("has_marshmallow", this.conditionsFrom(CampanionItems.MARSHMALLOW)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionItems.MRE).input(CampanionItemTags.PROTEINS).input(CampanionItemTags.FRUITS).input(CampanionItemTags.GRAINS).input(CampanionItemTags.VEGETABLES).criterion("has_mre_components", this.conditionsFrom(CampanionItemTags.MRE_COMPONENTS)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionItems.CRACKER, 4).input('W', Items.WHEAT).pattern("WW").criterion("has_wheat", this.conditionsFrom(Items.WHEAT)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionItems.SMORE).input(CampanionItems.CRACKER).input(CampanionItemTags.MELTED_MARSHMALLOWS).input(Items.COCOA_BEANS).input(CampanionItems.CRACKER).criterion("has_marshmallow", this.conditionsFrom(CampanionItemTags.MARSHMALLOWS)).offerTo(consumer);

		ShapedRecipeJsonFactory.create(CampanionItems.ROPE).input('S', Items.STRING).pattern("SSS").criterion("has_string", this.conditionsFrom(Items.STRING)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionBlocks.ROPE_BRIDGE_POST, 2).input('L', ItemTags.LOGS).pattern("L L").pattern("L L").criterion("has_rope", this.conditionsFrom(CampanionItems.ROPE)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionBlocks.ROPE_LADDER, 16).input('R', CampanionItems.ROPE).input('S', Items.STICK).pattern("RSR").pattern("RSR").pattern("RSR").criterion("has_string", this.conditionsFrom(Items.STRING)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionItems.SKIPPING_STONE, 8).input(Blocks.COBBLESTONE).criterion("has_cobblestone", this.conditionsFrom(Blocks.COBBLESTONE)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionItems.SLEEPING_BAG).input('T', CampanionItems.WOOL_TARP).pattern("TT").criterion("has_wool", this.conditionsFrom(ItemTags.WOOL)).offerTo(consumer);

		ShapedRecipeJsonFactory.create(CampanionItems.WOODEN_ROD).input('S', Items.STICK).pattern("S").pattern("S").pattern("S").criterion("has_stick", this.conditionsFrom(Items.STICK)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionItems.WOOL_TARP).input('W', ItemTags.WOOL).pattern("WW").pattern("WW").criterion("has_wool", this.conditionsFrom(ItemTags.WOOL)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionItems.SMALL_UNBUILT_TENT).input('T', CampanionItems.WOOL_TARP).input('S', CampanionItems.WOODEN_ROD).input('R', CampanionItems.ROPE).pattern(" T ").pattern("TST").pattern("R R").criterion("has_wool", this.conditionsFrom(ItemTags.WOOL)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionItems.LARGE_UNBUILT_TENT).input('T', CampanionItems.WOOL_TARP).input('S', CampanionItems.WOODEN_ROD).input('R', CampanionItems.ROPE).pattern("TTT").pattern("TST").pattern("RSR").criterion("has_wool", this.conditionsFrom(ItemTags.WOOL)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionItems.TENT_BAG).input('T', CampanionItems.TANNED_LEATHER).input('I', Items.IRON_INGOT).input('R', CampanionItems.ROPE).pattern("IRI").pattern("TTT").criterion("has_rope", this.conditionsFrom(CampanionItems.ROPE)).offerTo(consumer);

		ShapedRecipeJsonFactory.create(CampanionItems.LEATHER_POUCH).input('L', Items.LEATHER).input('S', Items.STRING).pattern("L L").pattern("SLS").criterion("has_leather", this.conditionsFrom(Items.LEATHER)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionItems.TANNED_LEATHER_POUCH).input('T', CampanionItems.TANNED_LEATHER).input('S', Items.STRING).pattern("T T").pattern("T T").pattern("STS").criterion("has_tanned_leather", this.conditionsFrom(CampanionItems.TANNED_LEATHER)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionBlocks.LEATHER_TANNER).input('S', Items.STICK).pattern("S S").pattern("S S").pattern("S S").criterion("has_stick", this.conditionsFrom(Items.STICK)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionItems.DAY_PACK).input('I', Items.IRON_INGOT).input('R', CampanionItems.ROPE).input('P', CampanionItems.LEATHER_POUCH).pattern(" I ").pattern("RPR").criterion("has_leather", this.conditionsFrom(Items.LEATHER)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionItems.CAMPING_PACK).input('I', Items.IRON_INGOT).input('R', CampanionItems.ROPE).input('P', CampanionItems.LEATHER_POUCH).input('T', CampanionItems.TANNED_LEATHER_POUCH).pattern(" I ").pattern("RTR").pattern(" P ").criterion("has_tanned_leather", this.conditionsFrom(CampanionItems.TANNED_LEATHER)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionItems.HIKING_PACK).input('I', Items.IRON_INGOT).input('R', CampanionItems.ROPE).input('T', CampanionItems.TANNED_LEATHER_POUCH).pattern(" I ").pattern("RTR").pattern("ITI").criterion("has_tanned_leather", this.conditionsFrom(CampanionItems.TANNED_LEATHER)).offerTo(consumer);

		ShapedRecipeJsonFactory.create(CampanionItems.GRAPPLING_HOOK).input('I', Items.IRON_INGOT).input('P', ItemTags.PLANKS).input('R', CampanionItems.ROPE).input('S', Items.STICK).pattern(" I ").pattern("PRI").pattern("SI ").criterion("has_grappling_hook", this.conditionsFrom(CampanionItems.GRAPPLING_HOOK)).offerTo(consumer);

		ShapedRecipeJsonFactory.create(CampanionItems.WOODEN_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', ItemTags.PLANKS).pattern("X").pattern("#").criterion("has_stick", this.conditionsFrom(Items.STICK)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionItems.STONE_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', Blocks.COBBLESTONE).pattern("X").pattern("#").criterion("has_stone", this.conditionsFrom(Blocks.COBBLESTONE)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionItems.IRON_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', Items.IRON_INGOT).pattern("X").pattern("#").criterion("has_iron", this.conditionsFrom(Items.IRON_INGOT)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionItems.GOLDEN_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', Items.GOLD_INGOT).pattern("X").pattern("#").criterion("has_gold", this.conditionsFrom(Items.GOLD_INGOT)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionItems.DIAMOND_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', Items.DIAMOND).pattern("X").pattern("#").criterion("has_diamond", this.conditionsFrom(Items.DIAMOND)).offerTo(consumer);

		ShapedRecipeJsonFactory.create(CampanionBlocks.WHITE_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.WHITE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_white_carpet", this.conditionsFrom(Blocks.WHITE_CARPET)).offerTo(consumer);
		ShapedRecipeJsonFactory.create(CampanionBlocks.ORANGE_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.ORANGE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_orange_carpet", this.conditionsFrom(Blocks.ORANGE_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.ORANGE_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.ORANGE_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:orange_lawn_chair_from_existing_chair");
		ShapedRecipeJsonFactory.create(CampanionBlocks.MAGENTA_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.MAGENTA_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_magenta_carpet", this.conditionsFrom(Blocks.MAGENTA_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.MAGENTA_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.MAGENTA_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:magenta_lawn_chair_from_existing_chair");
		ShapedRecipeJsonFactory.create(CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.LIGHT_BLUE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_light_blue_carpet", this.conditionsFrom(Blocks.LIGHT_BLUE_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.LIGHT_BLUE_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:light_blue_lawn_chair_from_existing_chair");
		ShapedRecipeJsonFactory.create(CampanionBlocks.YELLOW_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.YELLOW_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_yellow_carpet", this.conditionsFrom(Blocks.YELLOW_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.YELLOW_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.YELLOW_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:yellow_lawn_chair_from_existing_chair");
		ShapedRecipeJsonFactory.create(CampanionBlocks.LIME_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.LIME_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_lime_carpet", this.conditionsFrom(Blocks.LIME_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.LIME_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.LIME_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:lime_lawn_chair_from_existing_chair");
		ShapedRecipeJsonFactory.create(CampanionBlocks.PINK_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.PINK_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_pink_carpet", this.conditionsFrom(Blocks.PINK_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.PINK_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.PINK_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:pink_lawn_chair_from_existing_chair");
		ShapedRecipeJsonFactory.create(CampanionBlocks.GRAY_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.GRAY_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_gray_carpet", this.conditionsFrom(Blocks.GRAY_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.GRAY_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.GRAY_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:gray_lawn_chair_from_existing_chair");
		ShapedRecipeJsonFactory.create(CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.LIGHT_GRAY_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_light_gray_carpet", this.conditionsFrom(Blocks.LIGHT_GRAY_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.LIGHT_GRAY_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:light_gray_lawn_chair_from_existing_chair");
		ShapedRecipeJsonFactory.create(CampanionBlocks.CYAN_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.CYAN_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_cyan_carpet", this.conditionsFrom(Blocks.CYAN_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.CYAN_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.CYAN_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:cyan_lawn_chair_from_existing_chair");
		ShapedRecipeJsonFactory.create(CampanionBlocks.PURPLE_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.PURPLE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_purple_carpet", this.conditionsFrom(Blocks.PURPLE_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.PURPLE_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.PURPLE_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:purple_lawn_chair_from_existing_chair");
		ShapedRecipeJsonFactory.create(CampanionBlocks.BLUE_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.BLUE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_blue_carpet", this.conditionsFrom(Blocks.BLUE_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.BLUE_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.BLUE_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:blue_lawn_chair_from_existing_chair");
		ShapedRecipeJsonFactory.create(CampanionBlocks.BROWN_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.BROWN_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_brown_carpet", this.conditionsFrom(Blocks.BROWN_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.BROWN_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.BROWN_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:brown_lawn_chair_from_existing_chair");
		ShapedRecipeJsonFactory.create(CampanionBlocks.GREEN_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.GREEN_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_green_carpet", this.conditionsFrom(Blocks.GREEN_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.GREEN_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.GREEN_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:green_lawn_chair_from_existing_chair");
		ShapedRecipeJsonFactory.create(CampanionBlocks.RED_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.RED_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_red_carpet", this.conditionsFrom(Blocks.RED_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.RED_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.RED_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:red_lawn_chair_from_existing_chair");
		ShapedRecipeJsonFactory.create(CampanionBlocks.BLACK_LAWN_CHAIR).input('P', ItemTags.PLANKS).input('C', Blocks.BLACK_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_black_carpet", this.conditionsFrom(Blocks.BLACK_CARPET)).offerTo(consumer);
		ShapelessRecipeJsonFactory.create(CampanionBlocks.BLACK_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.BLACK_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(consumer, "campanion:black_lawn_chair_from_existing_chair");

		ComplexRecipeJsonFactory.create(CampanionRecipeSerializers.TENT_BUILDING_RECIPE).offerTo(consumer, "campanion:tent_building");
	}

	private InventoryChangedCriterion.Conditions conditionsFrom(ItemConvertible item) {
		return this.conditionsFromItemPredicates(ItemPredicate.Builder.create().item(item).build());
	}

	private InventoryChangedCriterion.Conditions conditionsFrom(Tag<Item> tag) {
		return this.conditionsFromItemPredicates(createItemPredicate(tag));
	}

	private ItemPredicate createItemPredicate(Tag<Item> tag) {
		return ItemPredicate.Builder.create().tag(tag).build();
	}

	private InventoryChangedCriterion.Conditions conditionsFromItemPredicates(ItemPredicate... items) {
		return new InventoryChangedCriterion.Conditions(NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, NumberRange.IntRange.ANY, items);
	}

	@Override
	public String getName() {
		return "Campanion Recipes";
	}
}
