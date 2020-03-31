package com.campanion.ropebridge;

import com.campanion.client.renderer.model.BridgePlanksBakedModel;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.impl.client.indigo.renderer.IndigoRenderer;
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.GeometryHelper;
import net.fabricmc.fabric.impl.client.indigo.renderer.mesh.QuadViewImpl;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

public class RopeBridgePlank {
    private final BlockPos from;
    private final BlockPos to;
    private final Vec3d deltaPosition;
    private final float yAngle;
    private final float tiltAngle;
    private final float distToPrevious;
    private final float ropesSubtract;
    private final int ropeVariant; //Between 0-127
    private final boolean flat;
    private final boolean master;
    private final boolean ropes;
    private final boolean stopper;


    private boolean broken;
    private Mesh mesh;

    public RopeBridgePlank(
        BlockPos from, BlockPos to, Vec3d deltaPosition, double yAngle, double tiltAngle, float distToPrevious,
        float ropesSubtract, int ropeVariant, boolean flat, boolean master, boolean ropes, boolean stopper
    ) {
        this.from = from;
        this.to = to;
        this.deltaPosition = deltaPosition;
        this.yAngle = (float) yAngle;
        this.tiltAngle = (float) tiltAngle;
        this.distToPrevious = distToPrevious;
        this.ropesSubtract = ropesSubtract;
        this.ropeVariant = ropeVariant;
        this.flat = flat;
        this.master = master;
        this.ropes = ropes;
        this.stopper = stopper;
    }

    public Vec3d getDeltaPosition() {
        return deltaPosition;
    }

    public double getyAngle() {
        return yAngle;
    }

    public double getTiltAngle() {
        return tiltAngle;
    }

    public boolean isMaster() {
        return master;
    }

    public boolean isStopper() {
        return stopper;
    }

    public boolean isBroken() {
        return broken;
    }

    public void setBroken() {
        this.broken = true;
    }

    public BlockPos getFrom() {
        return from;
    }

    public BlockPos getTo() {
        return to;
    }

    public static RopeBridgePlank deserialize(CompoundTag tag) {
        RopeBridgePlank plank = new RopeBridgePlank(
            BlockPos.fromLong(tag.getLong("FromPos")),
            BlockPos.fromLong(tag.getLong("ToPos")),
            new Vec3d(tag.getDouble("DeltaPosX"), tag.getDouble("DeltaPosY"), tag.getDouble("DeltaPosZ")),
            tag.getFloat("YAng"),
            tag.getFloat("TiltAng"),
            tag.getFloat("DistToPrevious"),
            tag.getFloat("RopesSubtract"),
            tag.getByte("RopeVariant"),
            tag.getBoolean("Flat"),
            tag.getBoolean("Master"),
            tag.getBoolean("Ropes"),
            tag.getBoolean("Stopper")
        );
        plank.broken = tag.getBoolean("Broken");
        return plank;
    }

    public static CompoundTag serialize(RopeBridgePlank plank) {
        CompoundTag tag = new CompoundTag();
        tag.putLong("FromPos", plank.from.asLong());
        tag.putLong("ToPos", plank.to.asLong());
        tag.putDouble("DeltaPosX", plank.deltaPosition.x);
        tag.putDouble("DeltaPosY", plank.deltaPosition.y);
        tag.putDouble("DeltaPosZ", plank.deltaPosition.z);
        tag.putFloat("YAng", plank.yAngle);
        tag.putFloat("TiltAng", plank.tiltAngle);
        tag.putFloat("DistToPrevious", plank.distToPrevious);
        tag.putFloat("RopesSubtract", plank.ropesSubtract);
        tag.putByte("RopeVariant", (byte) plank.ropeVariant);
        tag.putBoolean("Flat", plank.flat);
        tag.putBoolean("Master", plank.master);
        tag.putBoolean("Ropes", plank.ropes);
        tag.putBoolean("Stopper", plank.stopper);
        tag.putBoolean("Broken", plank.broken);
        return tag;
    }

    public void deleteMeshCache() {
        this.mesh = null;
    }

    public Optional<Mesh> getOrGenerateMesh(boolean translucent) {
        if(!this.master) {
            return Optional.empty();
        }
        if(this.mesh != null) {
            return Optional.of(this.mesh);
        }
        Random random = new Random(this.ropeVariant);

        RenderMaterial material = RendererAccess.INSTANCE.getRenderer().materialFinder().blendMode(0, BlendMode.CUTOUT).find();
        MeshBuilder builder = IndigoRenderer.INSTANCE.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();


        MatrixStack stack = new MatrixStack();
        stack.push();
        stack.translate(this.deltaPosition.getX(), this.deltaPosition.getY(), this.deltaPosition.z);
        stack.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(-this.yAngle));

