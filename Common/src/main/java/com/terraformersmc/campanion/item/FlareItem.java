package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.entity.FlareEntity;
import com.terraformersmc.campanion.sound.CampanionSoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class FlareItem extends Item {

	public FlareItem(Properties settings) {
		super(settings);
	}

	protected final Random RANDOM = new Random();

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, @NotNull InteractionHand hand) {
		ItemStack itemStack = user.getItemInHand(hand);
		//TODO replace egg throw with flare crack and throw and fizzle away
		world.playSound(null, user.getX(), user.getY(), user.getZ(), CampanionSoundEvents.FLARE_STRIKE, SoundSource.NEUTRAL, 0.5F, 1.1F / (RANDOM.nextFloat() * 0.4F + 0.9F));
		if (!world.isClientSide) {
			FlareEntity flare = new FlareEntity(world, user);
			flare.setItem(itemStack);
			flare.shootFromRotation(user, user.getXRot(), user.getYRot(), 0.0F, 0.8F, 0.6F);
			world.addFreshEntity(flare);
		}
		user.awardStat(Stats.ITEM_USED.get(this));

		if (!user.getAbilities().instabuild) {
			itemStack.shrink(1);
		}

		return InteractionResultHolder.success(itemStack);
	}

}
