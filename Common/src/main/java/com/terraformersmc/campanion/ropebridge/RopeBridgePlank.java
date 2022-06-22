package com.terraformersmc.campanion.ropebridge;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;

public class RopeBridgePlank {
    private final BlockPos from;
    private final BlockPos to;
    private final Vec3 deltaPosition;
    private final float yAngle;
    private final float tiltAngle;
    private final float distToPrevious;
    private final float ropesSubtract;
    private final int ropeVariant; //Between 0-127
    private final boolean flat;
    private final boolean master;
    private final boolean ropes;
    private final boolean stopper;
    private boolean broken;

    public RopeBridgePlank(
        BlockPos from, BlockPos to, Vec3 deltaPosition, double yAngle, double tiltAngle, float distToPrevious,
        float ropesSubtract, int ropeVariant, boolean flat, boolean master, boolean ropes, boolean stopper
    ) {
        this.from = from;
        this.to = to;
        this.deltaPosition = deltaPosition;
        this.yAngle = (float) yAngle;
        this.tiltAngle = (float) tiltAngle;
        this.distToPrevious = distToPrevious;
        this.ropesSubtract = ropesSubtract;
        this.ropeVariant = ropeVariant;
        this.flat = flat;
        this.master = master;
        this.ropes = ropes;
        this.stopper = stopper;
    }

    public Vec3 deltaPosition() {
        return deltaPosition;
    }

    public float yAngle() {
        return yAngle;
    }

    public double tiltAngle() {
        return tiltAngle;
    }

    public boolean master() {
        return master;
    }

	public boolean flat() {
		return flat;
	}

	public boolean ropes() {
		return ropes;
	}

	public boolean stopper() {
        return stopper;
    }

    public boolean broken() {
        return broken;
    }

    public void setBroken() {
        this.broken = true;
    }

    public BlockPos from() {
        return from;
    }

    public BlockPos to() {
        return to;
    }

	public int ropeVariant() {
		return ropeVariant;
	}

	public float ropesSubtract() {
		return ropesSubtract;
	}

	public float distToPrevious() {
		return distToPrevious;
	}

	public static RopeBridgePlank deserialize(CompoundTag tag) {
        RopeBridgePlank plank = new RopeBridgePlank(
            BlockPos.of(tag.getLong("FromPos")),
            BlockPos.of(tag.getLong("ToPos")),
            new Vec3(tag.getDouble("DeltaPosX"), tag.getDouble("DeltaPosY"), tag.getDouble("DeltaPosZ")),
            tag.getFloat("YAng"),
            tag.getFloat("TiltAng"),
            tag.getFloat("DistToPrevious"),
            tag.getFloat("RopesSubtract"),
            tag.getByte("RopeVariant"),
            tag.getBoolean("Flat"),
            tag.getBoolean("Master"),
            tag.getBoolean("Ropes"),
            tag.getBoolean("Stopper")
        );
        plank.broken = tag.getBoolean("Broken");
        return plank;
    }

    public static CompoundTag serialize(RopeBridgePlank plank) {
        CompoundTag tag = new CompoundTag();
        tag.putLong("FromPos", plank.from.asLong());
        tag.putLong("ToPos", plank.to.asLong());
        tag.putDouble("DeltaPosX", plank.deltaPosition.x);
        tag.putDouble("DeltaPosY", plank.deltaPosition.y);
        tag.putDouble("DeltaPosZ", plank.deltaPosition.z);
        tag.putFloat("YAng", plank.yAngle);
        tag.putFloat("TiltAng", plank.tiltAngle);
        tag.putFloat("DistToPrevious", plank.distToPrevious);
        tag.putFloat("RopesSubtract", plank.ropesSubtract);
        tag.putByte("RopeVariant", (byte) plank.ropeVariant);
        tag.putBoolean("Flat", plank.flat);
        tag.putBoolean("Master", plank.master);
        tag.putBoolean("Ropes", plank.ropes);
        tag.putBoolean("Stopper", plank.stopper);
        tag.putBoolean("Broken", plank.broken);
        return tag;
    }


}
