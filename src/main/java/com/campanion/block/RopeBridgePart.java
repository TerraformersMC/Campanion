package com.campanion.block;

import com.campanion.blockentity.PlankBlockEntity;
import com.campanion.ropebridge.RopeBridge;
import com.campanion.ropebridge.RopeBridgePlank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import javax.annotation.Nullable;

public class RopeBridgePart extends Block implements BlockEntityProvider {

    public RopeBridgePart(Settings settings) {
        super(settings);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView view) {
        return new PlankBlockEntity();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getCullingShape(BlockState state, BlockView view, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean isSimpleFullBlock(BlockState state, BlockView view, BlockPos pos) {
        return super.isSimpleFullBlock(state, view, pos);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, EntityContext context) {
        BlockEntity entity = view.getBlockEntity(pos);
        if(entity instanceof PlankBlockEntity) {
            VoxelShape shape = VoxelShapes.empty();
            for (RopeBridgePlank plank : ((PlankBlockEntity) entity).getPlanks()) {

                //TODO: fix the bounding boxes
//                double minY = plank.getDeltaPosition().getY() - Math.abs(Math.sin(plank.getTiltAngle()))*RopeBridge.PLANK_WIDTH;
//                double maxY = plank.getDeltaPosition().getY() + Math.abs(Math.sin(plank.getTiltAngle()))*RopeBridge.PLANK_WIDTH;
//
//                double minX = plank.getDeltaPosition().getX() - Math.abs(Math.cos(plank.getyAngle()))*RopeBridge.PLANK_LENGTH;
//                double maxX = plank.getDeltaPosition().getX() + Math.abs(Math.cos(plank.getyAngle()))*RopeBridge.PLANK_LENGTH;
//
//                double minZ = plank.getDeltaPosition().getZ() - Math.abs(Math.sin(plank.getyAngle()))*RopeBridge.PLANK_WIDTH;
//                double maxZ = plank.getDeltaPosition().getZ() + Math.abs(Math.sin(plank.getyAngle()))*RopeBridge.PLANK_WIDTH;
//
//                shape = VoxelShapes.union(shape, VoxelShapes.cuboid(minX, minY, minZ, maxX, maxY, maxZ));
            }

            return shape;

        }
        return super.getOutlineShape(state, view, pos, context);
    }
}
