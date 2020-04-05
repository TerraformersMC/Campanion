package com.terraformersmc.campanion.entity;

import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.network.S2CEntitySpawnPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ProjectileUtil;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RayTraceContext;
import net.minecraft.world.World;

import java.util.stream.Collectors;

public class GrapplingHookEntity extends Entity implements AdditionalSpawnDataEntity {

	private static final TrackedData<Boolean> IS_IN_BLOCK = DataTracker.registerData(GrapplingHookEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	private PlayerEntity player;
	private int grappleTicks = -1;

	private Vec3d previousPlayerPos;

	public GrapplingHookEntity(World world) {
		super(CampanionEntities.GRAPPLING_HOOK, world);
	}

	public GrapplingHookEntity(PlayerEntity player, World world) {
		this(world);
		this.player = player;
		float playerPitch = this.player.pitch;
		float playerYaw = this.player.yaw;

		float velX = -MathHelper.sin(playerYaw * 0.017453292F) * MathHelper.cos(playerPitch * 0.017453292F);
		float velY = -MathHelper.sin(playerPitch * 0.017453292F);
		float velZ = MathHelper.cos(playerYaw * 0.017453292F) * MathHelper.cos(playerPitch * 0.017453292F);
		this.setVelocity(new Vec3d(velX, velY, velZ).multiply(1.5F));

		this.prevYaw = this.yaw;
		this.prevPitch = this.pitch;

		this.refreshPositionAndAngles(this.player.getX(), this.player.getEyeY() - 0.1, this.player.getZ(), this.yaw, this.pitch);
	}

	@Override
	protected void initDataTracker() {
		this.dataTracker.startTracking(IS_IN_BLOCK, false);
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean shouldRender(double distance) {
		return distance < 16384.0D;
	}

	@Override
	public void tick() {
		super.tick();

		Vec3d vec3d = this.getVelocity();
		if (!this.dataTracker.get(IS_IN_BLOCK) && this.prevPitch == 0.0F && this.prevYaw == 0.0F) {
			float f = MathHelper.sqrt(squaredHorizontalLength(vec3d));
			this.yaw = (float)(MathHelper.atan2(vec3d.x, vec3d.z) * 57.2957763671875D);
			this.pitch = (float)(MathHelper.atan2(vec3d.y, (double)f) * 57.2957763671875D);
			this.prevYaw = this.yaw;
			this.prevPitch = this.pitch;
		}

		if (this.player == null) {
			this.remove();
		} else if (this.world.isClient || this.removeIfInvalid()) {
			if (!this.world.isClient) {
				this.checkForCollision();
			}
			if(!this.dataTracker.get(IS_IN_BLOCK)) {
				this.move(MovementType.SELF, this.getVelocity());
				this.setVelocity(this.getVelocity().add(0, -0.02D, 0));
				this.refreshPosition();
			} else {
				this.setVelocity(Vec3d.ZERO);
				this.grappleTicks++;
				this.ensureEntityVelocity();
			}
		}

		if(!this.dataTracker.get(IS_IN_BLOCK)) {
			this.smoothMovement();
		}
	}

	private void smoothMovement() {
		Vec3d vec3d = this.getVelocity();
		double d = vec3d.x;
		double e = vec3d.y;
		double g = vec3d.z;

		float l = MathHelper.sqrt(squaredHorizontalLength(vec3d));

		this.yaw = (float)(MathHelper.atan2(d, g) * 57.2957763671875D);

		for(this.pitch = (float)(MathHelper.atan2(e, (double)l) * 57.2957763671875D); this.pitch - this.prevPitch < -180.0F; this.prevPitch -= 360.0F) {
		}

		while(this.pitch - this.prevPitch >= 180.0F) {
			this.prevPitch += 360.0F;
		}

		while(this.yaw - this.prevYaw < -180.0F) {
			this.prevYaw -= 360.0F;
		}

		while(this.yaw - this.prevYaw >= 180.0F) {
			this.prevYaw += 360.0F;
		}

		this.pitch = MathHelper.lerp(0.2F, this.prevPitch, this.pitch);
		this.yaw = MathHelper.lerp(0.2F, this.prevYaw, this.yaw);
	}

	private boolean removeIfInvalid() {
		ItemStack mainHandStack = this.player.getMainHandStack();
		ItemStack offHandStack = this.player.getOffHandStack();
		boolean inMainHand = mainHandStack.getItem() == CampanionItems.GRAPPLING_HOOK;
		boolean inOffHand = offHandStack.getItem() == CampanionItems.GRAPPLING_HOOK;
		boolean isHookedEntity = ((GrapplingHookUser)this.player).getGrapplingHook() == this;
		double dist = this.squaredDistanceTo(this.player);
		if (
			this.player.removed || !this.player.isAlive() || !isHookedEntity
				|| (!inMainHand && !inOffHand) || dist > 16384 ||
				(this.dataTracker.get(IS_IN_BLOCK) && dist < 2)) {
			this.remove();
			return false;
		} else {
			return true;
		}
	}

	private void checkForCollision() {
		HitResult hitResult = ProjectileUtil.getCollision(
			this, this.getBoundingBox().expand(1.0D),
			entity -> false, RayTraceContext.ShapeType.COLLIDER, true
		);

		if (this.horizontalCollision || this.verticalCollision || hitResult.getType() == HitResult.Type.BLOCK && this.grappleTicks == -1) {
			this.previousPlayerPos = this.player.getPos();
			this.dataTracker.set(IS_IN_BLOCK, true);
			this.grappleTicks = 0;
		}
	}

	public PlayerEntity getPlayer() {
		return player;
	}

	@Override
	public void remove() {
		super.remove();
		((GrapplingHookUser)this.player).setGrapplingHook(null);
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
	}


	protected void ensureEntityVelocity() {
		if (this.player != null) {
			double dx = this.getX() - this.player.getX();
			double dy = this.getY() - this.player.getY() + 1;
			double dz = this.getZ() - this.player.getZ();
			double xzDist = (double)MathHelper.sqrt(dx * dx + dz * dz);

			double theta = Math.atan2(dy, xzDist);
			double xzTheta = Math.atan2(dz, dx);

			double xzAmount = Math.cos(theta);
			Vec3d movement = new Vec3d(xzAmount*Math.cos(xzTheta), Math.sin(theta) , xzAmount*Math.sin(xzTheta));

			boolean xCollide = false;
			boolean zCollide = false;

			Box box = this.player.getBoundingBox().stretch(movement.normalize().x, 0, movement.normalize().z);
			for (VoxelShape shape : this.world.getBlockCollisions(this.player, box).collect(Collectors.toList())) {
				xCollide |= box.contains(shape.getMinimum(Direction.Axis.X), box.y1, box.z1) || box.contains(shape.getMaximum(Direction.Axis.X), box.y1, box.z1);
				zCollide |= box.contains(box.x1, box.y1, shape.getMinimum(Direction.Axis.Z)) || box.contains(box.x1, box.y1, shape.getMaximum(Direction.Axis.Z));
			}

			if(xCollide && zCollide) {
				movement = new Vec3d(0, movement.y, 0);
			} else if(xCollide) {
				movement = new Vec3d(0, movement.y, xzAmount*Math.signum(movement.z));
			} else if(zCollide) {
				movement = new Vec3d(xzAmount*Math.signum(movement.x), movement.y, 0);
			}

//			movement = movement.normalize().add(0, -0.25, 0);

            movement = movement.normalize();

			Vec3d playerVelocity = this.player.getVelocity();
			Vec3d velocity = movement.subtract(movement.subtract(playerVelocity).multiply(0.5));
			this.player.setVelocity(velocity);
            this.player.velocityModified = true;
		}
	}

	@Override
	protected boolean canClimb() {
		return false;
	}

	@Override
	public boolean canUsePortals() {
		return false;
	}

	@Override
	public Packet<?> createSpawnPacket() {
		return S2CEntitySpawnPacket.createPacket(this);
	}

	@Override
	public void writeToBuffer(PacketByteBuf buffer) {
		if(this.player != null) {
			buffer.writeBoolean(true);
			buffer.writeInt(this.player.getEntityId());
		} else {
			buffer.writeBoolean(false);
		}
	}

	@Override
	public void readFromBuffer(PacketByteBuf buffer) {
		if(buffer.readBoolean()) {
			int i = buffer.readInt();
			Entity entity = this.world.getEntityById(i);
			if(entity instanceof PlayerEntity) {
				this.player = (PlayerEntity) entity;
				((GrapplingHookUser)this.player).setGrapplingHook(this);
			}
		}
	}
}
