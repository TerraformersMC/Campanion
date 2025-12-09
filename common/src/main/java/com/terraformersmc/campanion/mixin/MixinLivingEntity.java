package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.item.SleepingBagItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

	public MixinLivingEntity(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "checkBedExists", at = @At("HEAD"), cancellable = true)
	protected void isSleepingInBed(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (SleepingBagItem.getUsingStack((LivingEntity) (Object) this).isPresent()) {
			callbackInfo.setReturnValue(true);
		}
	}

	@Inject(method = "stopSleeping", at = @At("HEAD"))
	protected void wakeUp(CallbackInfo callbackInfo) {
		for (InteractionHand value : InteractionHand.values()) {
			ItemStack item = ((LivingEntity) (Object) this).getItemInHand(value);
			if (SleepingBagItem.inUse(item)) {
				item.hurtAndBreak(1, (LivingEntity) (Object) this, LivingEntity.getSlotForHand(value));
				SleepingBagItem.setInUse(item, false);
			}
		}
	}

}
