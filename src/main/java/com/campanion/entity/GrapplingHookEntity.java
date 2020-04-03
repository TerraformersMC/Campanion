package com.campanion.entity;

import com.campanion.item.CampanionItems;
import com.campanion.network.S2CEntitySpawnPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ProjectileUtil;
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

	private static final int MAX_GRAPPLE_TICKS = 100;

	private PlayerEntity player;
	private int grapleTicks = -1;

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
		this.setVelocity(velX, velY, velZ);
//		this.setVelocity(this.getVelocity().normalize().multiply(2.5F).add(player.getVelocity().x, player.onGround ? 0.0D : player.getVelocity().y, player.getVelocity().z));

		this.prevYaw = this.yaw;
		this.prevPitch = this.pitch;
		this.refreshPositionAndAngles(this.player.getX(), this.player.getEyeY() - 0.1, this.player.getZ(), this.yaw, this.pitch);
	}

	@Override
	protected void initDataTracker() {

	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean shouldRender(double distance) {
		return distance < 16384.0D;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.player == null) {
			this.remove();
		} else if (this.world.isClient || this.removeIfInvalid()) {
			if (!this.world.isClient) {
				this.checkForCollision();
			}
			if(this.grapleTicks == -1) {
				this.move(MovementType.SELF, this.getVelocity());
				this.setVelocity(this.getVelocity().add(0, -0.02D, 0));
				this.refreshPosition();
			} else {
				this.setVelocity(Vec3d.ZERO);
				this.grapleTicks++;
				this.ensureEntityVelocity();
			}
		}
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
				(this.grapleTicks != -1 && dist < 1) || (this.player.verticalCollision && !this.player.onGround)) {
			this.remove();
			((GrapplingHookUser)this.player).setGrapplingHook(null);
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

		if (this.horizontalCollision || this.verticalCollision || hitResult.getType() == HitResult.Type.BLOCK && this.grapleTicks == -1) {
			this.grapleTicks = 0;
		}
	}

	public PlayerEntity getPlayer() {
		return player;
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
			double dy = this.getY() - this.player.getY();
			double dz = this.getZ() - this.player.getZ();
			double xzDist = (double)MathHelper.sqrt(dx * dx + dz * dz);

			double theta = Math.atan2(dy, xzDist);
			double xzTheta = Math.atan2(dz, dx);

			double xzAmount = Math.cos(theta);
			Vec3d movement = new Vec3d(xzAmount*Math.cos(xzTheta), Math.sin(theta), xzAmount*Math.sin(xzTheta));

//			boolean xCollide = false;
//			boolean zCollide = false;
//
//			Box box = this.player.getBoundingBox().expand(0.1, this.player.onGround ? 0 : 0.2, 0.1);
//			for (VoxelShape shape : this.world.getBlockCollisions(this.player, box).collect(Collectors.toList())) {
//				xCollide |= box.contains(shape.getMinimum(Direction.Axis.X), box.y1, box.z1) || box.contains(shape.getMaximum(Direction.Axis.X), box.y1, box.z1);
//				zCollide |= box.contains(box.x1, box.y1, shape.getMinimum(Direction.Axis.Z)) || box.contains(box.x1, box.y1, shape.getMaximum(Direction.Axis.Z));
//			}
//
//			if(xCollide && zCollide) {
//				movement = new Vec3d(0, movement.y, 0);
//			} else if(xCollide) {
//				movement = new Vec3d(0, movement.y, xzAmount*Math.signum(movement.z));
//			} else if(zCollide) {
//				movement = new Vec3d(xzAmount*Math.signum(movement.x), movement.y, 0);
//			}

			this.player.setVelocity(movement.normalize().add(0, -0.25, 0));
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
			}
		}
	}
}