        this.renderPlank(emitter, stack, material, random, translucent);

        this.drawRope(emitter, stack, material, random, translucent);
        stack.pop();

        return Optional.of(this.mesh = builder.build());
    }

    private void renderPlank(QuadEmitter emitter, MatrixStack stack, RenderMaterial material, Random random, boolean translucent) {
        stack.push();
        if(!this.flat) {
            stack.multiply(Vector3f.POSITIVE_Z.getRadialQuaternion(this.tiltAngle));
        }
        Sprite sprite = BridgePlanksBakedModel.PLANKS[random.nextInt(BridgePlanksBakedModel.PLANKS.length)].getSprite();
        int vStart = random.nextInt((int) (RopeBridge.PLANK_WIDTH*16F));
        float minV = sprite.getFrameV(vStart);
        float maxV = sprite.getFrameV(vStart+RopeBridge.PLANK_WIDTH*16F);

        float hl = (float) (RopeBridge.PLANK_LENGTH / 2D); //half length
        float hw = (float) (RopeBridge.PLANK_WIDTH / 2D); //half width
        this.drawDoubleSided(emitter, material,
            (v, m) -> add(emitter, v, stack, translucent, -hw, 0, -hl, sprite.getMaxU(), minV, 0, m, 0),
            (v, m) -> add(emitter, v, stack, translucent, -hw, 0,  hl, sprite.getMinU(), minV, 0, m, 0),
            (v, m) -> add(emitter, v, stack, translucent,  hw, 0,  hl, sprite.getMinU(), maxV, 0, m, 0),
            (v, m) -> add(emitter, v, stack, translucent,  hw, 0, -hl, sprite.getMaxU(), maxV, 0, m, 0)
        );

        stack.pop();
    }

    private void drawRope(QuadEmitter emitter, MatrixStack stack, RenderMaterial material, Random random, boolean translucent) {
        Sprite rope = BridgePlanksBakedModel.ROPE.getSprite();

        for (int modifier : new int[] {-1, 1}) {
            if(this.ropes) {
                this.drawVerticalRope(emitter, stack, material, rope, random, translucent, modifier);
            }
            if(this.stopper) {
                drawStopper(emitter, stack, material, random, translucent, modifier);
            }
            this.drawBottomRopes(emitter, stack, material, rope, random, translucent, modifier);

            stack.push();
            stack.translate(0, RopeBridge.ROPE_LENGTH/16F - this.ropesSubtract, modifier * RopeBridge.PLANK_LENGTH / 2D);
            if(this.ropes) {
                this.drawKnot(emitter, stack, material, rope, random, translucent);
            }
            stack.multiply(Vector3f.POSITIVE_Z.getRadialQuaternion(this.tiltAngle));
            this.drawConnectingRope(emitter, stack, material, rope, random, translucent, modifier);
            stack.pop();
        }
    }


    private void drawBottomRopes(QuadEmitter emitter, MatrixStack stack, RenderMaterial material, Sprite rope, Random random, boolean translucent, int modifier) {
        stack.push();
        stack.translate(0, -0.025, modifier * (RopeBridge.PLANK_LENGTH/2F - RopeBridge.UNDER_ROPE_DIST_FROM_EDGE/16F));
        stack.multiply(Vector3f.POSITIVE_Z.getRadialQuaternion(this.tiltAngle));

        float ropeStart = random.nextFloat()*(16F - this.distToPrevious*16F);
        float minU = rope.getFrameU(ropeStart);
        float maxU = rope.getFrameU(ropeStart + RopeBridge.ROPE_WIDTH);
        float minV = rope.getFrameV(ropeStart);
        float maxV = rope.getFrameV(ropeStart + this.distToPrevious*16F);

        this.drawDoubleSided(emitter, material,
            (v, m) -> add(emitter, v, stack, translucent, 0, 0, 0, maxU, minV, 0, -m*modifier, 0),
            (v, m) -> add(emitter, v, stack, translucent, 0, 0,  modifier*-RopeBridge.ROPE_WIDTH/16F, minU, minV, 0, -m*modifier, 0),
            (v, m) -> add(emitter, v, stack, translucent,  this.distToPrevious, 0,  modifier*-RopeBridge.ROPE_WIDTH/16F, minU, maxV, 0, -m*modifier, 0),
            (v, m) -> add(emitter, v, stack, translucent,  this.distToPrevious, 0, 0, maxU, maxV, 0, -m*modifier, 0)
        );

        stack.pop();
    }

    private void drawConnectingRope(QuadEmitter emitter, MatrixStack stack, RenderMaterial material, Sprite rope, Random random, boolean translucent, int modifier) {
        float ropeStart = random.nextFloat() * (16F - this.distToPrevious*16F);
        float ropeMinU = rope.getFrameU(ropeStart);
        float ropeMaxU = rope.getFrameU(ropeStart + RopeBridge.ROPE_WIDTH);
        float ropeMinV = rope.getFrameV(ropeStart);
        float ropeMaxV = rope.getFrameV(ropeStart + this.distToPrevious*16F);

        if(this.distToPrevious != 0) {
            this.drawDoubleSided(emitter, material,
                (v, m) -> add(emitter, v, stack, translucent, 0, -RopeBridge.ROPE_WIDTH/32F, 0, ropeMinU, ropeMinV, 0, 0, modifier*m),
                (v, m) -> add(emitter, v, stack, translucent, this.distToPrevious, -RopeBridge.ROPE_WIDTH/32F, 0, ropeMinU, ropeMaxV, 0, 0, modifier*m),
                (v, m) -> add(emitter, v, stack, translucent, this.distToPrevious, RopeBridge.ROPE_WIDTH/32F, 0, ropeMaxU, ropeMaxV, 0, 0, modifier*m),
                (v, m) -> add(emitter, v, stack, translucent, 0, RopeBridge.ROPE_WIDTH/32F, 0, ropeMaxU, ropeMinV, 0, 0, modifier*m)
            );
        }
    }

    private void drawVerticalRope(QuadEmitter emitter, MatrixStack stack, RenderMaterial material, Sprite rope, Random random, boolean translucent, int modifier) {
        float hl = (float) (RopeBridge.PLANK_LENGTH / 2D); //half length
        float mhl = modifier*hl;

        float ropeUStart = random.nextInt(16 - RopeBridge.ROPE_WIDTH);
        float ropeMinU = rope.getFrameU(ropeUStart);
        float ropeMaxU = rope.getFrameU(ropeUStart + RopeBridge.ROPE_WIDTH);
        float ropeMinV = rope.getMinV();
        float ropeMaxV = rope.getFrameV(RopeBridge.ROPE_LENGTH - this.ropesSubtract*16F);

        this.drawDoubleSided(emitter, material,
            (v, m) -> add(emitter, v, stack, translucent, -RopeBridge.ROPE_WIDTH/32F, RopeBridge.ROPE_LENGTH/16F - this.ropesSubtract, mhl, ropeMinU, ropeMinV, 0, 0, m*modifier),
            (v, m) -> add(emitter, v, stack, translucent, -RopeBridge.ROPE_WIDTH/32F, 0, mhl, ropeMinU, ropeMaxV, 0, 0, m*modifier),
            (v, m) -> add(emitter, v, stack, translucent, RopeBridge.ROPE_WIDTH/32F, 0, mhl, ropeMaxU, ropeMaxV, 0, 0, m*modifier),
            (v, m) -> add(emitter, v, stack, translucent, RopeBridge.ROPE_WIDTH/32F, RopeBridge.ROPE_LENGTH/16F - this.ropesSubtract, mhl, ropeMaxU, ropeMinV, 0, 0, m*modifier)
        );
    }

    private void drawKnot(QuadEmitter emitter, MatrixStack stack, RenderMaterial material, Sprite rope, Random random, boolean translucent) {
        int ropeKnotUStart = random.nextInt(16 - RopeBridge.KNOT_SIZE);
        int ropeKnotVStart = random.nextInt(16 - RopeBridge.KNOT_SIZE);
        drawCube(emitter, material, (index, x, y, z, u, v, nx, ny, nz, dir) ->
            add(
                emitter, index, stack, translucent,
                x*RopeBridge.KNOT_SIZE/32F, y*RopeBridge.KNOT_SIZE/32F, z*RopeBridge.KNOT_SIZE/32F,
                rope.getFrameU(ropeKnotUStart + RopeBridge.KNOT_SIZE*Math.signum(u+1F)), rope.getFrameV(ropeKnotVStart + RopeBridge.KNOT_SIZE*Math.signum(v+1F)),
                nx, ny, nz
            )
        );
    }

    public static void drawStopper(QuadEmitter emitter, MatrixStack stack, RenderMaterial material, Random random, boolean translucent, int modifier) {
        stack.push();
        stack.translate(0, 0, modifier * RopeBridge.PLANK_LENGTH/2F);

        float frameUStart = RopeBridge.STOPPER_WIDTH + random.nextInt(16 - 2 * RopeBridge.STOPPER_WIDTH);
        drawCube(emitter, material, (index, x, y, z, u, v, nx, ny, nz, dir) -> {
            Sprite sprite = BridgePlanksBakedModel.STOPPER.getSprite();
            float texU;
            float texV;
            if(dir.getAxis() == Direction.Axis.Y) {
                texU = sprite.getFrameU(u*RopeBridge.STOPPER_WIDTH);
                texV = sprite.getFrameV(16F - v*RopeBridge.STOPPER_WIDTH);
            } else {
                texU = sprite.getFrameU(frameUStart+u*RopeBridge.STOPPER_WIDTH);
                texV = sprite.getFrameV(16F - v*RopeBridge.STOPPER_HEIGHT);
            }
            add(emitter, index, stack, translucent,
                x*RopeBridge.STOPPER_WIDTH/32F, zo(y)*RopeBridge.STOPPER_HEIGHT/16F, z*RopeBridge.STOPPER_WIDTH/32F,
                texU, texV,
                nx, ny, nz
            );
        });
        stack.pop();
    }

    private static void drawCube(QuadEmitter emitter, RenderMaterial material, CubeRenderCallback callback) {
        int[] vertex1 = new int[]{1, -1, -1, 1};
        int[] vertex2 = new int[]{-1, -1, 1, 1};

        for (Direction d : Direction.values()) {
            int[] x = new int[4];
            int[] y = new int[4];
            int[] z = new int[4];

            switch (d.getAxis()) {
                case X:
                    Arrays.fill(x, d.getOffsetX());
                    y = vertex1;
                    z = vertex2;
                    break;
                case Y:
                    x = vertex1;
                    Arrays.fill(y, d.getOffsetY());
                    z = vertex2;
                    break;
                case Z:
                    x = vertex2;
                    y = vertex1;
                    Arrays.fill(z, d.getOffsetZ());
                    break;
            }

            emitter.material(material);
            for (int v = 0; v < 4; v++) {
                //In minecraft world, X axis is flipped
                boolean flip = (d.getDirection() == Direction.AxisDirection.POSITIVE) != (d.getAxis() == Direction.Axis.X);
                callback.apply(flip ? v : 3 - v, x[v], y[v], z[v], zo(vertex2[v]), zo(vertex1[v]), d.getOffsetX(), d.getOffsetY(), d.getOffsetZ(), d);
            }
            emitter.emit();
        }
    }

    //Convert [-1, 1] to [0, 1]
    private static int zo(int i) {
        if(i < 0) {
            return 0;
        } else {
            return 1;
        }
    }

    private void drawDoubleSided(QuadEmitter emitter, RenderMaterial material, VertexConsumer v1, VertexConsumer v2, VertexConsumer v3, VertexConsumer v4) {
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

    private static void add(QuadEmitter emitter, int index, MatrixStack stack, boolean translucent, float x, float y, float z, float u, float v, float nx, float ny, float nz) {
        Vector4f pos = new Vector4f(x, y, z, 1.0F);
        pos.transform(stack.peek().getModel());

        Vector3f normal = new Vector3f(nx, ny, nz);
        normal.transform(stack.peek().getNormal());

        emitter
            .pos(index, pos.getX(), pos.getY(), pos.getZ())
            .sprite(index, 0, u, v)
            .spriteColor(index, 0, translucent ? 0x88FFFFFF : 0xFFFFFFFF)
            .cullFace(null)
            .normal(index, normal);

        //Needed so we don't have problems with the knots sides being set to black
        if(emitter instanceof QuadViewImpl) {
            ((QuadViewImpl) emitter).geometryFlags(((QuadViewImpl) emitter).geometryFlags() - GeometryHelper.AXIS_ALIGNED_FLAG);
        }
    }

    private static Mesh emptyPlanksMesh;

    public static Mesh getEmptyPlanksMesh() {
        if(emptyPlanksMesh != null) {
            return emptyPlanksMesh;
        }

        RenderMaterial material = RendererAccess.INSTANCE.getRenderer().materialFinder().blendMode(0, BlendMode.CUTOUT).find();
        MeshBuilder builder = IndigoRenderer.INSTANCE.meshBuilder();
        QuadEmitter emitter = builder.getEmitter();

        MatrixStack stack = new MatrixStack();
        stack.push();
        stack.translate(0.5, 0, 0.5);

        drawStopper(emitter, stack, material, new Random(0), false, 1);
        drawStopper(emitter, stack, material, new Random(0), false, -1);
        stack.pop();

        return emptyPlanksMesh = builder.build();

    }

    @FunctionalInterface
    private interface VertexConsumer {
        void apply(int index, int modifier);
    }
    @FunctionalInterface
    private interface CubeRenderCallback {
        void apply(int index, int x, int y, int z, int u, int v, int nx, int ny, int nz, Direction dir);
    }

}
