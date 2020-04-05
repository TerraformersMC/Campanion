package com.terraformersmc.campanion.client.renderer.blockentity;

import com.terraformersmc.campanion.blockentity.RopeBridgePostBlockEntity;
import com.terraformersmc.campanion.client.model.block.BridgePlanksBakedModel;
import com.terraformersmc.campanion.ropebridge.RopeBridgePlank;
import net.fabricmc.fabric.impl.client.indigo.renderer.render.BlockRenderContext;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class RopeBridgePostBlockEntityRenderer extends BlockEntityRenderer<RopeBridgePostBlockEntity> {

    private static final ThreadLocal<BlockRenderContext> CONTEXTS = ThreadLocal.withInitial(BlockRenderContext::new);

    public RopeBridgePostBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(RopeBridgePostBlockEntity blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getTranslucent());
        BlockModelRenderer bmr = MinecraftClient.getInstance().getBlockRenderManager().getModelRenderer();

        blockEntity.getGhostPlanks().forEach((pos, pairs) -> {
            for (Pair<BlockPos, List<RopeBridgePlank>> pair : pairs) {
                BlockPos deltaPos = pair.getLeft().subtract(blockEntity.getPos());
                matrices.push();
                matrices.translate(deltaPos.getX(), deltaPos.getY(), deltaPos.getZ());

                CONTEXTS.get().tesselate(
                    bmr, blockEntity.getWorld(), BridgePlanksBakedModel.createStaticModel(pair.getRight()), Blocks.AIR.getDefaultState(), BlockPos.ORIGIN.up(500),
                    matrices, buffer, false, -1, BlockPos.ORIGIN.equals(deltaPos) ? overlay : OverlayTexture.DEFAULT_UV
                );
                matrices.pop();
            }
        });
    }

    @Override
    public boolean rendersOutsideBoundingBox(RopeBridgePostBlockEntity blockEntity) {
        return true;
    }
}
