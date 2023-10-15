package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.entity.GrapplingHookEntity;
import com.terraformersmc.campanion.entity.GrapplingHookUser;

import java.util.Random;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class GrapplingHookItem extends Item {
	public GrapplingHookItem(Item.Properties settings) {
		super(settings);
	}

	protected final Random RANDOM = new Random();

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
		GrapplingHookUser user = (GrapplingHookUser) player;
		ItemStack itemStack = player.getItemInHand(hand);
		GrapplingHookEntity hook = user.getGrapplingHook();

		if (hook == null || !hook.isAlive()) {
			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
			if (!world.isClientSide) {
				GrapplingHookEntity hookEntity = new GrapplingHookEntity(player, world);
				user.setGrapplingHook(hookEntity);
				world.addFreshEntity(hookEntity);
				itemStack.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(hand));
			}

			player.awardStat(Stats.ITEM_USED.get(this));
		} else if (!world.isClientSide) {
			hook.kill();
		}

		return InteractionResultHolder.success(itemStack);
	}

	@Override
	public int getEnchantmentValue() {
		return 1;
	}
}
