package com.terraformersmc.campanion.platform.services;

import com.terraformersmc.campanion.client.model.block.BridgePlanksBakedModel;
import com.terraformersmc.campanion.platform.services.rendering.BlockModelPartCreator;

public interface IClientPlatformHelper {
	BlockModelPartCreator blockModelCreator();

	BridgePlanksBakedModel createPlanksModel();
}
