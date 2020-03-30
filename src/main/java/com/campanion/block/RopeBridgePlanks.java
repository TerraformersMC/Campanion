package com.campanion.block;

import com.campanion.blockentity.RopeBridgePlanksBlockEntity;
import com.campanion.ropebridge.RopeBridge;
import com.campanion.ropebridge.RopeBridgePlank;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RopeBridgePlanks extends Block implements BlockEntityProvider {

	public RopeBridgePlanks(Settings settings) {
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView view) {
		return new RopeBridgePlanksBlockEntity();
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
	public boolean canSuffocate(BlockState state, BlockView view, BlockPos pos) {
		return false;
	}

	@Override
	public boolean isSimpleFullBlock(BlockState state, BlockView view, BlockPos pos) {
		return false;
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (this.shouldBreak(world, pos)) {
			world.breakBlock(pos, true);
		}
	}

	@Override
	public void onBlockRemoved(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		Set<BlockPos> neighbours = new HashSet<>();
		BlockEntity entity = world.getBlockEntity(pos);
		if(entity instanceof RopeBridgePlanksBlockEntity) {
			for (RopeBridgePlank plank : ((RopeBridgePlanksBlockEntity) entity).getPlanks()) {
				neighbours.add(plank.getPrevious());
				neighbours.add(plank.getNext());
			}
		}

		neighbours.remove(BlockPos.ORIGIN);
		for (BlockPos neighbour : neighbours) {
			world.getBlockTickScheduler().schedule(neighbour, this, 1);
		}
		super.onBlockRemoved(state, world, pos, newState, moved);
	}

	public boolean shouldBreak(WorldView world, BlockPos pos) {
		BlockEntity entity = world.getBlockEntity(pos);
		if(entity instanceof RopeBridgePlanksBlockEntity) {
			for (RopeBridgePlank plank : ((RopeBridgePlanksBlockEntity) entity).getPlanks()) {
				if(!BlockPos.ORIGIN.equals(plank.getPrevious()) && !(world.getBlockEntity(plank.getPrevious()) instanceof RopeBridgePlanksBlockEntity)) {
					return true;
				}
				if(!BlockPos.ORIGIN.equals(plank.getNext()) && !(world.getBlockEntity(plank.getNext()) instanceof RopeBridgePlanksBlockEntity)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
		BlockEntity entity = view.getBlockEntity(pos);
		if (entity instanceof RopeBridgePlanksBlockEntity) {
			VoxelShape shape = VoxelShapes.empty();
			for (RopeBridgePlank plank : ((RopeBridgePlanksBlockEntity) entity).getPlanks()) {

				double xRange = Math.abs(Math.sin(plank.getyAngle())) * RopeBridge.PLANK_LENGTH / 2 + 2 / 16F;
				double yRange = Math.abs(Math.sin(plank.getTiltAngle())) * RopeBridge.PLANK_WIDTH / 2 + 2 / 16F;
				double zRange = Math.abs(Math.cos(plank.getyAngle())) * RopeBridge.PLANK_LENGTH / 2 + 2 / 16F;

				double minY = plank.getDeltaPosition().getY() - yRange;
				double maxY = plank.getDeltaPosition().getY() + yRange;

				double minX = plank.getDeltaPosition().getX() - xRange;
				double maxX = plank.getDeltaPosition().getX() + xRange;

				double minZ = plank.getDeltaPosition().getZ() - zRange;
				double maxZ = plank.getDeltaPosition().getZ() + zRange;

				shape = VoxelShapes.union(shape, VoxelShapes.cuboid(minX, minY, minZ, maxX, maxY, maxZ));
			}

			return shape;

		}
		return super.getOutlineShape(state, view, pos, context);
	}
}
