package com.terraformersmc.campanion.block;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public class TentSideBlock extends BaseTent4WayBlock {

	public TentSideBlock(Settings settings, DyeColor color) {
		super(settings, color, Direction.SOUTH);
	}

	@Override
	protected VoxelShape createShape() {
		return createDiagonals(15, 16, false);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing());
	}

}
