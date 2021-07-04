package com.terraformersmc.campanion.recipe;

import com.terraformersmc.campanion.item.AccessorStructure;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.item.TentBagItem;
import com.terraformersmc.campanion.item.TentItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TentBuildingRecipe extends SpecialCraftingRecipe {

	private static final ThreadLocal<Structure> STRUCTURE_CACHE = ThreadLocal.withInitial(() -> null);

	public TentBuildingRecipe(Identifier id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		boolean foundTent = false;
		boolean foundBag = false;

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);

			if (isBag(stack) && !foundBag) {
				foundBag = true;
			} else if (isTent(stack) && !foundTent) {
				if (world instanceof ServerWorld) {
					STRUCTURE_CACHE.set(((ServerWorld) world).getStructureManager().getStructure(((TentItem) stack.getItem()).getStructure()).orElseThrow());
				}
				foundTent = true;
			} else if (!stack.isEmpty()) {
				STRUCTURE_CACHE.set(null);
				return false;
			}

			if (foundBag && foundTent) {
				return true;
			}
		}

		STRUCTURE_CACHE.set(null);
		return false;
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		Structure structure = STRUCTURE_CACHE.get();
		STRUCTURE_CACHE.set(null);

		for (int i = 0; i < inv.size(); i++) {
			ItemStack stack = inv.getStack(i);

			if (isTent(stack)) {
				if (structure != null) {
					ItemStack out = new ItemStack(CampanionItems.TENT_BAG);
					TentItem.initNbt(out, structure);
					return out;
				}
			}

		}
		return ItemStack.EMPTY;
	}

	private static boolean isBag(ItemStack stack) {
		return stack.getItem() instanceof TentBagItem && (!stack.hasTag() || !stack.getOrCreateTag().contains("Blocks", 9));
	}

	private static boolean isTent(ItemStack stack) {
		return stack.getItem() instanceof TentItem;
	}

	@Override
	public boolean fits(int width, int height) {
		return false;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CampanionRecipeSerializers.TENT_BUILDING_RECIPE;
	}
}
