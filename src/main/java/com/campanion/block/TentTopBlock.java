package com.campanion.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class TentTopBlock extends BaseTentBlock {

	private static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;

	private static final VoxelShape XSHAPE = createDiagonals(7, 8, true);
	private static final VoxelShape ZSHAPE = rotateShape(Direction.NORTH, Direction.EAST, XSHAPE);

	public TentTopBlock(Settings settings, DyeColor color) {
		super(settings, color);
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(AXIS, ctx.getPlayerFacing().getAxis());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
		switch (state.get(AXIS)) {
			case X: return XSHAPE;
			case Z: return ZSHAPE;
			default: return super.getOutlineShape(state, view, pos, context);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AXIS);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		if(rotation == BlockRotation.NONE || rotation == BlockRotation.CLOCKWISE_180) {
			return state;
		}
		return state.with(AXIS, state.get(AXIS) == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X);
	}
}
