package com.terraformersmc.campanion.recipe;

import com.terraformersmc.campanion.item.CampanionItems;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
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
			if(stack.isIn(ConventionalItemTags.SHEARS)) {
				shearAmount++;
			} else if(stack.isIn(ItemTags.WOOL)) {
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

		list.add(Ingredient.fromTag(ConventionalItemTags.SHEARS));
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
			ItemStack stack = inventory.getStack(i);
			Item item = stack.getItem();
			if (item.hasRecipeRemainder()) {
				defaultedList.set(i, new ItemStack(item.getRecipeRemainder()));
			}
			if(stack.isIn(ConventionalItemTags.SHEARS)) {
				ItemStack copy = stack.copy();
				if (!copy.damage(1, new Random(), null)) {
					defaultedList.set(i, copy);
				}
			}
		}

		return defaultedList;
	}
}
