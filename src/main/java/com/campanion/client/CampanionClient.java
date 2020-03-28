package com.campanion.client;

import com.campanion.block.CampanionBlocks;
import com.campanion.client.renderer.entity.SpearEntityRenderer;
import com.campanion.entity.CampanionEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;

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
		BlockRenderLayerMap.INSTANCE.putBlock(CampanionBlocks.ROPE_LADDER, RenderLayer.getCutout());
	}

	private static void registerTextures() {

	}
}
