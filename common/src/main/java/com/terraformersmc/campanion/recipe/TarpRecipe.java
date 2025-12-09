package com.terraformersmc.campanion.recipe;

import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.platform.Services;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TarpRecipe extends CustomRecipe {
	public TarpRecipe(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput inv, Level world) {
		int woolAmount = 0;
		int shearAmount = 0;
		for (int i = 0; i < inv.size(); i++) {
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
	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return new ItemStack(CampanionItems.WOOL_TARP);
	}

	@Override
	public ItemStack assemble(CraftingInput inv, HolderLookup.Provider provider) {
		return this.getResultItem(provider);
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
	public NonNullList<ItemStack> getRemainingItems(CraftingInput inventory) {
		NonNullList<ItemStack> defaultedList = NonNullList.withSize(inventory.size(), ItemStack.EMPTY);

		for(int i = 0; i < defaultedList.size(); ++i) {
			ItemStack stack = inventory.getItem(i);
			Item item = stack.getItem();
			if (item.hasCraftingRemainingItem()) {
				defaultedList.set(i, new ItemStack(item.getCraftingRemainingItem()));
			}
			if(stack.is(Services.PLATFORM.getShearsTag())) {
				ItemStack copy = stack.copy();
				if (!this.hurt(stack, 1, null)) {
					defaultedList.set(i, copy);
				}
			}
		}

		return defaultedList;
	}

	private boolean hurt(ItemStack stack, int pDamage, @Nullable ServerPlayer pPlayer) {
		if (!stack.isDamageableItem()) {
			return false;
		} else {
			if (pDamage > 0 && pPlayer != null) {
				pDamage = EnchantmentHelper.processDurabilityChange(pPlayer.serverLevel(), stack, pDamage);
				if (pDamage <= 0) {
					return false;
				}
			}

			if (pPlayer != null && pDamage != 0) {
				CriteriaTriggers.ITEM_DURABILITY_CHANGED.trigger(pPlayer, stack, stack.getDamageValue() + pDamage);
			}

			int i = stack.getDamageValue() + pDamage;
			stack.setDamageValue(i);
			return i >= stack.getMaxDamage();
		}
	}
}
