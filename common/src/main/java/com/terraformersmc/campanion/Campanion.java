package com.terraformersmc.campanion;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.terraformersmc.campanion.config.CampanionConfigManager;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.network.C2SOpenBackpack;
import com.terraformersmc.campanion.network.C2SRotateHeldItem;
import com.terraformersmc.campanion.network.S2CEntitySpawnGrapplingHookPacket;
import com.terraformersmc.campanion.network.S2CSyncBackpackContents;
import com.terraformersmc.campanion.platform.Services;
import com.terraformersmc.campanion.tag.CampanionBlockTags;
import com.terraformersmc.campanion.tag.CampanionItemTags;
import net.minecraft.core.dispenser.ProjectileDispenseBehavior;
import net.minecraft.world.level.block.DispenserBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Campanion {

	public static final String MOD_ID = "campanion";
	public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();
	public static final Logger LOG = LoggerFactory.getLogger("Campanion");

	public static void init() {

		CampanionConfigManager.initializeConfig();

		//CampanionCriteria.loadClass();

		registerPackets();

		CampanionBlockTags.load();
		CampanionItemTags.load();
    }

	public static void registerDispenserBehavior() {
		DispenserBlock.registerBehavior(CampanionItems.SKIPPING_STONE, new ProjectileDispenseBehavior(CampanionItems.SKIPPING_STONE));

		DispenserBlock.registerBehavior(CampanionItems.FLARE, new ProjectileDispenseBehavior(CampanionItems.FLARE));

	}



	public static void registerPackets() {
		Services.NETWORK.registerServerBound(C2SOpenBackpack.TYPE, C2SOpenBackpack.STREAM_CODEC, C2SOpenBackpack::handle);
		Services.NETWORK.registerServerBound(C2SRotateHeldItem.TYPE, C2SRotateHeldItem.STREAM_CODEC, C2SRotateHeldItem::handle);

		Services.NETWORK.registerClientBound(S2CSyncBackpackContents.TYPE, S2CSyncBackpackContents.STREAM_CODEC);
		Services.NETWORK.registerClientBound(S2CEntitySpawnGrapplingHookPacket.TYPE, S2CEntitySpawnGrapplingHookPacket.STREAM_CODEC);
	}
}
