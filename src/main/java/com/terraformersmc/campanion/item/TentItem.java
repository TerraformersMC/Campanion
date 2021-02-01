package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.Campanion;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.Structure;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;

public class TentItem extends PlaceableTentItem {

	private final Identifier structure;

	public TentItem(Settings settings, String structureName) {
		super(settings);
		this.structure = new Identifier(Campanion.MOD_ID, "tents/" + structureName + "_tent");
	}

	public Identifier getStructure() {
		return structure;
	}

	@Override
	public void onPlaceTent(ItemStack stack) {
		stack.decrement(1);
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
		if (!hasBlocks(stack) && world instanceof ServerWorld) {
			initNbt(stack, Objects.requireNonNull(((ServerWorld) world).getStructureManager().getStructure(((TentItem) stack.getItem()).getStructure())));
		}
		super.inventoryTick(stack, world, entity, slot, selected);
	}

	public static void initNbt(ItemStack stack, Structure structure) {
		BlockPos size = structure.getSize();
		ListTag list = new ListTag();
		for (Structure.StructureBlockInfo info : ((AccessorStructure) structure).getBlocks().get(0).getAll()) {
			CompoundTag tag = new CompoundTag();
			tag.put("Pos", NbtHelper.fromBlockPos(info.pos.add(-size.getX() / 2, 0, -size.getZ() / 2)));
			tag.put("BlockState", NbtHelper.fromBlockState(info.state));
			if (info.tag != null && !info.tag.isEmpty()) {
				tag.put("BlockEntityData", info.tag);
			}
			list.add(tag);
		}

		stack.getOrCreateTag().put("Blocks", list);
		stack.getOrCreateTag().put("TentSize", NbtHelper.fromBlockPos(size));
	}
}
