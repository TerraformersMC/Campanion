package com.campanion.ropebridge;

import com.campanion.client.renderer.model.BridgePlanksBakedModel;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.impl.client.indigo.renderer.IndigoRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class RopeBridgePlank {
    private final Vec3d deltaPosition;
    private final float yAngle;
    private final float tiltAngle;
    private final float distToPrevious;
    private final int variant;
    private final int ropeVariant; //Between 0-127
    private BlockPos previous = BlockPos.ORIGIN;
    private BlockPos next = BlockPos.ORIGIN;
    private Mesh mesh;

    public Vec3d getDeltaPosition() {
        return deltaPosition;
    }

    public double getyAngle() {
        return yAngle;
    }

    public double getTiltAngle() {
        return tiltAngle;
    }

    public BlockPos getPrevious() {
        return previous;
    }

    public void setPrevious(BlockPos previous) {
        this.previous = previous;
    }

    public BlockPos getNext() {
        return next;
    }

    public void setNext(BlockPos next) {
        this.next = next;
    }

    public RopeBridgePlank(Vec3d deltaPosition, double yAngle, double tiltAngle, float distToPrevious, int variant, int ropeVariant) {
        this.deltaPosition = deltaPosition;
        this.yAngle = (float) yAngle;
        this.tiltAngle = (float) tiltAngle;
        this.distToPrevious = distToPrevious;
        this.variant = variant;
        this.ropeVariant = ropeVariant;
    }

    public static RopeBridgePlank deserialize(CompoundTag tag) {
        RopeBridgePlank plank = new RopeBridgePlank(
            new Vec3d(tag.getDouble("DeltaPosX"), tag.getDouble("DeltaPosY"), tag.getDouble("DeltaPosZ")),
            tag.getFloat("YAng"),
            tag.getFloat("TiltAng"),
            tag.getFloat("DistToPrevious"),
            tag.getInt("Variant"),
            tag.getByte("RopeValiant"));
        plank.previous = BlockPos.fromLong(tag.getLong("Previous"));
        plank.next = BlockPos.fromLong(tag.getLong("Next"));
        return plank;
    }

    public static CompoundTag serialize(RopeBridgePlank plank) {
        CompoundTag tag = new CompoundTag();
        tag.putDouble("DeltaPosX", plank.deltaPosition.x);
        tag.putDouble("DeltaPosY", plank.deltaPosition.y);
        tag.putDouble("DeltaPosZ", plank.deltaPosition.z);
        tag.putFloat("YAng", plank.yAngle);
        tag.putFloat("TiltAng", plank.tiltAngle);
        tag.putFloat("DistToPrevious", plank.distToPrevious);
        tag.putInt("Variant", plank.variant);
        tag.putByte("RopeValiant", (byte) plank.ropeVariant);
        tag.putLong("Previous", plank.previous.asLong());
        tag.putLong("Next", plank.next.asLong());
        return tag;
    }

    public Mesh getOrGenerateMesh() {
        MeshBuilder builder = IndigoRenderer.INSTANCE.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        Sprite sprite = BridgePlanksBakedModel.PLANKS[MathHelper.clamp(this.variant, 0, BridgePlanksBakedModel.PLANKS.length - 1)].getSprite();
        Sprite rope = BridgePlanksBakedModel.ROPE.getSprite();

        float ropeWidth = 1F;
        float ropeLength = 16F;

        MatrixStack stack = new MatrixStack();
        stack.push();
        stack.translate(this.deltaPosition.getX(), this.deltaPosition.getY(), this.deltaPosition.z);
        stack.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(-this.yAngle));
        stack.push();
        stack.multiply(Vector3f.POSITIVE_Z.getRadialQuaternion(this.tiltAngle));

        float hl = (float) (RopeBridge.PLANK_LENGTH / 2D); //half length

        this.drawDoubleSided(emitter,
            (v, m) -> this.add(emitter, v, stack, -hl, 0, -hl, sprite.getMaxU(), sprite.getMinV(), 0, m, 0),
            (v, m) -> this.add(emitter, v, stack, -hl, 0,  hl, sprite.getMinU(), sprite.getMinV(), 0, m, 0),
            (v, m) -> this.add(emitter, v, stack,  hl, 0,  hl, sprite.getMinU(), sprite.getMaxV(), 0, m, 0),
            (v, m) -> this.add(emitter, v, stack,  hl, 0, -hl, sprite.getMaxU(), sprite.getMaxV(), 0, m, 0)
        );

        stack.pop();

        float ropeUStart = this.ropeVariant % (16F - ropeWidth);

        float ropeMinU = rope.getFrameU(ropeUStart);
        float ropeMaxU = rope.getFrameU(ropeUStart + ropeWidth);
        float ropeMinV = rope.getMinV();
        float ropeMaxV = rope.getFrameV(ropeLength);

        float topRopeVStart = this.ropeVariant % (16F - this.distToPrevious*16F);
        float topRopeMinV = rope.getFrameV(topRopeVStart);
        float topRopeMaxV = rope.getFrameV(topRopeVStart + this.distToPrevious*16F);

        for (int modifier : new int[] {-1, 1}) {
            float mhl = modifier*hl;
            this.drawDoubleSided(emitter,
                (v, m) -> this.add(emitter, v, stack, -ropeWidth/32F, ropeLength/16F, mhl, ropeMinU, ropeMinV, 0, 0, m*modifier),
                (v, m) -> this.add(emitter, v, stack, -ropeWidth/32F, 0, mhl, ropeMinU, ropeMaxV, 0, 0, m*modifier),
                (v, m) -> this.add(emitter, v, stack, ropeWidth/32F, 0, mhl, ropeMaxU, ropeMaxV, 0, 0, m*modifier),
                (v, m) -> this.add(emitter, v, stack, ropeWidth/32F, ropeLength/16F, mhl, ropeMaxU, ropeMinV, 0, 0, m*modifier)
            );
            stack.push();
            stack.translate(0, ropeLength/16F, mhl);
            stack.multiply(Vector3f.POSITIVE_Z.getRadialQuaternion(this.tiltAngle));

            if(this.distToPrevious != 0) {
                this.drawDoubleSided(emitter,
                    (v, m) -> this.add(emitter, v, stack, 0, -ropeWidth/32F, 0, ropeMinU, topRopeMinV, 0, 0, modifier*m),
                    (v, m) -> this.add(emitter, v, stack, this.distToPrevious, -ropeWidth/32F, 0, ropeMinU, topRopeMaxV, 0, 0, modifier*m),
                    (v, m) -> this.add(emitter, v, stack, this.distToPrevious, ropeWidth/32F, 0, ropeMaxU, topRopeMaxV, 0, 0, modifier*m),
                    (v, m) -> this.add(emitter, v, stack, 0, ropeWidth/32F, 0, ropeMaxU, topRopeMinV, 0, 0, modifier*m)
                );
            }

            stack.pop();
        }
        stack.pop();

        return this.mesh = builder.build();
    }

    private void drawDoubleSided(QuadEmitter emitter, VertexConsumer v1, VertexConsumer v2, VertexConsumer v3, VertexConsumer v4) {
        RenderMaterial material = RendererAccess.INSTANCE.getRenderer().materialFinder().blendMode(0, BlendMode.CUTOUT).find();
        emitter.material(material);
        v1.apply(0, 1);
        v2.apply(1, 1);
        v3.apply(2, 1);
        v4.apply(3, 1);
        emitter.emit();

        emitter.material(material);
        v1.apply(3, -1);
        v2.apply(2, -1);
        v3.apply(1, -1);
        v4.apply(0, -1);
        emitter.emit();
    }

    private void add(QuadEmitter emitter, int index, MatrixStack stack, float x, float y, float z, float u, float v, float nx, float ny, float nz) {
        emitter
            .pos(index, this.transform(stack.peek().getModel(), x, y, z))
            .sprite(index, 0, u, v)
            .spriteColor(index, 0, -1)
            .normal(index, this.transform(stack.peek().getNormal(), nx, ny, nz));
    }

    private Vector3f transform(Matrix4f matrix, float x, float y, float z) {
        Vector4f f = new Vector4f(x, y, z, 1.0F);
        f.transform(matrix);
        return new Vector3f(f.getX(), f.getY(), f.getZ());
    }

    private Vector3f transform(Matrix3f matrix, float x, float y, float z) {
        Vector3f f = new Vector3f(x, y, z);
        f.transform(matrix);
        return new Vector3f(f.getX(), f.getY(), f.getZ());
    }

    @FunctionalInterface
    private interface VertexConsumer {
        void apply(int index, int modifier);
    }
}
