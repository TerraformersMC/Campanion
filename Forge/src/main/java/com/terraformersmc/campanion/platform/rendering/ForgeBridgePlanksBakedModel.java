package com.terraformersmc.campanion.platform.rendering;

import com.terraformersmc.campanion.client.model.block.BridgePlanksBakedModel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForgeBridgePlanksBakedModel extends BridgePlanksBakedModel {

	private static final ModelProperty<ForgeBlockModelCreatedData> CREATED_DATA = new ModelProperty<>();

	@Override
	public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull IModelData extraData) {
		if(side == null && extraData.hasProperty(CREATED_DATA)) {
			return extraData.getData(CREATED_DATA).quads();
		}
		return super.getQuads(state, side, rand, extraData);
	}

	@Override
	public @NotNull IModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull IModelData modelData) {
		return this.getData(level, pos, ForgeBlockModelCreatedData.class).map(data -> {
			if(modelData == EmptyModelData.INSTANCE) {
				return new ModelDataMap.Builder().withInitial(CREATED_DATA, data).build();
			} else {
				modelData.setData(CREATED_DATA, data);
				return modelData;
			}
		}).orElse(modelData);
	}
}
