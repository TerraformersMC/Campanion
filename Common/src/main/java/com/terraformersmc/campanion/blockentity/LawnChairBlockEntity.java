package com.terraformersmc.campanion.blockentity;

import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.entity.LawnChairEntity;
import java.util.List;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class LawnChairBlockEntity extends SerializableBlockEntity {

	private UUID entityUUID = UUID.randomUUID();

	private LawnChairEntity cachedEntity;

	public LawnChairBlockEntity(BlockPos pos, BlockState state) {
		super(CampanionBlockEntities.LAWN_CHAIR, pos, state);
	}

	public LawnChairEntity findOrCreateEntity() {
		if (this.cachedEntity != null && this.cachedEntity.isAlive()) {
			return this.cachedEntity;
		}
		assert this.level != null;
		List<LawnChairEntity> entities = this.level.getEntities(CampanionEntities.LAWN_CHAIR, new AABB(this.worldPosition).inflate(2, 2, 2), e -> e.blockPosition().equals(this.worldPosition) && e.getUUID().equals(this.entityUUID));
		if (entities.isEmpty()) {
			this.cachedEntity = new LawnChairEntity(this.level, this.worldPosition);
			this.level.addFreshEntity(this.cachedEntity);
			this.entityUUID = this.cachedEntity.getUUID();
			this.setChanged();
			this.sync();
			return this.cachedEntity;
		}
		return this.cachedEntity = entities.get(0);
	}

	@Override
	public void fromTag(CompoundTag tag) {
		this.entityUUID = tag.getUUID("EntityUUID");
		this.cachedEntity = null;
	}

	@Override
	public void toTag(CompoundTag tag) {
		tag.putUUID("EntityUUID", this.entityUUID);
	}

	public void fromClientTag(CompoundTag tag) {
		fromTag(tag);
	}

	public void toClientTag(CompoundTag tag) {
		toTag(tag);
	}
}
