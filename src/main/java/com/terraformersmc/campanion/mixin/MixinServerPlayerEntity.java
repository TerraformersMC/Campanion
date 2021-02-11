package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class MixinServerPlayerEntity {
	@Inject(method = "onSpawn", at = @At("TAIL"))
	public void onSpawn(CallbackInfo info) {
		((BackpackStorePlayer) this).syncChanges();
	}

	@Inject(method = "copyFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;clone(Lnet/minecraft/entity/player/PlayerInventory;)V"))
	public void onCopyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo info) {
		((BackpackStorePlayer) this).setBackpackStacks(((BackpackStorePlayer) oldPlayer).getBackpackStacks());
	}
}
