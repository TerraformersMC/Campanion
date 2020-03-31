package com.campanion.client.renderer.entity;

import com.campanion.entity.GrapplingHookEntity;
import com.campanion.item.CampanionItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.Matrix3f;
import net.minecraft.client.util.math.Matrix4f;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class GrapplingHookEntityRenderer extends EntityRenderer<GrapplingHookEntity> {
	private static final Identifier SKIN = new Identifier("textures/entity/fishing_hook.png");
	private static final RenderLayer ENTITY_CUTOUT = RenderLayer.getEntityCutout(SKIN);

	public GrapplingHookEntityRenderer(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	public void render(GrapplingHookEntity hook, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int i) {
		PlayerEntity player = hook.getPlayer();
		if (player != null) {
			matrices.push();
			matrices.push();
			matrices.scale(0.5F, 0.5F, 0.5F);
			matrices.multiply(this.renderManager.getRotation());
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
			MatrixStack.Entry entry = matrices.peek();
			Matrix4f matrix4f = entry.getModel();
			Matrix3f matrix3f = entry.getNormal();
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(ENTITY_CUTOUT);
			method_23840(vertexConsumer, matrix4f, matrix3f, i, 0.0F, 0, 0, 1);
			method_23840(vertexConsumer, matrix4f, matrix3f, i, 1.0F, 0, 1, 1);
			method_23840(vertexConsumer, matrix4f, matrix3f, i, 1.0F, 1, 1, 0);
			method_23840(vertexConsumer, matrix4f, matrix3f, i, 0.0F, 1, 0, 0);
			matrices.pop();
			int j = player.getMainArm() == Arm.RIGHT ? 1 : -1;
			ItemStack itemStack = player.getMainHandStack();
			if (itemStack.getItem() != CampanionItems.GRAPPLING_HOOK) {
				j = -j;
			}

			float h = player.getHandSwingProgress(g);
			float k = MathHelper.sin(MathHelper.sqrt(h) * 3.1415927F);
			float l = MathHelper.lerp(g, player.prevBodyYaw, player.bodyYaw) * 0.017453292F;
			double d = MathHelper.sin(l);
			double e = MathHelper.cos(l);
			double m = (double) j * 0.35D;
			double n = 0.8D;
			double t;
			double u;
			double v;
			float w;
			double x;
			if ((this.renderManager.gameOptions == null || this.renderManager.gameOptions.perspective <= 0) && player == MinecraftClient.getInstance().player) {
				x = this.renderManager.gameOptions.fov;
				x /= 100.0D;
				Vec3d vec3d = new Vec3d((double) j * -0.36D * x, -0.045D * x, 0.4D);
				vec3d = vec3d.rotateX(-MathHelper.lerp(g, player.prevPitch, player.pitch) * 0.017453292F);
				vec3d = vec3d.rotateY(-MathHelper.lerp(g, player.prevYaw, player.yaw) * 0.017453292F);
				vec3d = vec3d.rotateY(k * 0.5F);
				vec3d = vec3d.rotateX(-k * 0.7F);
				t = MathHelper.lerp(g, player.prevX, player.getX()) + vec3d.x;
				u = MathHelper.lerp(g, player.prevY, player.getY()) + vec3d.y;
				v = MathHelper.lerp(g, player.prevZ, player.getZ()) + vec3d.z;
				w = player.getStandingEyeHeight();
			} else {
				t = MathHelper.lerp(g, player.prevX, player.getX()) - e * m - d * 0.8D;
				u = player.prevY + (double) player.getStandingEyeHeight() + (player.getY() - player.prevY) * (double) g - 0.45D;
				v = MathHelper.lerp(g, player.prevZ, player.getZ()) - d * m + e * 0.8D;
				w = player.isInSneakingPose() ? -0.1875F : 0.0F;
			}

			x = MathHelper.lerp(g, hook.prevX, hook.getX());
			double y = MathHelper.lerp(g, hook.prevY, hook.getY()) + 0.25D;
			double z = MathHelper.lerp(g, hook.prevZ, hook.getZ());
			float aa = (float) (t - x);
			float ab = (float) (u - y) + w;
			float ac = (float) (v - z);
			VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getLines());
			Matrix4f matrix4f2 = matrices.peek().getModel();

			for (int ae = 0; ae < 16; ++ae) {
				method_23172(aa, ab, ac, vertexConsumer2, matrix4f2, method_23954(ae, 16));
				method_23172(aa, ab, ac, vertexConsumer2, matrix4f2, method_23954(ae + 1, 16));
			}

			matrices.pop();
			super.render(hook, f, g, matrices, vertexConsumerProvider, i);
		}
	}

	private static float method_23954(int i, int j) {
		return (float) i / (float) j;
	}

	private static void method_23840(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int i, float f, int j, int k, int l) {
		vertexConsumer.vertex(matrix4f, f - 0.5F, (float) j - 0.5F, 0.0F).color(255, 255, 255, 255).texture((float) k, (float) l).overlay(OverlayTexture.DEFAULT_UV).light(i).normal(matrix3f, 0.0F, 1.0F, 0.0F).next();
	}

	private static void method_23172(float f, float g, float h, VertexConsumer vertexConsumer, Matrix4f matrix4f, float i) {
		vertexConsumer.vertex(matrix4f, f * i, g * (i * i + i) * 0.5F + 0.25F, h * i).color(0, 0, 0, 255).next();
	}

	public Identifier getTexture(GrapplingHookEntity hook) {
		return SKIN;
	}
}