package com.campanion.client;

import com.campanion.block.CampanionBlocks;
import com.campanion.blockentity.CampanionBlockEntities;
import com.campanion.client.renderer.blockentity.PlankBlockEntityRenderer;
import com.campanion.client.renderer.blockentity.SleepingBagBlockEntityRenderer;
import com.campanion.client.renderer.entity.SpearEntityRenderer;
import com.campanion.entity.CampanionEntities;
import com.campanion.network.S2CEntitySpawnPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.container.PlayerContainer;
import net.minecraft.util.Identifier;

public class CampanionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerEntityRenderers();
		registerBlockEntityRenderers();
		registerRenderLayers();
		registerTextures();
		registerClientboundPackets();
		CampanionKeybinds.initialize();
	}

	private static void registerEntityRenderers() {
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.WOODEN_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.STONE_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.IRON_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.GOLDEN_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.DIAMOND_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));
	}

	private static void registerBlockEntityRenderers() {
		BlockEntityRendererRegistry.INSTANCE.register(CampanionBlockEntities.ROPE_BRIDGE_PLANK, PlankBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(CampanionBlockEntities.SLEEPING_BAG, SleepingBagBlockEntityRenderer::new);
	}

	private static void registerRenderLayers() {
		BlockRenderLayerMap.INSTANCE.putBlock(CampanionBlocks.ROPE_LADDER, RenderLayer.getCutout());
	}

	private static void registerTextures() {
		ClientSpriteRegistryCallback.event(PlayerContainer.BLOCK_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
			for (Identifier plank : PlankBlockEntityRenderer.PLANKS) {
				registry.register(plank);
			}
		});
	}

	public static void registerClientboundPackets() {
		ClientSidePacketRegistry.INSTANCE.register(S2CEntitySpawnPacket.ID, S2CEntitySpawnPacket::onPacket);
	}
}
