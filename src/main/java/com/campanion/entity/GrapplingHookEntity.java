package com.campanion.entity;

import com.campanion.item.CampanionItems;
import com.campanion.network.S2CEntitySpawnPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ProjectileUtil;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

public class GrapplingHookEntity extends Entity {
	private static final TrackedData<Integer> HOOK_ENTITY_ID;
	private boolean stuckOnBlock;
	private int removalTimer;
	private final PlayerEntity player;
	private final GrapplingHookUser user;
	private int selfHitTimer;
	public Entity hookedEntity;
	private GrapplingHookEntity.State state;

	private GrapplingHookEntity(World world, PlayerEntity player) {
		super(CampanionEntities.GRAPPLING_HOOK, world);
		this.state = GrapplingHookEntity.State.FLYING;
		this.ignoreCameraFrustum = true;
		this.player = player;
		this.user = (GrapplingHookUser) player;
		this.user.setGrapplingHook(this);
	}

	@Environment(EnvType.CLIENT)
	public GrapplingHookEntity(World world, PlayerEntity player, double x, double y, double z) {
		this(world, player);
		this.updatePosition(x, y, z);
		this.prevX = this.getX();
		this.prevY = this.getY();
		this.prevZ = this.getZ();
	}

	public GrapplingHookEntity(PlayerEntity player, World world) {
		this(world, player);
		float playerPitch = this.player.pitch;
		float playerYaw = this.player.yaw;
		float h = MathHelper.cos(-playerYaw * 0.017453292F - 3.1415927F);
		float i = MathHelper.sin(-playerYaw * 0.017453292F - 3.1415927F);
		float j = -MathHelper.cos(-playerPitch * 0.017453292F);
		float k = MathHelper.sin(-playerPitch * 0.017453292F);
		double d = this.player.getX() - (double) i * 0.3D;
		double e = this.player.getEyeY();
		double l = this.player.getZ() - (double) h * 0.3D;
		this.refreshPositionAndAngles(d, e, l, playerYaw, playerPitch);
		Vec3d vec3d = new Vec3d(-i, MathHelper.clamp(-(k / j), -5.0F, 5.0F), -h);
		double m = vec3d.length();
		vec3d = vec3d.multiply(0.6D / m + 0.5D + this.random.nextGaussian() * 0.0045D, 0.6D / m + 0.5D + this.random.nextGaussian() * 0.0045D, 0.6D / m + 0.5D + this.random.nextGaussian() * 0.0045D);
		this.setVelocity(vec3d);
		this.yaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875D);
		this.pitch = (float) (MathHelper.atan2(vec3d.y, MathHelper.sqrt(squaredHorizontalLength(vec3d))) * 57.2957763671875D);
		this.prevYaw = this.yaw;
		this.prevPitch = this.pitch;
	}

	@Override
	protected void initDataTracker() {
		this.getDataTracker().startTracking(HOOK_ENTITY_ID, 0);
	}

	@Override
	public void onTrackedDataSet(TrackedData<?> data) {
		if (HOOK_ENTITY_ID.equals(data)) {
			int i = this.getDataTracker().get(HOOK_ENTITY_ID);
			this.hookedEntity = i > 0 ? this.world.getEntityById(i - 1) : null;
		}

		super.onTrackedDataSet(data);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean shouldRender(double distance) {
		return distance < 4096.0D;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
	}

	@Override
	public void tick() {
		super.tick();
		if (this.player == null) {
			this.remove();
		} else if (this.world.isClient || !this.removeIfInvalid()) {
			if (this.stuckOnBlock) {
				++this.removalTimer;
				if (this.removalTimer >= 1200) {
					this.remove();
					return;
				}
			}

			if (this.state == GrapplingHookEntity.State.FLYING) {
				if (this.hookedEntity != null) {
					this.setVelocity(Vec3d.ZERO);
					this.state = GrapplingHookEntity.State.HOOKED_IN_ENTITY;
					return;
				}

				if (!this.world.isClient) {
					this.checkForCollision();
				}

				if (!this.stuckOnBlock && !this.onGround && !this.horizontalCollision) {
					++this.selfHitTimer;
				} else {
					this.selfHitTimer = 0;
					this.setVelocity(Vec3d.ZERO);
				}
			} else {
				if (this.state == GrapplingHookEntity.State.HOOKED_IN_ENTITY) {
					if (this.hookedEntity != null) {
						if (this.hookedEntity.removed) {
							this.hookedEntity = null;
							this.state = GrapplingHookEntity.State.FLYING;
						} else {
							this.updatePosition(this.hookedEntity.getX(), this.hookedEntity.getBodyY(0.8D), this.hookedEntity.getZ());
						}
					}

					return;
				}
			}

			this.move(MovementType.SELF, this.getVelocity());
			this.smoothenMovement();
			this.setVelocity(this.getVelocity().multiply(1.2D));
			this.refreshPosition();
		}
	}

	private boolean removeIfInvalid() {
		ItemStack mainHandStack = this.player.getMainHandStack();
		ItemStack offHandStack = this.player.getOffHandStack();
		boolean inMainHand = mainHandStack.getItem() == CampanionItems.GRAPPLING_HOOK;
		boolean inOffHand = offHandStack.getItem() == CampanionItems.GRAPPLING_HOOK;
		if (!this.player.removed && this.player.isAlive() && (inMainHand || inOffHand) && this.squaredDistanceTo(this.player) <= 1024.0D) {
			return false;
		} else {
			this.remove();
			return true;
		}
	}

	private void smoothenMovement() {
		Vec3d vec3d = this.getVelocity();
		float f = MathHelper.sqrt(squaredHorizontalLength(vec3d));
		this.yaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875D);

		for (this.pitch = (float) (MathHelper.atan2(vec3d.y, f) * 57.2957763671875D); this.pitch - this.prevPitch < -180.0F; this.prevPitch -= 360.0F) {
		}

		while (this.pitch - this.prevPitch >= 180.0F) {
			this.prevPitch += 360.0F;
		}

		while (this.yaw - this.prevYaw < -180.0F) {
			this.prevYaw -= 360.0F;
		}

		while (this.yaw - this.prevYaw >= 180.0F) {
			this.prevYaw += 360.0F;
		}

		this.pitch = MathHelper.lerp(0.2F, this.prevPitch, this.pitch);
		this.yaw = MathHelper.lerp(0.2F, this.prevYaw, this.yaw);
	}

	private void checkForCollision() {
		HitResult hitResult = ProjectileUtil.getCollision(this, this.getBoundingBox().stretch(this.getVelocity()).expand(1.0D), (entity) -> !entity.isSpectator() && (entity.collides() || entity instanceof ItemEntity) && (entity != this.player || this.selfHitTimer >= 5), RayTraceContext.ShapeType.COLLIDER, true);
		if (hitResult.getType() != HitResult.Type.MISS) {
			if (hitResult.getType() == HitResult.Type.ENTITY) {
				this.hookedEntity = ((EntityHitResult) hitResult).getEntity();
				this.updateHookedEntityId();
			} else {
				this.stuckOnBlock = true;
			}
		}

	}

	private void updateHookedEntityId() {
		this.getDataTracker().set(HOOK_ENTITY_ID, this.hookedEntity.getEntityId() + 1);
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
	}

	public int use(ItemStack usedItem) {
		if (!this.world.isClient && this.player != null) {
			int i = 0;
			if (this.hookedEntity != null) {
				this.launchTowardsEntity();
				this.world.sendEntityStatus(this, (byte) 31);
				i = this.hookedEntity instanceof ItemEntity ? 3 : 5;
			}

			if (this.stuckOnBlock) {
				i = 2;
			}

			this.remove();
			return i;
		} else {
			return 0;
		}
	}

	@Override
	@Environment(EnvType.CLIENT)
	public void handleStatus(byte status) {
		if (status == 31 && this.world.isClient && this.hookedEntity != null) {
			this.launchTowardsEntity();
		}

		super.handleStatus(status);
	}

	protected void launchTowardsEntity() {
		if (this.player != null) {
			Vec3d vec3d = (new Vec3d(this.hookedEntity.getX() - this.getX(), this.hookedEntity.getY() - this.getY(), this.hookedEntity.getZ() - this.getZ())).multiply(0.5D);
			this.player.setVelocity(this.player.getVelocity().add(vec3d));
		}
	}

	@Override
	protected boolean canClimb() {
		return false;
	}

	@Override
	public void remove() {
		super.remove();
		if (this.player != null) {
			this.user.setGrapplingHook(null);
		}

	}

	public PlayerEntity getPlayer() {
		return this.player;
	}

	@Override
	public boolean canUsePortals() {
		return false;
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return S2CEntitySpawnPacket.createPacket(this);
	}

	static {
		HOOK_ENTITY_ID = DataTracker.registerData(GrapplingHookEntity.class, TrackedDataHandlerRegistry.INTEGER);
	}

	enum State {
		FLYING,
		HOOKED_IN_ENTITY;
	}
}
