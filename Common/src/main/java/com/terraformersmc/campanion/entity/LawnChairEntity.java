package com.terraformersmc.campanion.entity;

import com.terraformersmc.campanion.block.LawnChairBlock;
import com.terraformersmc.campanion.blockentity.LawnChairBlockEntity;
import com.terraformersmc.campanion.network.S2CEntitySpawnGrapplingHookPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LawnChairEntity extends Entity {

    public LawnChairEntity(Level world, BlockPos pos) {
        this(world);
        this.setPos(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
    }

    public LawnChairEntity(Level world) {
        super(CampanionEntities.LAWN_CHAIR, world);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void baseTick() {
        super.baseTick();
        BlockPos linkedPosition = this.blockPosition();
        BlockEntity blockEntity = this.level.getBlockEntity(linkedPosition);

        if(BlockPos.ZERO.equals(linkedPosition) || !(this.level.getBlockState(linkedPosition).getBlock() instanceof LawnChairBlock) ||
            !(blockEntity instanceof LawnChairBlockEntity) || (!this.level.isClientSide && ((LawnChairBlockEntity) blockEntity).findOrCreateEntity() != this)) {
            this.kill();
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return 0.25D;
    }

    @Override
    protected void removePassenger(Entity passenger) {
        BlockPos pos = this.blockPosition();
        BlockState state = this.level.getBlockState(pos);
        if(state.getBlock() instanceof LawnChairBlock) {
            Direction d = state.getValue(LawnChairBlock.FACING);
            passenger.setPos(pos.getX() + d.getStepX() + 0.5D, pos.getY(), pos.getZ() + d.getStepZ() + 0.5D);
        }
        super.removePassenger(passenger);
    }

    @Override
    protected void addPassenger(Entity passenger) {
        BlockPos pos = this.blockPosition();
        BlockState state = this.level.getBlockState(pos);
        if(state.getBlock() instanceof LawnChairBlock) {
            Direction d = state.getValue(LawnChairBlock.FACING);
            passenger.setYBodyRot(d.get2DDataValue() * 90F);
        }
        super.addPassenger(passenger);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
