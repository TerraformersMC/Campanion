package com.campanion.client.items;

import com.campanion.item.TentBagItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import net.minecraft.world.LightType;
import net.minecraft.world.chunk.light.LightingProvider;
import net.minecraft.world.level.ColorResolver;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public enum BuiltTentItemRenderer {
    INSTANCE;

    public boolean render(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
        if(renderMode == ModelTransformation.Mode.GUI || !stack.hasTag() || !stack.getOrCreateTag().contains("Blocks")) {
            return false;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        FakeWorld fakeWorld = new FakeWorld(LightmapTextureManager.getBlockLightCoordinates(light), LightmapTextureManager.getSkyLightCoordinates(light));
        TentBagItem.traverseBlocks(stack, (pos, state, tag) -> {
            fakeWorld.blockStateMap.put(pos, state);
            if(!tag.isEmpty()) {
                fakeWorld.blockEntityMap.put(pos, BlockEntity.createFromTag(tag));
            }
        });

        matrices.push();
        matrices.scale(1/4F, 1/4F, 1/4F);
        client.getBlockRenderManager().getModel(Blocks.STONE.getDefaultState()).getTransformation().getTransformation(renderMode).apply(leftHanded, matrices);
        fakeWorld.blockStateMap.forEach((pos, state) -> {
            VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state));
            matrices.push();
            matrices.translate(pos.getX(), pos.getY(), pos.getZ());
            client.getBlockRenderManager().renderBlock(state, pos.up(500), fakeWorld, matrices, buffer, true, client.world.random);
            matrices.pop();
        });

        matrices.pop();


        return true;
    }

    private class FakeWorld implements BlockRenderView {

        private Map<BlockPos, BlockState> blockStateMap = new HashMap<>();
        private Map<BlockPos, BlockEntity> blockEntityMap = new HashMap<>();

        private final int blocklight;
        private final int skylight;

        private FakeWorld(int blocklight, int skylight) {
            this.blocklight = blocklight;
            this.skylight = skylight;
        }

        @Override
        public LightingProvider getLightingProvider() {
            return MinecraftClient.getInstance().world.getLightingProvider();
        }

        @Override
        public int getLightLevel(LightType type, BlockPos pos) {
            if (type == LightType.BLOCK) {
                return this.blocklight;
            }
            return this.skylight;
        }

        @Override
        public BlockState getBlockState(BlockPos pos) {
            return this.blockStateMap.getOrDefault(pos, Blocks.AIR.getDefaultState());
        }

        @Nullable
        @Override
        public BlockEntity getBlockEntity(BlockPos pos) {
            return this.blockEntityMap.get(pos);
        }

        @Override
        public int getColor(BlockPos pos, ColorResolver colorResolver) {
            return MinecraftClient.getInstance().world.getColor(pos, colorResolver);
        }

        @Override
        public FluidState getFluidState(BlockPos pos) {
            return Fluids.EMPTY.getDefaultState();
        }
    }
}
