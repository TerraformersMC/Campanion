package com.terraformersmc.campanion.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.terraformersmc.campanion.blockentity.RopeBridgePostBlockEntity;
import com.terraformersmc.campanion.client.model.block.BridgePlanksBakedModel;
import com.terraformersmc.campanion.ropebridge.RopeBridgePlank;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;

public class RopeBridgePostBlockEntityRenderer implements BlockEntityRenderer<RopeBridgePostBlockEntity> {

    private static final ThreadLocal<ModelBlockRenderer> RENDERER = ThreadLocal.withInitial(() -> Minecraft.getInstance().getBlockRenderer().getModelRenderer());

    private static final Random RND = new Random();
    public RopeBridgePostBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(RopeBridgePostBlockEntity blockEntity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderType.translucent());

        blockEntity.getGhostPlanks().forEach((pos, pairs) -> {
            for (Pair<BlockPos, List<RopeBridgePlank>> pair : pairs) {
                BlockPos deltaPos = pair.getLeft().subtract(blockEntity.getBlockPos());
                matrices.pushPose();
                matrices.translate(deltaPos.getX(), deltaPos.getY(), deltaPos.getZ());

                RENDERER.get().tesselateBlock(blockEntity.getLevel(), BridgePlanksBakedModel.createStaticModel(pair.getRight()), Blocks.AIR.defaultBlockState(), BlockPos.ZERO.above(500), matrices, buffer, false, RND, 0, BlockPos.ZERO.equals(deltaPos) ? overlay : OverlayTexture.NO_OVERLAY);
                matrices.popPose();
            }
        });
    }

    @Override
    public boolean rendersOutsideBoundingBox(RopeBridgePostBlockEntity blockEntity) {
        return true;
    }
}
