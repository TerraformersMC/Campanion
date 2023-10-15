package com.terraformersmc.campanion.entity;

import com.terraformersmc.campanion.advancement.criterion.CampanionCriteria;
import com.terraformersmc.campanion.stat.CampanionStats;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class SkippingStoneEntity extends ThrowableItemProjectile {

	private static final EntityDataAccessor<Integer> NUMBER_OF_SKIPS = SynchedEntityData.defineId(SkippingStoneEntity.class, EntityDataSerializers.INT);

	public SkippingStoneEntity(Level world, LivingEntity owner) {
		super(EntityType.SNOWBALL, owner, world);
	}

	public SkippingStoneEntity(Level world, double x, double y, double z) {
		super(EntityType.SNOWBALL, x, y, z, world);
	}

	@Override
	protected @NotNull Item getDefaultItem() {
		return Items.SNOWBALL;
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(NUMBER_OF_SKIPS, 0);
		super.defineSynchedData();
	}

	private ParticleOptions getParticleParameters() {
		ItemStack itemStack = this.getItemRaw();
		return itemStack.isEmpty() ? ParticleTypes.SPLASH : new ItemParticleOption(ParticleTypes.ITEM, itemStack);
	}

	@Override
	public void handleEntityEvent(byte status) {
		if (status == 3) {
			ParticleOptions particleEffect = this.getParticleParameters();
			for (int i = 0; i < 8; ++i) {
				this.level().addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected void doWaterSplashEffect() {
		if (!this.level().isClientSide) {
			Vec3 vel = this.getDeltaMovement();
			double squaredHorizontalVelocity = (vel.x() * vel.x()) + (vel.z() * vel.z());

			this.level().broadcastEntityEvent(this, (byte) 3);
			if (vel.y() * vel.y() > squaredHorizontalVelocity || this.random.nextInt(3) == 0) {
				this.remove(RemovalReason.DISCARDED);
			} else {
				this.entityData.set(NUMBER_OF_SKIPS, this.entityData.get(NUMBER_OF_SKIPS) + 1);
				if (getOwner() instanceof Player player) {
					player.awardStat(CampanionStats.STONE_SKIPS);
				}
				this.push(0, (0.5 + -vel.y()) / 1.5, 0);
			}
		}
	}

	@Override
	public void onHit(HitResult hitResult) {
		if (hitResult.getType() == HitResult.Type.ENTITY) {
			Entity entity = ((EntityHitResult) hitResult).getEntity();
			entity.hurt(this.damageSources().thrown(this, this.getOwner()), this.entityData.get(NUMBER_OF_SKIPS) + 1);
			Entity owner = getOwner();
			if (!entity.isAlive() && owner != null) {
				CampanionCriteria.KILLED_WITH_STONE.trigger((ServerPlayer) owner, entity, this.entityData.get(NUMBER_OF_SKIPS));
			}
		}

		if (!this.level().isClientSide) {
			this.remove(RemovalReason.DISCARDED);
			this.level().broadcastEntityEvent(this, (byte) 3);
		}
	}

	@Override
	public void remove(@NotNull RemovalReason reason) {
		if (getOwner() instanceof ServerPlayer) {
			CampanionCriteria.STONE_SKIPS.trigger((ServerPlayer) getOwner(), this.entityData.get(NUMBER_OF_SKIPS));
		}
		super.remove(reason);
	}
}
