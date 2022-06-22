package com.terraformersmc.campanion.entity;

import com.terraformersmc.campanion.block.CampanionBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class FlareEntity extends ThrowableItemProjectile {

	public FlareEntity(Level world, LivingEntity owner) {
		super(EntityType.SNOWBALL, owner, world);
	}

	public FlareEntity(Level world, double x, double y, double z) {
		super(EntityType.SNOWBALL, x, y, z, world);
	}

	@Override
	protected Item getDefaultItem() {
		return Items.SNOWBALL;
	}

	@Environment(EnvType.CLIENT)
	private ParticleOptions getParticleParameters() {
		ItemStack itemStack = this.getItemRaw();
		return itemStack.isEmpty() ? ParticleTypes.LAVA : new ItemParticleOption(ParticleTypes.ITEM, itemStack);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void handleEntityEvent(byte status) {
		//Status 3 just means it was just destroyed
		if (status == 3) {
			ParticleOptions particleEffect = this.getParticleParameters();
			for (int i = 0; i < 8; ++i) {
				this.level.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected void doWaterSplashEffect() {
		//TODO spawn some smoke particles and destroy the entity
	}

	@Override
	public void onHit(HitResult hitResult) {
		if (hitResult.getType().equals(HitResult.Type.BLOCK)) {
			if (!getBlockStateOn().isAir()) {
				BlockPos pos = getOnPos().above();
				if (level.getBlockState(pos).isAir()) {
					level.setBlockAndUpdate(pos, CampanionBlocks.FLARE_BLOCK.defaultBlockState());
				}
				if (!this.level.isClientSide) {
					this.remove(RemovalReason.DISCARDED);
					this.level.broadcastEntityEvent(this, (byte) 3);
				}
			} else {
				this.setDeltaMovement(0, -0.1, 0);
			}
		}
	}
}
