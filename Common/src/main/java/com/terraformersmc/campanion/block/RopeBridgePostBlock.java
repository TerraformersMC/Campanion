package com.terraformersmc.campanion.block;

import com.terraformersmc.campanion.blockentity.RopeBridgePlanksBlockEntity;
import com.terraformersmc.campanion.blockentity.RopeBridgePostBlockEntity;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.ropebridge.RopeBridge;
import com.terraformersmc.campanion.ropebridge.RopeBridgePlank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RopeBridgePostBlock extends RopeBridgePlanksBlock {

	private static final String CLICKED_POSITION_KEY = "ClickedPosition";

	public RopeBridgePostBlock(Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new RopeBridgePostBlockEntity(pos, state);
	}

	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof RopeBridgePostBlockEntity) {
			RopeBridgePostBlockEntity be = (RopeBridgePostBlockEntity) entity;
			ItemStack stack = player.getItemInHand(hand);
			if (!world.isClientSide) {
				if (stack.getItem() == CampanionItems.ROPE) {
					CompoundTag tag = stack.getOrCreateTag();
					if (tag.contains(CLICKED_POSITION_KEY, 4)) {
						BlockPos clickedPos = BlockPos.of(tag.getLong(CLICKED_POSITION_KEY));
						tag.remove(CLICKED_POSITION_KEY);
						RopeBridge bridge = new RopeBridge(clickedPos, pos);
						Optional<Component> reason = bridge.getFailureReason();
						if (!reason.isPresent() && !player.isCreative()) {
							float xzDist = (float) Math.sqrt(pos.distSqr(clickedPos));
							int itemsToUse = Math.round(xzDist / RopeBridge.BLOCKS_PER_ROPE);
							if (stack.getCount() < itemsToUse) {
								reason = Optional.of(Component.translatable("message.campanion.rope_bridge.ropes", itemsToUse - stack.getCount()));
							} else {
								stack.shrink(itemsToUse);
							}
						}
						if (reason.isPresent()) {
							player.displayClientMessage(reason.get(), false);
							return InteractionResult.SUCCESS;
						} else {
							List<Pair<BlockPos, List<RopeBridgePlank>>> planks = bridge.generateBlocks(world);
							long failed = planks.stream().map(Pair::getLeft).filter(p -> !world.mayInteract(player, p)).count();
							if (failed > 1) {
								player.displayClientMessage(Component.translatable("message.campanion.rope_bridge.no_permission", failed), true);
								return InteractionResult.SUCCESS;
							}
							if (player.isCreative()) {

								//Check for obstructions
								for (Pair<BlockPos, ?> pair : planks) {
									BlockPos planksPos = pair.getLeft();
									if (!world.getBlockState(planksPos).canBeReplaced() && world.getBlockState(planksPos).getBlock() != CampanionBlocks.ROPE_BRIDGE_POST) {
										player.displayClientMessage(Component.translatable("message.campanion.rope_bridge.obstructed", planksPos.getX(), planksPos.getY(), planksPos.getZ(), Component.translatable(world.getBlockState(planksPos).getBlock().getDescriptionId())), false);
										return InteractionResult.PASS;
									}
								}

								planks.forEach(pair -> {
									BlockPos left = pair.getLeft();
									BlockEntity blockEntityTwo = world.getBlockEntity(left);
									if (!(blockEntityTwo instanceof RopeBridgePlanksBlockEntity)) {
										world.setBlockAndUpdate(left, CampanionBlocks.ROPE_BRIDGE_PLANKS.defaultBlockState());
										blockEntityTwo = world.getBlockEntity(left);
									}
									if (blockEntityTwo instanceof RopeBridgePlanksBlockEntity) {
										for (RopeBridgePlank plank : pair.getRight()) {
											((RopeBridgePlanksBlockEntity) blockEntityTwo).addPlank(plank);
										}
										blockEntityTwo.setChanged();
										((RopeBridgePlanksBlockEntity) blockEntityTwo).sync();
									}
								});

							} else {
								be.getGhostPlanks().put(clickedPos, planks);
								BlockEntity blockEntityTwo = world.getBlockEntity(clickedPos);
								if (blockEntityTwo instanceof RopeBridgePostBlockEntity) {
									((RopeBridgePostBlockEntity) blockEntityTwo).getLinkedPositions().add(pos);
								}
							}
							entity.setChanged();
							be.sync();
						}
					} else {
						tag.putLong(CLICKED_POSITION_KEY, pos.asLong());
					}
				}
				if (stack.is(ItemTags.PLANKS) && this.incrementBridge(world, player, be, pos, true) && !player.isCreative()) {
					stack.shrink(1);
				}
			}
		}
		return InteractionResult.CONSUME;
	}

	@Override
	public void onRemove(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean moved) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof RopeBridgePostBlockEntity) {
			for (BlockPos position : ((RopeBridgePostBlockEntity) entity).getLinkedPositions()) {
				BlockEntity other = world.getBlockEntity(position);
				if (other instanceof RopeBridgePostBlockEntity) {
					((RopeBridgePostBlockEntity) other).getLinkedPositions().remove(pos);
					((RopeBridgePostBlockEntity) other).getGhostPlanks().remove(pos);

					if (!world.isClientSide) {
						other.setChanged();
						((RopeBridgePostBlockEntity) other).sync();
					}
				}
			}
		}
		super.onRemove(state, world, pos, newState, moved);
	}

	@Override
	public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction facing, @NotNull BlockState neighborState, @NotNull LevelAccessor world, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
		if (facing == Direction.DOWN && !world.getBlockState(pos.below()).isFaceSturdy(world, pos, Direction.UP)) {
			world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
		}
		return super.updateShape(state, facing, neighborState, world, pos, neighborPos);
	}

	@Override
	public boolean canSurvive(@NotNull BlockState state, LevelReader world, BlockPos pos) {
		return world.getBlockState(pos.below()).isFaceSturdy(world, pos, Direction.UP);
	}

	private boolean incrementBridge(Level world, Player player, RopeBridgePostBlockEntity blockEntity, BlockPos pos, boolean generateLinked) {
		BlockPos otherPos = null;
		List<Pair<BlockPos, List<RopeBridgePlank>>> list = null;
		for (Map.Entry<BlockPos, List<Pair<BlockPos, List<RopeBridgePlank>>>> entry : blockEntity.getGhostPlanks().entrySet()) {
			otherPos = entry.getKey();
			list = entry.getValue();
			break;
		}
		if (list == null || otherPos == null || list.isEmpty()) {
			if (generateLinked) {
				for (BlockPos position : blockEntity.getLinkedPositions()) {
					BlockEntity linkedEntity = world.getBlockEntity(position);
					if (linkedEntity instanceof RopeBridgePostBlockEntity) {
						return this.incrementBridge(world, player, (RopeBridgePostBlockEntity) linkedEntity, position, false);
					}
				}
			}
			return false;
		}
		for (int i = 0; i < RopeBridge.PLANKS_PER_ITEM; i++) {
			if (list.isEmpty()) {
				break;
			}
			for (Pair<BlockPos, List<RopeBridgePlank>> plankPair : list) {
				BlockPos planksPos = plankPair.getLeft();
				List<RopeBridgePlank> planks = plankPair.getRight();

				BlockEntity be = world.getBlockEntity(planksPos);
				for (int j = 0; j < Math.min(RopeBridge.PLANKS_PER_ITEM - i, planks.size()); j++) {
					if (!(be instanceof RopeBridgePlanksBlockEntity)) {
						BlockState state = world.getBlockState(planksPos);
						if (!state.canBeReplaced()) {
							player.displayClientMessage(Component.translatable("message.campanion.rope_bridge.obstructed", planksPos.getX(), planksPos.getY(), planksPos.getZ(), Component.translatable(world.getBlockState(planksPos).getBlock().getDescriptionId())), false);
							return false;
						}
						world.setBlockAndUpdate(planksPos, CampanionBlocks.ROPE_BRIDGE_PLANKS.defaultBlockState());
						be = world.getBlockEntity(planksPos);
					}

					if (be instanceof RopeBridgePlanksBlockEntity planksBlockEntity) {
						RopeBridgePlank plank = planks.get(0);
						planksBlockEntity.addPlank(plank);
						planks.remove(plank);
						planksBlockEntity.setChanged();
						planksBlockEntity.sync();
						if (plank.master()) {
							i++;
						} else {
							j--;
						}
					}
				}
			}
			list.removeIf(p -> p.getRight().isEmpty());
		}
		if (list.isEmpty()) {
			if (!world.isClientSide) {
				blockEntity.getLinkedPositions().remove(otherPos);
				blockEntity.getGhostPlanks().remove(otherPos);

				blockEntity.setChanged();
				blockEntity.sync();

				BlockEntity other = world.getBlockEntity(otherPos);
				if (other instanceof RopeBridgePostBlockEntity) {
					((RopeBridgePostBlockEntity) other).getLinkedPositions().remove(pos);
					((RopeBridgePostBlockEntity) other).getGhostPlanks().remove(pos);
					other.setChanged();
					((RopeBridgePostBlockEntity) other).sync();
				}
			}
			player.displayClientMessage(Component.translatable("message.campanion.rope_bridge.finished"), false);
		} else {
			double counted = list.stream().flatMap(p -> p.getRight().stream()).filter(RopeBridgePlank::master).count();
			player.displayClientMessage(Component.translatable("message.campanion.rope_bridge.constructed", Math.round(counted / RopeBridge.PLANKS_PER_ITEM) + 1), true);
		}
		return true;
	}

	@Override
	protected boolean canBeCompletelyRemoved() {
		return false;
	}
}
