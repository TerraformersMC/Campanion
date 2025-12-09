package com.terraformersmc.campanion.platform;

import com.terraformersmc.campanion.client.model.block.BridgePlanksBakedModel;
import com.terraformersmc.campanion.platform.rendering.ForgeBlockModelPartCreator;
import com.terraformersmc.campanion.platform.rendering.ForgeBridgePlanksBakedModel;
import com.terraformersmc.campanion.platform.services.IClientPlatformHelper;
import com.terraformersmc.campanion.platform.services.rendering.BlockModelPartCreator;

public class ForgeClientPlatformHelper implements IClientPlatformHelper {

	@Override
	public BlockModelPartCreator blockModelCreator() {
		return new ForgeBlockModelPartCreator();
	}

	@Override
	public BridgePlanksBakedModel createPlanksModel() {
		return new ForgeBridgePlanksBakedModel();
	}
}
