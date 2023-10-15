package com.terraformersmc.campanion.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.terraformersmc.campanion.client.model.block.BridgePlanksBakedModel;
import com.terraformersmc.campanion.platform.services.rendering.BlockModelPartCreator;
import com.terraformersmc.campanion.ropebridge.RopeBridge;
import com.terraformersmc.campanion.ropebridge.RopeBridgePlank;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.Arrays;
import java.util.Random;

public class RopeBridgePlankRenderer {

	public static void emitAllQuads(RopeBridgePlank plank, boolean translucent, BlockModelPartCreator emitter) {
		if (!plank.master()) {
			return;
		}
		Random random = new Random(plank.ropeVariant());

		//Randoms at the start of their lifecycle will often give out the same data.
		//The following is to ensure that what's given to us isn't going to be similar
		byte[] discarded = new byte[256];
		random.nextBytes(discarded);

		PoseStack stack = new PoseStack();
		stack.pushPose();
		stack.translate(plank.deltaPosition().x(), plank.deltaPosition().y(), plank.deltaPosition().z);
		stack.mulPose(Axis.YP.rotation(-plank.yAngle()));

		renderPlank(plank, emitter, stack, random, translucent);

		drawRope(plank, emitter, stack, random, translucent);
		stack.popPose();
	}

	private static void renderPlank(RopeBridgePlank plank, BlockModelPartCreator emitter, PoseStack stack, Random random, boolean translucent) {
		stack.pushPose();
		if (!plank.flat()) {
			stack.mulPose(Axis.ZP.rotation((float) plank.tiltAngle()));
		}
		TextureAtlasSprite sprite = BridgePlanksBakedModel.PLANKS[random.nextInt(RopeBridge.PLANK_VARIANT_TEXTURES)].sprite();
		double vStart = random.nextInt((int) (1F / RopeBridge.PLANK_WIDTH)) * RopeBridge.PLANK_WIDTH;
		float minV = sprite.getV(vStart * 16F);
		float maxV = sprite.getV((vStart + RopeBridge.PLANK_WIDTH) * 16F);

		float hl = (float) (RopeBridge.PLANK_LENGTH / 2D); //half length
		float hw = (float) (RopeBridge.PLANK_WIDTH / 2D); //half width
		drawDoubleSided(emitter,
			m -> add(emitter, stack, translucent, -hw, 0, -hl, sprite.getU1(), minV, 0, -m, 0),
			m -> add(emitter, stack, translucent, -hw, 0, hl, sprite.getU0(), minV, 0, -m, 0),
			m -> add(emitter, stack, translucent, hw, 0, hl, sprite.getU0(), maxV, 0, -m, 0),
			m -> add(emitter, stack, translucent, hw, 0, -hl, sprite.getU1(), maxV, 0, -m, 0)
		);

		stack.popPose();
	}

	private static void drawRope(RopeBridgePlank plank, BlockModelPartCreator emitter, PoseStack stack, Random random, boolean translucent) {
		TextureAtlasSprite rope = BridgePlanksBakedModel.ROPE.sprite();

		for (int modifier : new int[]{-1, 1}) {
			if (plank.ropes()) {
				drawVerticalRope(plank, emitter, stack, rope, random, translucent, modifier);
			}
			if (plank.stopper()) {
				drawStopper(emitter, stack, random, translucent, modifier);
			}
			drawBottomRopes(plank, emitter, stack, rope, random, translucent, modifier);

			stack.pushPose();
			stack.translate(0, RopeBridge.ROPE_LENGTH / 16F - plank.ropesSubtract(), modifier * RopeBridge.PLANK_LENGTH / 2D);
			if (plank.ropes()) {
				//Rotate it a tiny bit as to not make the knots axis aligned. See: https://imgur.com/iCGrpn9
				stack.pushPose();
				stack.mulPose(Axis.XP.rotationDegrees(0.5F));
				stack.mulPose(Axis.YP.rotationDegrees(0.5F));
				drawKnot(plank, emitter, stack, rope, random, translucent);
				stack.popPose();
			}
			stack.mulPose(Axis.ZP.rotation((float) plank.tiltAngle()));
			drawConnectingRope(plank, emitter, stack, rope, random, translucent, modifier);
			stack.popPose();
		}
	}


