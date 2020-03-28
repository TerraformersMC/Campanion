package com.campanion.client;

import net.fabricmc.api.ClientModInitializer;

public class CampanionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerEntityRenderers();
		registerRenderLayers();
		registerTextures();
	}

	private static void registerEntityRenderers() {

	}

	private static void registerRenderLayers() {

	}

	private static void registerTextures() {

	}
}
