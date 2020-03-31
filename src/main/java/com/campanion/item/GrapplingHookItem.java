package com.campanion.item;

import com.campanion.entity.GrapplingHookEntity;
import com.campanion.entity.GrapplingHookUser;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class GrapplingHookItem extends Item {
	public GrapplingHookItem(Item.Settings settings) {
		super(settings);
		this.addPropertyGetter(new Identifier("cast"), (stack, world, entity) -> {
			if (entity == null) {
				return 0.0F;
			} else {
				boolean inMainHand = entity.getMainHandStack() == stack;
				boolean inOffHand = entity.getOffHandStack() == stack;
				if (entity.getMainHandStack().getItem() instanceof GrapplingHookItem) {
					inOffHand = false;
				}

				return (inMainHand || inOffHand) && entity instanceof GrapplingHookUser && ((GrapplingHookUser) entity).getGrapplingHook() != null ? 1.0F : 0.0F;
			}
		});
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		GrapplingHookUser user = (GrapplingHookUser) player;
		ItemStack itemStack = player.getStackInHand(hand);
		int damage;
		GrapplingHookEntity hook = user.getGrapplingHook();
		if (hook != null) {
			if (!world.isClient) {
				damage = hook.use(itemStack);
				itemStack.damage(damage, player, entity -> entity.sendToolBreakStatus(hand));
			}

			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
		} else {
			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
			if (!world.isClient) {
				world.spawnEntity(new GrapplingHookEntity(player, world));
			}

			player.incrementStat(Stats.USED.getOrCreateStat(this));
		}

		return TypedActionResult.success(itemStack);
	}

	public int getEnchantability() {
		return 1;
	}
}
