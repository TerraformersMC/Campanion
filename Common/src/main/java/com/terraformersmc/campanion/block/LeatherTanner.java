package com.terraformersmc.campanion.block;

import com.terraformersmc.campanion.item.CampanionItems;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import static net.minecraft.world.level.block.AbstractFurnaceBlock.LIT;
import static net.minecraft.core.Direction.*;

public class LeatherTanner extends HorizontalDirectionalBlock {

	private static final VoxelShape EAST_SHAPE;
	private static final VoxelShape WEST_SHAPE;
	private static final VoxelShape SOUTH_SHAPE;
	private static final VoxelShape NORTH_SHAPE;

	private static final IntegerProperty AGE;

	public LeatherTanner(Properties settings) {
		super(settings);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, NORTH).setValue(AGE, 0));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
		builder.add(new Property[]{AGE});
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (getAge(state) == 1) {
			world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.LEATHER)));
		} else if (getAge(state) == 2) {
			world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CampanionItems.TANNED_LEATHER)));
		}
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (getAge(state) == 0) {
			ItemStack stack = player.getItemInHand(hand);
			if (stack.getItem() == Items.LEATHER) {
				setAge(world, pos, state, 1);
				if (!player.isCreative()) {
					stack.setCount(stack.getCount() - 1);
				}
				return InteractionResult.CONSUME;
			}
		} else if (getAge(state) == 1) {
			setAge(world, pos, state, 0);
			world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.LEATHER)));
			return InteractionResult.SUCCESS;
		} else if (getAge(state) == 2) {
			setAge(world, pos, state, 0);
			world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CampanionItems.TANNED_LEATHER)));
			return InteractionResult.SUCCESS;
		}
		return InteractionResult.FAIL;
	}

	public void setAge(Level world, BlockPos pos, BlockState state, int age) {
		world.setBlock(pos, state.setValue(AGE, age), 2);
	}

	public int getAge(BlockState state) {
		return (Integer) state.getValue(AGE);
	}

	@Override
	public boolean isRandomlyTicking(BlockState state) {
		return true;
	}

	@Override
	public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		if (getAge(state) == 1) {

			float speedMultiplier = 1;
			Direction facing = state.getValue(FACING);
			ArrayList<BlockState> blockStates = new ArrayList<>();

			//Check if there is a lit campfire within the immediate surrounding blocks and on the flat face of the tanner
			blockStates.add(world.getBlockState(pos.east().north()));
			blockStates.add(world.getBlockState(pos.east().south()));
			blockStates.add(world.getBlockState(pos.west().north()));
			blockStates.add(world.getBlockState(pos.west().south()));
			if (facing == NORTH || facing == SOUTH) {
				blockStates.add(world.getBlockState(pos.south()));
				blockStates.add(world.getBlockState(pos.north()));
			} else if (facing == EAST || facing == WEST){
				blockStates.add(world.getBlockState(pos.west()));
				blockStates.add(world.getBlockState(pos.east()));
			}

			for (BlockState currState : blockStates) {
				if (currState.getBlock() instanceof CampfireBlock && currState.getValue(LIT)) {
					speedMultiplier *= 1.5;
				}
			}

			if (random.nextFloat() <= speedMultiplier / 8) {
				world.setBlock(pos, state.setValue(AGE, 2), 2);
			}
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
		switch (state.getValue(FACING)) {
			case NORTH:
				return NORTH_SHAPE;
			case EAST:
				return EAST_SHAPE;
			case SOUTH:
				return SOUTH_SHAPE;
			case WEST:
				return WEST_SHAPE;
			default:
				return super.getShape(state, view, pos, context);
		}
	}

	static {
		VoxelShape shape = box(5, 0, 1, 10, 16, 15);

		EAST_SHAPE = shape;
		NORTH_SHAPE = rotate(Direction.EAST, NORTH, shape);
		SOUTH_SHAPE = rotate(Direction.EAST, Direction.SOUTH, shape);
		WEST_SHAPE = rotate(Direction.EAST, Direction.WEST, shape);

		AGE = BlockStateProperties.AGE_2;
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
