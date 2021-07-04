package com.terraformersmc.campanion.recipe;

import com.terraformersmc.campanion.item.CampanionItems;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Random;

public class TarpRecipe extends SpecialCraftingRecipe {
	public TarpRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		int woolAmount = 0;
		int shearAmount = 0;
		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);
			if(FabricToolTags.SHEARS.contains(stack.getItem())) {
				shearAmount++;
			} else if(ItemTags.WOOL.contains(stack.getItem())) {
				woolAmount++;
			}
		}
		return woolAmount == 3 && shearAmount == 1;
	}

	@Override
	public boolean isIgnoredInRecipeBook() {
		return false;
	}

	@Override
	public DefaultedList<Ingredient> getIngredients() {
		DefaultedList<Ingredient> list = DefaultedList.of();

		list.add(Ingredient.fromTag(ItemTags.WOOL));
		list.add(Ingredient.fromTag(ItemTags.WOOL));
		list.add(Ingredient.fromTag(ItemTags.WOOL));

		list.add(Ingredient.fromTag(FabricToolTags.SHEARS));
		return list;
	}

	@Override
	public ItemStack getOutput() {
		return new ItemStack(CampanionItems.WOOL_TARP);
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		return this.getOutput();
	}

	@Override
	public boolean fits(int width, int height) {
		return width*height >= 4;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CampanionRecipeSerializers.TARP_RECIPE;
	}

	@Override
	public DefaultedList<ItemStack> getRemainder(CraftingInventory inventory) {
		DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);

		for(int i = 0; i < defaultedList.size(); ++i) {
			Item item = inventory.getStack(i).getItem();
			if (item.hasRecipeRemainder()) {
				defaultedList.set(i, new ItemStack(item.getRecipeRemainder()));
			}
			if(FabricToolTags.SHEARS.contains(item)) {
				ItemStack stack = inventory.getStack(i).copy();
				if (!stack.damage(1, new Random(), null)) {
					defaultedList.set(i, stack);
				}
			}
		}

		return defaultedList;
	}
}
