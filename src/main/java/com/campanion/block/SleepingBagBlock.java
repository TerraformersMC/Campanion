package com.campanion.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BedBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.BedPart;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.explosion.Explosion;

import java.util.List;
import java.util.Optional;

public class SleepingBagBlock extends HorizontalFacingBlock implements BlockEntityProvider {
	public static final EnumProperty<BedPart> PART;
	public static final BooleanProperty OCCUPIED;
	protected static final VoxelShape TOP_SHAPE;
	private final DyeColor color;

	public SleepingBagBlock(DyeColor color, Settings settings) {
		super(settings);
		this.color = color;
		this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(PART, BedPart.FOOT)).with(OCCUPIED, false));
	}

	public MaterialColor getMapColor(BlockState state, BlockView view, BlockPos pos) {
		return state.get(PART) == BedPart.FOOT ? this.color.getMaterialColor() : MaterialColor.WEB;
	}

	@Environment(EnvType.CLIENT)
	public static Direction getDirection(BlockView world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos);
		return blockState.getBlock() instanceof SleepingBagBlock ? (Direction)blockState.get(FACING) : null;
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.CONSUME;
		} else {
			if (state.get(PART) != BedPart.HEAD) {
				pos = pos.offset((Direction)state.get(FACING));
				state = world.getBlockState(pos);
				if (state.getBlock() != this) {
					return ActionResult.CONSUME;
				}
			}

			if (world.dimension.canPlayersSleep() && world.getBiome(pos) != Biomes.NETHER) {
				if ((Boolean)state.get(OCCUPIED)) {
					if (!this.hasVillager(world, pos)) {
						player.addChatMessage(new TranslatableText("block.minecraft.bed.occupied", new Object[0]), true);
					}

					return ActionResult.SUCCESS;
				} else {
					player.trySleep(pos).ifLeft((sleepFailureReason) -> {
						if (sleepFailureReason != null) {
							player.addChatMessage(sleepFailureReason.toText(), true);
						}

					});
					return ActionResult.SUCCESS;
				}
			} else {
				world.removeBlock(pos, false);
				BlockPos blockPos = pos.offset(((Direction)state.get(FACING)).getOpposite());
				if (world.getBlockState(blockPos).getBlock() == this) {
					world.removeBlock(blockPos, false);
				}

				world.createExplosion((Entity)null, DamageSource.netherBed(), (double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, 5.0F, true, Explosion.DestructionType.DESTROY);
				return ActionResult.SUCCESS;
			}
		}
	}

	private boolean hasVillager(World world, BlockPos blockPos) {
		List<VillagerEntity> list = world.getEntities(VillagerEntity.class, new Box(blockPos), LivingEntity::isSleeping);
		if (list.isEmpty()) {
			return false;
		} else {
			((VillagerEntity)list.get(0)).wakeUp();
			return true;
		}
	}

	public BlockState getStateForNeighborUpdate(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (facing == getDirectionTowardsOtherPart((BedPart)state.get(PART), (Direction)state.get(FACING))) {
			return neighborState.getBlock() == this && neighborState.get(PART) != state.get(PART) ? (BlockState)state.with(OCCUPIED, neighborState.get(OCCUPIED)) : Blocks.AIR.getDefaultState();
		} else {
			return super.getStateForNeighborUpdate(state, facing, neighborState, world, pos, neighborPos);
		}
	}

	private static Direction getDirectionTowardsOtherPart(BedPart part, Direction direction) {
		return part == BedPart.FOOT ? direction : direction.getOpposite();
	}

	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
		super.afterBreak(world, player, pos, Blocks.AIR.getDefaultState(), blockEntity, stack);
	}

	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BedPart bedPart = (BedPart)state.get(PART);
		BlockPos blockPos = pos.offset(getDirectionTowardsOtherPart(bedPart, (Direction)state.get(FACING)));
		BlockState blockState = world.getBlockState(blockPos);
		if (blockState.getBlock() == this && blockState.get(PART) != bedPart) {
			world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 35);
			world.playLevelEvent(player, 2001, blockPos, Block.getRawIdFromState(blockState));
			if (!world.isClient && !player.isCreative()) {
				ItemStack itemStack = player.getMainHandStack();
				dropStacks(state, world, pos, (BlockEntity)null, player, itemStack);
				dropStacks(blockState, world, blockPos, (BlockEntity)null, player, itemStack);
			}

			player.incrementStat(Stats.MINED.getOrCreateStat(this));
		}

		super.onBreak(world, pos, state, player);
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		Direction direction = ctx.getPlayerFacing();
		BlockPos blockPos = ctx.getBlockPos();
		BlockPos blockPos2 = blockPos.offset(direction);
		return ctx.getWorld().getBlockState(blockPos2).canReplace(ctx) ? (BlockState)this.getDefaultState().with(FACING, direction) : null;
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
		return TOP_SHAPE;
	}

	//No idea what this does
	public static Direction method_24163(BlockState blockState) {
		Direction direction = (Direction)blockState.get(FACING);
		return blockState.get(PART) == BedPart.HEAD ? direction.getOpposite() : direction;
	}

	//no idea what this does
	@Environment(EnvType.CLIENT)
	public static DoubleBlockProperties.Type method_24164(BlockState blockState) {
		BedPart bedPart = (BedPart)blockState.get(PART);
		return bedPart == BedPart.HEAD ? DoubleBlockProperties.Type.FIRST : DoubleBlockProperties.Type.SECOND;
	}

	public static Optional<Vec3d> findWakeUpPosition(EntityType<?> type, WorldView worldView, BlockPos pos, int index) {
		Direction direction = (Direction)worldView.getBlockState(pos).get(FACING);
		int i = pos.getX();
		int j = pos.getY();
		int k = pos.getZ();

		for(int l = 0; l <= 1; ++l) {
			int m = i - direction.getOffsetX() * l - 1;
			int n = k - direction.getOffsetZ() * l - 1;
			int o = m + 2;
			int p = n + 2;

			for(int q = m; q <= o; ++q) {
				for(int r = n; r <= p; ++r) {
					BlockPos blockPos = new BlockPos(q, j, r);
					Optional<Vec3d> optional = canWakeUpAt(type, worldView, blockPos);
					if (optional.isPresent()) {
						if (index <= 0) {
							return optional;
						}

						--index;
					}
				}
			}
		}

		return Optional.empty();
	}

	protected static Optional<Vec3d> canWakeUpAt(EntityType<?> type, WorldView worldView, BlockPos pos) {
		VoxelShape voxelShape = worldView.getBlockState(pos).getCollisionShape(worldView, pos);
		if (voxelShape.getMaximum(Direction.Axis.Y) > 0.4375D) {
			return Optional.empty();
		} else {
			BlockPos.Mutable mutable = new BlockPos.Mutable(pos);

			while(mutable.getY() >= 0 && pos.getY() - mutable.getY() <= 2 && worldView.getBlockState(mutable).getCollisionShape(worldView, mutable).isEmpty()) {
				mutable.setOffset(Direction.DOWN);
			}

			VoxelShape voxelShape2 = worldView.getBlockState(mutable).getCollisionShape(worldView, mutable);
			if (voxelShape2.isEmpty()) {
				return Optional.empty();
			} else {
				double d = (double)mutable.getY() + voxelShape2.getMaximum(Direction.Axis.Y) + 2.0E-7D;
				if ((double)pos.getY() - d > 2.0D) {
					return Optional.empty();
				} else {
					float f = type.getWidth() / 2.0F;
					Vec3d vec3d = new Vec3d((double)mutable.getX() + 0.5D, d, (double)mutable.getZ() + 0.5D);
					return worldView.doesNotCollide(new Box(vec3d.x - (double)f, vec3d.y, vec3d.z - (double)f, vec3d.x + (double)f, vec3d.y + (double)type.getHeight(), vec3d.z + (double)f)) ? Optional.of(vec3d) : Optional.empty();
				}
			}
		}
	}

	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}

	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(new Property[]{FACING, PART, OCCUPIED});
	}

	public BlockEntity createBlockEntity(BlockView view) {
		return new BedBlockEntity(this.color);
	}

	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		if (!world.isClient) {
			BlockPos blockPos = pos.offset((Direction)state.get(FACING));
			world.setBlockState(blockPos, (BlockState)state.with(PART, BedPart.HEAD), 3);
			world.updateNeighbors(pos, Blocks.AIR);
			state.updateNeighborStates(world, pos, 3);
		}

	}

	@Environment(EnvType.CLIENT)
	public DyeColor getColor() {
		return this.color;
	}

	@Environment(EnvType.CLIENT)
	public long getRenderingSeed(BlockState state, BlockPos pos) {
		BlockPos blockPos = pos.offset((Direction)state.get(FACING), state.get(PART) == BedPart.HEAD ? 0 : 1);
		return MathHelper.hashCode(blockPos.getX(), pos.getY(), blockPos.getZ());
	}

	public boolean canPlaceAtSide(BlockState world, BlockView view, BlockPos pos, BlockPlacementEnvironment env) {
		return false;
	}

	static {
		PART = Properties.BED_PART;
		OCCUPIED = Properties.OCCUPIED;
		TOP_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D);
	}
}
