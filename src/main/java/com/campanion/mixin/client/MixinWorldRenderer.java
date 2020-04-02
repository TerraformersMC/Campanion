package com.campanion.mixin.client;

import com.campanion.client.items.BuiltTentItemRenderer;
import com.campanion.client.util.TentPreviewImmediate;
import com.campanion.item.TentBagItem;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Final
    @Shadow
    private BufferBuilderStorage bufferBuilders;

    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", ordinal = 11, shift = At.Shift.BEFORE))
    public void render(MatrixStack matrices, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightmapTextureManager lightmapTextureManager, Matrix4f matrix4f, CallbackInfo info) {
        TentPreviewImmediate immediate = TentPreviewImmediate.STORAGE;

        for (PlayerEntity player : this.client.world.getPlayers()) {
            if (player != null) {
                ItemStack stack = player.getMainHandStack();
                if(TentBagItem.hasBlocks(stack)) {
                    HitResult result = player.rayTrace(10, 0, true);
                    if (result instanceof BlockHitResult && result.getType() == HitResult.Type.BLOCK) {
                        BlockPos placePos = ((BlockHitResult) result).getBlockPos().offset(((BlockHitResult) result).getSide());
                        Vec3d d = camera.getPos().subtract(new Vec3d(placePos));

                        matrices.push();
                        matrices.translate(-d.x, -d.y, -d.z);

                        List<BlockPos> list = TentBagItem.getErrorPosition(this.client.world, placePos, stack);
                        TentPreviewImmediate.STORAGE.setApplyModifiers(!list.isEmpty());
                        BuiltTentItemRenderer.INSTANCE.render(stack, matrices, placePos, immediate, -1);

                        for (BlockPos pos : list) {
                            matrices.push();
                            matrices.translate(pos.getX()-placePos.getX(), pos.getY()-placePos.getY(), pos.getZ()-placePos.getZ());
                            float scale = 1.03F;
                            matrices.scale(scale, scale, scale);
                            matrices.translate(-0.5*scale+0.5, -0.5*scale+0.5, -0.5*scale+0.5);
                            BuiltTentItemRenderer.renderFakeBlock(this.client.world, pos, BlockPos.ORIGIN, matrices, immediate);
                            matrices.pop();
                        }

                        matrices.pop();
                    }
                }
            }
        }
        immediate.draw();
    }



}
