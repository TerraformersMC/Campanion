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
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

public class SpearEntity extends ProjectileEntity {
	private static final TrackedData<Boolean> ENCHANTMENT_GLINT;
	private ItemStack spearStack;
	private final Set<UUID> piercedEntities = new HashSet<>();

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

	@Override
	public Packet<?> createSpawnPacket() {
		return S2CEntitySpawnPacket.createPacket(this);
	}

	protected ItemStack asItemStack() {
		return this.spearStack.copy();
	}

	@Environment(EnvType.CLIENT)
	public boolean method_23751() {
		return this.dataTracker.get(ENCHANTMENT_GLINT);
	}

	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
		int level = EnchantmentHelper.getLevel(Enchantments.PIERCING, this.spearStack);
		Entity hitEntity = entityHitResult.getEntity();
		if(this.piercedEntities.contains(hitEntity.getUuid()) || this.piercedEntities.size() > level) {
			return;
		}
		this.piercedEntities.add(hitEntity.getUuid());
		float damage = 8.0F;
		if (hitEntity instanceof AnimalEntity) {
			int impalingLevel = EnchantmentHelper.getLevel(Enchantments.IMPALING, this.spearStack);
			if (impalingLevel > 0) {
				damage += impalingLevel * 1.5F;
			}
		}

		Entity owner = this.getOwner();
		DamageSource damageSource = createSpearDamageSource(this, owner == null ? this : owner);
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
			}
		}

		if (this.piercedEntities.size() > level) {
			this.setVelocity(this.getVelocity().multiply(-0.01D, -0.1D, -0.01D));
		} else {
			this.setVelocity(this.getVelocity().multiply(0.75));
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
			this.dataTracker.set(ENCHANTMENT_GLINT, this.spearStack.hasEnchantmentGlint());
		}

		this.piercedEntities.clear();
		if(tag.contains("HitEntities", 9)) {
			for (Tag hitEntity : tag.getList("HitEntities", 10)) {
				this.piercedEntities.add(((CompoundTag)hitEntity).getUuid("UUID"));
			}
		}
	}

	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.put("Trident", this.spearStack.toTag(new CompoundTag()));

		ListTag tags = new ListTag();
		for (UUID uuid : this.piercedEntities) {
			CompoundTag c = new CompoundTag();
			c.putUuid("UUID", uuid);
			tags.add(c);
		}
		tag.put("HitEntities", tags);
	}

	public void age() {
		if (this.pickupType != ProjectileEntity.PickupPermission.ALLOWED) {
			super.age();
		}
	}

	@Override
	public byte getPierceLevel() {
		return 0;
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
		return new ProjectileDamageSource("spear", trident, owner).setProjectile();
	}


}
