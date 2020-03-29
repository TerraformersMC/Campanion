package com.campanion.ropebridge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class RopeBridgePlank {
    private final Vec3d deltaPosition;
    private final double yAngle;
    private final double tiltAngle;
    private final int variant;
    private BlockPos previous = BlockPos.ORIGIN;
    private BlockPos next = BlockPos.ORIGIN;

    public Vec3d getDeltaPosition() {
        return deltaPosition;
    }

    public double getyAngle() {
        return yAngle;
    }

    public double getTiltAngle() {
        return tiltAngle;
    }

    public int getVariant() {
        return variant;
    }

    public BlockPos getPrevious() {
        return previous;
    }

    public void setPrevious(BlockPos previous) {
        this.previous = previous;
    }

    public BlockPos getNext() {
        return next;
    }

    public void setNext(BlockPos next) {
        this.next = next;
    }

    public RopeBridgePlank(Vec3d deltaPosition, double yAngle, double tiltAngle, int variant) {
        this.deltaPosition = deltaPosition;
        this.yAngle = yAngle;
        this.tiltAngle = tiltAngle;
        this.variant = variant;
    }

    public static RopeBridgePlank deserailize(CompoundTag tag) {
        RopeBridgePlank plank = new RopeBridgePlank(
            new Vec3d(tag.getDouble("DeltaPosX"), tag.getDouble("DeltaPosY"), tag.getDouble("DeltaPosZ")),
            tag.getDouble("YAng"),
            tag.getDouble("TiltAng"),
            tag.getInt("Variant"));
        plank.previous = BlockPos.fromLong(tag.getLong("Previous"));
        plank.next = BlockPos.fromLong(tag.getLong("Next"));
        return plank;
    }

    public static CompoundTag seralize(RopeBridgePlank plank) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("DeltaPosX", plank.deltaPosition.x);
        tag.putDouble("DeltaPosY", plank.deltaPosition.y);
        tag.putDouble("DeltaPosZ", plank.deltaPosition.z);
        tag.putDouble("YAng", plank.yAngle);
        tag.putDouble("TiltAng", plank.tiltAngle);
        tag.putInt("Variant", plank.variant);
        tag.putLong("Previous", plank.previous.asLong());
        tag.putLong("Next", plank.next.asLong());
        return tag;
    }
}
