package com.terraformersmc.campanion.platform.rendering;

import com.terraformersmc.campanion.client.model.block.BridgePlanksBakedModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForgeBridgePlanksBakedModel extends BridgePlanksBakedModel {

	private static final ModelProperty<ForgeBlockModelCreatedData> CREATED_DATA = new ModelProperty<>();


	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
		if(side == null && data.has(CREATED_DATA)) {
			return data.get(CREATED_DATA).quads();
		}
		return super.getQuads(state, side, rand, data, renderType);
	}

	@Override
	public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
		return this.getData(level, pos, ForgeBlockModelCreatedData.class).map(data ->
			modelData.derive().with(CREATED_DATA, data).build()
		).orElse(modelData);
	}
}
