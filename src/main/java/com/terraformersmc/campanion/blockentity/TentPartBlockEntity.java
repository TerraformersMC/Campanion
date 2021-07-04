package com.terraformersmc.campanion.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;

public class TentPartBlockEntity extends BlockEntity {

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
	public NbtCompound writeNbt(NbtCompound tag) {
		tag.put("LinkedPos", NbtHelper.fromBlockPos(this.linkedPos));
		tag.put("Size", NbtHelper.fromBlockPos(this.size));
		return super.writeNbt(tag);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		this.linkedPos = NbtHelper.toBlockPos(tag.getCompound("LinkedPos"));
		this.size = NbtHelper.toBlockPos(tag.getCompound("Size"));
		super.readNbt(tag);
	}
}
