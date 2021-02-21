package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.client.renderer.item.FakeWorld;
import net.minecraft.util.math.BlockPos;

public interface CampanionRenderWorldStasher {
	void setCampanionRenderWorld(FakeWorld world);

	FakeWorld getCampanionRenderWorld(BlockPos basePos, int lightOverride);
}
