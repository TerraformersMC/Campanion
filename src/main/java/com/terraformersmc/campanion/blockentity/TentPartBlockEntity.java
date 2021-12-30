package com.terraformersmc.campanion.blockentity;

import com.terraformersmc.campanion.mixin.InvokerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;

public class TentPartBlockEntity extends SerializableBlockEntity {

	private BlockPos linkedPos = BlockPos.ORIGIN;
	private BlockPos size = BlockPos.ORIGIN;

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
	public void toTag(NbtCompound tag) {
		((InvokerBlockEntity) this).callWriteIdentifyingData(tag);
		tag.put("LinkedPos", NbtHelper.fromBlockPos(this.linkedPos));
		tag.put("Size", NbtHelper.fromBlockPos(this.size));
	}

	@Override
	public void fromTag(NbtCompound tag) {
		this.linkedPos = NbtHelper.toBlockPos(tag.getCompound("LinkedPos"));
		this.size = NbtHelper.toBlockPos(tag.getCompound("Size"));
	}

	@Override
	public void toClientTag(NbtCompound tag) {
		toTag(tag);
	}

	@Override
	public void fromClientTag(NbtCompound tag) {
		fromTag(tag);
	}
}
