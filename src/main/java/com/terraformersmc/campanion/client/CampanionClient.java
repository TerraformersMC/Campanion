package com.terraformersmc.campanion.client;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.blockentity.CampanionBlockEntities;
import com.terraformersmc.campanion.client.model.block.BridgePlanksUnbakedModel;
import com.terraformersmc.campanion.client.renderer.blockentity.RopeBridgePostBlockEntityRenderer;
import com.terraformersmc.campanion.client.renderer.entity.EmptyRenderer;
import com.terraformersmc.campanion.client.renderer.entity.GrapplingHookEntityRenderer;
import com.terraformersmc.campanion.client.renderer.entity.SpearEntityRenderer;
import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.entity.FlareEntity;
import com.terraformersmc.campanion.entity.GrapplingHookUser;
import com.terraformersmc.campanion.entity.SkippingStoneEntity;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.item.SleepingBagItem;
import com.terraformersmc.campanion.item.TentBagItem;
import com.terraformersmc.campanion.network.S2CEntitySpawnPacket;
import com.terraformersmc.campanion.network.S2CSyncBackpackContents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.block.BlockModels;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class CampanionClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerEntityRenderers();
		registerColorProviders();
		registerRenderLayers();
		registerBlockEntityRenderers();
		registerClientboundPackets();
		registerModelPredicateProviders();
		CampanionKeybinds.initialize();

		ModelLoadingRegistry.INSTANCE.registerVariantProvider(rm -> (modelId, context) -> {
			if (modelId.equals(BlockModels.getModelId(CampanionBlocks.ROPE_BRIDGE_PLANKS.getDefaultState())) || modelId.equals(BlockModels.getModelId(CampanionBlocks.ROPE_BRIDGE_POST.getDefaultState()))) {
				return new BridgePlanksUnbakedModel();
			}
			return null;
		});
	}

	private static void registerEntityRenderers() {
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.WOODEN_SPEAR, SpearEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.STONE_SPEAR, SpearEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.IRON_SPEAR, SpearEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.GOLDEN_SPEAR, SpearEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.DIAMOND_SPEAR, SpearEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.NETHERITE_SPEAR, SpearEntityRenderer::new);

		EntityRendererRegistry.INSTANCE.register(CampanionEntities.GRAPPLING_HOOK, GrapplingHookEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.LAWN_CHAIR, EmptyRenderer::new);

		EntityRendererRegistry.INSTANCE.register(CampanionEntities.THROWING_STONE, FlyingItemEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(CampanionEntities.FLARE, FlyingItemEntityRenderer::new);
	}

	private static void registerRenderLayers() {
		BlockRenderLayerMap.INSTANCE.putBlock(CampanionBlocks.ROPE_LADDER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CampanionBlocks.LEATHER_TANNER, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CampanionBlocks.FLARE_BLOCK, RenderLayer.getCutout());
	}

	private static void registerBlockEntityRenderers() {
		BlockEntityRendererRegistry.INSTANCE.register(CampanionBlockEntities.ROPE_BRIDGE_POST, RopeBridgePostBlockEntityRenderer::new);
	}


	private static void registerColorProviders() {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? ((DyeableItem) stack.getItem()).getColor(stack) : -1, CampanionItems.SLEEPING_BAG);
	}

	public static void registerClientboundPackets() {
		ClientPlayNetworking.registerGlobalReceiver(S2CEntitySpawnPacket.ID, S2CEntitySpawnPacket::onPacket);
		ClientPlayNetworking.registerGlobalReceiver(S2CSyncBackpackContents.ID, S2CSyncBackpackContents::onPacket);
	}

	public static void registerModelPredicateProviders() {
		FabricModelPredicateProviderRegistry.register(CampanionItems.GRAPPLING_HOOK, new Identifier(Campanion.MOD_ID, "deployed"), (stack, world, entity, seed) -> {
			if (entity instanceof PlayerEntity) {
				for (Hand value : Hand.values()) {
					ItemStack heldStack = entity.getStackInHand(value);
					if (heldStack == stack && ((GrapplingHookUser) entity).getGrapplingHook() != null) {
						return 1;
					}
				}
			}
			return 0;
		});
		FabricModelPredicateProviderRegistry.register(CampanionItems.TENT_BAG, new Identifier(Campanion.MOD_ID, "open"), (stack, world, entity, seed) -> TentBagItem.isEmpty(stack) ? 1 : 0);
		FabricModelPredicateProviderRegistry.register(CampanionItems.SLEEPING_BAG, new Identifier(Campanion.MOD_ID, "open"), (stack, world, entity, seed) -> SleepingBagItem.inUse(stack) ? 1 : 0);
	}
}
