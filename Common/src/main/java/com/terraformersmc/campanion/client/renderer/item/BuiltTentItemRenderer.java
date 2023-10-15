package com.terraformersmc.campanion.client.renderer.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.terraformersmc.campanion.item.CampanionRenderWorldStasher;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public enum BuiltTentItemRenderer {
	INSTANCE;

	public boolean render(ItemStack stack, PoseStack matrices, BlockPos basePos, MultiBufferSource provider, int lightOverride) {
		if (!stack.hasTag() || !stack.getOrCreateTag().contains("Blocks")) {
			return false;
		}
		FakeWorld fakeWorld = ((CampanionRenderWorldStasher) (Object) stack).getCampanionRenderWorld(stack, basePos, lightOverride);
		fakeWorld.blockStateMap.forEach((pos, state) -> {
			matrices.pushPose();
			matrices.translate(pos.getX(), pos.getY(), pos.getZ());
			renderFakeBlock(fakeWorld, pos, basePos, matrices, provider);
			matrices.popPose();
		});
		return true;
	}

	public static void renderFakeBlock(Level world, BlockPos pos, BlockPos basePos, PoseStack matrices, MultiBufferSource provider) {
		BlockState state = world.getBlockState(pos);
		VertexConsumer buffer = provider.getBuffer(ItemBlockRenderTypes.getChunkRenderType(state));

		BlockPos off = basePos.offset(pos);
		BlockRenderDispatcher manager = Minecraft.getInstance().getBlockRenderer();
		if (state.getRenderShape() == RenderShape.MODEL) {
			manager.renderBatched(state, off, world, matrices, buffer, false, RandomSource.create());
		}

		BlockEntity entity = world.getBlockEntity(pos);
		if (entity != null) {
			renderBlockEntity(entity, matrices, provider, LevelRenderer.getLightColor(world, pos));
		}
	}

	private static <E extends BlockEntity> void renderBlockEntity(E entity, PoseStack matrices, MultiBufferSource provider, int light) {
		BlockEntityRenderer<E> blockEntityRenderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(entity);
		if (blockEntityRenderer != null) {
			try {
				blockEntityRenderer.render(entity, Minecraft.getInstance().getFrameTime(), matrices, provider, light, OverlayTexture.NO_OVERLAY);
			} catch (Throwable var5) {
				CrashReport crashReport = CrashReport.forThrowable(var5, "Tent Rendering Block Entity");
				CrashReportCategory crashReportSection = crashReport.addCategory("Block Entity Details");
				entity.fillCrashReportCategory(crashReportSection);
				throw new ReportedException(crashReport);
			}
		}
	}
}
