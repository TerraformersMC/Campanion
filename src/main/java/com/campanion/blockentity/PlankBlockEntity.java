package com.campanion.blockentity;

import com.campanion.ropebridge.RopeBridgePlank;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.ArrayList;
import java.util.List;

public class PlankBlockEntity extends BlockEntity implements BlockEntityClientSerializable {

    private final List<RopeBridgePlank> planks = new ArrayList<>();

    public PlankBlockEntity() {
        super(CampanionBlockEntities.RBP_BLOCK_ENTITY);
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
            this.planks.add(RopeBridgePlank.deserailize((CompoundTag) nbt));
        }
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        ListTag list = new ListTag();
        for (RopeBridgePlank plank : this.planks) {
            list.add(RopeBridgePlank.seralize(plank));
        }
        tag.put("Planks", list);
        return tag;
    }
}
