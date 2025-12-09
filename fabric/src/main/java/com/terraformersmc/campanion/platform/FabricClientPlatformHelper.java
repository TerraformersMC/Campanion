package com.terraformersmc.campanion.platform;

import com.terraformersmc.campanion.client.model.block.BridgePlanksBakedModel;
import com.terraformersmc.campanion.platform.rendering.FabricBlockModelPartCreator;
import com.terraformersmc.campanion.platform.rendering.FabricBridgePlanksBakedModel;
import com.terraformersmc.campanion.platform.services.IClientPlatformHelper;
import com.terraformersmc.campanion.platform.services.rendering.BlockModelPartCreator;

public class FabricClientPlatformHelper implements IClientPlatformHelper {

	@Override
	public BlockModelPartCreator blockModelCreator() {
		return new FabricBlockModelPartCreator();
	}

	@Override
	public BridgePlanksBakedModel createPlanksModel() {
		return new FabricBridgePlanksBakedModel();
	}
}
