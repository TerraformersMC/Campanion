package com.campanion.entity;

import com.campanion.item.SpearItem;
import com.campanion.network.S2CEntitySpawnPacket;
import com.campanion.sound.CampanionSoundEvents;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class SpearEntity extends ProjectileEntity {
	private static final TrackedData<Boolean> ENCHANTMENT_GLINT;
	private ItemStack spearStack;
	private boolean dealtDamage;
	private IntOpenHashSet piercedEntities;
	private List<Entity> piercingKilledEntities;

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

	private void clearPiercingStatus() {
		if (this.piercingKilledEntities != null) {
			this.piercingKilledEntities.clear();
		}

		if (this.piercedEntities != null) {
			this.piercedEntities.clear();
		}

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
		return this.dataTracker.get(ENCHANTMENT_GLINT);
	}

	protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
		return this.dealtDamage ? null : super.getEntityCollision(currentPosition, nextPosition);
	}

	protected void onEntityHit(EntityHitResult entityHitResult) {
		Entity hitEntity = entityHitResult.getEntity();
		float damage = 8.0F;
		if (hitEntity instanceof AnimalEntity) {
			int impalingLevel = EnchantmentHelper.getLevel(Enchantments.IMPALING, this.spearStack);
			if (impalingLevel > 0) {
				damage += impalingLevel * 1.5F;
			}
		}

		Entity owner = this.getOwner();
		DamageSource damageSource = createSpearDamageSource(this, owner == null ? this : owner);
		this.dealtDamage = true;
		SoundEvent soundEvent = CampanionSoundEvents.SPEAR_HIT_FLESH;
		if (hitEntity.damage(damageSource, damage)) {
			if (hitEntity.getType() == EntityType.ENDERMAN) {
				return;
			}

			if (hitEntity instanceof LivingEntity) {
				LivingEntity hitLivingEntity = (LivingEntity) hitEntity;
				if (owner instanceof LivingEntity) {
					EnchantmentHelper.onUserDamaged(hitLivingEntity, owner);
					EnchantmentHelper.onTargetDamaged((LivingEntity) owner, hitLivingEntity);
				}

				this.onHit(hitLivingEntity);

				if (!hitEntity.isAlive() && this.piercingKilledEntities != null) {
					this.piercingKilledEntities.add(hitLivingEntity);
				}
			}
		}

		if (this.getPierceLevel() <= 0) {
			this.setVelocity(this.getVelocity().multiply(-0.01D, -0.1D, -0.01D));
		}
		this.playSound(soundEvent, 1.0F, 1.0F);
	}

	protected SoundEvent getHitSound() {
		return CampanionSoundEvents.SPEAR_HIT_GROUND;
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
