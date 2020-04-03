package com.campanion.block;

import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class TentTopPoleBlock extends BaseTentBlock {

	public TentTopPoleBlock(Settings settings) {
		super(settings, Direction.WEST);
	}

	@Override
	protected VoxelShape createShape() {//1-maxZ, minY, minX, 1-minZ, maxY, maxX
		return VoxelShapes.union(
			createDiagonals(7, 8, true),
			createCuboidShape(1, 0, 7.5, 2, 8, 8.5)
		);
	}
}
