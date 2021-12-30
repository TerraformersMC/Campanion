package com.terraformersmc.campanion.blockentity;

import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.entity.LawnChairEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.List;
import java.util.UUID;

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
		assert this.world != null;
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
	public void fromTag(NbtCompound tag) {
		this.entityUUID = tag.getUuid("EntityUUID");
		this.cachedEntity = null;
	}

	@Override
	public void toTag(NbtCompound tag) {
		tag.putUuid("EntityUUID", this.entityUUID);
	}

	public void fromClientTag(NbtCompound tag) {
		fromTag(tag);
	}

	public void toClientTag(NbtCompound tag) {
		toTag(tag);
	}
}
