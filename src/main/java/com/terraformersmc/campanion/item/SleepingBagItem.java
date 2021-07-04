package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.advancement.criterion.CampanionCriteria;
import com.terraformersmc.campanion.entity.SleepNoSetSpawnPlayer;
import com.terraformersmc.campanion.stat.CampanionStats;
import net.minecraft.block.BedBlock;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;
import java.util.Optional;

public class SleepingBagItem extends Item implements DyeableItem {

	public static final Text CANT_SLEEP_DAY = PlayerEntity.SleepFailureReason.NOT_POSSIBLE_NOW.toText();
	public static final Text NOT_SAFE = PlayerEntity.SleepFailureReason.NOT_SAFE.toText();
	public static final Text NOT_ON_GROUND = new TranslatableText("item.campanion.sleeping_bag.not_on_ground");
	public static final Text TOO_WET = new TranslatableText("item.campanion.sleeping_bag.too_wet");

	public SleepingBagItem(Settings settings) {
		super(settings);
	}

	@Override
	public int getColor(ItemStack stack) {
		NbtCompound compoundTag = stack.getSubTag("display");
		return compoundTag != null && compoundTag.contains("color", 99) ? compoundTag.getInt("color") : 0xffffff;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack stack = user.getStackInHand(hand);
		if (!world.isClient) {
			BlockPos pos = user.getBlockPos();
			if (!BedBlock.isOverworld(world)) {
				world.createExplosion(null, DamageSource.badRespawnPoint(), null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 5.0F, true, Explosion.DestructionType.DESTROY);
				stack.damage(25, user, e -> e.sendToolBreakStatus(hand));
			} else if (world.isDay()) {
				user.sendMessage(CANT_SLEEP_DAY, true);
			} else if (!user.isOnGround()) {
				user.sendMessage(NOT_ON_GROUND, true);
			} else if (world.getDimension().hasSkyLight()) {
				if (!user.isCreative()) {
					List<HostileEntity> list = world.getEntitiesByClass(HostileEntity.class,
							new Box(pos).offset(0.5D, 0.0D, 0.5D).expand(8.0D, 5.0D, 8.0D),
							hostileEntity -> hostileEntity.isAngryAt(user));
					if (!list.isEmpty()) {
						user.sendMessage(NOT_SAFE, true);
						return new TypedActionResult<>(ActionResult.SUCCESS, stack);
					}
				}
				if (world.isRaining()) {
					if (!checkForCover(world, user.getBlockPos().mutableCopy())) {
						user.sendMessage(TOO_WET, true);
						return new TypedActionResult<>(ActionResult.SUCCESS, stack);
					}
				}
				((SleepNoSetSpawnPlayer) user).sleepWithoutSpawnPoint(pos);
				if (user instanceof ServerPlayerEntity) {
					CampanionCriteria.SLEPT_IN_SLEEPING_BAG.trigger((ServerPlayerEntity) user);
					user.incrementStat(CampanionStats.SLEEP_IN_SLEEPING_BAG);
				}
				user.incrementStat(Stats.USED.getOrCreateStat(this));
				setInUse(stack, true);
			}
		}
		return new TypedActionResult<>(ActionResult.SUCCESS, stack);
	}

	private boolean checkForCover(World world, BlockPos.Mutable pos) {
		for (int i = pos.getY(); i < world.getHeight(); i++) {
			pos.move(Direction.UP);
			if (!world.getBlockState(pos).isAir()) {
				return true;
			}
		}
		return false;
	}

	public static Optional<ItemStack> getUsingStack(LivingEntity user) {
		if (user.getPose() == EntityPose.SLEEPING || user.isSleeping()) {
			for (Hand value : Hand.values()) {
				ItemStack stack = user.getStackInHand(value);
				if (inUse(stack)) {
					return Optional.of(stack);
				}
			}
		}
		return Optional.empty();
	}

	public static boolean inUse(ItemStack stack) {
		return stack.getItem() == CampanionItems.SLEEPING_BAG && stack.getOrCreateTag().getBoolean("IsInUse");
	}

	public static void setInUse(ItemStack stack, boolean inUse) {
		stack.getOrCreateTag().putBoolean("IsInUse", inUse);
	}
}
