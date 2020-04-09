package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.entity.GrapplingHookEntity;
import com.terraformersmc.campanion.entity.GrapplingHookUser;
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
		this.addPropertyGetter(new Identifier(Campanion.MOD_ID, "deployed"), (stack, world, entity) -> {
			if(entity instanceof PlayerEntity) {
				for (Hand value : Hand.values()) {
					ItemStack heldStack = entity.getStackInHand(value);
					if(heldStack == stack && ((GrapplingHookUser)entity).getGrapplingHook() != null) {
						return 1;
					}
				}
			}
			return 0;
		});
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		GrapplingHookUser user = (GrapplingHookUser) player;
		ItemStack itemStack = player.getStackInHand(hand);
		GrapplingHookEntity hook = user.getGrapplingHook();

		if (hook == null || !hook.isAlive()) {
			world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
			if (!world.isClient) {
				GrapplingHookEntity hookEntity = new GrapplingHookEntity(player, world);
				user.setGrapplingHook(hookEntity);
				world.spawnEntity(hookEntity);
				itemStack.damage(1, player, entity -> entity.sendToolBreakStatus(hand));
			}

			player.incrementStat(Stats.USED.getOrCreateStat(this));
		} else if(!world.isClient) {
			hook.kill();
		}

		return TypedActionResult.success(itemStack);
	}

	@Override
	public int getEnchantability() {
		return 1;
	}
}
