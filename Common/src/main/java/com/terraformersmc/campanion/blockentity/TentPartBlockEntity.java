package com.terraformersmc.campanion.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.state.BlockState;

public class TentPartBlockEntity extends SerializableBlockEntity {

	private BlockPos linkedPos = BlockPos.ZERO;
	private BlockPos size = BlockPos.ZERO;

	public TentPartBlockEntity(BlockPos pos, BlockState state) {
		super(CampanionBlockEntities.TENT_PART, pos, state);
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
	public void toTag(CompoundTag tag) {
		tag.put("LinkedPos", NbtUtils.writeBlockPos(this.linkedPos));
		tag.put("Size", NbtUtils.writeBlockPos(this.size));
	}

	@Override
	public void fromTag(CompoundTag tag) {
		this.linkedPos = NbtUtils.readBlockPos(tag.getCompound("LinkedPos"));
		this.size = NbtUtils.readBlockPos(tag.getCompound("Size"));
	}

	@Override
	public void toClientTag(CompoundTag tag) {
		toTag(tag);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		fromTag(tag);
	}
}
