package com.terraformersmc.campanion;

import com.terraformersmc.campanion.client.CampanionKeybinds;
import net.minecraft.client.renderer.item.ItemProperties;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;

public class CampanionForgeClient {

	public static void registerEvents(IEventBus modEventBus) {
		modEventBus.addListener((RegisterKeyMappingsEvent event) -> CampanionClient.registerKeybindings(event::register));
		modEventBus.addListener((EntityRenderersEvent.RegisterRenderers event) -> CampanionClient.registerEntityRenderers(event::registerEntityRenderer));
		modEventBus.addListener((RegisterColorHandlersEvent.Item event) -> CampanionClient.registerItemColours(event::register));
	}

	public static void init() {
		CampanionClient.registerClientPacketHandlers();
		CampanionClient.registerModelPredicateProviders(ItemProperties::register);

		NeoForge.EVENT_BUS.addListener((ClientTickEvent.Post event) -> {
			CampanionKeybinds.onClientTick();
		});
	}
}
