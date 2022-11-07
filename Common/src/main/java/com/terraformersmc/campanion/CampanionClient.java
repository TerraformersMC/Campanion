package com.terraformersmc.campanion;

import com.terraformersmc.campanion.network.S2CEntitySpawnGrapplingHookPacket;
import com.terraformersmc.campanion.network.S2CEntitySpawnGrapplingHookPacketHandler;
import com.terraformersmc.campanion.network.S2CSyncBackpackContents;
import com.terraformersmc.campanion.platform.Services;

public class CampanionClient {

	public static void registerClientPacketHandlers() {
		Services.NETWORK.registerClientBoundHandler(S2CSyncBackpackContents.class, S2CSyncBackpackContents::decode, S2CSyncBackpackContents::handle);
		Services.NETWORK.registerClientBoundHandler(S2CEntitySpawnGrapplingHookPacket.class, S2CEntitySpawnGrapplingHookPacket::decode, S2CEntitySpawnGrapplingHookPacketHandler::handle);
	}
}
