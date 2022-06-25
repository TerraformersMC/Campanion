package com.terraformersmc.campanion.recipe;

import com.terraformersmc.campanion.item.AccessorStructure;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.item.TentBagItem;
import com.terraformersmc.campanion.item.TentItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class TentBuildingRecipe extends CustomRecipe {

	private static final ThreadLocal<StructureTemplate> STRUCTURE_CACHE = ThreadLocal.withInitial(() -> null);

	public TentBuildingRecipe(ResourceLocation id) {
		super(id);
	}

	@Override
	public boolean matches(CraftingContainer inv, Level world) {
		boolean foundTent = false;
		boolean foundBag = false;

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);

			if (isBag(stack) && !foundBag) {
				foundBag = true;
			} else if (isTent(stack) && !foundTent) {
				if (world instanceof ServerLevel) {
					STRUCTURE_CACHE.set(((ServerLevel) world).getStructureManager().get(((TentItem) stack.getItem()).getStructure()).orElseThrow());
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
	public ItemStack assemble(CraftingContainer inv) {
		StructureTemplate structure = STRUCTURE_CACHE.get();
		STRUCTURE_CACHE.set(null);

		for (int i = 0; i < inv.getContainerSize(); i++) {
			ItemStack stack = inv.getItem(i);

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
	public boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CampanionRecipeSerializers.TENT_BUILDING_RECIPE;
	}
}
