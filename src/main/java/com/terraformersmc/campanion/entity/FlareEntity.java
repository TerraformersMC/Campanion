package com.terraformersmc.campanion.entity;

import com.terraformersmc.campanion.block.CampanionBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FlareEntity extends ThrownItemEntity {

	public FlareEntity(World world, LivingEntity owner) {
		super(EntityType.SNOWBALL, owner, world);
	}

	public FlareEntity(World world, double x, double y, double z) {
		super(EntityType.SNOWBALL, x, y, z, world);
	}

	@Override
	protected Item getDefaultItem() {
		return Items.SNOWBALL;
	}

	@Environment(EnvType.CLIENT)
	private ParticleEffect getParticleParameters() {
		ItemStack itemStack = this.getItem();
		return itemStack.isEmpty() ? ParticleTypes.LAVA : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void handleStatus(byte status) {
		//Status 3 just means it was just destroyed
		if (status == 3) {
			ParticleEffect particleEffect = this.getParticleParameters();
			for (int i = 0; i < 8; ++i) {
				this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected void onSwimmingStart() {
		//TODO spawn some smoke particles and destroy the entity
	}

	@Override
	public void onCollision(HitResult hitResult) {
		if (hitResult.getType().equals(HitResult.Type.BLOCK)) {
			if (!getLandingBlockState().isAir()) {
				BlockPos pos = getLandingPos().up();
				if (world.getBlockState(pos).isAir()) {
					world.setBlockState(pos, CampanionBlocks.FLARE_BLOCK.getDefaultState());
				}
				if (!this.world.isClient) {
					this.remove(RemovalReason.DISCARDED);
					this.world.sendEntityStatus(this, (byte) 3);
				}
			} else {
				this.setVelocity(0, -0.1, 0);
			}
		}
	}
}
