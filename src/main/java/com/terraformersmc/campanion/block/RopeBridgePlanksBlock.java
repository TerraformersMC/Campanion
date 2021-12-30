package com.terraformersmc.campanion.block;

import com.terraformersmc.campanion.blockentity.RopeBridgePlanksBlockEntity;
import com.terraformersmc.campanion.ropebridge.RopeBridgePlank;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RopeBridgePlanksBlock extends Block implements BlockEntityProvider {

	public RopeBridgePlanksBlock(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new RopeBridgePlanksBlockEntity(pos, state);
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getCullingShape(BlockState STATE, BlockView view, BlockPos pos) {
		return VoxelShapes.empty();
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView view, BlockPos pos) {
		return true;
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof RopeBridgePlanksBlockEntity) {
			RopeBridgePlanksBlockEntity be = (RopeBridgePlanksBlockEntity) entity;

			this.scheduleRemoved(world, pos);
			boolean hasMaster = be.getPlanks().stream().anyMatch(RopeBridgePlank::isMaster);
			boolean removed = be.removeBroken();
			boolean deleted = be.getPlanks().isEmpty() && this.canBeCompletelyRemoved();
			if (removed && hasMaster) {
				world.syncWorldEvent(2001, pos, getRawIdFromState(state));
			}
			if (deleted) {
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
		}
	}

	protected boolean canBeCompletelyRemoved() {
		return true;
	}

	private void scheduleRemoved(World world, BlockPos pos) {
		Set<Pair<BlockPos, BlockPos>> brokenLines = new HashSet<>();
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof RopeBridgePlanksBlockEntity) {
			for (RopeBridgePlank plank : ((RopeBridgePlanksBlockEntity) entity).getPlanks()) {
				if (plank.isBroken()) {
					brokenLines.add(Pair.of(plank.getFrom(), plank.getTo()));
				}
			}
		}

		Set<BlockPos> neighbours = new HashSet<>();
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				for (int z = -1; z <= 1; z++) {
					if (x != 0 || y != 0 || z != 0) {
						BlockPos off = pos.add(x, y, z);
						BlockEntity be = world.getBlockEntity(off);
						if (be instanceof RopeBridgePlanksBlockEntity) {
							((RopeBridgePlanksBlockEntity) be).getPlanks()
									.stream()
									.filter(plank -> brokenLines.contains(Pair.of(plank.getFrom(), plank.getTo())))
									.forEach(plank -> {
										plank.setBroken();
										neighbours.add(off);
									});
						}
					}
				}
			}
		}

		for (BlockPos neighbour : neighbours) {
			world.createAndScheduleBlockTick(neighbour, world.getBlockState(neighbour).getBlock(), 1);
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof RopeBridgePlanksBlockEntity) {
			for (RopeBridgePlank plank : ((RopeBridgePlanksBlockEntity) entity).getPlanks()) {
				plank.setBroken();
			}
		}
		this.scheduleRemoved(world, pos);
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public VoxelShape getRaycastShape(BlockState state, BlockView view, BlockPos pos) {
		BlockEntity entity = view.getBlockEntity(pos);
		if (entity instanceof RopeBridgePlanksBlockEntity) {
			return ((RopeBridgePlanksBlockEntity) entity).getRaytraceShape();
		}
		return super.getRaycastShape(state, view, pos);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		BlockEntity entity = view.getBlockEntity(pos);
		if (entity instanceof RopeBridgePlanksBlockEntity) {
			return ((RopeBridgePlanksBlockEntity) entity).getCollisionShape();
		}
		return super.getCollisionShape(state, view, pos, context);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		BlockEntity entity = view.getBlockEntity(pos);
		if (entity instanceof RopeBridgePlanksBlockEntity) {
			return ((RopeBridgePlanksBlockEntity) entity).getOutlineShape();
		}
		return super.getOutlineShape(state, view, pos, context);
	}
}
