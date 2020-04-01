package com.campanion.block;

import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;

public class TentTopBlock extends BaseTentBlock {
	public TentTopBlock(Settings settings) {
		super(settings, Direction.EAST);
	}

	@Override
	protected VoxelShape createShape() {
		return createDiagonals(7, 8, true);
	}
}
