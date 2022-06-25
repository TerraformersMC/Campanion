package com.terraformersmc.campanion.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.terraformersmc.campanion.client.renderer.item.BuiltTentItemRenderer;
import com.terraformersmc.campanion.client.util.TentPreviewImmediate;
import com.terraformersmc.campanion.item.PlaceableTentItem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Random;

@Mixin(LevelRenderer.class)
public class MixinWorldRenderer {

	@Final
	@Shadow
	private RenderBuffers renderBuffers;

	@Final
	@Shadow
	private Minecraft minecraft;

	@Inject(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V", ordinal = 11, shift = At.Shift.BEFORE))
	public void renderLevel(PoseStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightmapTextureManager, Matrix4f matrix4f, CallbackInfo info) {
		TentPreviewImmediate immediate = TentPreviewImmediate.STORAGE;

		for (Player player : this.minecraft.level.players()) {
			if (player != null) {
				ItemStack stack = player.getMainHandItem();
				if (stack.getItem() instanceof PlaceableTentItem) {
					PlaceableTentItem tent = (PlaceableTentItem) stack.getItem();
					if (tent.hasBlocks(stack)) {
						HitResult result = player.pick(10, 0, true);
						if (result instanceof BlockHitResult && result.getType() == HitResult.Type.BLOCK) {
							BlockPos placePos = ((BlockHitResult) result).getBlockPos().relative(((BlockHitResult) result).getDirection());
							Vec3 d = camera.getPosition().subtract(Vec3.atLowerCornerOf(placePos));

							matrices.pushPose();
							matrices.translate(-d.x, -d.y, -d.z);

							List<BlockPos> list = tent.getErrorPosition(this.minecraft.level, placePos, stack);
							TentPreviewImmediate.STORAGE.setApplyModifiers(!list.isEmpty());
							BuiltTentItemRenderer.INSTANCE.render(stack, matrices, placePos, immediate, -1);

							for (BlockPos pos : list) {
								matrices.pushPose();
								matrices.translate(pos.getX() - placePos.getX(), pos.getY() - placePos.getY(), pos.getZ() - placePos.getZ());
								if (this.minecraft.level.getBlockState(pos).getMaterial() == Material.AIR) {
									BlockState stone = Blocks.STONE.defaultBlockState();
									Minecraft.getInstance().getBlockRenderer().renderBatched(stone, pos, this.minecraft.level, matrices, immediate.getBuffer(ItemBlockRenderTypes.getChunkRenderType(stone)), false, RandomSource.create());
								} else {
									float scale = 1.03F;
									matrices.scale(scale, scale, scale);
									matrices.translate(-0.5 * scale + 0.5, -0.5 * scale + 0.5, -0.5 * scale + 0.5);
									BuiltTentItemRenderer.renderFakeBlock(this.minecraft.level, pos, BlockPos.ZERO, matrices, immediate);
								}
								matrices.popPose();
							}

							matrices.popPose();
						}
					}
				}
			}
		}
		immediate.endBatch();
	}


}
