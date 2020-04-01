package com.campanion.entity;

import com.campanion.block.LawnChairBlock;
import com.campanion.blockentity.LawnChairBlockEntity;
import com.campanion.network.S2CEntitySpawnPacket;
import com.campanion.tags.CampanionBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class LawnChairEntity extends Entity {

    private static final TrackedData<BlockPos> LINKED_POSITION = DataTracker.registerData(LawnChairEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);

    public LawnChairEntity(World world, BlockPos pos) {
        this(world);
        this.dataTracker.set(LINKED_POSITION, pos);
    }

    public LawnChairEntity(World world) {
        super(CampanionEntities.LAWN_CHAIR, world);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(LINKED_POSITION, BlockPos.ORIGIN);
    }

    public BlockPos getLinkedPosition() {
        return this.dataTracker.get(LINKED_POSITION);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        BlockPos linkedPosition = this.getLinkedPosition();
        BlockEntity blockEntity = this.world.getBlockEntity(linkedPosition);

        if(BlockPos.ORIGIN.equals(linkedPosition) || !this.world.getBlockState(linkedPosition).getBlock().matches(CampanionBlockTags.LAWN_CHAIR)||
            !(blockEntity instanceof LawnChairBlockEntity) || ((LawnChairBlockEntity) blockEntity).findOrCreateEntity() != this) {
            this.kill();
        }
        this.setPos(linkedPosition.getX() + 0.5, linkedPosition.getY() + 1D, linkedPosition.getZ() + 0.5);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        BlockPos pos = getLinkedPosition();
        BlockState state = this.world.getBlockState(pos);
        if(state.getBlock().matches(CampanionBlockTags.LAWN_CHAIR)) {
            Direction d = state.get(LawnChairBlock.FACING);
            passenger.setPos(pos.getX() + d.getOffsetX() + 0.5D, pos.getY() + 1D, pos.getZ() + d.getOffsetZ() + 0.5D);
        }
        super.removePassenger(passenger);
    }

    @Override
    protected void addPassenger(Entity passenger) {
        BlockPos pos = getLinkedPosition();
        BlockState state = this.world.getBlockState(pos);
        if(state.getBlock().matches(CampanionBlockTags.LAWN_CHAIR)) {
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
