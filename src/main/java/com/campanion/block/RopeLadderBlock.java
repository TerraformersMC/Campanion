package com.campanion.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class RopeLadderBlock extends LadderBlock {

	public RopeLadderBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockState state = super.getPlacementState(ctx);
		if (state == null) {
			state = getDefaultState();
		}
		BlockState upperState = ctx.getWorld().getBlockState(ctx.getBlockPos().up());
		if (upperState.getBlock() == this) {
			return state.with(FACING, upperState.get(FACING));
		}
		return state;
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return super.canPlaceAt(state, world, pos) || world.getBlockState(pos.up()).getBlock() instanceof RopeLadderBlock;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stackInHand = player.getStackInHand(hand);
		if (stackInHand.getItem() == CampanionBlocks.ROPE_LADDER.asItem()) {
			for (int i = 1; i < world.getHeight(); i++) {
				BlockPos attemptPos = pos.offset(Direction.DOWN, i);
				BlockState upperState = world.getBlockState(attemptPos.up());
				if (upperState.getBlock() != this && !upperState.isAir()) {
					return ActionResult.FAIL;
				}
				ActionResult actionResult = stackInHand.useOnBlock(new ItemUsageContext(player, hand, new BlockHitResult(Vec3d.ZERO, Direction.UP, attemptPos, false)));
				if (actionResult.isAccepted()) {
					return actionResult;
				}
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		return !state.canPlaceAt(world, pos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
	}
}
