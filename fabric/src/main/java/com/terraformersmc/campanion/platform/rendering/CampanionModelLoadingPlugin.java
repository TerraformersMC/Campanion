package com.terraformersmc.campanion.platform.rendering;

import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.client.model.block.BridgePlanksUnbakedModel;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.ModelResourceLocation;

public class CampanionModelLoadingPlugin implements ModelLoadingPlugin {
	@Override
	public void onInitializeModelLoader(Context pluginContext) {
		pluginContext.modifyModelOnLoad().register((model, context) -> {
			ModelResourceLocation modelId = context.topLevelId();
			if (modelId != null && (modelId.equals(BlockModelShaper.stateToModelLocation(CampanionBlocks.ROPE_BRIDGE_PLANKS.defaultBlockState())) || modelId.equals(BlockModelShaper.stateToModelLocation(CampanionBlocks.ROPE_BRIDGE_POST.defaultBlockState())))) {
				return new BridgePlanksUnbakedModel();
			}
			return model;
		});
	}
}
