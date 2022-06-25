package com.terraformersmc.campanion.client.model.block;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.blockentity.RopeBridgePlanksBlockEntity;
import com.terraformersmc.campanion.client.renderer.RopeBridgePlankRenderer;
import com.terraformersmc.campanion.platform.Services;
import com.terraformersmc.campanion.platform.services.ClientServices;
import com.terraformersmc.campanion.platform.services.rendering.BlockModelCreatedData;
import com.terraformersmc.campanion.platform.services.rendering.BlockModelPartCreator;
import com.terraformersmc.campanion.ropebridge.RopeBridge;
import com.terraformersmc.campanion.ropebridge.RopeBridgePlank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public abstract class BridgePlanksBakedModel implements BakedModel {

	public static final Material[] PLANKS = IntStream.range(0, RopeBridge.PLANK_VARIANT_TEXTURES)
			.mapToObj(i -> new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Campanion.MOD_ID, "ropebridge/plank" + i)))
			.toArray(Material[]::new);
	public static final Material ROPE = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Campanion.MOD_ID, "ropebridge/rope"));
	public static final Material STOPPER = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Campanion.MOD_ID, "ropebridge/stopper"));

	private BlockModelCreatedData staticData;

	protected <T extends BlockModelCreatedData> Optional<T> getData(BlockAndTintGetter blockView, BlockPos pos, Class<T> expectedClass) {
		if(this.staticData != null) {
			if(expectedClass.isInstance(this.staticData)) {
				return Optional.of((T) this.staticData);
			}
		}
		if (blockView.getBlockEntity(pos) instanceof RopeBridgePlanksBlockEntity blockEntity) {
			BlockModelCreatedData data = blockEntity.getModelCreatedData();
			if(expectedClass.isInstance(data)) {
				return Optional.of((T) data);
			}
		}
		return Optional.empty();
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction face, RandomSource random) {
		return new ArrayList<>();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean isGui3d() {
		return false;
	}

	@Override
	public boolean usesBlockLight() {
		return false;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.OAK_PLANKS.defaultBlockState()).getParticleIcon();
	}

	@Override
	public ItemTransforms getTransforms() {
		return ItemTransforms.NO_TRANSFORMS;
	}

	@Override
	public ItemOverrides getOverrides() {
		return ItemOverrides.EMPTY;
	}

	public static BridgePlanksBakedModel createStaticModel(List<RopeBridgePlank> planks) {
		BridgePlanksBakedModel planksModel = ClientServices.CLIENT_PLATFORM.createPlanksModel();
		BlockModelPartCreator emitter = ClientServices.CLIENT_PLATFORM.blockModelCreator();
		for (RopeBridgePlank plank : planks) {
			RopeBridgePlankRenderer.emitAllQuads(plank, true, emitter);
		}

		planksModel.staticData = emitter.created();

		return planksModel;
	}


}
