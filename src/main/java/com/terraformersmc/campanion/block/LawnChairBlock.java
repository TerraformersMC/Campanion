package com.terraformersmc.campanion.block;

import com.terraformersmc.campanion.blockentity.LawnChairBlockEntity;
import com.terraformersmc.campanion.entity.LawnChairEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class LawnChairBlock extends HorizontalFacingBlock implements BlockEntityProvider {

	private static final VoxelShape EAST_SHAPE;
	private static final VoxelShape WEST_SHAPE;
	private static final VoxelShape SOUTH_SHAPE;
	private static final VoxelShape NORTH_SHAPE;

	public LawnChairBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof LawnChairBlockEntity && !world.isClient) {
			((LawnChairBlockEntity) entity).findOrCreateEntity();
		}
		super.onPlaced(world, pos, state, placer, itemStack);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof LawnChairBlockEntity && !world.isClient) {
			LawnChairEntity chairEntity = ((LawnChairBlockEntity) entity).findOrCreateEntity();

			player.startRiding(chairEntity);
		}
		return ActionResult.CONSUME;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		switch (state.get(FACING)) {
			case NORTH:
				return NORTH_SHAPE;
			case EAST:
				return EAST_SHAPE;
			case SOUTH:
				return SOUTH_SHAPE;
			case WEST:
				return WEST_SHAPE;
			default:
				return super.getOutlineShape(state, view, pos, context);
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new LawnChairBlockEntity();
	}

	static {
		VoxelShape baseShape = createCuboidShape(1, 6, 1, 15, 7, 15);
		VoxelShape armLeft = createCuboidShape(2, 10, -0.5, 13, 12, 2.5);
		VoxelShape armRight = createCuboidShape(2, 10, 13.5, 13, 12, 16.5);
		VoxelShape legLeft = createCuboidShape(0, 0, 0, 2, 20.4, 2);
		VoxelShape legRight = createCuboidShape(0, 0, 14, 2, 20.4, 16);
		VoxelShape backBase = createCuboidShape(0.5, 10, 2, 1.5, 19, 14);

		VoxelShape shape = VoxelShapes.union(baseShape, armLeft, armRight, legLeft, legRight, backBase);

		EAST_SHAPE = shape;
		NORTH_SHAPE = rotate(Direction.EAST, Direction.NORTH, shape);
		SOUTH_SHAPE = rotate(Direction.EAST, Direction.SOUTH, shape);
		WEST_SHAPE = rotate(Direction.EAST, Direction.WEST, shape);
	}

	private static VoxelShape rotate(Direction from, Direction to, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

		int times = (to.getHorizontal() - from.getHorizontal() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
			buffer[0] = buffer[1];
			buffer[1] = VoxelShapes.empty();
		}

		return buffer[0];
	}
}
