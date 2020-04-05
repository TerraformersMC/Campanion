package com.terraformersmc.campanion.mixin.client;

import com.terraformersmc.campanion.entity.HowlingEntity;
import net.minecraft.entity.passive.WolfEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfEntity.class)
public class MixinWolfEntityClient {

    @Inject(method = "handleStatus", at = @At("HEAD"), cancellable = true)
    public void handleStatus(byte status, CallbackInfo callbackInfo) {
        HowlingEntity thiz = (HowlingEntity) (Object) this;
        if (status == 64) {
            thiz.setHowling(true);
            callbackInfo.cancel();
        } else {
            thiz.setHowling(false);
        }
    }

}
