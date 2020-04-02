package com.campanion.client.items;

import com.campanion.client.util.TentPreviewImmediate;
import com.campanion.item.TentBagItem;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public enum BuiltTentItemRenderer {
    INSTANCE;

    public boolean render(ItemStack stack, MatrixStack matrices, BlockPos basePos, VertexConsumerProvider provider, int lightOverride) {
        if(!stack.hasTag() || !stack.getOrCreateTag().contains("Blocks")) {
            return false;
        }
        FakeWorld fakeWorld = new FakeWorld(basePos, lightOverride);
        TentBagItem.traverseBlocks(stack, (pos, state, tag) -> {
            fakeWorld.blockStateMap.put(pos, state);
            if(!tag.isEmpty()) {
                fakeWorld.blockEntityTagMap.put(pos, tag);
            }
        });
        fakeWorld.blockStateMap.forEach((pos, state) -> {
            matrices.push();
            matrices.translate(pos.getX(), pos.getY(), pos.getZ());
            renderFakeBlock(fakeWorld, pos, basePos, matrices, provider);
            matrices.pop();
        });
        return true;
    }

    public static void renderFakeBlock(World world, BlockPos pos, BlockPos basePos, MatrixStack matrices, VertexConsumerProvider provider) {
        BlockState state = world.getBlockState(pos);
        VertexConsumer buffer = provider.getBuffer(RenderLayers.getBlockLayer(state));

        BlockPos off = basePos.add(pos);
        BlockRenderManager manager = MinecraftClient.getInstance().getBlockRenderManager();
        if(state.getRenderType() == BlockRenderType.MODEL) {
            manager.renderBlock(state, off, world, matrices, buffer, false, new Random());
        }

        BlockEntity entity = world.getBlockEntity(pos);
        if(entity != null) {
            renderBlockEntity(entity, matrices, provider, WorldRenderer.getLightmapCoordinates(world, pos));
        }
    }

    private static <E extends BlockEntity> void renderBlockEntity(E entity, MatrixStack matrices, VertexConsumerProvider provider, int light) {
        BlockEntityRenderer<E> blockEntityRenderer = BlockEntityRenderDispatcher.INSTANCE.get(entity);
        if (blockEntityRenderer != null) {
            try {
                blockEntityRenderer.render(entity, MinecraftClient.getInstance().getTickDelta(), matrices, provider, light, OverlayTexture.getUv(0, true));
            } catch (Throwable var5) {
                CrashReport crashReport = CrashReport.create(var5, "Tent Rendering Block Entity");
                CrashReportSection crashReportSection = crashReport.addElement("Block Entity Details");
                entity.populateCrashReport(crashReportSection);
                throw new CrashException(crashReport);
            }
        }
    }
}
