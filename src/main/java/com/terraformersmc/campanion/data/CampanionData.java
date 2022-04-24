package com.terraformersmc.campanion.data;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.recipe.CampanionRecipeSerializers;
import com.terraformersmc.campanion.tag.CampanionBlockTags;
import com.terraformersmc.campanion.tag.CampanionItemTags;
import com.terraformersmc.dossier.DossierProvider;
import com.terraformersmc.dossier.Dossiers;
import com.terraformersmc.dossier.generator.BlockTagsDossier;
import com.terraformersmc.dossier.generator.ItemTagsDossier;
import com.terraformersmc.dossier.generator.LootTablesDossier;
import com.terraformersmc.dossier.generator.RecipesDossier;
import com.terraformersmc.dossier.util.BlockLootTableCreator;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.*;
import net.minecraft.item.Items;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.recipe.Ingredient;
import java.util.function.Consumer;
import static net.minecraft.block.MapColor.COLORS;

public class CampanionData implements DossierProvider {
	@Override
	public Dossiers createDossiers() {
		return Dossiers.builder()
				.addBlockTags(CampanionBlockTagsGenerator::new)
				.addItemTags(CampanionItemTagsGenerator::new)
				.addRecipes(CampanionRecipesGenerator::new)
				.addLootTables(CampanionLootTablesGenerator::new);
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	private static class CampanionBlockTagsGenerator extends BlockTagsDossier {
		@Override
		protected void addBlockTags() {
			this.addReplaceTransformed(CampanionBlockTags.LAWN_CHAIRS, Campanion.MOD_ID, "<color>_lawn_chair", "color", COLORS);
			this.addReplaceTransformed(CampanionBlockTags.TENT_SIDES, Campanion.MOD_ID, "<color>_tent_side", "color", COLORS);
			this.addReplaceTransformed(CampanionBlockTags.TENT_TOPS, Campanion.MOD_ID, "<color>_tent_top", "color", COLORS);
			this.addReplaceTransformed(CampanionBlockTags.TOPPED_TENT_POLES, Campanion.MOD_ID, "<color>_topped_tent_pole", "color", COLORS);
			this.addReplaceTransformed(CampanionBlockTags.FLAT_TENT_TOPS, Campanion.MOD_ID, "<color>_flat_tent_top", "color", COLORS);
			this.get(CampanionBlockTags.TENT_POLES).add(CampanionBlocks.TENT_POLE).addTag(CampanionBlockTags.TOPPED_TENT_POLES);
		}
	}

	private static class CampanionItemTagsGenerator extends ItemTagsDossier {
		@Override
		protected void addItemTags() {
			this.copyFromBlock(CampanionItemTags.LAWN_CHAIRS, CampanionBlockTags.LAWN_CHAIRS);
			this.copyFromBlock(CampanionItemTags.TENT_SIDES, CampanionBlockTags.TENT_SIDES);
			this.copyFromBlock(CampanionItemTags.TENT_TOPS, CampanionBlockTags.TENT_TOPS);
			this.copyFromBlock(CampanionItemTags.TOPPED_TENT_POLES, CampanionBlockTags.TOPPED_TENT_POLES);
			this.copyFromBlock(CampanionItemTags.FLAT_TENT_TOPS, CampanionBlockTags.FLAT_TENT_TOPS);
			this.copyFromBlock(CampanionItemTags.TENT_POLES, CampanionBlockTags.TENT_POLES);

			this.get(CampanionItemTags.MELTED_MARSHMALLOWS).add(CampanionItems.COOKED_MARSHMALLOW, CampanionItems.BLACKENED_MARSHMALLOW);
			this.get(CampanionItemTags.MARSHMALLOWS).add(CampanionItems.MARSHMALLOW).addTag(CampanionItemTags.MELTED_MARSHMALLOWS);
			this.get(CampanionItemTags.MARSHMALLOWS_ON_STICKS).add(CampanionItems.MARSHMALLOW_ON_A_STICK, CampanionItems.COOKED_MARSHMALLOW_ON_A_STICK, CampanionItems.BLACKENED_MARSHMALLOW_ON_A_STICK);
			this.get(CampanionItemTags.SPEARS).add(CampanionItems.WOODEN_SPEAR, CampanionItems.STONE_SPEAR, CampanionItems.IRON_SPEAR, CampanionItems.GOLDEN_SPEAR, CampanionItems.DIAMOND_SPEAR, CampanionItems.NETHERITE_SPEAR);
			this.get(CampanionItemTags.BACKPACKS).add(CampanionItems.DAY_PACK, CampanionItems.CAMPING_PACK, CampanionItems.HIKING_PACK);
			this.get(CampanionItemTags.FRUITS).add(Items.APPLE, Items.CHORUS_FRUIT, Items.MELON_SLICE, Items.SWEET_BERRIES);
			this.get(CampanionItemTags.GRAINS).add(Items.BREAD, Items.CAKE, Items.COOKIE, CampanionItems.CRACKER);
			this.get(CampanionItemTags.PROTEINS).add(Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_COD, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_RABBIT, Items.COOKED_SALMON);
			this.get(CampanionItemTags.VEGETABLES).add(Items.BEETROOT, Items.CARROT, Items.POTATO, Items.BAKED_POTATO);
			this.get(CampanionItemTags.MRE_COMPONENTS).addTag(CampanionItemTags.FRUITS).addTag(CampanionItemTags.GRAINS).addTag(CampanionItemTags.PROTEINS).addTag(CampanionItemTags.VEGETABLES);
		}
	}

	private static class CampanionRecipesGenerator extends RecipesDossier {
		@Override
		protected void addRecipes(Consumer<RecipeJsonProvider> exporter) {
			ShapedRecipeJsonBuilder.create(CampanionItems.MARSHMALLOW, 2).input('S', Items.SUGAR).pattern("SS").pattern("SS").criterion("has_sugar", this.conditionsFrom(Items.SUGAR)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionItems.MARSHMALLOW_ON_A_STICK).input(CampanionItems.MARSHMALLOW).input(Items.STICK).criterion("has_marshmallow", this.conditionsFrom(CampanionItems.MARSHMALLOW)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionItems.MRE).input(CampanionItemTags.PROTEINS).input(CampanionItemTags.FRUITS).input(CampanionItemTags.GRAINS).input(CampanionItemTags.VEGETABLES).criterion("has_mre_components", this.conditionsFrom(CampanionItemTags.MRE_COMPONENTS)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionItems.CRACKER, 4).input('W', Items.WHEAT).pattern("WW").criterion("has_wheat", this.conditionsFrom(Items.WHEAT)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionItems.SMORE).input(CampanionItems.CRACKER).input(CampanionItemTags.MELTED_MARSHMALLOWS).input(Items.COCOA_BEANS).input(CampanionItems.CRACKER).criterion("has_marshmallow", this.conditionsFrom(CampanionItemTags.MARSHMALLOWS)).offerTo(exporter);

			ShapedRecipeJsonBuilder.create(CampanionItems.ROPE).input('S', Items.STRING).pattern("SSS").criterion("has_string", this.conditionsFrom(Items.STRING)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionBlocks.ROPE_BRIDGE_POST, 2).input('L', net.minecraft.tag.ItemTags.LOGS).pattern("L L").pattern("L L").criterion("has_rope", this.conditionsFrom(CampanionItems.ROPE)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionBlocks.ROPE_LADDER, 16).input('R', CampanionItems.ROPE).input('S', Items.STICK).pattern("RSR").pattern("RSR").pattern("RSR").criterion("has_string", this.conditionsFrom(Items.STRING)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionItems.SKIPPING_STONE, 8).input(Blocks.COBBLESTONE).criterion("has_cobblestone", this.conditionsFrom(Blocks.COBBLESTONE)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionItems.SLEEPING_BAG).input('T', CampanionItems.WOOL_TARP).pattern("TT").criterion("has_wool", this.conditionsFrom(net.minecraft.tag.ItemTags.WOOL)).offerTo(exporter);

			ShapedRecipeJsonBuilder.create(CampanionItems.WOODEN_ROD).input('S', Items.STICK).pattern("S").pattern("S").pattern("S").criterion("has_stick", this.conditionsFrom(Items.STICK)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionItems.SMALL_UNBUILT_TENT).input('T', CampanionItems.WOOL_TARP).input('S', CampanionItems.WOODEN_ROD).input('R', CampanionItems.ROPE).pattern(" T ").pattern("TST").pattern("R R").criterion("has_wool", this.conditionsFrom(net.minecraft.tag.ItemTags.WOOL)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionItems.LARGE_UNBUILT_TENT).input('T', CampanionItems.WOOL_TARP).input('S', CampanionItems.WOODEN_ROD).input('R', CampanionItems.ROPE).pattern("TTT").pattern("TST").pattern("RSR").criterion("has_wool", this.conditionsFrom(net.minecraft.tag.ItemTags.WOOL)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionItems.TENT_BAG).input('T', CampanionItems.TANNED_LEATHER).input('I', Items.IRON_INGOT).input('R', CampanionItems.ROPE).pattern("IRI").pattern("TTT").criterion("has_rope", this.conditionsFrom(CampanionItems.ROPE)).offerTo(exporter);

			ShapedRecipeJsonBuilder.create(CampanionItems.LEATHER_POUCH).input('L', Items.LEATHER).input('S', Items.STRING).pattern("L L").pattern("SLS").criterion("has_leather", this.conditionsFrom(Items.LEATHER)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionItems.TANNED_LEATHER_POUCH).input('T', CampanionItems.TANNED_LEATHER).input('S', Items.STRING).pattern("T T").pattern("T T").pattern("STS").criterion("has_tanned_leather", this.conditionsFrom(CampanionItems.TANNED_LEATHER)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionBlocks.LEATHER_TANNER).input('S', Items.STICK).pattern("S S").pattern("S S").pattern("S S").criterion("has_stick", this.conditionsFrom(Items.STICK)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionItems.DAY_PACK).input('I', Items.IRON_INGOT).input('R', CampanionItems.ROPE).input('P', CampanionItems.LEATHER_POUCH).pattern(" I ").pattern("RPR").criterion("has_leather", this.conditionsFrom(Items.LEATHER)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionItems.CAMPING_PACK).input('I', Items.IRON_INGOT).input('R', CampanionItems.ROPE).input('P', CampanionItems.LEATHER_POUCH).input('T', CampanionItems.TANNED_LEATHER_POUCH).pattern(" I ").pattern("RTR").pattern(" P ").criterion("has_tanned_leather", this.conditionsFrom(CampanionItems.TANNED_LEATHER)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionItems.HIKING_PACK).input('I', Items.IRON_INGOT).input('R', CampanionItems.ROPE).input('T', CampanionItems.TANNED_LEATHER_POUCH).pattern(" I ").pattern("RTR").pattern("ITI").criterion("has_tanned_leather", this.conditionsFrom(CampanionItems.TANNED_LEATHER)).offerTo(exporter);

			ShapedRecipeJsonBuilder.create(CampanionItems.GRAPPLING_HOOK).input('I', Items.IRON_INGOT).input('P', net.minecraft.tag.ItemTags.PLANKS).input('R', CampanionItems.ROPE).input('S', Items.STICK).pattern(" I ").pattern("PRI").pattern("SI ").criterion("has_grappling_hook", this.conditionsFrom(CampanionItems.GRAPPLING_HOOK)).offerTo(exporter);

			ShapedRecipeJsonBuilder.create(CampanionItems.WOODEN_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', net.minecraft.tag.ItemTags.PLANKS).pattern("X").pattern("#").criterion("has_stick", this.conditionsFrom(Items.STICK)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionItems.STONE_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', net.minecraft.tag.ItemTags.STONE_TOOL_MATERIALS).pattern("X").pattern("#").criterion("has_stone", this.conditionsFrom(Blocks.COBBLESTONE)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionItems.IRON_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', Items.IRON_INGOT).pattern("X").pattern("#").criterion("has_iron", this.conditionsFrom(Items.IRON_INGOT)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionItems.GOLDEN_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', Items.GOLD_INGOT).pattern("X").pattern("#").criterion("has_gold", this.conditionsFrom(Items.GOLD_INGOT)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionItems.DIAMOND_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', Items.DIAMOND).pattern("X").pattern("#").criterion("has_diamond", this.conditionsFrom(Items.DIAMOND)).offerTo(exporter);

			ShapedRecipeJsonBuilder.create(CampanionBlocks.WHITE_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.WHITE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_white_carpet", this.conditionsFrom(Blocks.WHITE_CARPET)).offerTo(exporter);
			ShapedRecipeJsonBuilder.create(CampanionBlocks.ORANGE_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.ORANGE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_orange_carpet", this.conditionsFrom(Blocks.ORANGE_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.ORANGE_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.ORANGE_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:orange_lawn_chair_from_existing_chair");
			ShapedRecipeJsonBuilder.create(CampanionBlocks.MAGENTA_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.MAGENTA_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_magenta_carpet", this.conditionsFrom(Blocks.MAGENTA_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.MAGENTA_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.MAGENTA_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:magenta_lawn_chair_from_existing_chair");
			ShapedRecipeJsonBuilder.create(CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.LIGHT_BLUE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_light_blue_carpet", this.conditionsFrom(Blocks.LIGHT_BLUE_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.LIGHT_BLUE_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:light_blue_lawn_chair_from_existing_chair");
			ShapedRecipeJsonBuilder.create(CampanionBlocks.YELLOW_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.YELLOW_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_yellow_carpet", this.conditionsFrom(Blocks.YELLOW_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.YELLOW_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.YELLOW_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:yellow_lawn_chair_from_existing_chair");
			ShapedRecipeJsonBuilder.create(CampanionBlocks.LIME_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.LIME_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_lime_carpet", this.conditionsFrom(Blocks.LIME_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.LIME_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.LIME_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:lime_lawn_chair_from_existing_chair");
			ShapedRecipeJsonBuilder.create(CampanionBlocks.PINK_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.PINK_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_pink_carpet", this.conditionsFrom(Blocks.PINK_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.PINK_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.PINK_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:pink_lawn_chair_from_existing_chair");
			ShapedRecipeJsonBuilder.create(CampanionBlocks.GRAY_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.GRAY_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_gray_carpet", this.conditionsFrom(Blocks.GRAY_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.GRAY_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.GRAY_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:gray_lawn_chair_from_existing_chair");
			ShapedRecipeJsonBuilder.create(CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.LIGHT_GRAY_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_light_gray_carpet", this.conditionsFrom(Blocks.LIGHT_GRAY_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.LIGHT_GRAY_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:light_gray_lawn_chair_from_existing_chair");
			ShapedRecipeJsonBuilder.create(CampanionBlocks.CYAN_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.CYAN_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_cyan_carpet", this.conditionsFrom(Blocks.CYAN_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.CYAN_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.CYAN_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:cyan_lawn_chair_from_existing_chair");
			ShapedRecipeJsonBuilder.create(CampanionBlocks.PURPLE_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.PURPLE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_purple_carpet", this.conditionsFrom(Blocks.PURPLE_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.PURPLE_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.PURPLE_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:purple_lawn_chair_from_existing_chair");
			ShapedRecipeJsonBuilder.create(CampanionBlocks.BLUE_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.BLUE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_blue_carpet", this.conditionsFrom(Blocks.BLUE_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.BLUE_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.BLUE_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:blue_lawn_chair_from_existing_chair");
			ShapedRecipeJsonBuilder.create(CampanionBlocks.BROWN_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.BROWN_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_brown_carpet", this.conditionsFrom(Blocks.BROWN_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.BROWN_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.BROWN_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:brown_lawn_chair_from_existing_chair");
			ShapedRecipeJsonBuilder.create(CampanionBlocks.GREEN_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.GREEN_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_green_carpet", this.conditionsFrom(Blocks.GREEN_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.GREEN_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.GREEN_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:green_lawn_chair_from_existing_chair");
			ShapedRecipeJsonBuilder.create(CampanionBlocks.RED_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.RED_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_red_carpet", this.conditionsFrom(Blocks.RED_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.RED_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.RED_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:red_lawn_chair_from_existing_chair");
			ShapedRecipeJsonBuilder.create(CampanionBlocks.BLACK_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.BLACK_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_black_carpet", this.conditionsFrom(Blocks.BLACK_CARPET)).offerTo(exporter);
			ShapelessRecipeJsonBuilder.create(CampanionBlocks.BLACK_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.BLACK_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(exporter, "campanion:black_lawn_chair_from_existing_chair");

			SmithingRecipeJsonBuilder.create(Ingredient.ofItems(CampanionItems.DIAMOND_SPEAR), Ingredient.ofItems(Items.NETHERITE_INGOT), CampanionItems.NETHERITE_SPEAR).criterion("has_netherite_ingot", this.conditionsFrom(Items.NETHERITE_INGOT)).offerTo(exporter, "campanion:netherite_spear");

			ComplexRecipeJsonBuilder.create(CampanionRecipeSerializers.TENT_BUILDING_RECIPE).offerTo(exporter, "campanion:tent_building");
			ComplexRecipeJsonBuilder.create(CampanionRecipeSerializers.TARP_RECIPE).offerTo(exporter, "campanion:wool_tarp");
		}
	}


	private static class CampanionLootTablesGenerator extends LootTablesDossier {
		@Override
		protected void addLootTables() {
			this.drops(CampanionBlocks.ROPE_BRIDGE_POST);
			this.drops(CampanionBlocks.TENT_POLE);

			this.drops(CampanionBlocks.WHITE_LAWN_CHAIR);
			this.drops(CampanionBlocks.ORANGE_LAWN_CHAIR);
			this.drops(CampanionBlocks.MAGENTA_LAWN_CHAIR);
			this.drops(CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR);
			this.drops(CampanionBlocks.YELLOW_LAWN_CHAIR);
			this.drops(CampanionBlocks.LIME_LAWN_CHAIR);
			this.drops(CampanionBlocks.PINK_LAWN_CHAIR);
			this.drops(CampanionBlocks.GRAY_LAWN_CHAIR);
			this.drops(CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR);
			this.drops(CampanionBlocks.CYAN_LAWN_CHAIR);
			this.drops(CampanionBlocks.PURPLE_LAWN_CHAIR);
			this.drops(CampanionBlocks.BLUE_LAWN_CHAIR);
			this.drops(CampanionBlocks.BROWN_LAWN_CHAIR);
			this.drops(CampanionBlocks.GREEN_LAWN_CHAIR);
			this.drops(CampanionBlocks.RED_LAWN_CHAIR);
			this.drops(CampanionBlocks.BLACK_LAWN_CHAIR);
			this.drops(CampanionBlocks.LEATHER_TANNER);


			this.addTentPartDrop(CampanionBlocks.WHITE_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.ORANGE_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.MAGENTA_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.LIGHT_BLUE_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.YELLOW_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.LIME_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.PINK_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.GRAY_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.LIGHT_GRAY_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.CYAN_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.PURPLE_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.BLUE_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.BROWN_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.GREEN_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.RED_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.BLACK_TENT_SIDE);

			this.addTentPartDrop(CampanionBlocks.WHITE_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.ORANGE_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.MAGENTA_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.LIGHT_BLUE_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.YELLOW_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.LIME_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.PINK_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.GRAY_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.LIGHT_GRAY_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.CYAN_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.PURPLE_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.BLUE_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.BROWN_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.GREEN_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.RED_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.BLACK_TENT_TOP);

			this.addTentPartDrop(CampanionBlocks.WHITE_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.ORANGE_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.MAGENTA_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.LIGHT_BLUE_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.YELLOW_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.LIME_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.PINK_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.GRAY_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.LIGHT_GRAY_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.CYAN_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.PURPLE_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.BLUE_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.BROWN_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.GREEN_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.RED_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.BLACK_TOPPED_TENT_POLE);

			this.addTentPartDrop(CampanionBlocks.WHITE_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.ORANGE_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.MAGENTA_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.LIGHT_BLUE_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.YELLOW_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.LIME_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.PINK_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.GRAY_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.LIGHT_GRAY_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.CYAN_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.PURPLE_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.BLUE_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.BROWN_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.GREEN_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.RED_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.BLACK_FLAT_TENT_TOP);
		}

		public void addTentPartDrop(Block block) {
			this.drops(block, BlockLootTableCreator.drops(Items.STRING).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(5F, 5F))));
		}
	}
}
