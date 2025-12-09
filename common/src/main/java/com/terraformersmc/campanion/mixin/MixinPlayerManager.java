package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.entity.LawnChairEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class MixinPlayerManager {
    @Inject(method = "remove", at = @At("HEAD"))
    public void remove(ServerPlayer player, CallbackInfo info) {
        if(player.isPassenger() && player.getVehicle() instanceof LawnChairEntity) {
            player.stopRiding();
        }
    }
}
