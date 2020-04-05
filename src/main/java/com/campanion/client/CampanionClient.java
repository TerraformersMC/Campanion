package com.campanion.client;

import com.campanion.block.CampanionBlocks;
import com.campanion.blockentity.CampanionBlockEntities;
import com.campanion.client.renderer.blockentity.RopeBridgePostBlockEntityRenderer;
import com.campanion.client.renderer.entity.EmptyRenderer;
import com.campanion.client.renderer.entity.GrapplingHookEntityRenderer;
import com.campanion.client.renderer.entity.SpearEntityRenderer;
import com.campanion.client.model.block.BridgePlanksUnbakedModel;
import com.campanion.entity.CampanionEntities;
import com.campanion.entity.SkippingStoneEntity;
import com.campanion.item.CampanionItems;
import com.campanion.network.S2CClearBackpackHeldItem;
import com.campanion.network.S2CEntitySpawnPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.item.DyeableItem;

public class CampanionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerEntityRenderers();
		registerColorProviders();
		registerRenderLayers();
		registerBlockEntityRenderers();
		registerClientboundPackets();
		CampanionKeybinds.initialize();

		ModelLoadingRegistry.INSTANCE.registerVariantProvider(rm -> (modelId, context) -> {
			if(modelId.equals(BlockModels.getModelId(CampanionBlocks.ROPE_BRIDGE_PLANKS.getDefaultState())) || modelId.equals(BlockModels.getModelId(CampanionBlocks.ROPE_BRIDGE_POST.getDefaultState()))) {
				return new BridgePlanksUnbakedModel();
			}
			return null;
		});
	}

	private static void registerEntityRenderers() {
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.WOODEN_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.STONE_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.IRON_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.GOLDEN_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.DIAMOND_SPEAR, (dispatcher, context) -> new SpearEntityRenderer(dispatcher));

		EntityRendererRegistry.INSTANCE.register(CampanionEntities.GRAPPLING_HOOK, (dispatcher, context) -> new GrapplingHookEntityRenderer(dispatcher));
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.LAWN_CHAIR, (dispatcher, context) -> new EmptyRenderer<>(dispatcher));

		EntityRendererRegistry.INSTANCE.register(CampanionEntities.THROWING_STONE, (dispatcher, context) -> new FlyingItemEntityRenderer<SkippingStoneEntity>(dispatcher, context.getItemRenderer()));
	}
	private static void registerRenderLayers() {
		BlockRenderLayerMap.INSTANCE.putBlock(CampanionBlocks.ROPE_LADDER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CampanionBlocks.LEATHER_TANNER, RenderLayer.getCutout());
	}

	private static void registerBlockEntityRenderers() {
		BlockEntityRendererRegistry.INSTANCE.register(CampanionBlockEntities.ROPE_BRIDGE_POST, RopeBridgePostBlockEntityRenderer::new);
	}


	private static void registerColorProviders() {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? ((DyeableItem)stack.getItem()).getColor(stack) : -1, CampanionItems.SLEEPING_BAG);
	}

	public static void registerClientboundPackets() {
		ClientSidePacketRegistry.INSTANCE.register(S2CEntitySpawnPacket.ID, S2CEntitySpawnPacket::onPacket);
		ClientSidePacketRegistry.INSTANCE.register(S2CClearBackpackHeldItem.ID, S2CClearBackpackHeldItem::onPacket);
	}
}
