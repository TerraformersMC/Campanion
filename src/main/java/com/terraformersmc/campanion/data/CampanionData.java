package com.terraformersmc.campanion.data;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.recipe.CampanionRecipeSerializers;
import com.terraformersmc.campanion.tag.CampanionBlockTags;
import com.terraformersmc.campanion.tag.CampanionItemTags;
import com.terraformersmc.dossier.Dossier;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.ComplexRecipeJsonFactory;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory;
import net.minecraft.item.Items;
import net.minecraft.loot.UniformLootTableRange;
import net.minecraft.loot.function.SetCountLootFunction;

import java.util.function.Consumer;

public class CampanionData {

	public static void generate() {
		Dossier.generateData(Campanion.MOD_ID, true, Dossier.Builder.create()
				.add(new BlockTags())
				.add(new ItemTags())
				.add(new Recipes())
				.add(new LootTables())
		);
	}

	private static class BlockTags extends Dossier.BlockTagsDossier {
		@Override
		protected void addBlockTags() {
			this.addReplaceTransformed(CampanionBlockTags.LAWN_CHAIRS, Campanion.MOD_ID, "<color>_lawn_chair", "color", COLORS);
			this.addReplaceTransformed(CampanionBlockTags.TENT_SIDES, Campanion.MOD_ID, "<color>_tent_side", "color", COLORS);
			this.addReplaceTransformed(CampanionBlockTags.TENT_TOPS, Campanion.MOD_ID, "<color>_tent_top", "color", COLORS);
			this.addReplaceTransformed(CampanionBlockTags.TOPPED_TENT_POLES, Campanion.MOD_ID, "<color>_topped_tent_pole", "color", COLORS);
			this.addReplaceTransformed(CampanionBlockTags.FLAT_TENT_TOPS, Campanion.MOD_ID, "<color>_flat_tent_top", "color", COLORS);
			this.get(CampanionBlockTags.TENT_POLES).add(CampanionBlocks.TENT_POLE).add(CampanionBlockTags.TOPPED_TENT_POLES);
		}
	}

	private static class ItemTags extends Dossier.ItemTagsDossier {
		@Override
		protected void addItemTags() {
			this.copyFromBlock(CampanionItemTags.LAWN_CHAIRS, CampanionBlockTags.LAWN_CHAIRS);
			this.copyFromBlock(CampanionItemTags.TENT_SIDES, CampanionBlockTags.TENT_SIDES);
			this.copyFromBlock(CampanionItemTags.TENT_TOPS, CampanionBlockTags.TENT_TOPS);
			this.copyFromBlock(CampanionItemTags.TOPPED_TENT_POLES, CampanionBlockTags.TOPPED_TENT_POLES);
			this.copyFromBlock(CampanionItemTags.FLAT_TENT_TOPS, CampanionBlockTags.FLAT_TENT_TOPS);
			this.copyFromBlock(CampanionItemTags.TENT_POLES, CampanionBlockTags.TENT_POLES);

			this.get(CampanionItemTags.MELTED_MARSHMALLOWS).add(CampanionItems.COOKED_MARSHMALLOW, CampanionItems.BLACKENED_MARSHMALLOW);
			this.get(CampanionItemTags.MARSHMALLOWS).add(CampanionItems.MARSHMALLOW).add(CampanionItemTags.MELTED_MARSHMALLOWS);
			this.get(CampanionItemTags.MARSHMALLOWS_ON_STICKS).add(CampanionItems.MARSHMALLOW_ON_A_STICK, CampanionItems.COOKED_MARSHMALLOW_ON_A_STICK, CampanionItems.BLACKENED_MARSHMALLOW_ON_A_STICK);
			this.get(CampanionItemTags.SPEARS).add(CampanionItems.WOODEN_SPEAR, CampanionItems.STONE_SPEAR, CampanionItems.IRON_SPEAR, CampanionItems.GOLDEN_SPEAR, CampanionItems.DIAMOND_SPEAR);
			this.get(CampanionItemTags.BACKPACKS).add(CampanionItems.DAY_PACK, CampanionItems.CAMPING_PACK, CampanionItems.HIKING_PACK);
			this.get(CampanionItemTags.FRUITS).add(Items.APPLE, Items.CHORUS_FRUIT, Items.MELON_SLICE, Items.SWEET_BERRIES);
			this.get(CampanionItemTags.GRAINS).add(Items.BREAD, Items.CAKE, Items.COOKIE, CampanionItems.CRACKER);
			this.get(CampanionItemTags.PROTEINS).add(Items.COOKED_BEEF, Items.COOKED_CHICKEN, Items.COOKED_COD, Items.COOKED_MUTTON, Items.COOKED_PORKCHOP, Items.COOKED_RABBIT, Items.COOKED_SALMON);
			this.get(CampanionItemTags.VEGETABLES).add(Items.BEETROOT, Items.CARROT, Items.POTATO, Items.BAKED_POTATO);
			this.get(CampanionItemTags.MRE_COMPONENTS).add(CampanionItemTags.FRUITS).add(CampanionItemTags.GRAINS).add(CampanionItemTags.PROTEINS).add(CampanionItemTags.VEGETABLES);
		}
	}

