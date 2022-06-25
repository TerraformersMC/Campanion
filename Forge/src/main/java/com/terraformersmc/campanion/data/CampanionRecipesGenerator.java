package com.terraformersmc.campanion.data;

import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.recipe.CampanionRecipeSerializers;
import com.terraformersmc.campanion.tag.CampanionItemTags;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class CampanionRecipesGenerator extends RecipeProvider {

	public CampanionRecipesGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> exporter) {
		ShapedRecipeBuilder.shaped(CampanionItems.MARSHMALLOW, 2).define('S', Items.SUGAR).pattern("SS").pattern("SS").unlockedBy("has_sugar", conditionsFrom(Items.SUGAR)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionItems.MARSHMALLOW_ON_A_STICK).requires(CampanionItems.MARSHMALLOW).requires(Items.STICK).unlockedBy("has_marshmallow", conditionsFrom(CampanionItems.MARSHMALLOW)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionItems.MRE).requires(CampanionItemTags.PROTEINS).requires(CampanionItemTags.FRUITS).requires(CampanionItemTags.GRAINS).requires(CampanionItemTags.VEGETABLES).unlockedBy("has_mre_components", conditionsFrom(CampanionItemTags.MRE_COMPONENTS)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionItems.CRACKER, 4).define('W', Items.WHEAT).pattern("WW").unlockedBy("has_wheat", conditionsFrom(Items.WHEAT)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionItems.SMORE).requires(CampanionItems.CRACKER).requires(CampanionItemTags.MELTED_MARSHMALLOWS).requires(Items.COCOA_BEANS).requires(CampanionItems.CRACKER).unlockedBy("has_marshmallow", conditionsFrom(CampanionItemTags.MARSHMALLOWS)).save(exporter);

		ShapedRecipeBuilder.shaped(CampanionItems.ROPE).define('S', Items.STRING).pattern("SSS").unlockedBy("has_string", conditionsFrom(Items.STRING)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionBlocks.ROPE_BRIDGE_POST, 2).define('L', net.minecraft.tags.ItemTags.LOGS).pattern("L L").pattern("L L").unlockedBy("has_rope", conditionsFrom(CampanionItems.ROPE)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionBlocks.ROPE_LADDER, 16).define('R', CampanionItems.ROPE).define('S', Items.STICK).pattern("RSR").pattern("RSR").pattern("RSR").unlockedBy("has_string", conditionsFrom(Items.STRING)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionItems.SKIPPING_STONE, 8).requires(Blocks.COBBLESTONE).unlockedBy("has_cobblestone", conditionsFrom(Blocks.COBBLESTONE)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionItems.SLEEPING_BAG).define('T', CampanionItems.WOOL_TARP).pattern("TT").unlockedBy("has_wool", conditionsFrom(net.minecraft.tags.ItemTags.WOOL)).save(exporter);

		ShapedRecipeBuilder.shaped(CampanionItems.WOODEN_ROD).define('S', Items.STICK).pattern("S").pattern("S").pattern("S").unlockedBy("has_stick", conditionsFrom(Items.STICK)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionItems.SMALL_UNBUILT_TENT).define('T', CampanionItems.WOOL_TARP).define('S', CampanionItems.WOODEN_ROD).define('R', CampanionItems.ROPE).pattern(" T ").pattern("TST").pattern("R R").unlockedBy("has_wool", conditionsFrom(net.minecraft.tags.ItemTags.WOOL)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionItems.LARGE_UNBUILT_TENT).define('T', CampanionItems.WOOL_TARP).define('S', CampanionItems.WOODEN_ROD).define('R', CampanionItems.ROPE).pattern("TTT").pattern("TST").pattern("RSR").unlockedBy("has_wool", conditionsFrom(net.minecraft.tags.ItemTags.WOOL)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionItems.TENT_BAG).define('T', CampanionItems.TANNED_LEATHER).define('I', Items.IRON_INGOT).define('R', CampanionItems.ROPE).pattern("IRI").pattern("TTT").unlockedBy("has_rope", conditionsFrom(CampanionItems.ROPE)).save(exporter);

		ShapedRecipeBuilder.shaped(CampanionItems.LEATHER_POUCH).define('L', Items.LEATHER).define('S', Items.STRING).pattern("L L").pattern("SLS").unlockedBy("has_leather", conditionsFrom(Items.LEATHER)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionItems.TANNED_LEATHER_POUCH).define('T', CampanionItems.TANNED_LEATHER).define('S', Items.STRING).pattern("T T").pattern("T T").pattern("STS").unlockedBy("has_tanned_leather", conditionsFrom(CampanionItems.TANNED_LEATHER)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionBlocks.LEATHER_TANNER).define('S', Items.STICK).pattern("S S").pattern("S S").pattern("S S").unlockedBy("has_stick", conditionsFrom(Items.STICK)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionItems.DAY_PACK).define('I', Items.IRON_INGOT).define('R', CampanionItems.ROPE).define('P', CampanionItems.LEATHER_POUCH).pattern(" I ").pattern("RPR").unlockedBy("has_leather", conditionsFrom(Items.LEATHER)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionItems.CAMPING_PACK).define('I', Items.IRON_INGOT).define('R', CampanionItems.ROPE).define('P', CampanionItems.LEATHER_POUCH).define('T', CampanionItems.TANNED_LEATHER_POUCH).pattern(" I ").pattern("RTR").pattern(" P ").unlockedBy("has_tanned_leather", conditionsFrom(CampanionItems.TANNED_LEATHER)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionItems.HIKING_PACK).define('I', Items.IRON_INGOT).define('R', CampanionItems.ROPE).define('T', CampanionItems.TANNED_LEATHER_POUCH).pattern(" I ").pattern("RTR").pattern("ITI").unlockedBy("has_tanned_leather", conditionsFrom(CampanionItems.TANNED_LEATHER)).save(exporter);

		ShapedRecipeBuilder.shaped(CampanionItems.GRAPPLING_HOOK).define('I', Items.IRON_INGOT).define('P', net.minecraft.tags.ItemTags.PLANKS).define('R', CampanionItems.ROPE).define('S', Items.STICK).pattern(" I ").pattern("PRI").pattern("SI ").unlockedBy("has_grappling_hook", conditionsFrom(CampanionItems.GRAPPLING_HOOK)).save(exporter);

		ShapedRecipeBuilder.shaped(CampanionItems.WOODEN_SPEAR).define('#', CampanionItems.WOODEN_ROD).define('X', net.minecraft.tags.ItemTags.PLANKS).pattern("X").pattern("#").unlockedBy("has_stick", conditionsFrom(Items.STICK)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionItems.STONE_SPEAR).define('#', CampanionItems.WOODEN_ROD).define('X', net.minecraft.tags.ItemTags.STONE_TOOL_MATERIALS).pattern("X").pattern("#").unlockedBy("has_stone", conditionsFrom(Blocks.COBBLESTONE)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionItems.IRON_SPEAR).define('#', CampanionItems.WOODEN_ROD).define('X', Items.IRON_INGOT).pattern("X").pattern("#").unlockedBy("has_iron", conditionsFrom(Items.IRON_INGOT)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionItems.GOLDEN_SPEAR).define('#', CampanionItems.WOODEN_ROD).define('X', Items.GOLD_INGOT).pattern("X").pattern("#").unlockedBy("has_gold", conditionsFrom(Items.GOLD_INGOT)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionItems.DIAMOND_SPEAR).define('#', CampanionItems.WOODEN_ROD).define('X', Items.DIAMOND).pattern("X").pattern("#").unlockedBy("has_diamond", conditionsFrom(Items.DIAMOND)).save(exporter);

		ShapedRecipeBuilder.shaped(CampanionBlocks.WHITE_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.WHITE_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_white_carpet", conditionsFrom(Blocks.WHITE_CARPET)).save(exporter);
		ShapedRecipeBuilder.shaped(CampanionBlocks.ORANGE_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.ORANGE_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_orange_carpet", conditionsFrom(Blocks.ORANGE_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.ORANGE_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.ORANGE_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:orange_lawn_chair_from_existing_chair");
		ShapedRecipeBuilder.shaped(CampanionBlocks.MAGENTA_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.MAGENTA_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_magenta_carpet", conditionsFrom(Blocks.MAGENTA_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.MAGENTA_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.MAGENTA_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:magenta_lawn_chair_from_existing_chair");
		ShapedRecipeBuilder.shaped(CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.LIGHT_BLUE_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_light_blue_carpet", conditionsFrom(Blocks.LIGHT_BLUE_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.LIGHT_BLUE_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:light_blue_lawn_chair_from_existing_chair");
		ShapedRecipeBuilder.shaped(CampanionBlocks.YELLOW_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.YELLOW_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_yellow_carpet", conditionsFrom(Blocks.YELLOW_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.YELLOW_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.YELLOW_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:yellow_lawn_chair_from_existing_chair");
		ShapedRecipeBuilder.shaped(CampanionBlocks.LIME_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.LIME_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_lime_carpet", conditionsFrom(Blocks.LIME_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.LIME_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.LIME_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:lime_lawn_chair_from_existing_chair");
		ShapedRecipeBuilder.shaped(CampanionBlocks.PINK_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.PINK_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_pink_carpet", conditionsFrom(Blocks.PINK_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.PINK_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.PINK_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:pink_lawn_chair_from_existing_chair");
		ShapedRecipeBuilder.shaped(CampanionBlocks.GRAY_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.GRAY_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_gray_carpet", conditionsFrom(Blocks.GRAY_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.GRAY_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.GRAY_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:gray_lawn_chair_from_existing_chair");
		ShapedRecipeBuilder.shaped(CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.LIGHT_GRAY_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_light_gray_carpet", conditionsFrom(Blocks.LIGHT_GRAY_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.LIGHT_GRAY_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:light_gray_lawn_chair_from_existing_chair");
		ShapedRecipeBuilder.shaped(CampanionBlocks.CYAN_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.CYAN_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_cyan_carpet", conditionsFrom(Blocks.CYAN_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.CYAN_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.CYAN_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:cyan_lawn_chair_from_existing_chair");
		ShapedRecipeBuilder.shaped(CampanionBlocks.PURPLE_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.PURPLE_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_purple_carpet", conditionsFrom(Blocks.PURPLE_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.PURPLE_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.PURPLE_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:purple_lawn_chair_from_existing_chair");
		ShapedRecipeBuilder.shaped(CampanionBlocks.BLUE_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.BLUE_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_blue_carpet", conditionsFrom(Blocks.BLUE_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.BLUE_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.BLUE_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:blue_lawn_chair_from_existing_chair");
		ShapedRecipeBuilder.shaped(CampanionBlocks.BROWN_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.BROWN_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_brown_carpet", conditionsFrom(Blocks.BROWN_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.BROWN_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.BROWN_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:brown_lawn_chair_from_existing_chair");
		ShapedRecipeBuilder.shaped(CampanionBlocks.GREEN_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.GREEN_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_green_carpet", conditionsFrom(Blocks.GREEN_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.GREEN_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.GREEN_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:green_lawn_chair_from_existing_chair");
		ShapedRecipeBuilder.shaped(CampanionBlocks.RED_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.RED_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_red_carpet", conditionsFrom(Blocks.RED_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.RED_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.RED_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:red_lawn_chair_from_existing_chair");
		ShapedRecipeBuilder.shaped(CampanionBlocks.BLACK_LAWN_CHAIR).define('P', net.minecraft.tags.ItemTags.PLANKS).define('C', Blocks.BLACK_CARPET).define('S', Items.STICK).pattern("P  ").pattern("PCP").pattern("S S").group("lawn_chair").unlockedBy("has_black_carpet", conditionsFrom(Blocks.BLACK_CARPET)).save(exporter);
		ShapelessRecipeBuilder.shapeless(CampanionBlocks.BLACK_LAWN_CHAIR).requires(CampanionItemTags.LAWN_CHAIRS).requires(Items.BLACK_DYE).group("dyed_lawn_chair").unlockedBy("has_lawn_chair", conditionsFrom(CampanionItemTags.LAWN_CHAIRS)).save(exporter, "campanion:black_lawn_chair_from_existing_chair");

		UpgradeRecipeBuilder.smithing(Ingredient.of(CampanionItems.DIAMOND_SPEAR), Ingredient.of(Items.NETHERITE_INGOT), CampanionItems.NETHERITE_SPEAR).unlocks("has_netherite_ingot", conditionsFrom(Items.NETHERITE_INGOT)).save(exporter, "campanion:netherite_spear");

		SpecialRecipeBuilder.special(CampanionRecipeSerializers.TENT_BUILDING_RECIPE).save(exporter, "campanion:tent_building");
		SpecialRecipeBuilder.special(CampanionRecipeSerializers.TARP_RECIPE).save(exporter, "campanion:wool_tarp");

	}

	public static InventoryChangeTrigger.TriggerInstance conditionsFrom(ItemLike itemLike) {
		return inventoryTrigger(ItemPredicate.Builder.item().of(new ItemLike[]{itemLike}).build());
	}

	public static InventoryChangeTrigger.TriggerInstance conditionsFrom(TagKey<Item> tagKey) {
		return inventoryTrigger(ItemPredicate.Builder.item().of(tagKey).build());
	}

	public static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... itemPredicates) {
		return new InventoryChangeTrigger.TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, itemPredicates);
	}

}
