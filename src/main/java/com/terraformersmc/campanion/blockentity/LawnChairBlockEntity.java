package com.terraformersmc.campanion.blockentity;

import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.entity.LawnChairEntity;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.UUID;

public class LawnChairBlockEntity extends BlockEntity implements BlockEntityClientSerializable {

	private UUID entityUUID = UUID.randomUUID();

	private LawnChairEntity cachedEntity;

	public LawnChairBlockEntity() {
		super(CampanionBlockEntities.LAWN_CHAIR);
	}

	public LawnChairEntity findOrCreateEntity() {
		if (this.cachedEntity != null && this.cachedEntity.isAlive()) {
			return this.cachedEntity;
		}
		List<LawnChairEntity> entities = this.world.getEntitiesByType(CampanionEntities.LAWN_CHAIR, new Box(this.pos).expand(2, 2, 2), e -> e.getBlockPos().equals(this.pos) && e.getUuid().equals(this.entityUUID));
		if (entities.isEmpty()) {
			this.cachedEntity = new LawnChairEntity(this.world, this.pos);
			this.world.spawnEntity(this.cachedEntity);
			this.entityUUID = this.cachedEntity.getUuid();
			this.markDirty();
			this.sync();
			return this.cachedEntity;
		}
		return this.cachedEntity = entities.get(0);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		return super.toTag(this.toClientTag(tag));
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		this.fromClientTag(tag);
		super.fromTag(state, tag);
	}

	@Override
	public void fromClientTag(CompoundTag tag) {
		this.entityUUID = tag.getUuid("EntityUUID");
		this.cachedEntity = null;
	}

	@Override
	public CompoundTag toClientTag(CompoundTag tag) {
		tag.putUuid("EntityUUID", this.entityUUID);
		return tag;
	}
}
