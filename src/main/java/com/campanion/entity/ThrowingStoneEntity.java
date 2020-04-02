package com.campanion.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.thrown.SnowballEntity;
import net.minecraft.entity.thrown.ThrownItemEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ThrowingStoneEntity extends ThrownItemEntity {

	public ThrowingStoneEntity(EntityType<? extends SnowballEntity> entityType, World world) {
		super(entityType, world);
	}

	public ThrowingStoneEntity(World world, LivingEntity owner) {
		super(EntityType.SNOWBALL, owner, world);
	}

	public ThrowingStoneEntity(World world, double x, double y, double z) {
		super(EntityType.SNOWBALL, x, y, z, world);
	}

	protected Item getDefaultItem() {
		return Items.SNOWBALL;
	}

	@Environment(EnvType.CLIENT)
	private ParticleEffect getParticleParameters() {
		ItemStack itemStack = this.getItem();
		return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.SPLASH : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
	}

	@Environment(EnvType.CLIENT)
	public void handleStatus(byte status) {
		if (status == 3) {
			ParticleEffect particleEffect = this.getParticleParameters();
			for(int i = 0; i < 8; ++i) {
				this.world.addParticle(particleEffect, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	protected void onSwimmingStart() {
		if (!this.world.isClient) {

			Vec3d vel = this.getVelocity();
			double squaredHorizontalVelocity = (vel.getX() * vel.getX()) + (vel.getZ() * vel.getZ());

			this.world.sendEntityStatus(this, (byte)3);
			if (vel.getY() * vel.getY() > squaredHorizontalVelocity || this.random.nextInt(3) == 0) {
				this.remove();
			} else {
				this.addVelocity(0, (0.5 + -vel.getY()) / 1.5, 0);
			}
		}
	}

	public void onCollision(HitResult hitResult) {
		if (hitResult.getType() == HitResult.Type.ENTITY) {
			Entity entity = ((EntityHitResult)hitResult).getEntity();
			entity.damage(DamageSource.thrownProjectile(this, this.getOwner()), 1.0F);
		}

		if (!this.world.isClient) {
			this.remove();
			this.world.sendEntityStatus(this, (byte)3);
		}
	}
}
