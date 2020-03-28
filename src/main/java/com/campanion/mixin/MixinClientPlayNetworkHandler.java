package com.campanion.mixin;

import com.campanion.entity.CampanionEntities;
import com.campanion.entity.SpearEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Shadow
    private MinecraftClient client;

    @Shadow
    private ClientWorld world;


    @Inject(method = "onEntitySpawn", at = @At("HEAD"))
    public void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo info) {
        if(this.client.isOnThread()) {
            double x = packet.getX();
            double y = packet.getY();
            double z = packet.getZ();
            EntityType<?> id = packet.getEntityTypeId();
            if(id == CampanionEntities.WOODEN_SPEAR || id == CampanionEntities.STONE_SPEAR || id == CampanionEntities.IRON_SPEAR || id == CampanionEntities.GOLDEN_SPEAR || id == CampanionEntities.DIAMOND_SPEAR) {
                SpearEntity toSpawn = Objects.requireNonNull((SpearEntity) id.create(this.world));
                toSpawn.updatePosition(x, y, z);
                Entity thrower = this.world.getEntityById(packet.getEntityData());
                if (thrower != null) {
                    toSpawn.setOwner(thrower);
                }

                int i = packet.getId();
                toSpawn.updateTrackedPosition(x, y, z);
                toSpawn.pitch = (float)(packet.getPitch() * 360) / 256.0F;
                toSpawn.yaw = (float)(packet.getYaw() * 360) / 256.0F;
                toSpawn.setEntityId(i);
                toSpawn.setUuid(packet.getUuid());
                this.world.addEntity(i, toSpawn);
            }
        }
    }
}
