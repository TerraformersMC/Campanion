package com.terraformersmc.campanion;

import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.blockentity.CampanionBlockEntities;
import com.terraformersmc.campanion.client.CampanionKeybinds;
import com.terraformersmc.campanion.client.model.block.BridgePlanksUnbakedModel;
import com.terraformersmc.campanion.client.renderer.blockentity.RopeBridgePostBlockEntityRenderer;
import com.terraformersmc.campanion.client.renderer.entity.EmptyRenderer;
import com.terraformersmc.campanion.client.renderer.entity.GrapplingHookEntityRenderer;
import com.terraformersmc.campanion.client.renderer.entity.SpearEntityRenderer;
import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.entity.GrapplingHookUser;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.item.SleepingBagItem;
import com.terraformersmc.campanion.item.TentBagItem;
import com.terraformersmc.campanion.network.C2SOpenBackpack;
import com.terraformersmc.campanion.platform.Services;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.ItemStack;

public class CampanionFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		registerEntityRenderers();
		registerColorProviders();
		registerRenderLayers();
		registerBlockEntityRenderers();
		registerModelPredicateProviders();
		registerKeybinds();

		ModelLoadingRegistry.INSTANCE.registerVariantProvider(rm -> (modelId, context) -> {
			if (modelId.equals(BlockModelShaper.stateToModelLocation(CampanionBlocks.ROPE_BRIDGE_PLANKS.defaultBlockState())) || modelId.equals(BlockModelShaper.stateToModelLocation(CampanionBlocks.ROPE_BRIDGE_POST.defaultBlockState()))) {
				return new BridgePlanksUnbakedModel();
			}
			return null;
		});
	}

	private static void registerKeybinds() {
		KeyBindingRegistryImpl.registerKeyBinding(CampanionKeybinds.OPEN_BACKPACK_KEY);
		ClientTickEvents.END_CLIENT_TICK.register(e -> CampanionKeybinds.onClientTick());

	}

	private static void registerEntityRenderers() {
		EntityRendererRegistry.register(CampanionEntities.WOODEN_SPEAR, SpearEntityRenderer::new);
		EntityRendererRegistry.register(CampanionEntities.STONE_SPEAR, SpearEntityRenderer::new);
		EntityRendererRegistry.register(CampanionEntities.IRON_SPEAR, SpearEntityRenderer::new);
		EntityRendererRegistry.register(CampanionEntities.GOLDEN_SPEAR, SpearEntityRenderer::new);
		EntityRendererRegistry.register(CampanionEntities.DIAMOND_SPEAR, SpearEntityRenderer::new);
		EntityRendererRegistry.register(CampanionEntities.NETHERITE_SPEAR, SpearEntityRenderer::new);

		EntityRendererRegistry.register(CampanionEntities.GRAPPLING_HOOK, GrapplingHookEntityRenderer::new);
		EntityRendererRegistry.register(CampanionEntities.LAWN_CHAIR, EmptyRenderer::new);

		EntityRendererRegistry.register(CampanionEntities.THROWING_STONE, ThrownItemRenderer::new);
		EntityRendererRegistry.register(CampanionEntities.FLARE, ThrownItemRenderer::new);
	}

	private static void registerRenderLayers() {
		BlockRenderLayerMap.INSTANCE.putBlock(CampanionBlocks.ROPE_LADDER, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CampanionBlocks.LEATHER_TANNER, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(CampanionBlocks.FLARE_BLOCK, RenderType.cutout());
	}

	private static void registerBlockEntityRenderers() {
		BlockEntityRendererRegistry.register(CampanionBlockEntities.ROPE_BRIDGE_POST, RopeBridgePostBlockEntityRenderer::new);
	}


	private static void registerColorProviders() {
		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> tintIndex == 0 ? ((DyeableLeatherItem) stack.getItem()).getColor(stack) : -1, CampanionItems.SLEEPING_BAG);
	}

	public static void registerModelPredicateProviders() {
		ItemProperties.register(CampanionItems.GRAPPLING_HOOK, new ResourceLocation(Campanion.MOD_ID, "deployed"), (stack, world, entity, seed) -> {
			if (entity instanceof Player) {
				for (InteractionHand value : InteractionHand.values()) {
					ItemStack heldStack = entity.getItemInHand(value);
					if (heldStack == stack && (((GrapplingHookUser) entity).getGrapplingHook() != null && !((GrapplingHookUser) entity).getGrapplingHook().isRemoved())) {
						return 1;
					}
				}
			}
			return 0;
		});
		ItemProperties.register(CampanionItems.TENT_BAG, new ResourceLocation(Campanion.MOD_ID, "open"), (stack, world, entity, seed) -> TentBagItem.isEmpty(stack) ? 1 : 0);
		ItemProperties.register(CampanionItems.SLEEPING_BAG, new ResourceLocation(Campanion.MOD_ID, "open"), (stack, world, entity, seed) -> SleepingBagItem.inUse(stack) ? 1 : 0);
	}
}
