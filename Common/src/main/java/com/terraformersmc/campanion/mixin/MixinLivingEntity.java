package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.item.BackpackItem;
import com.terraformersmc.campanion.item.SleepingBagItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

	public MixinLivingEntity(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "isSleepingInBed", at = @At("HEAD"), cancellable = true)
	protected void isSleepingInBed(CallbackInfoReturnable<Boolean> callbackInfo) {
		if (SleepingBagItem.getUsingStack((LivingEntity) (Object) this).isPresent()) {
			callbackInfo.setReturnValue(true);
		}
	}

	@Inject(method = "wakeUp", at = @At("HEAD"), cancellable = true)
	protected void wakeUp(CallbackInfo callbackInfo) {
		for (InteractionHand value : InteractionHand.values()) {
			ItemStack item = this.getStackInHand(value);
			if (SleepingBagItem.inUse(item)) {
				item.hurtAndBreak(1, (LivingEntity) (Object) this, e -> e.broadcastBreakEvent(value));
				SleepingBagItem.setInUse(item, false);
			}
		}
	}

	@Inject(method = "getPreferredEquipmentSlot", at = @At("HEAD"), cancellable = true)
	private static void onGetPreferredEquipmentSlot(ItemStack stack, CallbackInfoReturnable<EquipmentSlot> info) {
		Item item = stack.getItem();
		if (item instanceof BackpackItem) {
			info.setReturnValue(EquipmentSlot.CHEST);
		}
	}

	@Shadow
	public ItemStack getStackInHand(InteractionHand hand) {
		return null;
	}

	@Shadow
	public Random getRandom() {
		return null;
	}
}
