package com.terraformersmc.campanion.recipe;

import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.platform.Services;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class TarpRecipe extends CustomRecipe {
	public TarpRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingContainer inv, Level world) {
		int woolAmount = 0;
		int shearAmount = 0;
		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);
			if(stack.is(Services.PLATFORM.getShearsTag())) {
				shearAmount++;
			} else if(stack.is(ItemTags.WOOL)) {
				woolAmount++;
			}
		}
		return woolAmount == 3 && shearAmount == 1;
	}

	@Override
	public boolean isSpecial() {
		return false;
	}

	@Override
	public NonNullList<Ingredient> getIngredients() {
		NonNullList<Ingredient> list = NonNullList.create();

		list.add(Ingredient.of(ItemTags.WOOL));
		list.add(Ingredient.of(ItemTags.WOOL));
		list.add(Ingredient.of(ItemTags.WOOL));

		list.add(Ingredient.of(Services.PLATFORM.getShearsTag()));
		return list;
	}

	@Override
	public ItemStack getResultItem() {
		return new ItemStack(CampanionItems.WOOL_TARP);
	}

	@Override
	public ItemStack assemble(CraftingContainer inv) {
		return this.getResultItem();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width*height >= 4;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CampanionRecipeSerializers.TARP_RECIPE;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(CraftingContainer inventory) {
		NonNullList<ItemStack> defaultedList = NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);

		for(int i = 0; i < defaultedList.size(); ++i) {
			ItemStack stack = inventory.getItem(i);
			Item item = stack.getItem();
			if (item.hasCraftingRemainingItem()) {
				defaultedList.set(i, new ItemStack(item.getCraftingRemainingItem()));
			}
			if(stack.is(Services.PLATFORM.getShearsTag())) {
				ItemStack copy = stack.copy();
				if (!copy.hurt(1, RandomSource.create(), null)) {
					defaultedList.set(i, copy);
				}
			}
		}

		return defaultedList;
	}
}
