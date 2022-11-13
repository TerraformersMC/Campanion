package com.terraformersmc.campanion;

import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.client.CampanionKeybinds;
import com.terraformersmc.campanion.client.model.block.BridgePlanksUnbakedModel;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.item.ItemProperties;

public class CampanionFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerRenderLayers();

		ModelLoadingRegistry.INSTANCE.registerVariantProvider(rm -> (modelId, context) -> {
			if (modelId.equals(BlockModelShaper.stateToModelLocation(CampanionBlocks.ROPE_BRIDGE_PLANKS.defaultBlockState())) || modelId.equals(BlockModelShaper.stateToModelLocation(CampanionBlocks.ROPE_BRIDGE_POST.defaultBlockState()))) {
				return new BridgePlanksUnbakedModel();
			}
			return null;
		});


		CampanionClient.registerKeybindings(KeyBindingRegistryImpl::registerKeyBinding);
		CampanionClient.registerBlockEntityRenderers(BlockEntityRendererRegistry::register);
		CampanionClient.registerEntityRenderers(EntityRendererRegistry::register);
		CampanionClient.registerModelPredicateProviders(ItemProperties::register);
		CampanionClient.registerItemColours(ColorProviderRegistry.ITEM::register);
		CampanionClient.registerClientPacketHandlers();

		ClientTickEvents.END_CLIENT_TICK.register(e -> CampanionKeybinds.onClientTick());

	}

	// This is handled inside the model in forge
	private static void registerRenderLayers() {
		BlockRenderLayerMap.INSTANCE.putBlock(CampanionBlocks.ROPE_LADDER, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CampanionBlocks.LEATHER_TANNER, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CampanionBlocks.FLARE_BLOCK, RenderType.cutout());
	}

}
