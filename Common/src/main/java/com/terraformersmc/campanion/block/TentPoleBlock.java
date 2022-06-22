package com.terraformersmc.campanion.block;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TentPoleBlock extends BaseTent4WayBlock {

	public TentPoleBlock(Properties settings) {
		super(settings, null, Direction.SOUTH);
	}

	@Override
	protected VoxelShape createShape() {
		return box(7.5, 0, 14, 8.5, 16, 15);
	}
}
