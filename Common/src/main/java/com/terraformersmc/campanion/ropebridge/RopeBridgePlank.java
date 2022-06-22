package com.terraformersmc.campanion.ropebridge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import com.terraformersmc.campanion.client.model.block.BridgePlanksBakedModel;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import java.util.Arrays;
import java.util.Random;

public class RopeBridgePlank {
    private final BlockPos from;
    private final BlockPos to;
    private final Vec3 deltaPosition;
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

    public RopeBridgePlank(
        BlockPos from, BlockPos to, Vec3 deltaPosition, double yAngle, double tiltAngle, float distToPrevious,
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

    public Vec3 getDeltaPosition() {
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
            BlockPos.of(tag.getLong("FromPos")),
            BlockPos.of(tag.getLong("ToPos")),
            new Vec3(tag.getDouble("DeltaPosX"), tag.getDouble("DeltaPosY"), tag.getDouble("DeltaPosZ")),
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

    public void generateMesh(boolean translucent, QuadEmitter emitter) {
        if(!this.master) {
            return;
        }
        Random random = new Random(this.ropeVariant);

        //Randoms at the start of their lifecycle will often give out the same data.
        //The following is to ensure that what's given to us isn't going to be similar
        byte[] discarded = new byte[256];
        random.nextBytes(discarded);

        RenderMaterial material = RendererAccess.INSTANCE.getRenderer().materialFinder().blendMode(0, BlendMode.CUTOUT).find();

        PoseStack stack = new PoseStack();
        stack.pushPose();
        stack.translate(this.deltaPosition.x(), this.deltaPosition.y(), this.deltaPosition.z);
        stack.mulPose(Vector3f.YP.rotation(-this.yAngle));

        this.renderPlank(emitter, stack, material, random, translucent);

        this.drawRope(emitter, stack, material, random, translucent);
        stack.popPose();
    }

    private void renderPlank(QuadEmitter emitter, PoseStack stack, RenderMaterial material, Random random, boolean translucent) {
        stack.pushPose();
        if(!this.flat) {
            stack.mulPose(Vector3f.ZP.rotation(this.tiltAngle));
        }
        TextureAtlasSprite sprite = BridgePlanksBakedModel.PLANKS[random.nextInt(RopeBridge.PLANK_VARIANT_TEXTURES)].sprite();
        double vStart = random.nextInt((int) (1F / RopeBridge.PLANK_WIDTH))*RopeBridge.PLANK_WIDTH;
        float minV = sprite.getV(vStart*16F);
        float maxV = sprite.getV((vStart+RopeBridge.PLANK_WIDTH)*16F);

        float hl = (float) (RopeBridge.PLANK_LENGTH / 2D); //half length
        float hw = (float) (RopeBridge.PLANK_WIDTH / 2D); //half width
        this.drawDoubleSided(emitter, material,
            (v, m) -> add(emitter, v, stack, translucent, -hw, 0, -hl, sprite.getU1(), minV, 0, m, 0),
            (v, m) -> add(emitter, v, stack, translucent, -hw, 0,  hl, sprite.getU0(), minV, 0, m, 0),
            (v, m) -> add(emitter, v, stack, translucent,  hw, 0,  hl, sprite.getU0(), maxV, 0, m, 0),
            (v, m) -> add(emitter, v, stack, translucent,  hw, 0, -hl, sprite.getU1(), maxV, 0, m, 0)
        );

        stack.popPose();
    }

    private void drawRope(QuadEmitter emitter, PoseStack stack, RenderMaterial material, Random random, boolean translucent) {
        TextureAtlasSprite rope = BridgePlanksBakedModel.ROPE.sprite();

        for (int modifier : new int[] {-1, 1}) {
            if(this.ropes) {
                this.drawVerticalRope(emitter, stack, material, rope, random, translucent, modifier);
            }
            if(this.stopper) {
                drawStopper(emitter, stack, material, random, translucent, modifier);
            }
            this.drawBottomRopes(emitter, stack, material, rope, random, translucent, modifier);

            stack.pushPose();
            stack.translate(0, RopeBridge.ROPE_LENGTH/16F - this.ropesSubtract, modifier * RopeBridge.PLANK_LENGTH / 2D);
            if(this.ropes) {
            	//Rotate it a tiny bit as to not make the knots axis aligned. See: https://imgur.com/iCGrpn9
				stack.pushPose();
				stack.mulPose(Vector3f.XP.rotationDegrees(0.5F));
				stack.mulPose(Vector3f.YP.rotationDegrees(0.5F));
                this.drawKnot(emitter, stack, material, rope, random, translucent);
            	stack.popPose();
            }
            stack.mulPose(Vector3f.ZP.rotation(this.tiltAngle));
            this.drawConnectingRope(emitter, stack, material, rope, random, translucent, modifier);
            stack.popPose();
        }
    }


    private void drawBottomRopes(QuadEmitter emitter, PoseStack stack, RenderMaterial material, TextureAtlasSprite rope, Random random, boolean translucent, int modifier) {
        stack.pushPose();
        stack.translate(0, -0.025, modifier * (RopeBridge.PLANK_LENGTH/2F - RopeBridge.UNDER_ROPE_DIST_FROM_EDGE/16F));
        stack.mulPose(Vector3f.ZP.rotation(this.tiltAngle));

        float ropeStart = random.nextFloat()*(16F - this.distToPrevious*16F);
        float minU = rope.getU(ropeStart);
        float maxU = rope.getU(ropeStart + RopeBridge.ROPE_WIDTH);
        float minV = rope.getV(ropeStart);
        float maxV = rope.getV(ropeStart + this.distToPrevious*16F);

        this.drawDoubleSided(emitter, material,
            (v, m) -> add(emitter, v, stack, translucent, 0, 0, 0, maxU, minV, 0, -m*modifier, 0),
            (v, m) -> add(emitter, v, stack, translucent, 0, 0,  modifier*-RopeBridge.ROPE_WIDTH/16F, minU, minV, 0, -m*modifier, 0),
            (v, m) -> add(emitter, v, stack, translucent,  this.distToPrevious, 0,  modifier*-RopeBridge.ROPE_WIDTH/16F, minU, maxV, 0, -m*modifier, 0),
            (v, m) -> add(emitter, v, stack, translucent,  this.distToPrevious, 0, 0, maxU, maxV, 0, -m*modifier, 0)
        );

        stack.popPose();
    }

    private void drawConnectingRope(QuadEmitter emitter, PoseStack stack, RenderMaterial material, TextureAtlasSprite rope, Random random, boolean translucent, int modifier) {
        float ropeStart = random.nextFloat() * (16F - this.distToPrevious*16F);
        float ropeMinU = rope.getU(ropeStart);
        float ropeMaxU = rope.getU(ropeStart + RopeBridge.ROPE_WIDTH);
        float ropeMinV = rope.getV(ropeStart);
        float ropeMaxV = rope.getV(ropeStart + this.distToPrevious*16F);

        if(this.distToPrevious != 0) {
            this.drawDoubleSided(emitter, material,
                (v, m) -> add(emitter, v, stack, translucent, 0, -RopeBridge.ROPE_WIDTH/32F, 0, ropeMinU, ropeMinV, 0, 0, modifier*m),
                (v, m) -> add(emitter, v, stack, translucent, this.distToPrevious, -RopeBridge.ROPE_WIDTH/32F, 0, ropeMinU, ropeMaxV, 0, 0, modifier*m),
                (v, m) -> add(emitter, v, stack, translucent, this.distToPrevious, RopeBridge.ROPE_WIDTH/32F, 0, ropeMaxU, ropeMaxV, 0, 0, modifier*m),
                (v, m) -> add(emitter, v, stack, translucent, 0, RopeBridge.ROPE_WIDTH/32F, 0, ropeMaxU, ropeMinV, 0, 0, modifier*m)
            );
        }
    }

    private void drawVerticalRope(QuadEmitter emitter, PoseStack stack, RenderMaterial material, TextureAtlasSprite rope, Random random, boolean translucent, int modifier) {
        float hl = (float) (RopeBridge.PLANK_LENGTH / 2D); //half length
        float mhl = modifier*hl;

        float ropeUStart = random.nextInt(16 - RopeBridge.ROPE_WIDTH);
        float ropeMinU = rope.getU(ropeUStart);
        float ropeMaxU = rope.getU(ropeUStart + RopeBridge.ROPE_WIDTH);
        float ropeMinV = rope.getV0();
        float ropeMaxV = rope.getV(RopeBridge.ROPE_LENGTH - this.ropesSubtract*16F);

        this.drawDoubleSided(emitter, material,
            (v, m) -> add(emitter, v, stack, translucent, -RopeBridge.ROPE_WIDTH/32F, RopeBridge.ROPE_LENGTH/16F - this.ropesSubtract, mhl, ropeMinU, ropeMinV, 0, 0, m*modifier),
            (v, m) -> add(emitter, v, stack, translucent, -RopeBridge.ROPE_WIDTH/32F, 0, mhl, ropeMinU, ropeMaxV, 0, 0, m*modifier),
            (v, m) -> add(emitter, v, stack, translucent, RopeBridge.ROPE_WIDTH/32F, 0, mhl, ropeMaxU, ropeMaxV, 0, 0, m*modifier),
            (v, m) -> add(emitter, v, stack, translucent, RopeBridge.ROPE_WIDTH/32F, RopeBridge.ROPE_LENGTH/16F - this.ropesSubtract, mhl, ropeMaxU, ropeMinV, 0, 0, m*modifier)
        );
    }

    private void drawKnot(QuadEmitter emitter, PoseStack stack, RenderMaterial material, TextureAtlasSprite rope, Random random, boolean translucent) {
        int ropeKnotUStart = random.nextInt(16 - RopeBridge.KNOT_SIZE);
        int ropeKnotVStart = random.nextInt(16 - RopeBridge.KNOT_SIZE);
        drawCube(emitter, material, (index, x, y, z, u, v, nx, ny, nz, dir) ->
            add(
                emitter, index, stack, translucent,
                x*RopeBridge.KNOT_SIZE/32F, y*RopeBridge.KNOT_SIZE/32F, z*RopeBridge.KNOT_SIZE/32F,
                rope.getU(ropeKnotUStart + RopeBridge.KNOT_SIZE*Math.signum(u+1F)), rope.getV(ropeKnotVStart + RopeBridge.KNOT_SIZE*Math.signum(v+1F)),
                nx, ny, nz
            )
        );
    }

    public static void drawStopper(QuadEmitter emitter, PoseStack stack, RenderMaterial material, Random random, boolean translucent, int modifier) {
        stack.pushPose();
        stack.translate(0, 0, modifier * RopeBridge.PLANK_LENGTH/2F);

        float frameUStart = RopeBridge.STOPPER_WIDTH + random.nextInt(16 - 2 * RopeBridge.STOPPER_WIDTH);
        drawCube(emitter, material, (index, x, y, z, u, v, nx, ny, nz, dir) -> {
            TextureAtlasSprite sprite = BridgePlanksBakedModel.STOPPER.sprite();
            float texU;
            float texV;
            if(dir.getAxis() == Direction.Axis.Y) {
                texU = sprite.getU(u*RopeBridge.STOPPER_WIDTH);
                texV = sprite.getV(16F - v*RopeBridge.STOPPER_WIDTH);
            } else {
                texU = sprite.getU(frameUStart+u*RopeBridge.STOPPER_WIDTH);
                texV = sprite.getV(16F - v*RopeBridge.STOPPER_HEIGHT);
            }
            add(emitter, index, stack, translucent,
                x*RopeBridge.STOPPER_WIDTH/32F, zo(y)*RopeBridge.STOPPER_HEIGHT/16F, z*RopeBridge.STOPPER_WIDTH/32F,
                texU, texV,
                nx, ny, nz
            );
        });
        stack.popPose();
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
                    Arrays.fill(x, d.getStepX());
                    y = vertex1;
                    z = vertex2;
                    break;
                case Y:
                    x = vertex1;
                    Arrays.fill(y, d.getStepY());
                    z = vertex2;
                    break;
                case Z:
                    x = vertex2;
                    y = vertex1;
                    Arrays.fill(z, d.getStepZ());
                    break;
            }

            emitter.material(material);
            for (int v = 0; v < 4; v++) {
                //In minecraft world, X axis is flipped
                boolean flip = (d.getAxisDirection() == Direction.AxisDirection.POSITIVE) != (d.getAxis() == Direction.Axis.X);
                callback.apply(flip ? v : 3 - v, x[v], y[v], z[v], zo(vertex2[v]), zo(vertex1[v]), d.getStepX(), d.getStepY(), d.getStepZ(), d);
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

    private static void add(QuadEmitter emitter, int index, PoseStack stack, boolean translucent, float x, float y, float z, float u, float v, float nx, float ny, float nz) {
        Vector4f pos = new Vector4f(x, y, z, 1.0F);
        pos.transform(stack.last().pose());

        Vector3f normal = new Vector3f(nx, ny, nz);
        normal.transform(stack.last().normal());

        emitter
            .pos(index, pos.x(), pos.y(), pos.z())
            .sprite(index, 0, u, v)
            .spriteColor(index, 0, translucent ? 0x88FFFFFF : 0xFFFFFFFF)
            .cullFace(null)
            .normal(index, normal);
    }

    public static void generateDefaultStoppers(QuadEmitter emitter) {
		RenderMaterial material = RendererAccess.INSTANCE.getRenderer().materialFinder().blendMode(0, BlendMode.CUTOUT).find();

        PoseStack stack = new PoseStack();
        stack.pushPose();
        stack.translate(0.5, 0, 0.5);

        drawStopper(emitter, stack, material, new Random(0), false, 1);
        drawStopper(emitter, stack, material, new Random(0), false, -1);
        stack.popPose();
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
