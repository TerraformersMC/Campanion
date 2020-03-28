package com.campanion.client.renderer.blockentity;

import com.campanion.Campanion;
import com.campanion.blockentity.RopeBridgePlanksBlockEntity;
import com.campanion.ropebridge.RopeBridge;
import com.campanion.ropebridge.RopeBridgePlank;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.container.PlayerContainer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.stream.IntStream;

public class PlankBlockEntityRenderer extends BlockEntityRenderer<RopeBridgePlanksBlockEntity> {

    public static final Identifier[] PLANKS = IntStream.range(0, RopeBridge.PLANK_VARIENTS)
        .mapToObj(i -> new Identifier(Campanion.MOD_ID, "ropebridge/plank"+i))
        .toArray(Identifier[]::new);

    public PlankBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(RopeBridgePlanksBlockEntity blockEntity, float tickDelta, MatrixStack stack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getCutout());

        for (RopeBridgePlank plank : blockEntity.getPlanks()) {
            Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(PlayerContainer.BLOCK_ATLAS_TEXTURE).apply(PLANKS[MathHelper.clamp(plank.getVariant(), 0, PLANKS.length - 1)]);

            stack.push();

            stack.translate(plank.getDeltaPosition().getX(), plank.getDeltaPosition().getY(), plank.getDeltaPosition().getZ());
            stack.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion((float) -plank.getyAngle()));
            stack.multiply(Vector3f.POSITIVE_Z.getRadialQuaternion((float) plank.getTiltAngle()));

            float hl = (float) (RopeBridge.PLANK_LENGTH / 2D); //half length
            this.add(buffer, stack, -hl, 0, -hl, sprite.getMaxU(), sprite.getMinV());
            this.add(buffer, stack, -hl, 0,  hl, sprite.getMinU(), sprite.getMinV());
            this.add(buffer, stack,  hl, 0,  hl, sprite.getMinU(), sprite.getMaxV());
            this.add(buffer, stack,  hl, 0, -hl, sprite.getMaxU(), sprite.getMaxV());

            this.add(buffer, stack, -hl, 0,  hl, sprite.getMinU(), sprite.getMinV());
            this.add(buffer, stack, -hl, 0, -hl, sprite.getMaxU(), sprite.getMinV());
            this.add(buffer, stack,  hl, 0, -hl, sprite.getMaxU(), sprite.getMaxV());
            this.add(buffer, stack,  hl, 0,  hl, sprite.getMinU(), sprite.getMaxV());

            stack.pop();
        }
    }

    private void add(VertexConsumer buffer, MatrixStack stack, float x, float y, float z, float u, float v) {
        buffer.vertex(stack.peek().getModel(), x, y, z)
            .color(1.0f, 1.0f, 1.0f, 1.0f)
            .texture(u, v)
            .light(0, 240)
            .normal(1, 0, 0)
            .next();
    }
}
