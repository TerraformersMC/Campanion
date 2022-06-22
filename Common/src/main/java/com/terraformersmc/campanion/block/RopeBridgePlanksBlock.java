package com.terraformersmc.campanion.block;

import com.terraformersmc.campanion.blockentity.RopeBridgePlanksBlockEntity;
import com.terraformersmc.campanion.ropebridge.RopeBridgePlank;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.Set;

public class RopeBridgePlanksBlock extends Block implements EntityBlock {

	public RopeBridgePlanksBlock(Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new RopeBridgePlanksBlockEntity(pos, state);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getOcclusionShape(BlockState STATE, BlockGetter view, BlockPos pos) {
		return Shapes.empty();
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter view, BlockPos pos) {
		return true;
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof RopeBridgePlanksBlockEntity) {
			RopeBridgePlanksBlockEntity be = (RopeBridgePlanksBlockEntity) entity;

			this.scheduleRemoved(world, pos);
			boolean hasMaster = be.getPlanks().stream().anyMatch(RopeBridgePlank::isMaster);
			boolean removed = be.removeBroken();
			boolean deleted = be.getPlanks().isEmpty() && this.canBeCompletelyRemoved();
			if (removed && hasMaster) {
				world.levelEvent(2001, pos, getId(state));
			}
			if (deleted) {
				world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			}
		}
	}

	protected boolean canBeCompletelyRemoved() {
		return true;
	}

	private void scheduleRemoved(Level world, BlockPos pos) {
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
						BlockPos off = pos.offset(x, y, z);
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
			world.scheduleTick(neighbour, world.getBlockState(neighbour).getBlock(), 1);
		}
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		BlockEntity entity = world.getBlockEntity(pos);
		if (entity instanceof RopeBridgePlanksBlockEntity) {
			for (RopeBridgePlank plank : ((RopeBridgePlanksBlockEntity) entity).getPlanks()) {
				plank.setBroken();
			}
		}
		this.scheduleRemoved(world, pos);
		super.onRemove(state, world, pos, newState, moved);
	}

	@Override
	public VoxelShape getInteractionShape(BlockState state, BlockGetter view, BlockPos pos) {
		BlockEntity entity = view.getBlockEntity(pos);
		if (entity instanceof RopeBridgePlanksBlockEntity) {
			return ((RopeBridgePlanksBlockEntity) entity).getRaytraceShape();
		}
		return super.getInteractionShape(state, view, pos);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
		BlockEntity entity = view.getBlockEntity(pos);
		if (entity instanceof RopeBridgePlanksBlockEntity) {
			return ((RopeBridgePlanksBlockEntity) entity).getCollisionShape();
		}
		return super.getCollisionShape(state, view, pos, context);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext context) {
		BlockEntity entity = view.getBlockEntity(pos);
		if (entity instanceof RopeBridgePlanksBlockEntity) {
			return ((RopeBridgePlanksBlockEntity) entity).getOutlineShape();
		}
		return super.getShape(state, view, pos, context);
	}
}
