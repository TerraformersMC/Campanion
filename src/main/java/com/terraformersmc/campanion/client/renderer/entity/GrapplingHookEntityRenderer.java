package com.terraformersmc.campanion.client.renderer.entity;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.client.model.entity.GrapplingHookEntityModel;
import com.terraformersmc.campanion.entity.GrapplingHookEntity;
import com.terraformersmc.campanion.item.CampanionItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class GrapplingHookEntityRenderer extends EntityRenderer<GrapplingHookEntity> {

	private static final Identifier TEXTURE = new Identifier(Campanion.MOD_ID, "textures/entity/grapple_hook.png");

	private final GrapplingHookEntityModel model = new GrapplingHookEntityModel();

	public GrapplingHookEntityRenderer(EntityRendererFactory.Context factoryCtx) {
		super(factoryCtx);
	}

	@Override
	public void render(GrapplingHookEntity entity, float yaw, float tickDelta, MatrixStack stack, VertexConsumerProvider vertexConsumers, int light) {
		PlayerEntity player = entity.getPlayer();
		if (player != null) {
			stack.push();

			stack.push();
			stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(tickDelta, entity.prevYaw, entity.getYaw()) - 90.0F));
			stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(tickDelta, entity.prevPitch, entity.getPitch()) + 90.0F));
			VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, model.getLayer(this.getTexture(entity)), false, false);
			this.model.render(stack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
			stack.pop();

			int armOffset = player.getMainArm() == Arm.RIGHT ? 1 : -1;
			ItemStack itemStack = player.getMainHandStack();
			if (itemStack.getItem() != CampanionItems.GRAPPLING_HOOK) {
				armOffset = -armOffset;
			}

			float h = player.getHandSwingProgress(tickDelta);
			float k = MathHelper.sin(MathHelper.sqrt(h) * 3.1415927F);
			float l = MathHelper.lerp(tickDelta, player.prevBodyYaw, player.bodyYaw) * 0.017453292F;
			double sinYaw = MathHelper.sin(l);
			double cosYaw = MathHelper.cos(l);
			double arm = (double)armOffset * 0.35D;
			double playerX;
			double playerY;
			double playerZ;
			float playerEye;
			if ((this.dispatcher.gameOptions == null || this.dispatcher.gameOptions.getPerspective().isFirstPerson()) && player == MinecraftClient.getInstance().player) {
				double x = 960.0D / this.dispatcher.gameOptions.fov;
				Vec3d vec3d = this.dispatcher.camera.getProjection().getPosition((float)armOffset * 0.525F, -0.1F);
				vec3d = vec3d.multiply(x);
				vec3d = vec3d.rotateY(k * 0.5F);
				vec3d = vec3d.rotateX(-k * 0.7F);
				playerX = MathHelper.lerp(tickDelta, player.prevX, player.getX()) + vec3d.x;
				playerY = MathHelper.lerp(tickDelta, player.prevY, player.getY()) + vec3d.y;
				playerZ = MathHelper.lerp(tickDelta, player.prevZ, player.getZ()) + vec3d.z;
				playerEye = player.getStandingEyeHeight();
			} else {
				playerX = MathHelper.lerp(tickDelta, player.prevX, player.getX()) - cosYaw * arm - sinYaw * 0.8D;
				playerY = player.prevY + player.getStandingEyeHeight() + (player.getY() - player.prevY) * (double)tickDelta - 0.45D;
				playerZ = MathHelper.lerp(tickDelta, player.prevZ, player.getZ()) - sinYaw * arm + cosYaw * 0.8D;
				playerEye = player.isInSneakingPose() ? -0.1875F : 0.0F;
			}

			double x = MathHelper.lerp(tickDelta, entity.prevX, entity.getX());
			double y = MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) + 0.25D;
			double z = MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ());
			float changeX = (float) (playerX - x);
			float changeY = (float) (playerY + playerEye - y);
			float changeZ = (float) (playerZ - z);
			VertexConsumer vertexConsumer2 = vertexConsumers.getBuffer(RenderLayer.getLineStrip());
			MatrixStack.Entry entry = stack.peek();

			float amplitude = MathHelper.clamp((15F - entity.age - tickDelta) / 15F, 0F, 1F);

			for (int p = 0; p < 256; ++p) {
				drawLineVertex(changeX, changeY, changeZ, vertexConsumer2, entry, p / 256F, (p + 1) / 256F, amplitude);
			}

			stack.pop();
			super.render(entity, yaw, tickDelta, stack, vertexConsumers, light);
		}
	}

	private static void drawLineVertex(float x, float y, float z, VertexConsumer buffer, MatrixStack.Entry normal, float t, float t1, float a) {
		float px = (float) (x * t + a * Math.sin(t * 2F * Math.PI));
		float py = y * t + 0.25F;
		float pz = z * t;
		float nx = (float) (x * t1 + a * Math.sin(t1 * 2F * Math.PI)) - px;
		float ny = y * t1 + 0.25F - py;
		float nz = z * t1 - pz;
		float s = MathHelper.sqrt(nx * nx + ny * ny + nz * nz);
		nx /= s;
		ny /= s;
		nz /= s;
		buffer.vertex(normal.getPositionMatrix(), px, py, pz).color(0, 0, 0, 255).normal(normal.getNormalMatrix(), nx, ny, nz).next();
	}


	@Override
	public Identifier getTexture(GrapplingHookEntity hook) {
		return TEXTURE;
	}
}
