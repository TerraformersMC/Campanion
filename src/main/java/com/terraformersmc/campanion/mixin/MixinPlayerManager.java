package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.entity.LawnChairEntity;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {
    @Inject(method = "remove", at = @At("HEAD"))
    public void remove(ServerPlayerEntity player, CallbackInfo info) {
        if(player.hasVehicle() && player.getVehicle() instanceof LawnChairEntity) {
            player.stopRiding();
        }
    }
}
