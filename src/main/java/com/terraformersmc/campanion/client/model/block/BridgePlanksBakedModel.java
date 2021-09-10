package com.terraformersmc.campanion.client.model.block;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.blockentity.RopeBridgePlanksBlockEntity;
import com.terraformersmc.campanion.ropebridge.RopeBridge;
import com.terraformersmc.campanion.ropebridge.RopeBridgePlank;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class BridgePlanksBakedModel implements FabricBakedModel, BakedModel {

	public static final SpriteIdentifier[] PLANKS = IntStream.range(0, RopeBridge.PLANK_VARIANT_TEXTURES)
			.mapToObj(i -> new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Campanion.MOD_ID, "ropebridge/plank" + i)))
			.toArray(SpriteIdentifier[]::new);
	public static final SpriteIdentifier ROPE = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Campanion.MOD_ID, "ropebridge/rope"));
	public static final SpriteIdentifier STOPPER = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Campanion.MOD_ID, "ropebridge/stopper"));

	@Override
	public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
		BlockEntity entity = blockView.getBlockEntity(pos);
		if (entity instanceof RopeBridgePlanksBlockEntity) {
			context.meshConsumer().accept(((RopeBridgePlanksBlockEntity) entity).getMesh());
		}
	}

	@Override
	public boolean isVanillaAdapter() {
		return false;
	}

	@Override
	public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {
	}

	@Override
	public List<BakedQuad> getQuads(BlockState state, Direction face, Random random) {
		return new ArrayList<>();
	}

	@Override
	public boolean useAmbientOcclusion() {
		return false;
	}

	@Override
	public boolean hasDepth() {
		return false;
	}

	@Override
	public boolean isSideLit() {
		return false;
	}

	@Override
	public boolean isBuiltin() {
		return false;
	}

	@Override
	public Sprite getParticleSprite() {
		return MinecraftClient.getInstance().getBlockRenderManager().getModel(Blocks.OAK_PLANKS.getDefaultState()).getParticleSprite();
	}

	@Override
	public ModelTransformation getTransformation() {
		return ModelTransformation.NONE;
	}

	@Override
	public ModelOverrideList getOverrides() {
		return ModelOverrideList.EMPTY;
	}

	public static BridgePlanksBakedModel createStaticModel(List<RopeBridgePlank> planks) {
		return new StaticPlanksModel(planks);
	}

	private static class StaticPlanksModel extends BridgePlanksBakedModel {

		private final Mesh mesh;

		private StaticPlanksModel(List<RopeBridgePlank> planks) {
			Renderer renderer = RendererAccess.INSTANCE.getRenderer();
			MeshBuilder builder = renderer.meshBuilder();
			QuadEmitter emitter = builder.getEmitter();
			for (RopeBridgePlank plank : planks) {
				plank.generateMesh(true, emitter);
			}
			this.mesh = builder.build();
		}

		@Override
		public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
			context.meshConsumer().accept(this.mesh);
		}
	}
}
