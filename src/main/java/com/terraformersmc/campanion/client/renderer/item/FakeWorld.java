package com.terraformersmc.campanion.client.renderer.item;

import com.terraformersmc.campanion.item.PlaceableTentItem;
import com.terraformersmc.campanion.mixin.AccessorBiomeAccess;

import net.fabricmc.fabric.impl.registry.sync.FabricRegistry;
import net.fabricmc.fabric.impl.registry.sync.FabricRegistryInit;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.LightType;
import net.minecraft.world.dimension.DimensionType;

import java.util.HashMap;
import java.util.Map;

public class FakeWorld extends ClientWorld {

	private static final MinecraftClient CLIENT = MinecraftClient.getInstance();

	public final Map<BlockPos, BlockState> blockStateMap = new HashMap<>();
	public final Map<BlockPos, BlockEntity> blockEntityMap = new HashMap<>();
	public final Map<BlockPos, NbtCompound> blockEntityTagMap = new HashMap<>();

	private BlockPos basePos;
	private int blockLight;
	private int skyLight;

	public FakeWorld(ItemStack stack, BlockPos basePos, int lightOverride) {
		super(CLIENT.player.networkHandler,
				new ClientWorld.Properties(CLIENT.world.getLevelProperties().getDifficulty(), CLIENT.world.getLevelProperties().isDifficultyLocked(), CLIENT.world.getLevelProperties().isHardcore()),
				CLIENT.world.getRegistryKey(), RegistryEntry.of(CLIENT.world.getDimension()), 3, 3, CLIENT::getProfiler,
				CLIENT.worldRenderer, CLIENT.world.isDebugWorld(),
				((AccessorBiomeAccess) CLIENT.world.getBiomeAccess()).getSeed());
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
				BlockEntity entity = BlockEntity.createFromNbt(pos, getBlockState(pos), this.blockEntityTagMap.get(p));

				if (entity != null) {
					entity.setWorld(this);
				}

				return entity;
			}
			return null;
		});
	}


	@Override
	public FluidState getFluidState(BlockPos pos) {
		return Fluids.EMPTY.getDefaultState();
	}

}
