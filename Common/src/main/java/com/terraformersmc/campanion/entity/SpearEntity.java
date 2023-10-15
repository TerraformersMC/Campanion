package com.terraformersmc.campanion.entity;

import com.terraformersmc.campanion.item.SpearItem;
import com.terraformersmc.campanion.sound.CampanionSoundEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SpearEntity extends AbstractArrow {
	private static final EntityDataAccessor<Boolean> ENCHANTMENT_GLINT;
	private ItemStack spearStack;
	private final Set<UUID> piercedEntities = new HashSet<>();

	public SpearEntity(EntityType<? extends SpearEntity> entityType, Level world, SpearItem item) {
		super(entityType, world);
		this.spearStack = new ItemStack(item);
	}

	public SpearEntity(Level world, LivingEntity owner, SpearItem item, ItemStack stack) {
		super(item.getType(), owner, world);
		this.spearStack = new ItemStack(item);
		this.spearStack = stack.copy();
		this.entityData.set(ENCHANTMENT_GLINT, stack.hasFoil());
	}

	public SpearEntity(Level world, double x, double y, double z, SpearItem item) {
		super(item.getType(), x, y, z, world);
		this.spearStack = new ItemStack(item);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(ENCHANTMENT_GLINT, false);
	}


	@Override
	protected ItemStack getPickupItem() {
		return this.spearStack.copy();
	}


	public boolean isEnchanted() {
		return this.entityData.get(ENCHANTMENT_GLINT);
	}

	@Override
	protected void onHitEntity(EntityHitResult entityHitResult) {
		int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, this.spearStack);
		Entity hitEntity = entityHitResult.getEntity();
		if (this.piercedEntities.contains(hitEntity.getUUID()) || this.piercedEntities.size() > level) {
			return;
		}
		this.piercedEntities.add(hitEntity.getUUID());
		float damage = ((SpearItem) this.spearStack.getItem()).getAttackDamage() * 2;
		if (hitEntity instanceof Animal) {
			int impalingLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.IMPALING, this.spearStack);
			if (impalingLevel > 0) {
				damage += impalingLevel * 1.5F;
			}
		}

		Entity owner = this.getOwner();
		DamageSource damageSource = createSpearDamageSource(this, owner == null ? this : owner);
		SoundEvent soundEvent = CampanionSoundEvents.SPEAR_HIT_FLESH;
		if (hitEntity.hurt(damageSource, damage)) {
			if (hitEntity.getType() == EntityType.ENDERMAN) {
				return;
			}

			if (hitEntity instanceof LivingEntity) {
				LivingEntity hitLivingEntity = (LivingEntity) hitEntity;
				if (owner instanceof LivingEntity) {
					EnchantmentHelper.doPostHurtEffects(hitLivingEntity, owner);
					EnchantmentHelper.doPostDamageEffects((LivingEntity) owner, hitLivingEntity);
				}

				this.doPostHurtEffects(hitLivingEntity);
			}
		}

		if (this.piercedEntities.size() > level) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));
		} else {
			this.setDeltaMovement(this.getDeltaMovement().scale(0.75));
		}
		this.playSound(soundEvent, 1.0F, 1.0F);
	}

	@Override
	protected SoundEvent getDefaultHitGroundSoundEvent() {
		return CampanionSoundEvents.SPEAR_HIT_GROUND;
	}

	@Override
	public void playerTouch(Player player) {
		Entity entity = this.getOwner();
		if (entity == null || entity.getUUID() == player.getUUID()) {
			super.playerTouch(player);
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		if (tag.contains("Item", 10)) {
			this.spearStack = ItemStack.of(tag.getCompound("Item"));
			this.entityData.set(ENCHANTMENT_GLINT, this.spearStack.hasFoil());
		}

		this.piercedEntities.clear();
		if (tag.contains("HitEntities", 9)) {
			for (Tag hitEntity : tag.getList("HitEntities", 10)) {
				this.piercedEntities.add(((CompoundTag) hitEntity).getUUID("UUID"));
			}
		}
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put("Item", this.spearStack.save(new CompoundTag()));

		ListTag tags = new ListTag();
		for (UUID uuid : this.piercedEntities) {
			CompoundTag c = new CompoundTag();
			c.putUUID("UUID", uuid);
			tags.add(c);
		}
		tag.put("HitEntities", tags);
	}

	@Override
	public void tickDespawn() {
		if (this.pickup != AbstractArrow.Pickup.ALLOWED) {
			super.tickDespawn();
		}
	}

	@Override
	public boolean shouldRender(double cameraX, double cameraY, double cameraZ) {
		return true;
	}

	static {
		ENCHANTMENT_GLINT = SynchedEntityData.defineId(net.minecraft.world.entity.projectile.ThrownTrident.class, EntityDataSerializers.BOOLEAN);
	}

	public static DamageSource createSpearDamageSource(Entity spear, Entity owner) {
		return new IndirectEntityDamageSource("spear", spear, owner).setProjectile();
	}


}
