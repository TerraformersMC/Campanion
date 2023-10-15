package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.advancement.criterion.CampanionCriteria;
import com.terraformersmc.campanion.entity.SleepNoSetSpawnPlayer;
import com.terraformersmc.campanion.stat.CampanionStats;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class SleepingBagItem extends Item implements DyeableLeatherItem {

	public static final Component CANT_SLEEP_DAY = Player.BedSleepingProblem.NOT_POSSIBLE_NOW.getMessage();
	public static final Component NOT_SAFE = Player.BedSleepingProblem.NOT_SAFE.getMessage();
	public static final Component NOT_ON_GROUND = Component.translatable("item.campanion.sleeping_bag.not_on_ground");
	public static final Component TOO_WET = Component.translatable("item.campanion.sleeping_bag.too_wet");

	public SleepingBagItem(Properties settings) {
		super(settings);
	}

	@Override
	public int getColor(ItemStack stack) {
		CompoundTag compoundTag = stack.getTagElement("display");
		return compoundTag != null && compoundTag.contains("color", 99) ? compoundTag.getInt("color") : 0xffffff;
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, @NotNull InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand);
		if (!world.isClientSide) {
			BlockPos pos = user.blockPosition();
			if (!BedBlock.canSetSpawn(world)) {
				Vec3 vec3 = pos.getCenter();
				world.explode(null, world.damageSources().badRespawnPointExplosion(vec3), null, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 5.0F, true, Level.ExplosionInteraction.BLOCK);
				stack.hurtAndBreak(25, user, e -> e.broadcastBreakEvent(hand));
			} else if (world.isDay()) {
				user.displayClientMessage(CANT_SLEEP_DAY, true);
			} else if (!user.onGround()) {
				user.displayClientMessage(NOT_ON_GROUND, true);
			} else if (world.dimensionType().hasSkyLight()) {
				if (!user.isCreative()) {
					List<Monster> list = world.getEntitiesOfClass(Monster.class,
						new AABB(pos).move(0.5D, 0.0D, 0.5D).inflate(8.0D, 5.0D, 8.0D),
						hostileEntity -> hostileEntity.isPreventingPlayerRest(user));
					if (!list.isEmpty()) {
						user.displayClientMessage(NOT_SAFE, true);
						return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
					}
				}
				if (world.isRaining()) {
					if (!checkForCover(world, user.blockPosition().mutable())) {
						user.displayClientMessage(TOO_WET, true);
						return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
					}
				}
				((SleepNoSetSpawnPlayer) user).sleepWithoutSpawnPoint(pos);
				if (user instanceof ServerPlayer) {
					CampanionCriteria.SLEPT_IN_SLEEPING_BAG.trigger((ServerPlayer) user);
					user.awardStat(CampanionStats.SLEEP_IN_SLEEPING_BAG);
				}
				user.awardStat(Stats.ITEM_USED.get(this));
				setInUse(stack, true);
			}
		}
		return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
	}

	private boolean checkForCover(Level world, BlockPos.MutableBlockPos pos) {
		for (int i = pos.getY(); i < world.getHeight(); i++) {
			pos.move(Direction.UP);
			if (!world.getBlockState(pos).isAir()) {
				return true;
			}
		}
		return false;
	}

	public static Optional<ItemStack> getUsingStack(LivingEntity user) {
		if (user.getPose() == Pose.SLEEPING || user.getSleepingPos().isPresent()) {
			for (InteractionHand value : InteractionHand.values()) {
				ItemStack stack = user.getItemInHand(value);
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
