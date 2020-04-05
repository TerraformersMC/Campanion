package com.campanion.recipe;

import com.campanion.item.AccessorStructure;
import com.campanion.item.CampanionItems;
import com.campanion.item.TentBagItem;
import com.campanion.item.UnbuiltTent;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TentBuildingRecipe extends SpecialCraftingRecipe {

    private static final ThreadLocal<Structure> STRUCTURE_CACHE =ThreadLocal.withInitial(() -> null);

    public TentBuildingRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        boolean foundTent = false;
        boolean foundBag = false;

        for (int i = 0; i < inv.getInvSize(); i++) {
            ItemStack stack = inv.getInvStack(i);

            if(isBag(stack) && !foundBag) {
                foundBag = true;
            } else if(isTent(stack) && !foundTent) {
                STRUCTURE_CACHE.set(((ServerWorld) world).getStructureManager().getStructure(((UnbuiltTent)stack.getItem()).getStructure()));
                foundTent = true;
            } else if(!stack.isEmpty()) {
                STRUCTURE_CACHE.set(null);
                return false;
            }

            if(foundBag && foundTent) {
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

        for (int i = 0; i < inv.getInvSize(); i++) {
            ItemStack stack = inv.getInvStack(i);

            if(isTent(stack)) {
                if(structure != null) {
                    BlockPos size = structure.getSize();
                    ItemStack out = new ItemStack(CampanionItems.TENT_BAG);
                    ListTag list = new ListTag();
                    for (Structure.StructureBlockInfo info : ((AccessorStructure) structure).getBlocks().get(0)) {
                        CompoundTag tag = new CompoundTag();
                        tag.put("Pos", NbtHelper.fromBlockPos(info.pos.add(-size.getX()/2, 0, -size.getZ()/2)));
                        tag.put("BlockState", NbtHelper.fromBlockState(info.state));
                        if(info.tag != null && !info.tag.isEmpty()) {
                            tag.put("BlockEntityData", info.tag);
                        }
                        list.add(tag);
                    }

                    out.getOrCreateTag().put("Blocks", list);
                    out.getOrCreateTag().put("TentSize", NbtHelper.fromBlockPos(size));

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
        return stack.getItem() instanceof UnbuiltTent;
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }
}
