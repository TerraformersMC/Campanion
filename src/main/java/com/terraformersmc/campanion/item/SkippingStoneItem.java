package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.entity.SkippingStoneEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Random;

public class SkippingStoneItem extends Item {

	public SkippingStoneItem(Settings settings) {
		super(settings);
	}
	protected final Random RANDOM = new Random();

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ENDER_PEARL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
		if (!world.isClient) {
			SkippingStoneEntity stoneEntity = new SkippingStoneEntity(world, user);
			stoneEntity.setItem(itemStack);
			stoneEntity.setProperties(user, user.getPitch(), user.getYaw(), 0.0F, 1.5F, 1.0F);
			world.spawnEntity(stoneEntity);
		}
		user.incrementStat(Stats.USED.getOrCreateStat(this));

		if (!user.getAbilities().creativeMode) {
			itemStack.decrement(1);
		}

		return TypedActionResult.success(itemStack);
	}
}
