package com.terraformersmc.campanion.client.renderer.item;

import com.terraformersmc.campanion.item.CampanionRenderWorldStasher;
import com.terraformersmc.campanion.item.PlaceableTentItem;
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
		if (!stack.hasTag() || !stack.getOrCreateTag().contains("Blocks")) {
			return false;
		}
		FakeWorld fakeWorld = ((CampanionRenderWorldStasher)(Object) stack).getCampanionRenderWorld(stack, basePos, lightOverride);
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
		if (state.getRenderType() == BlockRenderType.MODEL) {
			manager.renderBlock(state, off, world, matrices, buffer, false, new Random());
		}

		BlockEntity entity = world.getBlockEntity(pos);
		if (entity != null) {
			renderBlockEntity(entity, matrices, provider, WorldRenderer.getLightmapCoordinates(world, pos));
		}
	}

	private static <E extends BlockEntity> void renderBlockEntity(E entity, MatrixStack matrices, VertexConsumerProvider provider, int light) {
		BlockEntityRenderer<E> blockEntityRenderer = MinecraftClient.getInstance().getBlockEntityRenderDispatcher().get(entity);
		if (blockEntityRenderer != null) {
			try {
				blockEntityRenderer.render(entity, MinecraftClient.getInstance().getTickDelta(), matrices, provider, light, OverlayTexture.DEFAULT_UV);
			} catch (Throwable var5) {
				CrashReport crashReport = CrashReport.create(var5, "Tent Rendering Block Entity");
				CrashReportSection crashReportSection = crashReport.addElement("Block Entity Details");
				entity.populateCrashReport(crashReportSection);
				throw new CrashException(crashReport);
			}
		}
	}
}
