package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.entity.FlareEntity;
import com.terraformersmc.campanion.sound.CampanionSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class FlareItem extends Item {

	public FlareItem(Settings settings) {
		super(settings);
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		//TODO replace egg throw with flare crack and throw and fizzle away
		world.playSound(null, user.getX(), user.getY(), user.getZ(), CampanionSoundEvents.FLARE_STRIKE, SoundCategory.NEUTRAL, 0.5F, 1.1F / (RANDOM.nextFloat() * 0.4F + 0.9F));
		if (!world.isClient) {
			FlareEntity flare = new FlareEntity(world, user);
			flare.setItem(itemStack);
			flare.setProperties(user, user.pitch, user.yaw, 0.0F, 0.8F, 0.6F);
			world.spawnEntity(flare);
		}
		user.incrementStat(Stats.USED.getOrCreateStat(this));

		if (!user.abilities.creativeMode) {
			itemStack.decrement(1);
		}

		return TypedActionResult.success(itemStack);
	}

}