	private static void drawBottomRopes(RopeBridgePlank plank, BlockModelPartCreator emitter, PoseStack stack, TextureAtlasSprite rope, Random random, boolean translucent, int modifier) {
		stack.pushPose();
		stack.translate(0, -0.025, modifier * (RopeBridge.PLANK_LENGTH / 2F - RopeBridge.UNDER_ROPE_DIST_FROM_EDGE / 16F));
		stack.mulPose(Axis.ZP.rotation((float) plank.tiltAngle()));

		float ropeStart = random.nextFloat() * (16F - plank.distToPrevious() * 16F);
		float minU = rope.getU(ropeStart);
		float maxU = rope.getU(ropeStart + RopeBridge.ROPE_WIDTH);
		float minV = rope.getV(ropeStart);
		float maxV = rope.getV(ropeStart + plank.distToPrevious() * 16F);

		drawDoubleSided(emitter,
			m -> add(emitter, stack, translucent, 0, 0, 0, maxU, minV, 0, m * modifier, 0),
			m -> add(emitter, stack, translucent, 0, 0, modifier * -RopeBridge.ROPE_WIDTH / 16F, minU, minV, 0, m * modifier, 0),
			m -> add(emitter, stack, translucent, plank.distToPrevious(), 0, modifier * -RopeBridge.ROPE_WIDTH / 16F, minU, maxV, 0, m * modifier, 0),
			m -> add(emitter, stack, translucent, plank.distToPrevious(), 0, 0, maxU, maxV, 0, m * modifier, 0)
		);

		stack.popPose();
	}

	private static void drawConnectingRope(RopeBridgePlank plank, BlockModelPartCreator emitter, PoseStack stack, TextureAtlasSprite rope, Random random, boolean translucent, int modifier) {
		float ropeStart = random.nextFloat() * (16F - plank.distToPrevious() * 16F);
		float ropeMinU = rope.getU(ropeStart);
		float ropeMaxU = rope.getU(ropeStart + RopeBridge.ROPE_WIDTH);
		float ropeMinV = rope.getV(ropeStart);
		float ropeMaxV = rope.getV(ropeStart + plank.distToPrevious() * 16F);

		if (plank.distToPrevious() != 0) {
			drawDoubleSided(emitter,
				m -> add(emitter, stack, translucent, 0, -RopeBridge.ROPE_WIDTH / 32F, 0, ropeMinU, ropeMinV, 0, 0, modifier * m),
				m -> add(emitter, stack, translucent, plank.distToPrevious(), -RopeBridge.ROPE_WIDTH / 32F, 0, ropeMinU, ropeMaxV, 0, 0, modifier * m),
				m -> add(emitter, stack, translucent, plank.distToPrevious(), RopeBridge.ROPE_WIDTH / 32F, 0, ropeMaxU, ropeMaxV, 0, 0, modifier * m),
				m -> add(emitter, stack, translucent, 0, RopeBridge.ROPE_WIDTH / 32F, 0, ropeMaxU, ropeMinV, 0, 0, modifier * m)
			);
		}
	}

	private static void drawVerticalRope(RopeBridgePlank plank, BlockModelPartCreator emitter, PoseStack stack, TextureAtlasSprite rope, Random random, boolean translucent, int modifier) {
		float hl = (float) (RopeBridge.PLANK_LENGTH / 2D); //half length
		float mhl = modifier * hl;

		float ropeUStart = random.nextInt(16 - RopeBridge.ROPE_WIDTH);
		float ropeMinU = rope.getU(ropeUStart);
		float ropeMaxU = rope.getU(ropeUStart + RopeBridge.ROPE_WIDTH);
		float ropeMinV = rope.getV0();
		float ropeMaxV = rope.getV(RopeBridge.ROPE_LENGTH - plank.ropesSubtract() * 16F);

		drawDoubleSided(emitter,
			m -> add(emitter, stack, translucent, -RopeBridge.ROPE_WIDTH / 32F, RopeBridge.ROPE_LENGTH / 16F - plank.ropesSubtract(), mhl, ropeMinU, ropeMinV, 0, 0, m * modifier),
			m -> add(emitter, stack, translucent, -RopeBridge.ROPE_WIDTH / 32F, 0, mhl, ropeMinU, ropeMaxV, 0, 0, m * modifier),
			m -> add(emitter, stack, translucent, RopeBridge.ROPE_WIDTH / 32F, 0, mhl, ropeMaxU, ropeMaxV, 0, 0, m * modifier),
			m -> add(emitter, stack, translucent, RopeBridge.ROPE_WIDTH / 32F, RopeBridge.ROPE_LENGTH / 16F - plank.ropesSubtract(), mhl, ropeMaxU, ropeMinV, 0, 0, m * modifier)
		);
	}

	private static void drawKnot(RopeBridgePlank plank, BlockModelPartCreator emitter, PoseStack stack, TextureAtlasSprite rope, Random random, boolean translucent) {
		int ropeKnotUStart = random.nextInt(16 - RopeBridge.KNOT_SIZE);
		int ropeKnotVStart = random.nextInt(16 - RopeBridge.KNOT_SIZE);
		drawCube(emitter, (x, y, z, u, v, nx, ny, nz, dir) ->
			add(
				emitter, stack, translucent,
				x * RopeBridge.KNOT_SIZE / 32F, y * RopeBridge.KNOT_SIZE / 32F, z * RopeBridge.KNOT_SIZE / 32F,
				rope.getU(ropeKnotUStart + RopeBridge.KNOT_SIZE * Math.signum(u + 1F)), rope.getV(ropeKnotVStart + RopeBridge.KNOT_SIZE * Math.signum(v + 1F)),
				nx, ny, nz
			)
		);
	}

