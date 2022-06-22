package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.entity.SkippingStoneEntity;
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

public class SkippingStoneItem extends Item {

	public SkippingStoneItem(Properties settings) {
		super(settings);
	}
	protected final Random RANDOM = new Random();

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
		if (!world.isClientSide) {
			SkippingStoneEntity stoneEntity = new SkippingStoneEntity(world, user);
			stoneEntity.setItem(itemStack);
			stoneEntity.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 1.5F, 1.0F);
			world.addFreshEntity(stoneEntity);
		}
		user.awardStat(Stats.ITEM_USED.get(this));

		if (!user.getAbilities().instabuild) {
			itemStack.shrink(1);
		}

		return InteractionResultHolder.success(itemStack);
	}
}
