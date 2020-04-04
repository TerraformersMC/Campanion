package com.campanion.client.renderer.entity;

import com.campanion.Campanion;
import com.campanion.client.model.entity.GrapplingHookEntityModel;
import com.campanion.entity.GrapplingHookEntity;
import com.campanion.item.CampanionItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
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

	private static final Identifier TEXTURE = new Identifier(Campanion.MOD_ID, "textures/entity/grapple_hook.png");

	private final GrapplingHookEntityModel model = new GrapplingHookEntityModel();

	public GrapplingHookEntityRenderer(EntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	public void render(GrapplingHookEntity entity, float yaw, float tickDelta, MatrixStack stack, VertexConsumerProvider vertexConsumers, int light) {
		PlayerEntity player = entity.getPlayer();
		if (player != null) {
			stack.push();

			stack.push();
			stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(tickDelta, entity.prevYaw, entity.yaw) - 90.0F));
			stack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(tickDelta, entity.prevPitch, entity.pitch) + 90.0F));
			VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexConsumers, model.getLayer(this.getTexture(entity)), false, false);
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
			double playerX;
			double playerY;
			double playerZ;
			float playerEye;
			if (this.renderManager.gameOptions != null && this.renderManager.gameOptions.perspective <= 0 && player == MinecraftClient.getInstance().player) {
				double x = this.renderManager.gameOptions.fov / 100.0D;
				Vec3d vec3d = new Vec3d((double) armOffset * -0.36D * x, -0.045D * x, 0.4D);
				vec3d = vec3d.rotateX(-MathHelper.lerp(tickDelta, player.prevPitch, player.pitch) * 0.017453292F);
				vec3d = vec3d.rotateY(-MathHelper.lerp(tickDelta, player.prevYaw, player.yaw) * 0.017453292F);
				vec3d = vec3d.rotateY(k * 0.5F);
				vec3d = vec3d.rotateX(-k * 0.7F);
				playerX = MathHelper.lerp(tickDelta, player.prevX, player.getX()) + vec3d.x;
				playerY = MathHelper.lerp(tickDelta, player.prevY, player.getY()) + vec3d.y;
				playerZ = MathHelper.lerp(tickDelta, player.prevZ, player.getZ()) + vec3d.z;
				playerEye = player.getStandingEyeHeight();
			} else {
				playerX = MathHelper.lerp(tickDelta, player.prevX, player.getX()) - cosYaw * armOffset * 0.35D - sinYaw * 0.8D;
				playerY = player.prevY + (double) player.getStandingEyeHeight() + (player.getY() - player.prevY) * (double) tickDelta - 0.45D;
				playerZ = MathHelper.lerp(tickDelta, player.prevZ, player.getZ()) - sinYaw * armOffset * 0.35D + cosYaw * 0.8D;
				playerEye = player.isInSneakingPose() ? -0.1875F : 0.0F;
			}

			double x = MathHelper.lerp(tickDelta, entity.prevX, entity.getX());
			double y = MathHelper.lerp(tickDelta, entity.prevY, entity.getY()) + 0.25D;
			double z = MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ());
			float changeX = (float) (playerX - x);
			float changeY = (float) (playerY + playerEye - y);
			float changeZ = (float) (playerZ - z);
			VertexConsumer vertexConsumer2 = vertexConsumers.getBuffer(RenderLayer.getLines());
			Matrix4f modelMatrix = stack.peek().getModel();

			float amplitude = MathHelper.clamp((15F - entity.age - tickDelta) / 15F, 0F, 1F);

			for (int p = 0; p < 128; ++p) {
				drawLineVertex(changeX, changeY, changeZ, vertexConsumer2, modelMatrix, p / 128F, amplitude);
				drawLineVertex(changeX, changeY, changeZ, vertexConsumer2, modelMatrix, (p + 1) / 128F, amplitude);
			}

			stack.pop();
			super.render(entity, yaw, tickDelta, stack, vertexConsumers, light);
		}
	}
	private static void drawLineVertex(float x, float y, float z, VertexConsumer buffer, Matrix4f mat, float t, float a) {
		buffer.vertex(mat, (float) (x * t + a*Math.sin(t * 2F*Math.PI)), y * t + 0.25F, z * t).color(0, 0, 0, 255).next();
	}

	public Identifier getTexture(GrapplingHookEntity hook) {
		return TEXTURE;
	}
}