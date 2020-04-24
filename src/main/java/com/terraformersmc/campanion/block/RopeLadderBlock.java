package com.terraformersmc.campanion.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LadderBlock;
import net.minecraft.entity.LivingEntity;
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
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		BlockPos.Mutable progrees = new BlockPos.Mutable(pos);
		// Variable to get more ladders from other parts of the inventory if the one in the hand runs out
		int extraLaddersLocation = -1;
		// Boolean to check if we are using a stack different than the original one the player used
		boolean usingAnotherStack = false;
		// Boolean to check if the stack was decremented, needed to prevent decrementing twice the same stack
		boolean stackDecremented = false;
		// The ItemStack the user placed originally
		ItemStack originalStack = itemStack;
		int count;
		for (count = 0; count < pos.getY(); count++) {
			progrees.setOffset(Direction.DOWN);
			if (!canPlaceAt(state, world, progrees) || !world.getBlockState(progrees).isAir() || itemStack.getCount() < 1) {
				break;
			}
			// Check if we are using the original stack containing the item the player placed
			if (!usingAnotherStack) {
				// Check if the item count of the stack is greater than one
				if (itemStack.getCount() > 1) {
					// Place the ladder
					world.setBlockState(progrees, state);
				} else if (itemStack.getCount() == 1 || itemStack.getCount() == 0) {
					// Since the item count on the stack is either 1 or 0
					// Get if the placer is a player
					if (placer instanceof PlayerEntity) {
						// Try to get more ladders from the inventory
						extraLaddersLocation = tryToGetMoreLadders((PlayerEntity) placer, originalStack);
						// If we got more ladders
						if (extraLaddersLocation != -1) {
							// The current itemStack will be the ItemStack that has more ladders
							itemStack = ((PlayerEntity) placer).inventory.getInvStack(extraLaddersLocation);
							// Set usingAnotherStack to true
							usingAnotherStack = true;
						}
					}
				}
			}

			// If we are using another RopeLadderBlock stack that is different from the one containing the item the player placed
			if (usingAnotherStack) {
				// Check if the ItemStack count is more than 1
				if (itemStack.getCount() > 1) {
					// Place the item and let the logic below know that we have not manually decremented the stack count
					world.setBlockState(progrees, state);
					stackDecremented = false;
				} else if (itemStack.getCount() == 1) {
					// Since the ItemStack count is 1 place the RopeLadderBlock
					world.setBlockState(progrees, state);
					// Decrement the stack to make it 0 and let the logic below know that we already decremented the stack count
					itemStack.decrement(1);
					stackDecremented = true;

					// Get if the placer is a player
					if (placer instanceof PlayerEntity) {
						// Since we ran out of ladders, check if we have more ladders on other parts of the inventory
						extraLaddersLocation = tryToGetMoreLadders((PlayerEntity) placer, originalStack);
						// Check if we have more ladders
						if (extraLaddersLocation != -1) {
							// The current itemStack will be the ItemStack that has more ladders
							itemStack = ((PlayerEntity) placer).inventory.getInvStack(extraLaddersLocation);
							// Make sure we let the logic know we are using another stack different from the original since the original at this point still has count 1 that won't
							// be decremented until the end of this method call
							usingAnotherStack = true;
						}
					}
				}
			}
			// Check if the stack has not been decremented yet
			if (!stackDecremented) {
				if (!((PlayerEntity) placer).abilities.creativeMode) {
					itemStack.decrement(1);
				}
			}

		}
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
				ItemStack previousStack = stackInHand.copy();
				ActionResult actionResult = stackInHand.useOnBlock(new ItemUsageContext(player, hand, new BlockHitResult(Vec3d.ZERO, Direction.UP, attemptPos, false)));
				if (player.abilities.creativeMode && stackInHand == player.getStackInHand(hand) && stackInHand.getCount() < previousStack.getCount()) {
					stackInHand.setCount(previousStack.getCount());
				}
				if (actionResult.isAccepted()) {
					return actionResult;
				}
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockPos.Mutable progress = new BlockPos.Mutable(pos);
		int count;
		for (count = 0; count < pos.getY(); count++) {
			progress.setOffset(Direction.DOWN);
			if (!world.getBlockState(progress).getBlock().equals(CampanionBlocks.ROPE_LADDER)) {
				break;
			}
			world.setBlockState(progress, Blocks.AIR.getDefaultState());
		}
		if (!player.abilities.creativeMode) {
			player.giveItemStack(new ItemStack(CampanionBlocks.ROPE_LADDER, count + 1));
		}
	}

	/**
	 * Tries to get more ladders from other stacks in the player inventory
	 *
	 * @param player            the current player whose inventory will be scanned for RopeLadderBlock stacks
	 * @param originalItemStack the original item stack that the player placed
	 * @return returns the number in the inventory where the RopeLadderBlock stack was found. Returns -1 if no RopeLadderBlock was found in the player's inventory
	 **/
	private int tryToGetMoreLadders(PlayerEntity player, ItemStack originalItemStack) {
		// Iterate through the player's inventory
		for (int i = 0; i < player.inventory.getInvSize(); i++) {
			// Check if the item in the current position being iterated over is a RopeLadderBlock
			if (player.inventory.getInvStack(i).getItem().equals(CampanionBlocks.ROPE_LADDER.asItem())) {
				// Check if the item in the current position being iterated over is not the one in the hand that was used to put it
				if (player.inventory.getInvStack(i) != originalItemStack) {
					// Check if the item count on the current position being iterated over is more than 0
					if (player.inventory.getInvStack(i).getCount() > 0) {
						// Return the position of the item in the inventory
						return i;
					}
				}
			}
		}
		return -1;
	}


}
