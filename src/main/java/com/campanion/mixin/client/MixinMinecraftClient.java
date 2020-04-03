package com.campanion.mixin.client;

import com.campanion.item.CampanionItems;
import com.campanion.item.TentBagItem;
import com.campanion.network.C2SRotateHeldItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.GameOptions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    @Final
    public GameOptions options;

    @Inject(method = "handleInputEvents", at = @At("HEAD"), cancellable = true)
    public void handleInputEvents(CallbackInfo info) {
        if(this.player != null && this.options.keyAttack.isPressed() && TentBagItem.hasBlocks(this.player.getMainHandStack())) {
            if(this.options.keyAttack.wasPressed()) {
                this.player.networkHandler.sendPacket(C2SRotateHeldItem.createPacket());
            }
            info.cancel();
        }
    }

}