	private static class Recipes extends Dossier.RecipesDossier {
		@Override
		protected void addRecipes(Consumer<RecipeJsonProvider> provider) {
			ShapedRecipeJsonFactory.create(CampanionItems.MARSHMALLOW, 2).input('S', Items.SUGAR).pattern("SS").pattern("SS").criterion("has_sugar", this.conditionsFrom(Items.SUGAR)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionItems.MARSHMALLOW_ON_A_STICK).input(CampanionItems.MARSHMALLOW).input(Items.STICK).criterion("has_marshmallow", this.conditionsFrom(CampanionItems.MARSHMALLOW)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionItems.MRE).input(CampanionItemTags.PROTEINS).input(CampanionItemTags.FRUITS).input(CampanionItemTags.GRAINS).input(CampanionItemTags.VEGETABLES).criterion("has_mre_components", this.conditionsFrom(CampanionItemTags.MRE_COMPONENTS)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionItems.CRACKER, 4).input('W', Items.WHEAT).pattern("WW").criterion("has_wheat", this.conditionsFrom(Items.WHEAT)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionItems.SMORE).input(CampanionItems.CRACKER).input(CampanionItemTags.MELTED_MARSHMALLOWS).input(Items.COCOA_BEANS).input(CampanionItems.CRACKER).criterion("has_marshmallow", this.conditionsFrom(CampanionItemTags.MARSHMALLOWS)).offerTo(provider);

			ShapedRecipeJsonFactory.create(CampanionItems.ROPE).input('S', Items.STRING).pattern("SSS").criterion("has_string", this.conditionsFrom(Items.STRING)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionBlocks.ROPE_BRIDGE_POST, 2).input('L', net.minecraft.tag.ItemTags.LOGS).pattern("L L").pattern("L L").criterion("has_rope", this.conditionsFrom(CampanionItems.ROPE)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionBlocks.ROPE_LADDER, 16).input('R', CampanionItems.ROPE).input('S', Items.STICK).pattern("RSR").pattern("RSR").pattern("RSR").criterion("has_string", this.conditionsFrom(Items.STRING)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionItems.SKIPPING_STONE, 8).input(Blocks.COBBLESTONE).criterion("has_cobblestone", this.conditionsFrom(Blocks.COBBLESTONE)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionItems.SLEEPING_BAG).input('T', CampanionItems.WOOL_TARP).pattern("TT").criterion("has_wool", this.conditionsFrom(net.minecraft.tag.ItemTags.WOOL)).offerTo(provider);

			ShapedRecipeJsonFactory.create(CampanionItems.WOODEN_ROD).input('S', Items.STICK).pattern("S").pattern("S").pattern("S").criterion("has_stick", this.conditionsFrom(Items.STICK)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionItems.WOOL_TARP).input('W', net.minecraft.tag.ItemTags.WOOL).pattern("WW").pattern("WW").criterion("has_wool", this.conditionsFrom(net.minecraft.tag.ItemTags.WOOL)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionItems.SMALL_UNBUILT_TENT).input('T', CampanionItems.WOOL_TARP).input('S', CampanionItems.WOODEN_ROD).input('R', CampanionItems.ROPE).pattern(" T ").pattern("TST").pattern("R R").criterion("has_wool", this.conditionsFrom(net.minecraft.tag.ItemTags.WOOL)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionItems.LARGE_UNBUILT_TENT).input('T', CampanionItems.WOOL_TARP).input('S', CampanionItems.WOODEN_ROD).input('R', CampanionItems.ROPE).pattern("TTT").pattern("TST").pattern("RSR").criterion("has_wool", this.conditionsFrom(net.minecraft.tag.ItemTags.WOOL)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionItems.TENT_BAG).input('T', CampanionItems.TANNED_LEATHER).input('I', Items.IRON_INGOT).input('R', CampanionItems.ROPE).pattern("IRI").pattern("TTT").criterion("has_rope", this.conditionsFrom(CampanionItems.ROPE)).offerTo(provider);

			ShapedRecipeJsonFactory.create(CampanionItems.LEATHER_POUCH).input('L', Items.LEATHER).input('S', Items.STRING).pattern("L L").pattern("SLS").criterion("has_leather", this.conditionsFrom(Items.LEATHER)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionItems.TANNED_LEATHER_POUCH).input('T', CampanionItems.TANNED_LEATHER).input('S', Items.STRING).pattern("T T").pattern("T T").pattern("STS").criterion("has_tanned_leather", this.conditionsFrom(CampanionItems.TANNED_LEATHER)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionBlocks.LEATHER_TANNER).input('S', Items.STICK).pattern("S S").pattern("S S").pattern("S S").criterion("has_stick", this.conditionsFrom(Items.STICK)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionItems.DAY_PACK).input('I', Items.IRON_INGOT).input('R', CampanionItems.ROPE).input('P', CampanionItems.LEATHER_POUCH).pattern(" I ").pattern("RPR").criterion("has_leather", this.conditionsFrom(Items.LEATHER)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionItems.CAMPING_PACK).input('I', Items.IRON_INGOT).input('R', CampanionItems.ROPE).input('P', CampanionItems.LEATHER_POUCH).input('T', CampanionItems.TANNED_LEATHER_POUCH).pattern(" I ").pattern("RTR").pattern(" P ").criterion("has_tanned_leather", this.conditionsFrom(CampanionItems.TANNED_LEATHER)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionItems.HIKING_PACK).input('I', Items.IRON_INGOT).input('R', CampanionItems.ROPE).input('T', CampanionItems.TANNED_LEATHER_POUCH).pattern(" I ").pattern("RTR").pattern("ITI").criterion("has_tanned_leather", this.conditionsFrom(CampanionItems.TANNED_LEATHER)).offerTo(provider);

			ShapedRecipeJsonFactory.create(CampanionItems.GRAPPLING_HOOK).input('I', Items.IRON_INGOT).input('P', net.minecraft.tag.ItemTags.PLANKS).input('R', CampanionItems.ROPE).input('S', Items.STICK).pattern(" I ").pattern("PRI").pattern("SI ").criterion("has_grappling_hook", this.conditionsFrom(CampanionItems.GRAPPLING_HOOK)).offerTo(provider);

			ShapedRecipeJsonFactory.create(CampanionItems.WOODEN_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', net.minecraft.tag.ItemTags.PLANKS).pattern("X").pattern("#").criterion("has_stick", this.conditionsFrom(Items.STICK)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionItems.STONE_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', Blocks.COBBLESTONE).pattern("X").pattern("#").criterion("has_stone", this.conditionsFrom(Blocks.COBBLESTONE)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionItems.IRON_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', Items.IRON_INGOT).pattern("X").pattern("#").criterion("has_iron", this.conditionsFrom(Items.IRON_INGOT)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionItems.GOLDEN_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', Items.GOLD_INGOT).pattern("X").pattern("#").criterion("has_gold", this.conditionsFrom(Items.GOLD_INGOT)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionItems.DIAMOND_SPEAR).input('#', CampanionItems.WOODEN_ROD).input('X', Items.DIAMOND).pattern("X").pattern("#").criterion("has_diamond", this.conditionsFrom(Items.DIAMOND)).offerTo(provider);

			ShapedRecipeJsonFactory.create(CampanionBlocks.WHITE_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.WHITE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_white_carpet", this.conditionsFrom(Blocks.WHITE_CARPET)).offerTo(provider);
			ShapedRecipeJsonFactory.create(CampanionBlocks.ORANGE_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.ORANGE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_orange_carpet", this.conditionsFrom(Blocks.ORANGE_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.ORANGE_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.ORANGE_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:orange_lawn_chair_from_existing_chair");
			ShapedRecipeJsonFactory.create(CampanionBlocks.MAGENTA_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.MAGENTA_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_magenta_carpet", this.conditionsFrom(Blocks.MAGENTA_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.MAGENTA_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.MAGENTA_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:magenta_lawn_chair_from_existing_chair");
			ShapedRecipeJsonFactory.create(CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.LIGHT_BLUE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_light_blue_carpet", this.conditionsFrom(Blocks.LIGHT_BLUE_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.LIGHT_BLUE_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:light_blue_lawn_chair_from_existing_chair");
			ShapedRecipeJsonFactory.create(CampanionBlocks.YELLOW_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.YELLOW_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_yellow_carpet", this.conditionsFrom(Blocks.YELLOW_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.YELLOW_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.YELLOW_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:yellow_lawn_chair_from_existing_chair");
			ShapedRecipeJsonFactory.create(CampanionBlocks.LIME_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.LIME_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_lime_carpet", this.conditionsFrom(Blocks.LIME_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.LIME_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.LIME_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:lime_lawn_chair_from_existing_chair");
			ShapedRecipeJsonFactory.create(CampanionBlocks.PINK_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.PINK_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_pink_carpet", this.conditionsFrom(Blocks.PINK_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.PINK_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.PINK_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:pink_lawn_chair_from_existing_chair");
			ShapedRecipeJsonFactory.create(CampanionBlocks.GRAY_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.GRAY_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_gray_carpet", this.conditionsFrom(Blocks.GRAY_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.GRAY_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.GRAY_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:gray_lawn_chair_from_existing_chair");
			ShapedRecipeJsonFactory.create(CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.LIGHT_GRAY_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_light_gray_carpet", this.conditionsFrom(Blocks.LIGHT_GRAY_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.LIGHT_GRAY_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:light_gray_lawn_chair_from_existing_chair");
			ShapedRecipeJsonFactory.create(CampanionBlocks.CYAN_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.CYAN_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_cyan_carpet", this.conditionsFrom(Blocks.CYAN_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.CYAN_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.CYAN_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:cyan_lawn_chair_from_existing_chair");
			ShapedRecipeJsonFactory.create(CampanionBlocks.PURPLE_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.PURPLE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_purple_carpet", this.conditionsFrom(Blocks.PURPLE_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.PURPLE_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.PURPLE_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:purple_lawn_chair_from_existing_chair");
			ShapedRecipeJsonFactory.create(CampanionBlocks.BLUE_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.BLUE_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_blue_carpet", this.conditionsFrom(Blocks.BLUE_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.BLUE_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.BLUE_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:blue_lawn_chair_from_existing_chair");
			ShapedRecipeJsonFactory.create(CampanionBlocks.BROWN_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.BROWN_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_brown_carpet", this.conditionsFrom(Blocks.BROWN_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.BROWN_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.BROWN_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:brown_lawn_chair_from_existing_chair");
			ShapedRecipeJsonFactory.create(CampanionBlocks.GREEN_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.GREEN_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_green_carpet", this.conditionsFrom(Blocks.GREEN_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.GREEN_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.GREEN_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:green_lawn_chair_from_existing_chair");
			ShapedRecipeJsonFactory.create(CampanionBlocks.RED_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.RED_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_red_carpet", this.conditionsFrom(Blocks.RED_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.RED_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.RED_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:red_lawn_chair_from_existing_chair");
			ShapedRecipeJsonFactory.create(CampanionBlocks.BLACK_LAWN_CHAIR).input('P', net.minecraft.tag.ItemTags.PLANKS).input('C', Blocks.BLACK_CARPET).input('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").criterion("has_black_carpet", this.conditionsFrom(Blocks.BLACK_CARPET)).offerTo(provider);
			ShapelessRecipeJsonFactory.create(CampanionBlocks.BLACK_LAWN_CHAIR).input(CampanionItemTags.LAWN_CHAIRS).input(Items.BLACK_DYE).group("dyed_lawn_chair").criterion("has_lawn_chair", this.conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).offerTo(provider, "campanion:black_lawn_chair_from_existing_chair");

			ComplexRecipeJsonFactory.create(CampanionRecipeSerializers.TENT_BUILDING_RECIPE).offerTo(provider, "campanion:tent_building");
		}
	}


	private static class LootTables extends Dossier.LootTablesDossier {
		@Override
		protected void addLootTables() {
			this.addSelfDrop(CampanionBlocks.ROPE_BRIDGE_POST);
			this.addSelfDrop(CampanionBlocks.TENT_POLE);

			this.addSelfDrop(CampanionBlocks.WHITE_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.ORANGE_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.MAGENTA_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.YELLOW_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.LIME_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.PINK_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.GRAY_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.CYAN_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.PURPLE_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.BLUE_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.BROWN_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.GREEN_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.RED_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.BLACK_LAWN_CHAIR);
			this.addSelfDrop(CampanionBlocks.LEATHER_TANNER);


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
			this.addDrop(block, createBlockLootTable(Items.STRING).withFunction(SetCountLootFunction.builder(UniformLootTableRange.between(5F, 5F))));
		}
	}
}
