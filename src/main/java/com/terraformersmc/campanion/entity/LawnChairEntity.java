package com.terraformersmc.campanion.entity;

import com.terraformersmc.campanion.block.LawnChairBlock;
import com.terraformersmc.campanion.blockentity.LawnChairBlockEntity;
import com.terraformersmc.campanion.network.S2CEntitySpawnPacket;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class LawnChairEntity extends Entity {

    public LawnChairEntity(World world, BlockPos pos) {
        this(world);
        this.updatePosition(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
    }

    public LawnChairEntity(World world) {
        super(CampanionEntities.LAWN_CHAIR, world);
    }

    @Override
    protected void initDataTracker() {
    }

    @Override
    public void baseTick() {
        super.baseTick();
        BlockPos linkedPosition = this.getBlockPos();
        BlockEntity blockEntity = this.world.getBlockEntity(linkedPosition);

        if(BlockPos.ORIGIN.equals(linkedPosition) || !(this.world.getBlockState(linkedPosition).getBlock() instanceof LawnChairBlock) ||
            !(blockEntity instanceof LawnChairBlockEntity) || (!this.world.isClient && ((LawnChairBlockEntity) blockEntity).findOrCreateEntity() != this)) {
            this.kill();
        }
    }

    @Override
    public double getMountedHeightOffset() {
        return 0.25D;
    }

    @Override
    protected void removePassenger(Entity passenger) {
        BlockPos pos = this.getBlockPos();
        BlockState state = this.world.getBlockState(pos);
        if(state.getBlock() instanceof LawnChairBlock) {
            Direction d = state.get(LawnChairBlock.FACING);
            passenger.updatePosition(pos.getX() + d.getOffsetX() + 0.5D, pos.getY(), pos.getZ() + d.getOffsetZ() + 0.5D);
        }
        super.removePassenger(passenger);
    }

    @Override
    protected void addPassenger(Entity passenger) {
        BlockPos pos = this.getBlockPos();
        BlockState state = this.world.getBlockState(pos);
        if(state.getBlock() instanceof LawnChairBlock) {
            Direction d = state.get(LawnChairBlock.FACING);
            passenger.setYaw(d.getHorizontal() * 90F);
        }
        super.addPassenger(passenger);
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag tag) {

    }

    @Override
    protected void writeCustomDataToTag(CompoundTag tag) {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        return S2CEntitySpawnPacket.createPacket(this);
    }
}
