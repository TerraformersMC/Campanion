package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.client.renderer.item.FakeWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;

public interface CampanionRenderWorldStasher {
	void setCampanionRenderWorld(FakeWorld world);

	FakeWorld getCampanionRenderWorld(ItemStack stack, BlockPos basePos, int lightOverride);
}
