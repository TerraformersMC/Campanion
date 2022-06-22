package com.terraformersmc.campanion.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TentTopPoleBlock extends BaseTent4WayBlock {

	public TentTopPoleBlock(Properties settings, DyeColor color) {
		super(settings, color, Direction.WEST);
	}

	@Override
	protected VoxelShape createShape() {//1-maxZ, minY, minX, 1-minZ, maxY, maxX
		return Shapes.or(
			createDiagonals(7, 8, true),
			box(1, 0, 7.5, 2, 8, 8.5)
		);
	}
}
