package com.terraformersmc.campanion.block;

import com.terraformersmc.campanion.blockentity.LawnChairBlockEntity;
import com.terraformersmc.campanion.entity.LawnChairEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class LawnChairBlock extends HorizontalDirectionalBlock implements EntityBlock {

	private static final VoxelShape EAST_SHAPE;
	private static final VoxelShape WEST_SHAPE;
	private static final VoxelShape SOUTH_SHAPE;
	private static final VoxelShape NORTH_SHAPE;

	public LawnChairBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	public void setPlacedBy(Level world, @NotNull BlockPos pos, @NotNull BlockState state, LivingEntity placer, @NotNull ItemStack itemStack) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof LawnChairBlockEntity && !world.isClientSide) {
			((LawnChairBlockEntity) entity).findOrCreateEntity();
		}
		super.setPlacedBy(world, pos, state, placer, itemStack);
	}

	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof LawnChairBlockEntity && !world.isClientSide) {
			LawnChairEntity chairEntity = ((LawnChairBlockEntity) entity).findOrCreateEntity();

			player.startRiding(chairEntity);
		}
		return InteractionResult.CONSUME;
	}

	@Override
	public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter view, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case EAST -> EAST_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case WEST -> WEST_SHAPE;
            default -> super.getShape(state, view, pos, context);
        };
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new LawnChairBlockEntity(pos, state);
	}

	static {
		VoxelShape baseShape = box(1, 6, 1, 15, 7, 15);
		VoxelShape armLeft = box(2, 10, -0.5, 13, 12, 2.5);
		VoxelShape armRight = box(2, 10, 13.5, 13, 12, 16.5);
		VoxelShape legLeft = box(0, 0, 0, 2, 20.4, 2);
		VoxelShape legRight = box(0, 0, 14, 2, 20.4, 16);
		VoxelShape backBase = box(0.5, 10, 2, 1.5, 19, 14);

		VoxelShape shape = Shapes.or(baseShape, armLeft, armRight, legLeft, legRight, backBase);

		EAST_SHAPE = shape;
		NORTH_SHAPE = rotate(Direction.EAST, Direction.NORTH, shape);
		SOUTH_SHAPE = rotate(Direction.EAST, Direction.SOUTH, shape);
		WEST_SHAPE = rotate(Direction.EAST, Direction.WEST, shape);
	}

	private static VoxelShape rotate(Direction from, Direction to, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

		int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
			buffer[0] = buffer[1];
			buffer[1] = Shapes.empty();
		}

		return buffer[0];
	}
}
