package com.terraformersmc.campanion.platform.rendering;

import com.terraformersmc.campanion.client.model.block.BridgePlanksBakedModel;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class FabricBridgePlanksBakedModel extends BridgePlanksBakedModel implements FabricBakedModel {

	@Override
	public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> randomSupplier, RenderContext context) {
		this.getData(blockView, pos, FabricBlockModelCreatedData.class).ifPresent(data -> context.meshConsumer().accept(data.mesh()));
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<RandomSource> randomSupplier, RenderContext context) {

	}
}
