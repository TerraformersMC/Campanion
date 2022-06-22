package com.terraformersmc.campanion.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class RopeLadderBlock extends LadderBlock {

	public RopeLadderBlock(Properties settings) {
		super(settings);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = super.getStateForPlacement(ctx);
		if (state == null) {
			state = defaultBlockState();
		}
		BlockState upperState = ctx.getLevel().getBlockState(ctx.getClickedPos().above());
		if (upperState.getBlock() == this) {
			return state.setValue(FACING, upperState.getValue(FACING));
		}
		return state;
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
		return super.canSurvive(state, world, pos) || world.getBlockState(pos.above()).getBlock() instanceof RopeLadderBlock;
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		BlockPos.MutableBlockPos progress = pos.mutable();
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
			progress.move(Direction.DOWN);
			if (!canSurvive(state, world, progress) || !world.getBlockState(progress).isAir() || itemStack.getCount() < 1) {
				break;
			}
			// Check if we are using the original stack containing the item the player placed
			if (!usingAnotherStack) {
				// Check if the item count of the stack is greater than one
				if (itemStack.getCount() > 1) {
					// Place the ladder
					world.setBlockAndUpdate(progress, state);
				} else if (itemStack.getCount() == 1 || itemStack.getCount() == 0) {
					// Since the item count on the stack is either 1 or 0
					// Get if the placer is a player
					if (placer instanceof Player) {
						// Try to get more ladders from the inventory
						extraLaddersLocation = tryToGetMoreLadders((Player) placer, originalStack);
						// If we got more ladders
						if (extraLaddersLocation != -1) {
							// The current itemStack will be the ItemStack that has more ladders
							itemStack = ((Player) placer).getInventory().getItem(extraLaddersLocation);
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
					world.setBlockAndUpdate(progress, state);
					stackDecremented = false;
				} else if (itemStack.getCount() == 1) {
					// Since the ItemStack count is 1 place the RopeLadderBlock
					world.setBlockAndUpdate(progress, state);
					// Decrement the stack to make it 0 and let the logic below know that we already decremented the stack count
					itemStack.shrink(1);
					stackDecremented = true;

					// Get if the placer is a player
					if (placer instanceof Player) {
						// Since we ran out of ladders, check if we have more ladders on other parts of the inventory
						extraLaddersLocation = tryToGetMoreLadders((Player) placer, originalStack);
						// Check if we have more ladders
						if (extraLaddersLocation != -1) {
							// The current itemStack will be the ItemStack that has more ladders
							itemStack = ((Player) placer).getInventory().getItem(extraLaddersLocation);
							// Make sure we let the logic know we are using another stack different from the original since the original at this point still has count 1 that won't
							// be decremented until the end of this method call
							usingAnotherStack = true;
						}
					}
				}
			}
			// Check if the stack has not been decremented yet
			if (!stackDecremented) {
				if (!((Player) placer).getAbilities().instabuild) {
					itemStack.shrink(1);
				}
			}

		}
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		ItemStack stackInHand = player.getItemInHand(hand);
		if (stackInHand.getItem() == CampanionBlocks.ROPE_LADDER.asItem()) {
			for (int i = 1; i < world.getHeight(); i++) {
				BlockPos attemptPos = pos.relative(Direction.DOWN, i);
				BlockState upperState = world.getBlockState(attemptPos.above());
				if (upperState.getBlock() != this && !upperState.isAir()) {
					return InteractionResult.FAIL;
				}
				ItemStack previousStack = stackInHand.copy();
				InteractionResult actionResult = stackInHand.useOn(new UseOnContext(player, hand, new BlockHitResult(Vec3.ZERO, Direction.UP, attemptPos, false)));
				if (player.getAbilities().instabuild && stackInHand == player.getItemInHand(hand) && stackInHand.getCount() < previousStack.getCount()) {
					stackInHand.setCount(previousStack.getCount());
				}
				if (actionResult.consumesAction()) {
					return actionResult;
				}
			}
		}
		return super.use(state, world, pos, player, hand, hit);
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		BlockPos.MutableBlockPos progress = pos.mutable();
		int count;
		for (count = 0; count < pos.getY(); count++) {
			progress.move(Direction.DOWN);
			if (!world.getBlockState(progress).getBlock().equals(CampanionBlocks.ROPE_LADDER)) {
				break;
			}
			world.setBlockAndUpdate(progress, Blocks.AIR.defaultBlockState());
		}
		if (!player.getAbilities().instabuild) {
			player.addItem(new ItemStack(CampanionBlocks.ROPE_LADDER, count + 1));
		}
	}

	/**
	 * Tries to get more ladders from other stacks in the player inventory
	 *
	 * @param player            the current player whose inventory will be scanned for RopeLadderBlock stacks
	 * @param originalItemStack the original item stack that the player placed
	 * @return returns the number in the inventory where the RopeLadderBlock stack was found. Returns -1 if no RopeLadderBlock was found in the player's inventory
	 **/
	private int tryToGetMoreLadders(Player player, ItemStack originalItemStack) {
		// Iterate through the player's inventory
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			// Check if the item in the current position being iterated over is a RopeLadderBlock
			if (player.getInventory().getItem(i).getItem().equals(CampanionBlocks.ROPE_LADDER.asItem())) {
				// Check if the item in the current position being iterated over is not the one in the hand that was used to put it
				if (player.getInventory().getItem(i) != originalItemStack) {
					// Check if the item count on the current position being iterated over is more than 0
					if (player.getInventory().getItem(i).getCount() > 0) {
						// Return the position of the item in the inventory
						return i;
					}
				}
			}
		}
		return -1;
	}
}
