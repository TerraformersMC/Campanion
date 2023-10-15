package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.entity.GrapplingHookEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.player.Player;

import java.util.function.Supplier;

// For whatever reason this needs to be a seperate class otherwise fabric throws a
//  	 Cannot load class net.minecraft.client.multiplayer.ClientLevel in environment type SERVER
// error

public class S2CEntitySpawnGrapplingHookPacketHandler {

	public static void handle(Supplier<Supplier<Minecraft>> minecraft, S2CEntitySpawnGrapplingHookPacket packet) {
		ClientLevel level = minecraft.get().get().level;
		if (level == null) {
			return;
		}
		GrapplingHookEntity entity = CampanionEntities.GRAPPLING_HOOK.create(level);
		if (entity == null) {
			return;
		}
		entity.syncPacketPositionCodec(packet.x(), packet.y(), packet.z());
		entity.setPosRaw(packet.x(), packet.y(), packet.z());
		entity.setXRot(packet.xRot());
		entity.getViewYRot(packet.yRot());
		entity.setId(packet.id());
		entity.setUUID(packet.uuid());

		if (packet.hasGrapplingPlayer() && level.getEntity(packet.grapplingPlayerId()) instanceof Player player) {
			entity.setPlayer(player);
		}
		level.putNonPlayerEntity(packet.id(), entity);


	}
}