	public static void drawStopper(BlockModelPartCreator emitter, PoseStack stack, Random random, boolean translucent, int modifier) {
		stack.pushPose();
		stack.translate(0, 0, modifier * RopeBridge.PLANK_LENGTH / 2F);

		float frameUStart = RopeBridge.STOPPER_WIDTH + random.nextInt(16 - 2 * RopeBridge.STOPPER_WIDTH);
		drawCube(emitter, (x, y, z, u, v, nx, ny, nz, dir) -> {
			TextureAtlasSprite sprite = BridgePlanksBakedModel.STOPPER.sprite();
			float texU;
			float texV;
			if (dir.getAxis() == Direction.Axis.Y) {
				texU = sprite.getU(u * RopeBridge.STOPPER_WIDTH);
				texV = sprite.getV(16F - v * RopeBridge.STOPPER_WIDTH);
			} else {
				texU = sprite.getU(frameUStart + u * RopeBridge.STOPPER_WIDTH);
				texV = sprite.getV(16F - v * RopeBridge.STOPPER_HEIGHT);
			}
			add(emitter, stack, translucent,
				x * RopeBridge.STOPPER_WIDTH / 32F, zo(y) * RopeBridge.STOPPER_HEIGHT / 16F, z * RopeBridge.STOPPER_WIDTH / 32F,
				texU, texV,
				nx, ny, nz
			);
		});
		stack.popPose();
	}

	private static void drawCube(BlockModelPartCreator emitter, CubeRenderCallback callback) {
		int[] vertex1 = new int[]{1, -1, -1, 1};
		int[] vertex2 = new int[]{1, 1, -1, -1};

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

			emitter.beginQuad();
			boolean flip = (d.getAxisDirection() == Direction.AxisDirection.POSITIVE) != (d.getAxis() == Direction.Axis.X);
			for (int v = 0; v < 4; v++) {
				//In minecraft world, X axis is flipped
				int i = flip ? 3 - v : v;
				callback.apply(x[i], y[i], z[i], zo(vertex2[i]), zo(vertex1[i]), d.getStepX(), d.getStepY(), d.getStepZ(), d);
			}
			emitter.finishQuad();
		}
	}

	//Convert [-1, 1] to [0, 1]
	private static int zo(int i) {
		if (i < 0) {
			return 0;
		} else {
			return 1;
		}
	}

	private static void drawDoubleSided(BlockModelPartCreator emitter, VertexConsumer v1, VertexConsumer v2, VertexConsumer v3, VertexConsumer v4) {
		emitter.beginQuad();
		v1.apply(-1);
		v2.apply(-1);
		v3.apply(-1);
		v4.apply(-1);
		emitter.finishQuad();

		emitter.beginQuad();
		v4.apply(1);
		v3.apply(1);
		v2.apply(1);
		v1.apply(1);
		emitter.finishQuad();
	}

	private static void add(BlockModelPartCreator emitter, PoseStack stack, boolean translucent, float x, float y, float z, float u, float v, float nx, float ny, float nz) {
		Vector4f pos = new Vector4f(x, y, z, 1.0F);
		pos = stack.last().pose().transform(pos);

		Vector3f normal = new Vector3f(nx, ny, nz);
		normal = stack.last().normal().transform(normal);

		emitter
			.position(pos.x(), pos.y(), pos.z())
			.uv(u, v)
			.colour(translucent ? 0x88FFFFFF : 0xFFFFFFFF)
//			.cullFace(null)
			.normal(normal.x(), normal.y(), normal.z())
			.finishVertex();
	}

	public static void generateDefaultStoppers(BlockModelPartCreator emitter) {
//		RenderMaterial material = RendererAccess.INSTANCE.getRenderer().materialFinder().blendMode(0, BlendMode.CUTOUT).find();

		PoseStack stack = new PoseStack();
		stack.pushPose();
		stack.translate(0.5, 0, 0.5);

		drawStopper(emitter, stack, new Random(0), false, 1);
		drawStopper(emitter, stack, new Random(0), false, -1);
		stack.popPose();
	}

	@FunctionalInterface
	private interface VertexConsumer {
		void apply(int modifier);
	}

	@FunctionalInterface
	private interface CubeRenderCallback {
		void apply(int x, int y, int z, int u, int v, int nx, int ny, int nz, Direction dir);
	}
}
