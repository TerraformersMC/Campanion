package com.campanion.block;

import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public class TentSideBlock extends BaseTentBlock {

	public TentSideBlock(Settings settings) {
		super(settings, Direction.NORTH);
	}

	@Override
	protected VoxelShape createShape() {
		return createDiagonals(15, 16, false);
	}

}
