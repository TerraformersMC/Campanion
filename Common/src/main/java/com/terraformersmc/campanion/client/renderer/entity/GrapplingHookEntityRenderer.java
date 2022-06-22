package com.terraformersmc.campanion.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.terraformersmc.campanion.client.model.entity.GrapplingHookEntityModel;
import com.terraformersmc.campanion.entity.GrapplingHookEntity;
import com.terraformersmc.campanion.item.CampanionItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class GrapplingHookEntityRenderer extends EntityRenderer<GrapplingHookEntity> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Campanion.MOD_ID, "textures/entity/grapple_hook.png");

	private final GrapplingHookEntityModel model = new GrapplingHookEntityModel();

	public GrapplingHookEntityRenderer(EntityRendererProvider.Context factoryCtx) {
		super(factoryCtx);
	}

	@Override
	public void render(GrapplingHookEntity entity, float yaw, float tickDelta, PoseStack stack, MultiBufferSource vertexConsumers, int light) {
		Player player = entity.getPlayer();
		if (player != null) {
			stack.pushPose();

			stack.pushPose();
			stack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(tickDelta, entity.yRotO, entity.getYRot()) - 90.0F));
			stack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(tickDelta, entity.xRotO, entity.getXRot()) + 90.0F));
			VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(vertexConsumers, model.renderType(this.getTexture(entity)), false, false);
			this.model.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
			stack.popPose();

			int armOffset = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
			ItemStack itemStack = player.getMainHandItem();
			if (itemStack.getItem() != CampanionItems.GRAPPLING_HOOK) {
				armOffset = -armOffset;
			}

			float h = player.getAttackAnim(tickDelta);
			float k = Mth.sin(Mth.sqrt(h) * 3.1415927F);
			float l = Mth.lerp(tickDelta, player.yBodyRotO, player.yBodyRot) * 0.017453292F;
			double sinYaw = Mth.sin(l);
			double cosYaw = Mth.cos(l);
			double arm = (double)armOffset * 0.35D;
			double playerX;
			double playerY;
			double playerZ;
			float playerEye;
			if ((this.entityRenderDispatcher.options == null || this.entityRenderDispatcher.options.getCameraType().isFirstPerson()) && player == Minecraft.getInstance().player) {
				double x = 960.0D / this.entityRenderDispatcher.options.fov;
				Vec3 vec3d = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float)armOffset * 0.525F, -0.1F);
				vec3d = vec3d.scale(x);
				vec3d = vec3d.yRot(k * 0.5F);
				vec3d = vec3d.xRot(-k * 0.7F);
				playerX = Mth.lerp(tickDelta, player.xo, player.getX()) + vec3d.x;
				playerY = Mth.lerp(tickDelta, player.yo, player.getY()) + vec3d.y;
				playerZ = Mth.lerp(tickDelta, player.zo, player.getZ()) + vec3d.z;
				playerEye = player.getEyeHeight();
			} else {
				playerX = Mth.lerp(tickDelta, player.xo, player.getX()) - cosYaw * arm - sinYaw * 0.8D;
				playerY = player.yo + player.getEyeHeight() + (player.getY() - player.yo) * (double)tickDelta - 0.45D;
				playerZ = Mth.lerp(tickDelta, player.zo, player.getZ()) - sinYaw * arm + cosYaw * 0.8D;
				playerEye = player.isCrouching() ? -0.1875F : 0.0F;
			}

			double x = Mth.lerp(tickDelta, entity.xo, entity.getX());
			double y = Mth.lerp(tickDelta, entity.yo, entity.getY()) + 0.25D;
			double z = Mth.lerp(tickDelta, entity.zo, entity.getZ());
			float changeX = (float) (playerX - x);
			float changeY = (float) (playerY + playerEye - y);
			float changeZ = (float) (playerZ - z);
			VertexConsumer vertexConsumer2 = vertexConsumers.getBuffer(RenderType.lineStrip());
			PoseStack.Pose entry = stack.last();

			float amplitude = Mth.clamp((15F - entity.tickCount - tickDelta) / 15F, 0F, 1F);

			for (int p = 0; p < 256; ++p) {
				drawLineVertex(changeX, changeY, changeZ, vertexConsumer2, entry, p / 256F, (p + 1) / 256F, amplitude);
			}

			stack.popPose();
			super.render(entity, yaw, tickDelta, stack, vertexConsumers, light);
		}
	}

	private static void drawLineVertex(float x, float y, float z, VertexConsumer buffer, PoseStack.Pose normal, float t, float t1, float a) {
		float px = (float) (x * t + a * Math.sin(t * 2F * Math.PI));
		float py = y * t + 0.25F;
		float pz = z * t;
		float nx = (float) (x * t1 + a * Math.sin(t1 * 2F * Math.PI)) - px;
		float ny = y * t1 + 0.25F - py;
		float nz = z * t1 - pz;
		float s = Mth.sqrt(nx * nx + ny * ny + nz * nz);
		nx /= s;
		ny /= s;
		nz /= s;
		buffer.vertex(normal.pose(), px, py, pz).color(0, 0, 0, 255).normal(normal.normal(), nx, ny, nz).endVertex();
	}


	@Override
	public ResourceLocation getTexture(GrapplingHookEntity hook) {
		return TEXTURE;
	}
}
