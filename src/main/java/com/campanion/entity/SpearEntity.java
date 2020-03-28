package com.campanion.entity;

import com.campanion.item.SpearItem;
import com.campanion.network.S2CEntitySpawnPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SpearEntity extends ProjectileEntity {
	private static final TrackedData<Boolean> ENCHANTMENT_GLINT;
	private ItemStack spearStack;
	private boolean dealtDamage;

	public SpearEntity(EntityType<? extends SpearEntity> entityType, World world, SpearItem item) {
		super(entityType, world);
		this.spearStack = new ItemStack(item);
	}

	public SpearEntity(World world, LivingEntity owner, SpearItem item, ItemStack stack) {
		super(item.getType(), owner, world);
		this.spearStack = new ItemStack(item);
		this.spearStack = stack.copy();
		this.dataTracker.set(ENCHANTMENT_GLINT, stack.hasEnchantmentGlint());
	}

	@Environment(EnvType.CLIENT)
	public SpearEntity(World world, double x, double y, double z, SpearItem item) {
		super(item.getType(), x, y, z, world);
		this.spearStack = new ItemStack(item);
	}

	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(ENCHANTMENT_GLINT, false);
	}

	public void tick() {
		if (this.inGroundTime > 4) {
			this.dealtDamage = true;
		}

		Entity entity = this.getOwner();
		if ((this.dealtDamage || this.isNoClip()) && entity != null) {
			if (!this.isOwnerAlive()) {
				if (!this.world.isClient && this.pickupType == ProjectileEntity.PickupPermission.ALLOWED) {
					this.dropStack(this.asItemStack(), 0.1F);
				}

				this.remove();
			}
		}

		super.tick();
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return S2CEntitySpawnPacket.createPacket(this);
	}

	private boolean isOwnerAlive() {
		Entity entity = this.getOwner();
		if (entity != null && entity.isAlive()) {
			return !(entity instanceof ServerPlayerEntity) || !entity.isSpectator();
		} else {
			return false;
		}
	}

	protected ItemStack asItemStack() {
		return this.spearStack.copy();
	}

	@Environment(EnvType.CLIENT)
	public boolean method_23751() {
		return (Boolean) this.dataTracker.get(ENCHANTMENT_GLINT);
	}

	protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
		return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
	}

	protected void onEntityHit(EntityHitResult entityHitResult) {
		Entity entity = entityHitResult.getEntity();
		float f = 8.0F;
		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			f += EnchantmentHelper.getAttackDamage(this.spearStack, livingEntity.getGroup());
		}

		Entity owner = this.getOwner();
		DamageSource damageSource = createSpearDamageSource(this, owner == null ? this : owner);
		this.dealtDamage = true;
		SoundEvent soundEvent = SoundEvents.ITEM_TRIDENT_HIT;
		if (entity.damage(damageSource, f)) {
			if (entity.getType() == EntityType.ENDERMAN) {
				return;
			}

			if (entity instanceof LivingEntity) {
				LivingEntity livingEntity2 = (LivingEntity) entity;
				if (owner instanceof LivingEntity) {
					EnchantmentHelper.onUserDamaged(livingEntity2, owner);
					EnchantmentHelper.onTargetDamaged((LivingEntity) owner, livingEntity2);
				}

				this.onHit(livingEntity2);
			}
		}

		this.setVelocity(this.getVelocity().multiply(-0.01D, -0.1D, -0.01D));
		float g = 1.0F;
		if (this.world instanceof ServerWorld && this.world.isThundering() && EnchantmentHelper.hasChanneling(this.spearStack)) {
			BlockPos blockPos = entity.getBlockPos();
			if (this.world.isSkyVisible(blockPos)) {
				LightningEntity lightningEntity = new LightningEntity(this.world, (double) blockPos.getX() + 0.5D, (double) blockPos.getY(), (double) blockPos.getZ() + 0.5D, false);
				lightningEntity.setChanneller(owner instanceof ServerPlayerEntity ? (ServerPlayerEntity) owner : null);
				((ServerWorld) this.world).addLightning(lightningEntity);
				soundEvent = SoundEvents.ITEM_TRIDENT_THUNDER;
				g = 5.0F;
			}
		}

		this.playSound(soundEvent, g, 1.0F);
	}

	protected SoundEvent getHitSound() {
		return SoundEvents.ITEM_TRIDENT_HIT_GROUND;
	}

	public void onPlayerCollision(PlayerEntity player) {
		Entity entity = this.getOwner();
		if (entity == null || entity.getUuid() == player.getUuid()) {
			super.onPlayerCollision(player);
		}
	}

	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		if (tag.contains("Trident", 10)) {
			this.spearStack = ItemStack.fromTag(tag.getCompound("Trident"));
		}

		this.dealtDamage = tag.getBoolean("DealtDamage");
	}

	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.put("Trident", this.spearStack.toTag(new CompoundTag()));
		tag.putBoolean("DealtDamage", this.dealtDamage);
	}

	public void age() {
		if (this.pickupType != ProjectileEntity.PickupPermission.ALLOWED) {
			super.age();
		}

	}

	protected float getDragInWater() {
		return 0.99F;
	}

	@Environment(EnvType.CLIENT)
	public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
		return true;
	}

	static {
		ENCHANTMENT_GLINT = DataTracker.registerData(net.minecraft.entity.projectile.TridentEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	}

	public static DamageSource createSpearDamageSource(Entity trident, Entity owner) {
		return (new ProjectileDamageSource("spear", trident, owner)).setProjectile();
	}
}
