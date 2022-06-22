package com.terraformersmc.campanion.data;

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
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SpecialRecipeBuilder;
import net.minecraft.data.recipes.UpgradeRecipeBuilder;
import net.minecraft.data.server.recipe.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import java.util.function.Consumer;

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
		protected void addRecipes(Consumer<FinishedRecipe> exporter) {
			ShapedRecipeBuilder.shaped(CampanionItems.MARSHMALLOW, 2).define('S', Items.SUGAR).pattern("SS").pattern("SS").unlockedBy("has_sugar", this.conditionsFrom(Items.SUGAR)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionItems.MARSHMALLOW_ON_A_STICK).requires(CampanionItems.MARSHMALLOW).requires(Items.STICK).unlockedBy("has_marshmallow", this.conditionsFrom(CampanionItems.MARSHMALLOW)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionItems.MRE).requires(CampanionItemTags.PROTEINS).requires(CampanionItemTags.FRUITS).requires(CampanionItemTags.GRAINS).requires(CampanionItemTags.VEGETABLES).unlockedBy("has_mre_components", this.conditionsFrom(CampanionItemTags.MRE_COMPONENTS)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionItems.CRACKER, 4).define('W', Items.WHEAT).pattern("WW").unlockedBy("has_wheat", this.conditionsFrom(Items.WHEAT)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionItems.SMORE).requires(CampanionItems.CRACKER).requires(CampanionItemTags.MELTED_MARSHMALLOWS).requires(Items.COCOA_BEANS).requires(CampanionItems.CRACKER).unlockedBy("has_marshmallow", this.conditionsFrom(CampanionItemTags.MARSHMALLOWS)).save(exporter);

			ShapedRecipeBuilder.shaped(CampanionItems.ROPE).define('S', Items.STRING).pattern("SSS").unlockedBy("has_string", this.conditionsFrom(Items.STRING)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionBlocks.ROPE_BRIDGE_POST, 2).define('L', net.minecraft.tags.ItemTags.LOGS).pattern("L L").pattern("L L").unlockedBy("has_rope", this.conditionsFrom(CampanionItems.ROPE)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionBlocks.ROPE_LADDER, 16).define('R', CampanionItems.ROPE).define('S', Items.STICK).pattern("RSR").pattern("RSR").pattern("RSR").unlockedBy("has_string", this.conditionsFrom(Items.STRING)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionItems.SKIPPING_STONE, 8).requires(Blocks.COBBLESTONE).unlockedBy("has_cobblestone", this.conditionsFrom(Blocks.COBBLESTONE)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionItems.SLEEPING_BAG).define('T', CampanionItems.WOOL_TARP).pattern("TT").unlockedBy("has_wool", this.conditionsFrom(net.minecraft.tags.ItemTags.WOOL)).save(exporter);

			ShapedRecipeBuilder.shaped(CampanionItems.WOODEN_ROD).define('S', Items.STICK).pattern("S").pattern("S").pattern("S").unlockedBy("has_stick", this.conditionsFrom(Items.STICK)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionItems.SMALL_UNBUILT_TENT).define('T', CampanionItems.WOOL_TARP).define('S', CampanionItems.WOODEN_ROD).define('R', CampanionItems.ROPE).pattern(" T ").pattern("TST").pattern("R R").unlockedBy("has_wool", this.conditionsFrom(net.minecraft.tags.ItemTags.WOOL)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionItems.LARGE_UNBUILT_TENT).define('T', CampanionItems.WOOL_TARP).define('S', CampanionItems.WOODEN_ROD).define('R', CampanionItems.ROPE).pattern("TTT").pattern("TST").pattern("RSR").unlockedBy("has_wool", this.conditionsFrom(net.minecraft.tags.ItemTags.WOOL)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionItems.TENT_BAG).define('T', CampanionItems.TANNED_LEATHER).define('I', Items.IRON_INGOT).define('R', CampanionItems.ROPE).pattern("IRI").pattern("TTT").unlockedBy("has_rope", this.conditionsFrom(CampanionItems.ROPE)).save(exporter);

			ShapedRecipeBuilder.shaped(CampanionItems.LEATHER_POUCH).define('L', Items.LEATHER).define('S', Items.STRING).pattern("L L").pattern("SLS").unlockedBy("has_leather", this.conditionsFrom(Items.LEATHER)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionItems.TANNED_LEATHER_POUCH).define('T', CampanionItems.TANNED_LEATHER).define('S', Items.STRING).pattern("T T").pattern("T T").pattern("STS").unlockedBy("has_tanned_leather", this.conditionsFrom(CampanionItems.TANNED_LEATHER)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionBlocks.LEATHER_TANNER).define('S', Items.STICK).pattern("S S").pattern("S S").pattern("S S").unlockedBy("has_stick", this.conditionsFrom(Items.STICK)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionItems.DAY_PACK).define('I', Items.IRON_INGOT).define('R', CampanionItems.ROPE).define('P', CampanionItems.LEATHER_POUCH).pattern(" I ").pattern("RPR").unlockedBy("has_leather", this.conditionsFrom(Items.LEATHER)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionItems.CAMPING_PACK).define('I', Items.IRON_INGOT).define('R', CampanionItems.ROPE).define('P', CampanionItems.LEATHER_POUCH).define('T', CampanionItems.TANNED_LEATHER_POUCH).pattern(" I ").pattern("RTR").pattern(" P ").unlockedBy("has_tanned_leather", this.conditionsFrom(CampanionItems.TANNED_LEATHER)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionItems.HIKING_PACK).define('I', Items.IRON_INGOT).define('R', CampanionItems.ROPE).define('T', CampanionItems.TANNED_LEATHER_POUCH).pattern(" I ").pattern("RTR").pattern("ITI").unlockedBy("has_tanned_leather", this.conditionsFrom(CampanionItems.TANNED_LEATHER)).save(exporter);

			ShapedRecipeBuilder.shaped(CampanionItems.GRAPPLING_HOOK).define('I', Items.IRON_INGOT).define('P', net.minecraft.tags.ItemTags.PLANKS).define('R', CampanionItems.ROPE).define('S', Items.STICK).pattern(" I ").pattern("PRI").pattern("SI ").unlockedBy("has_grappling_hook", this.conditionsFrom(CampanionItems.GRAPPLING_HOOK)).save(exporter);

			ShapedRecipeBuilder.shaped(CampanionItems.WOODEN_SPEAR).define('#', CampanionItems.WOODEN_ROD).define('X', net.minecraft.tags.ItemTags.PLANKS).pattern("X").pattern("#").unlockedBy("has_stick", this.conditionsFrom(Items.STICK)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionItems.STONE_SPEAR).define('#', CampanionItems.WOODEN_ROD).define('X', net.minecraft.tags.ItemTags.STONE_TOOL_MATERIALS).pattern("X").pattern("#").unlockedBy("has_stone", this.conditionsFrom(Blocks.COBBLESTONE)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionItems.IRON_SPEAR).define('#', CampanionItems.WOODEN_ROD).define('X', Items.IRON_INGOT).pattern("X").pattern("#").unlockedBy("has_iron", this.conditionsFrom(Items.IRON_INGOT)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionItems.GOLDEN_SPEAR).define('#', CampanionItems.WOODEN_ROD).define('X', Items.GOLD_INGOT).pattern("X").pattern("#").unlockedBy("has_gold", this.conditionsFrom(Items.GOLD_INGOT)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionItems.DIAMOND_SPEAR).define('#', CampanionItems.WOODEN_ROD).define('X', Items.DIAMOND).pattern("X").pattern("#").unlockedBy("has_diamond", this.conditionsFrom(Items.DIAMOND)).save(exporter);

			ShapedRecipeBuilder.shaped(CampanionBlocks.WHITE_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.WHITE_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_white_carpet", this.conditionsFrom(Blocks.WHITE_CARPET)).save(exporter);
			ShapedRecipeBuilder.shaped(CampanionBlocks.ORANGE_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.ORANGE_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_orange_carpet", this.conditionsFrom(Blocks.ORANGE_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.ORANGE_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.ORANGE_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:orange_lawn_chair_from_existing_chair");
			ShapedRecipeBuilder.shaped(CampanionBlocks.MAGENTA_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.MAGENTA_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_magenta_carpet", this.conditionsFrom(Blocks.MAGENTA_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.MAGENTA_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.MAGENTA_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:magenta_lawn_chair_from_existing_chair");
			ShapedRecipeBuilder.shaped(CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.LIGHT_BLUE_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_light_blue_carpet", this.conditionsFrom(Blocks.LIGHT_BLUE_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.LIGHT_BLUE_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:light_blue_lawn_chair_from_existing_chair");
			ShapedRecipeBuilder.shaped(CampanionBlocks.YELLOW_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.YELLOW_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_yellow_carpet", this.conditionsFrom(Blocks.YELLOW_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.YELLOW_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.YELLOW_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:yellow_lawn_chair_from_existing_chair");
			ShapedRecipeBuilder.shaped(CampanionBlocks.LIME_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.LIME_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_lime_carpet", this.conditionsFrom(Blocks.LIME_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.LIME_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.LIME_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:lime_lawn_chair_from_existing_chair");
			ShapedRecipeBuilder.shaped(CampanionBlocks.PINK_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.PINK_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_pink_carpet", this.conditionsFrom(Blocks.PINK_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.PINK_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.PINK_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:pink_lawn_chair_from_existing_chair");
			ShapedRecipeBuilder.shaped(CampanionBlocks.GRAY_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.GRAY_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_gray_carpet", this.conditionsFrom(Blocks.GRAY_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.GRAY_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.GRAY_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:gray_lawn_chair_from_existing_chair");
			ShapedRecipeBuilder.shaped(CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.LIGHT_GRAY_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_light_gray_carpet", this.conditionsFrom(Blocks.LIGHT_GRAY_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.LIGHT_GRAY_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:light_gray_lawn_chair_from_existing_chair");
			ShapedRecipeBuilder.shaped(CampanionBlocks.CYAN_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.CYAN_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_cyan_carpet", this.conditionsFrom(Blocks.CYAN_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.CYAN_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.CYAN_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:cyan_lawn_chair_from_existing_chair");
			ShapedRecipeBuilder.shaped(CampanionBlocks.PURPLE_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.PURPLE_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_purple_carpet", this.conditionsFrom(Blocks.PURPLE_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.PURPLE_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.PURPLE_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:purple_lawn_chair_from_existing_chair");
			ShapedRecipeBuilder.shaped(CampanionBlocks.BLUE_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.BLUE_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_blue_carpet", this.conditionsFrom(Blocks.BLUE_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.BLUE_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.BLUE_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:blue_lawn_chair_from_existing_chair");
			ShapedRecipeBuilder.shaped(CampanionBlocks.BROWN_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.BROWN_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_brown_carpet", this.conditionsFrom(Blocks.BROWN_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.BROWN_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.BROWN_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:brown_lawn_chair_from_existing_chair");
			ShapedRecipeBuilder.shaped(CampanionBlocks.GREEN_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.GREEN_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_green_carpet", this.conditionsFrom(Blocks.GREEN_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.GREEN_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.GREEN_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:green_lawn_chair_from_existing_chair");
			ShapedRecipeBuilder.shaped(CampanionBlocks.RED_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.RED_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_red_carpet", this.conditionsFrom(Blocks.RED_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.RED_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.RED_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:red_lawn_chair_from_existing_chair");
			ShapedRecipeBuilder.shaped(CampanionBlocks.BLACK_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.BLACK_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_black_carpet", this.conditionsFrom(Blocks.BLACK_CARPET)).save(exporter);
			ShapelessRecipeBuilder.shapeless(CampanionBlocks.BLACK_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.BLACK_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:black_lawn_chair_from_existing_chair");

			UpgradeRecipeBuilder.smithing(Ingredient.of(CampanionItems.DIAMOND_SPEAR), Ingredient.of(Items.NETHERITE_INGOT), CampanionItems.NETHERITE_SPEAR).unlocks("has_netherite_ingot", this.conditionsFrom(Items.NETHERITE_INGOT)).save(exporter, "campanion:netherite_spear");

			SpecialRecipeBuilder.special(CampanionRecipeSerializers.TENT_BUILDING_RECIPE).save(exporter, "campanion:tent_building");
			SpecialRecipeBuilder.special(CampanionRecipeSerializers.TARP_RECIPE).save(exporter, "campanion:wool_tarp");
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
			this.drops(block, BlockLootTableCreator.drops(Items.STRING).apply(SetItemCountFunction.setCount(UniformGenerator.between(5F, 5F))));
		}
	}
}
