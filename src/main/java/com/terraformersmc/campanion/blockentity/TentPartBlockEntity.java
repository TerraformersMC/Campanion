package com.terraformersmc.campanion.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;

public class TentPartBlockEntity extends BlockEntity {

	private BlockPos linkedPos = BlockPos.ORIGIN;
	private BlockPos size = BlockPos.ORIGIN;

	public TentPartBlockEntity() {
		super(CampanionBlockEntities.TENT_PART);
	}

	public BlockPos getLinkedPos() {
		return linkedPos;
	}

	public void setLinkedPos(BlockPos linkedPos) {
		this.linkedPos = linkedPos;
	}

	public void setTentSize(BlockPos size) {
		this.size = size;
	}

	public BlockPos getSize() {
		return size;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("LinkedPos", NbtHelper.fromBlockPos(this.linkedPos));
		tag.put("Size", NbtHelper.fromBlockPos(this.size));
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		this.linkedPos = NbtHelper.toBlockPos(tag.getCompound("LinkedPos"));
		this.size = NbtHelper.toBlockPos(tag.getCompound("Size"));
		super.fromTag(state, tag);
	}
}
