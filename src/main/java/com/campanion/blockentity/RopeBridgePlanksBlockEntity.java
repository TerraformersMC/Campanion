package com.campanion.blockentity;

import com.campanion.ropebridge.RopeBridgePlank;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

public class RopeBridgePlanksBlockEntity extends BlockEntity implements BlockEntityClientSerializable {

    private final List<RopeBridgePlank> planks = new ArrayList<>();

    public RopeBridgePlanksBlockEntity() {
        super(CampanionBlockEntities.ROPE_BRIDGE_PLANK);
    }

    public List<RopeBridgePlank> getPlanks() {
        return this.planks;
    }

    @Override
    public void fromTag(CompoundTag tag) {
        this.fromClientTag(tag);
        super.fromTag(tag);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        return this.toClientTag(super.toTag(tag));
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        this.planks.clear();
        for (Tag nbt : tag.getList("Planks", 10)) {
            this.planks.add(RopeBridgePlank.deserialize((CompoundTag) nbt));
        }
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        ListTag list = new ListTag();
        for (RopeBridgePlank plank : this.planks) {
            list.add(RopeBridgePlank.serialize(plank));
        }
        tag.put("Planks", list);
        return tag;
    }
}
