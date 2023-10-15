package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.mixin.AccessorStructureTemplate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class TentItem extends PlaceableTentItem {

	private final ResourceLocation structure;

	public TentItem(Properties settings, String structureName) {
		super(settings);
		this.structure = new ResourceLocation(Campanion.MOD_ID, "tents/" + structureName + "_tent");
	}

	public ResourceLocation getStructure() {
		return structure;
	}

	@Override
	public void onPlaceTent(ItemStack stack) {
		stack.shrink(1);
	}

	@Override
	public void inventoryTick(@NotNull ItemStack stack, @NotNull Level world, @NotNull Entity entity, int slot, boolean selected) {
		if (!hasBlocks(stack) && world instanceof ServerLevel) {
			initNbt(stack, Objects.requireNonNull(((ServerLevel) world).getStructureManager().get(((TentItem) stack.getItem()).getStructure()).orElseThrow()));
		}
		super.inventoryTick(stack, world, entity, slot, selected);
	}

	public static void initNbt(ItemStack stack, StructureTemplate structure) {
		Vec3i size = structure.getSize();
		ListTag list = new ListTag();
		for (StructureTemplate.StructureBlockInfo info : ((AccessorStructureTemplate) structure).getPalettes().get(0).blocks()) {
			CompoundTag tag = new CompoundTag();
			tag.put("Pos", NbtUtils.writeBlockPos(info.pos().offset(-size.getX() / 2, 0, -size.getZ() / 2)));
			tag.put("BlockState", NbtUtils.writeBlockState(info.state()));
			if (info.nbt() != null && !info.nbt().isEmpty()) {
				tag.put("BlockEntityData", info.nbt());
			}
			list.add(tag);
		}

		stack.getOrCreateTag().put("Blocks", list);
		stack.getOrCreateTag().put("TentSize", NbtUtils.writeBlockPos(new BlockPos(size)));
	}
}
