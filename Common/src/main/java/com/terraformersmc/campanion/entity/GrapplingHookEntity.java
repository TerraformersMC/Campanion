package com.terraformersmc.campanion.entity;

import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.network.S2CEntitySpawnGrapplingHookPacket;
import com.terraformersmc.campanion.platform.Services;
import com.terraformersmc.campanion.platform.services.OmniNetwork;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GrapplingHookEntity extends Entity {

	private static final EntityDataAccessor<Boolean> IS_IN_BLOCK = SynchedEntityData.defineId(GrapplingHookEntity.class, EntityDataSerializers.BOOLEAN);

	private Player player;
	private int grappleTicks = -1;

	private Vec3 previousPlayerPos;

	public GrapplingHookEntity(Level world) {
		super(CampanionEntities.GRAPPLING_HOOK, world);
	}

	public GrapplingHookEntity(Player player, Level world) {
		this(world);
		this.player = player;
		float playerPitch = this.player.getXRot();
		float playerYaw = this.player.getYRot();

		float velX = -Mth.sin(playerYaw * 0.017453292F) * Mth.cos(playerPitch * 0.017453292F);
		float velY = -Mth.sin(playerPitch * 0.017453292F);
		float velZ = Mth.cos(playerYaw * 0.017453292F) * Mth.cos(playerPitch * 0.017453292F);
		this.setDeltaMovement(new Vec3(velX, velY, velZ).scale(1.5F));

		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();

		this.moveTo(this.player.getX(), this.player.getEyeY() - 0.1, this.player.getZ(), this.getYRot(), this.getXRot());
	}

	@Override
	protected void defineSynchedData() {
		this.entityData.define(IS_IN_BLOCK, false);
	}

	@Override
	public boolean shouldRenderAtSqrDistance(double distance) {
		return distance < 16384.0D;
	}

	@Override
	protected void readAdditionalSaveData(CompoundTag compoundTag) {

	}

	@Override
	protected void addAdditionalSaveData(CompoundTag compoundTag) {

	}

	@Override
	public void tick() {
		super.tick();

		Vec3 vec3d = this.getDeltaMovement();
		if (!this.entityData.get(IS_IN_BLOCK) && this.xRotO == 0.0F && this.yRotO == 0.0F) {
			float f = Mth.sqrt((float) vec3d.horizontalDistanceSqr());
			this.setYRot((float) (Mth.atan2(vec3d.x, vec3d.z) * 57.2957763671875D));
			this.setXRot((float) (Mth.atan2(vec3d.y, (double) f) * 57.2957763671875D));
			this.yRotO = this.getYRot();
			this.xRotO = this.getXRot();
		}

		if (this.player == null) {
			this.remove(RemovalReason.DISCARDED);
		} else if (this.level.isClientSide || this.removeIfInvalid()) {
			if (!this.level.isClientSide) {
				this.checkForCollision();
			}
			if (!this.entityData.get(IS_IN_BLOCK)) {
				this.move(MoverType.SELF, this.getDeltaMovement());
				this.setDeltaMovement(this.getDeltaMovement().add(0, -0.02D, 0));
				this.reapplyPosition();
			} else {
				this.setDeltaMovement(Vec3.ZERO);
				this.grappleTicks++;
				this.ensureEntityVelocity();
			}
		}

		if (!this.entityData.get(IS_IN_BLOCK)) {
			this.smoothMovement();
		}
	}

	private void smoothMovement() {
		Vec3 vec3d = this.getDeltaMovement();
		double d = vec3d.x;
		double e = vec3d.y;
		double g = vec3d.z;

		float l = Mth.sqrt((float) vec3d.horizontalDistanceSqr());

		this.setYRot((float) (Mth.atan2(d, g) * 57.2957763671875D));

		for (this.setXRot((float) (Mth.atan2(e, (double) l) * 57.2957763671875D)); this.getXRot() - this.xRotO < -180.0F; this.xRotO -= 360.0F) {
		}

		while (this.getXRot() - this.xRotO >= 180.0F) {
			this.xRotO += 360.0F;
		}

		while (this.getYRot() - this.yRotO < -180.0F) {
			this.yRotO -= 360.0F;
		}

		while (this.getYRot() - this.yRotO >= 180.0F) {
			this.yRotO += 360.0F;
		}

		this.setXRot(Mth.lerp(0.2F, this.xRotO, this.getXRot()));
		this.setYRot(Mth.lerp(0.2F, this.yRotO, this.getYRot()));
	}

	private boolean removeIfInvalid() {
		ItemStack mainHandStack = this.player.getMainHandItem();
		ItemStack offHandStack = this.player.getOffhandItem();
		boolean inMainHand = mainHandStack.getItem() == CampanionItems.GRAPPLING_HOOK;
		boolean inOffHand = offHandStack.getItem() == CampanionItems.GRAPPLING_HOOK;
		boolean isHookedEntity = ((GrapplingHookUser) this.player).getGrapplingHook() == this;
		double dist = this.distanceToSqr(this.player);
		if (
				this.player.isRemoved() || !this.player.isAlive() || !isHookedEntity
						|| (!inMainHand && !inOffHand) || dist > 16384 ||
						(this.entityData.get(IS_IN_BLOCK) && dist < 2)) {
			this.remove(RemovalReason.DISCARDED);
			return false;
		} else {
			return true;
		}
	}

	private void checkForCollision() {
		HitResult hitResult = ProjectileUtil.getHitResult(this, entity -> false);

		if (hitResult.getType() == HitResult.Type.BLOCK && this.grappleTicks == -1) {
			this.previousPlayerPos = this.player.position();
			this.entityData.set(IS_IN_BLOCK, true);
			this.grappleTicks = 0;
		}
	}

	public Player getPlayer() {
		return player;
	}

	@Override
	public void remove(RemovalReason reason) {
		if (this.player != null) {
			((GrapplingHookUser) this.player).setGrapplingHook(null);
		}
		super.remove(RemovalReason.DISCARDED);
	}


	protected void ensureEntityVelocity() {
		if (this.player != null) {
			double dx = this.getX() - this.player.getX();
			double dy = this.getY() - this.player.getY() + 1;
			double dz = this.getZ() - this.player.getZ();
			double xzDist = (double) Mth.sqrt((float) (dx * dx + dz * dz));

			double theta = Math.atan2(dy, xzDist);
			double xzTheta = Math.atan2(dz, dx);

			double xzAmount = Math.cos(theta);
			Vec3 movement = new Vec3(xzAmount * Math.cos(xzTheta), Math.sin(theta), xzAmount * Math.sin(xzTheta));

			boolean xCollide = false;
			boolean zCollide = false;

			AABB box = this.player.getBoundingBox().expandTowards(movement.normalize().x, 0, movement.normalize().z);
			for (VoxelShape shape : this.level.getBlockCollisions(this.player, box)) {
				xCollide |= box.contains(shape.max(Direction.Axis.X), box.minY, box.minZ) || box.contains(shape.max(Direction.Axis.X), box.minY, box.minZ);
				zCollide |= box.contains(box.minX, box.minY, shape.min(Direction.Axis.Z)) || box.contains(box.minX, box.minY, shape.max(Direction.Axis.Z));
			}

			if (xCollide && zCollide) {
				movement = new Vec3(0, movement.y, 0);
			} else if (xCollide) {
				movement = new Vec3(0, movement.y, xzAmount * Math.signum(movement.z));
			} else if (zCollide) {
				movement = new Vec3(xzAmount * Math.signum(movement.x), movement.y, 0);
			}

//			movement = movement.normalize().add(0, -0.25, 0);

			movement = movement.normalize();

			Vec3 playerVelocity = this.player.getDeltaMovement();
			Vec3 velocity = movement.subtract(movement.subtract(playerVelocity).scale(0.5));
			this.player.setDeltaMovement(velocity);
			this.player.hurtMarked = true;
		}
	}

	@Override
	public boolean canChangeDimensions() {
		return false;
	}

	@Override
	public Packet<?> getAddEntityPacket() {
		return Services.NETWORK.toVanillaPacket(new S2CEntitySpawnGrapplingHookPacket(this), OmniNetwork.PacketType.PLAY_S2C);
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
