package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.item.SleepingBagItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity  {

    @Inject(method = "isSleepingInBed", at = @At("HEAD"), cancellable = true)
    protected void isSleepingInBed(CallbackInfoReturnable<Boolean> callbackInfo) {
        if (SleepingBagItem.getUsingStack((LivingEntity) (Object) this).isPresent()) {
            callbackInfo.setReturnValue(true);
        }
    }

    @Inject(method = "wakeUp", at = @At("HEAD"), cancellable = true)
    protected void wakeUp(CallbackInfo callbackInfo) {
        for (Hand value : Hand.values()) {
            ItemStack item = this.getStackInHand(value);
            if(SleepingBagItem.inUse(item)) {
                item.damage(1, (LivingEntity) (Object) this, e -> e.sendToolBreakStatus(value));
                SleepingBagItem.setInUse(item, false);
            }
        }
    }

    @Shadow
    public ItemStack getStackInHand(Hand hand) {
        return null;
    }

    @Shadow
    public Random getRandom() {
        return null;
    }
}
