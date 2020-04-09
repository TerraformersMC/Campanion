package com.terraformersmc.campanion.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public abstract class BaseTent4WayBlock extends BaseTentBlock {

	protected static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

	private final VoxelShape northShape;
	private final VoxelShape eastShape;
	private final VoxelShape southShape;
	private final VoxelShape westShape;

	public BaseTent4WayBlock(Settings settings, DyeColor color, Direction baseDir) {
		super(settings, color);

		VoxelShape shape = this.createShape();

		this.northShape = rotateShape(baseDir, Direction.NORTH, shape);
		this.eastShape = rotateShape(baseDir, Direction.EAST, shape);
		this.southShape = rotateShape(baseDir, Direction.SOUTH, shape);
		this.westShape = rotateShape(baseDir, Direction.WEST, shape);

		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		switch (state.get(FACING)) {
			case NORTH:
				return this.northShape;
			case EAST:
				return this.eastShape;
			case SOUTH:
				return this.southShape;
			case WEST:
				return this.westShape;
			default:
				return super.getOutlineShape(state, view, pos, context);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state.with(FACING, rotation.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state.rotate(mirror.getRotation(state.get(FACING)));
	}

	protected abstract VoxelShape createShape();
}
