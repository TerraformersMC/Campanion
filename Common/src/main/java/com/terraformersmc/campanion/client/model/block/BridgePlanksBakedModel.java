package com.terraformersmc.campanion.client.model.block;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.blockentity.RopeBridgePlanksBlockEntity;
import com.terraformersmc.campanion.platform.services.rendering.BlockModelCreatedData;
import com.terraformersmc.campanion.ropebridge.RopeBridge;
import com.terraformersmc.campanion.ropebridge.RopeBridgePlank;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
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

public class BridgePlanksBakedModel implements BakedModel {

	public static final Material[] PLANKS = IntStream.range(0, RopeBridge.PLANK_VARIANT_TEXTURES)
			.mapToObj(i -> new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Campanion.MOD_ID, "ropebridge/plank" + i)))
			.toArray(Material[]::new);
	public static final Material ROPE = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Campanion.MOD_ID, "ropebridge/rope"));
	public static final Material STOPPER = new Material(InventoryMenu.BLOCK_ATLAS, new ResourceLocation(Campanion.MOD_ID, "ropebridge/stopper"));

	protected <T extends BlockModelCreatedData> Optional<T> getData(BlockAndTintGetter blockView, BlockPos pos, Class<T> expectedClass) {
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
		//TODO: make this work
		return new StaticPlanksModel(planks);
	}

//	private static class StaticPlanksModel extends BridgePlanksBakedModel {
//
//		private final Mesh mesh;
//
//		private StaticPlanksModel(List<RopeBridgePlank> planks) {
//			Renderer renderer = RendererAccess.INSTANCE.getRenderer();
//			MeshBuilder builder = renderer.meshBuilder();
//			QuadEmitter emitter = builder.getEmitter();
//			for (RopeBridgePlank plank : planks) {
//				plank.generateMesh(true, emitter);
//			}
//			this.mesh = builder.build();
//		}
//
//		@Override
//		public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
//			context.meshConsumer().accept(this.mesh);
//		}
//	}
}
