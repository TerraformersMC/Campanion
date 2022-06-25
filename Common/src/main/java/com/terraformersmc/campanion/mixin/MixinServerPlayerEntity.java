package com.terraformersmc.campanion.mixin;

import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class MixinServerPlayerEntity {
	@Inject(method = "initInventoryMenu", at = @At("TAIL"))
	public void initInventoryMenu(CallbackInfo info) {
		((BackpackStorePlayer) this).syncChanges();
	}

	@Inject(method = "restoreFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;replaceWith(Lnet/minecraft/world/entity/player/Inventory;)V"))
	public void restoreFrom(ServerPlayer oldPlayer, boolean alive, CallbackInfo info) {
		((BackpackStorePlayer) this).setBackpackStacks(((BackpackStorePlayer) oldPlayer).getBackpackStacks());
	}
}
