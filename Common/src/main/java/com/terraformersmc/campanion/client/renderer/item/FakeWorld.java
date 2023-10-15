package com.terraformersmc.campanion.client.renderer.item;

import com.terraformersmc.campanion.item.PlaceableTentItem;
import com.terraformersmc.campanion.mixin.AccessorBiomeAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class FakeWorld extends ClientLevel {

	private static final Minecraft CLIENT = Minecraft.getInstance();

	public final Map<BlockPos, BlockState> blockStateMap = new HashMap<>();
	public final Map<BlockPos, BlockEntity> blockEntityMap = new HashMap<>();
	public final Map<BlockPos, CompoundTag> blockEntityTagMap = new HashMap<>();

	private BlockPos basePos;
	private int blockLight;
	private int skyLight;

	public FakeWorld(ItemStack stack, BlockPos basePos, int lightOverride) {
		super(CLIENT.player.connection,
			new ClientLevel.ClientLevelData(CLIENT.level.getLevelData().getDifficulty(), CLIENT.level.getLevelData().isDifficultyLocked(), CLIENT.level.getLevelData().isHardcore()),
			CLIENT.level.dimension(), CLIENT.level.dimensionTypeRegistration(), 3, 3, CLIENT::getProfiler,
			CLIENT.levelRenderer, CLIENT.level.isDebug(),
			((AccessorBiomeAccess) CLIENT.level.getBiomeManager()).getBiomeZoomSeed());
		updatePositioning(basePos, lightOverride);
		PlaceableTentItem tent = (PlaceableTentItem) stack.getItem();
		tent.traverseBlocks(stack, (pos, state, tag) -> {
			this.blockStateMap.put(pos, state);
			if (!tag.isEmpty()) {
				this.blockEntityTagMap.put(pos, tag);
			}
		});
	}

	public void updatePositioning(BlockPos basePos, int lightOverride) {
		this.basePos = basePos;
		this.blockLight = lightOverride == -1 ? -1 : LightTexture.block(lightOverride);
		this.skyLight = lightOverride == -1 ? -1 : LightTexture.sky(lightOverride);
	}

	@Override
	public int getBrightness(@NotNull LightLayer type, @NotNull BlockPos pos) {
		if (this.blockLight == -1 || this.skyLight == -1) {
			return CLIENT.level.getBrightness(type, this.basePos);
		}
		if (type == LightLayer.BLOCK) {
			return this.blockLight;
		}
		return this.skyLight;
	}

	@Override
	public @NotNull BlockState getBlockState(@NotNull BlockPos pos) {
		return this.blockStateMap.getOrDefault(pos, Blocks.AIR.defaultBlockState());
	}

	@Override
	public BlockEntity getBlockEntity(@NotNull BlockPos pos) {
		return this.blockEntityMap.computeIfAbsent(pos, p -> {
			if (this.blockEntityTagMap.containsKey(p)) {
				BlockEntity entity = BlockEntity.loadStatic(pos, getBlockState(pos), this.blockEntityTagMap.get(p));

				if (entity != null) {
					entity.setLevel(this);
				}

				return entity;
			}
			return null;
		});
	}


	@Override
	public @NotNull FluidState getFluidState(@NotNull BlockPos pos) {
		return Fluids.EMPTY.defaultFluidState();
	}

}
