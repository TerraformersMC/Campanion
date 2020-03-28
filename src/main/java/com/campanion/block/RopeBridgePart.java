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

public class RopeBridgePart extends Block implements BlockEntityProvider {

    public RopeBridgePart(Settings settings) {
        super(settings);
    }

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

                double xRange = Math.abs(Math.sin(plank.getyAngle()))*RopeBridge.PLANK_LENGTH/2 + 2/16F;
                double yRange = Math.abs(Math.sin(plank.getTiltAngle()))*RopeBridge.PLANK_WIDTH/2 + 2/16F;
                double zRange = Math.abs(Math.cos(plank.getyAngle()))*RopeBridge.PLANK_LENGTH/2 + 2/16F;

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
