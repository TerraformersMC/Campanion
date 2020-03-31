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
    public static final Identifier ROPE = new Identifier(Campanion.MOD_ID, "ropebridge/rope");

    public PlankBlockEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(RopeBridgePlanksBlockEntity blockEntity, float tickDelta, MatrixStack stack, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getCutout());
        Sprite rope = MinecraftClient.getInstance().getSpriteAtlas(PlayerContainer.BLOCK_ATLAS_TEXTURE).apply(ROPE);

        float ropeWidth = 1F;
        float ropeLength = 16F;

        for (RopeBridgePlank plank : blockEntity.getPlanks()) {
            Sprite sprite = MinecraftClient.getInstance().getSpriteAtlas(PlayerContainer.BLOCK_ATLAS_TEXTURE).apply(PLANKS[MathHelper.clamp(plank.getVariant(), 0, PLANKS.length - 1)]);

            stack.push();
            stack.translate(plank.getDeltaPosition().getX(), plank.getDeltaPosition().getY(), plank.getDeltaPosition().getZ());
            stack.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion((float) -plank.getyAngle()));
            stack.push();
            stack.multiply(Vector3f.POSITIVE_Z.getRadialQuaternion((float) plank.getTiltAngle()));

            float hl = (float) (RopeBridge.PLANK_LENGTH / 2D); //half length

            this.drawDoubleSided(
                () -> this.add(buffer, stack, light, -hl, 0, -hl, sprite.getMaxU(), sprite.getMinV()),
                () -> this.add(buffer, stack, light, -hl, 0,  hl, sprite.getMinU(), sprite.getMinV()),
                () -> this.add(buffer, stack, light,  hl, 0,  hl, sprite.getMinU(), sprite.getMaxV()),
                () -> this.add(buffer, stack, light,  hl, 0, -hl, sprite.getMaxU(), sprite.getMaxV())
            );

            stack.pop();

            float ropeUStart = plank.getRopeVariant() % (16F - ropeWidth);

            float ropeMinU = rope.getFrameU(ropeUStart);
            float ropeMaxU = rope.getFrameU(ropeUStart + ropeWidth);
            float ropeMinV = rope.getMinV();
            float ropeMaxV = rope.getFrameV(ropeLength);

            float topRopeVStart = plank.getRopeVariant() % (16F - plank.getDistToPrevious()*16F);
            float topRopeMinV = rope.getFrameV(topRopeVStart);
            float topRopeMaxV = rope.getFrameV(topRopeVStart + plank.getDistToPrevious()*16F);

            for (float mhl : new float[] {-hl, hl}) {
                this.drawDoubleSided(
                    () -> this.add(buffer, stack, light, -ropeWidth/32F, ropeLength/16F, mhl, ropeMinU, ropeMinV),
                    () -> this.add(buffer, stack, light, -ropeWidth/32F, 0, mhl, ropeMinU, ropeMaxV),
                    () -> this.add(buffer, stack, light, ropeWidth/32F, 0, mhl, ropeMaxU, ropeMaxV),
                    () -> this.add(buffer, stack, light, ropeWidth/32F, ropeLength/16F, mhl, ropeMaxU, ropeMinV)
                );
                stack.push();
                stack.translate(0, ropeLength/16F, mhl);
                stack.multiply(Vector3f.POSITIVE_Z.getRadialQuaternion((float) plank.getTiltAngle()));

                if(plank.getDistToPrevious() != 0) {
                    this.drawDoubleSided(
                        () -> this.add(buffer, stack, light, 0, -ropeWidth/32F, 0, ropeMinU, topRopeMinV),
                        () -> this.add(buffer, stack, light, plank.getDistToPrevious(), -ropeWidth/32F, 0, ropeMinU, topRopeMaxV),
                        () -> this.add(buffer, stack, light, plank.getDistToPrevious(), ropeWidth/32F, 0, ropeMaxU, topRopeMaxV),
                        () -> this.add(buffer, stack, light, 0, ropeWidth/32F, 0, ropeMaxU, topRopeMinV)
                    );
                }


                stack.pop();
            }
            stack.pop();
        }
    }

    private void drawDoubleSided(Runnable v1, Runnable v2, Runnable v3, Runnable v4) {
        v1.run();
        v2.run();
        v3.run();
        v4.run();

        v2.run();
        v1.run();
        v4.run();
        v3.run();
    }

    private void add(VertexConsumer buffer, MatrixStack stack, int light, float x, float y, float z, float u, float v) {
        buffer.vertex(stack.peek().getModel(), x, y, z)
            .color(1.0f, 1.0f, 1.0f, 1.0f)
            .texture(u, v)
            .light(light)
            .normal(1, 0, 0)
            .next();
    }
}
