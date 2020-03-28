package com.campanion.client;

import com.campanion.client.renderer.entity.SpearEntityRenderer;
import com.campanion.entity.CampanionEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;

public class CampanionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerEntityRenderers();
		registerRenderLayers();
		registerTextures();
	}

	private static void registerEntityRenderers() {
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.WOODEN_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.STONE_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.IRON_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.GOLDEN_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.DIAMOND_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));
	}

	private static void registerRenderLayers() {

	}

	private static void registerTextures() {

	}
}
