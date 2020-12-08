package com.terraformersmc.campanion.block;

import com.terraformersmc.campanion.item.CampanionItems;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

import static net.minecraft.block.AbstractFurnaceBlock.LIT;
import static net.minecraft.util.math.Direction.*;
import static net.minecraft.util.math.Direction.WEST;

public class LeatherTanner extends HorizontalFacingBlock {

	private static final VoxelShape EAST_SHAPE;
	private static final VoxelShape WEST_SHAPE;
	private static final VoxelShape SOUTH_SHAPE;
	private static final VoxelShape NORTH_SHAPE;

	private static final IntProperty AGE;

	public LeatherTanner(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(FACING, NORTH).with(AGE, 0));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
		builder.add(new Property[]{AGE});
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (getAge(state) == 1) {
			world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.LEATHER)));
		} else if (getAge(state) == 2) {
			world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CampanionItems.TANNED_LEATHER)));
		}
		super.onBreak(world, pos, state, player);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (getAge(state) == 0) {
			ItemStack stack = player.getStackInHand(hand);
			if (stack.getItem() == Items.LEATHER) {
				setAge(world, pos, state, 1);
				if (!player.isCreative()) {
					stack.setCount(stack.getCount() - 1);
				}
				return ActionResult.CONSUME;
			}
		} else if (getAge(state) == 1) {
			setAge(world, pos, state, 0);
			world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.LEATHER)));
			return ActionResult.SUCCESS;
		} else if (getAge(state) == 2) {
			setAge(world, pos, state, 0);
			world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(CampanionItems.TANNED_LEATHER)));
			return ActionResult.SUCCESS;
		}
		return ActionResult.FAIL;
	}

	public void setAge(World world, BlockPos pos, BlockState state, int age) {
		world.setBlockState(pos, state.with(AGE, age), 2);
	}

	public int getAge(BlockState state) {
		return (Integer) state.get(AGE);
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (getAge(state) == 1) {

			int speedMultiplier = 1;
			Direction facing = state.get(FACING);
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
				if (currState.getBlock() instanceof CampfireBlock && currState.get(LIT)) {
					speedMultiplier *= 1.5;
				}
			}

			if (random.nextInt(8 / speedMultiplier) == 0) {
				world.setBlockState(pos, state.with(AGE, 2), 2);
			}
		}
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

	static {
		VoxelShape shape = createCuboidShape(5, 0, 1, 10, 16, 15);

		EAST_SHAPE = shape;
		NORTH_SHAPE = rotate(Direction.EAST, NORTH, shape);
		SOUTH_SHAPE = rotate(Direction.EAST, Direction.SOUTH, shape);
		WEST_SHAPE = rotate(Direction.EAST, Direction.WEST, shape);

		AGE = Properties.AGE_2;
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
