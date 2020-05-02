package com.terraformersmc.campanion.client.renderer.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;

import java.util.HashMap;
import java.util.Map;

public class FakeWorld extends ClientWorld {

	private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

	public final Map<BlockPos, BlockState> blockStateMap = new HashMap<>();
	public final Map<BlockPos, BlockEntity> blockEntityMap = new HashMap<>();
	public final Map<BlockPos, CompoundTag> blockEntityTagMap = new HashMap<>();

	private final BlockPos basePos;
	private final int blockLight;
	private final int skyLight;

	public FakeWorld(BlockPos basePos, int lightOverride) {
		super(CLIENT.player.networkHandler, new class_5271(CLIENT.world.getLevelProperties().getSeed(), CLIENT.world.getLevelProperties().getDifficulty(), CLIENT.world.getLevelProperties().isDifficultyLocked(), CLIENT.world.getLevelProperties().method_27421()), CLIENT.world.dimension.getType(), 3, CLIENT::getProfiler, CLIENT.worldRenderer);
		this.basePos = basePos;
		this.blockLight = lightOverride == -1 ? -1 : LightmapTextureManager.getBlockLightCoordinates(lightOverride);
		this.skyLight = lightOverride == -1 ? -1 : LightmapTextureManager.getSkyLightCoordinates(lightOverride);
	}

	@Override
	public int getLightLevel(LightType type, BlockPos pos) {
		if (this.blockLight == -1 || this.skyLight == -1) {
			return CLIENT.world.getLightLevel(type, this.basePos);
		}
		if (type == LightType.BLOCK) {
			return this.blockLight;
		}
		return this.skyLight;
	}

	@Override
	public BlockState getBlockState(BlockPos pos) {
		return this.blockStateMap.getOrDefault(pos, Blocks.AIR.getDefaultState());
	}

	@Override
	public BlockEntity getBlockEntity(BlockPos pos) {
		return this.blockEntityMap.computeIfAbsent(pos, p -> {
			if (this.blockEntityTagMap.containsKey(p)) {
				BlockEntity entity = BlockEntity.createFromTag(getBlockState(pos), this.blockEntityTagMap.get(p));
				if (entity != null) {
					entity.setLocation(this, pos);
					return entity;
				}
			}
			return null;
		});
	}


	@Override
	public FluidState getFluidState(BlockPos pos) {
		return Fluids.EMPTY.getDefaultState();
	}

}
