package com.terraformersmc.campanion.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TentSideBlock extends BaseTent4WayBlock {

	public TentSideBlock(Properties settings, DyeColor color) {
		super(settings, color, Direction.SOUTH);
	}

	@Override
	protected VoxelShape createShape() {
		return createDiagonals(15, 16, false);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection());
	}

}
