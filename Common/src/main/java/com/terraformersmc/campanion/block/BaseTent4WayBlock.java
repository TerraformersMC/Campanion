package com.terraformersmc.campanion.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public abstract class BaseTent4WayBlock extends BaseTentBlock {
	protected static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	private final VoxelShape northShape;
	private final VoxelShape eastShape;
	private final VoxelShape southShape;
	private final VoxelShape westShape;

	public BaseTent4WayBlock(Properties settings, DyeColor color, Direction baseDir) {
		super(settings, color);

		VoxelShape shape = this.createShape();

		this.northShape = rotateShape(baseDir, Direction.NORTH, shape);
		this.eastShape = rotateShape(baseDir, Direction.EAST, shape);
		this.southShape = rotateShape(baseDir, Direction.SOUTH, shape);
		this.westShape = rotateShape(baseDir, Direction.WEST, shape);

		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter view, @NotNull BlockPos pos, @NotNull CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case NORTH -> this.northShape;
			case EAST -> this.eastShape;
			case SOUTH -> this.southShape;
			case WEST -> this.westShape;
			default -> super.getShape(state, view, pos, context);
		};
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}

	protected abstract VoxelShape createShape();
}
