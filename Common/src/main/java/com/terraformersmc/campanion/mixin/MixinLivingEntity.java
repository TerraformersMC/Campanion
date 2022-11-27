package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.item.BackpackItem;
import com.terraformersmc.campanion.item.SleepingBagItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
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

	@Inject(method = "stopSleeping", at = @At("HEAD"), cancellable = true)
	protected void wakeUp(CallbackInfo callbackInfo) {
		for (InteractionHand value : InteractionHand.values()) {
			ItemStack item = ((LivingEntity) (Object) this).getItemInHand(value);
			if (SleepingBagItem.inUse(item)) {
				item.hurtAndBreak(1, (LivingEntity) (Object) this, e -> e.broadcastBreakEvent(value));
				SleepingBagItem.setInUse(item, false);
			}
		}
	}

	@Inject(method = "getEquipmentSlotForItem", at = @At("HEAD"), cancellable = true)
	private static void getEquipmentSlotForItem(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> info) {
		Item item = stack.getItem();
		if (item instanceof BackpackItem) {
			info.setReturnValue(EquipmentSlot.CHEST);
		}
	}

}
