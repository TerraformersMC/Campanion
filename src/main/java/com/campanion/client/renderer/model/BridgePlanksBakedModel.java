package com.campanion.client.renderer.model;

import com.campanion.Campanion;
import com.campanion.blockentity.RopeBridgePlanksBlockEntity;
import com.campanion.ropebridge.RopeBridge;
import com.campanion.ropebridge.RopeBridgePlank;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.container.PlayerContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class BridgePlanksBakedModel implements FabricBakedModel, BakedModel {

    public static final SpriteIdentifier[] PLANKS = IntStream.range(0, RopeBridge.PLANK_VARIANT_TEXTURES)
        .mapToObj(i -> new SpriteIdentifier(PlayerContainer.BLOCK_ATLAS_TEXTURE, new Identifier(Campanion.MOD_ID, "ropebridge/plank"+i)))
        .toArray(SpriteIdentifier[]::new);
    public static final SpriteIdentifier ROPE = new SpriteIdentifier(PlayerContainer.BLOCK_ATLAS_TEXTURE, new Identifier(Campanion.MOD_ID, "ropebridge/rope"));
    public static final SpriteIdentifier STOPPER = new SpriteIdentifier(PlayerContainer.BLOCK_ATLAS_TEXTURE, new Identifier(Campanion.MOD_ID, "ropebridge/stopper"));

    @Override
    public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
        BlockEntity entity = blockView.getBlockEntity(pos);
        if(entity instanceof RopeBridgePlanksBlockEntity) {
            List<RopeBridgePlank> planks = ((RopeBridgePlanksBlockEntity) entity).getPlanks();
            if(planks.isEmpty() || ((RopeBridgePlanksBlockEntity) entity).forceRenderStopper()) {
                context.meshConsumer().accept(RopeBridgePlank.getEmptyPlanksMesh());
            }
            for (RopeBridgePlank plank : planks) {
                plank.getOrGenerateMesh(false).ifPresent(context.meshConsumer());
            }
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
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction face, Random random) {
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
    public Sprite getSprite() {
        return MinecraftClient.getInstance().getBlockRenderManager().getModel(Blocks.OAK_PLANKS.getDefaultState()).getSprite();
    }

    @Override
    public ModelTransformation getTransformation() {
        return ModelTransformation.NONE;
    }

    @Override
    public ModelItemPropertyOverrideList getItemPropertyOverrides() {
        return ModelItemPropertyOverrideList.EMPTY;
    }

    public static BridgePlanksBakedModel createStaticModel(List<RopeBridgePlank> planks) {
        return new StaticPlanksModel(planks);
    }

    private static class StaticPlanksModel extends BridgePlanksBakedModel {

        private final List<RopeBridgePlank> planks;

        private StaticPlanksModel(List<RopeBridgePlank> planks) {
            this.planks = planks;
        }

        @Override
        public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context) {
            for (RopeBridgePlank plank : this.planks) {
                plank.getOrGenerateMesh(true).ifPresent(context.meshConsumer());
            }
        }
    }
}
