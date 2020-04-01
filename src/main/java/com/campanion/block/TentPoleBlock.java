package com.campanion.block;

import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public class TentPoleBlock extends BaseTentBlock {

	public TentPoleBlock(Settings settings) {
		super(settings, Direction.SOUTH);
	}

	@Override
	protected VoxelShape createShape() {
		return createCuboidShape(7.5, 0, 14, 8.5, 16, 15);
	}
}
