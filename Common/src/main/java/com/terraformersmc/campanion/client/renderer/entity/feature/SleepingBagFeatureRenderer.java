package com.terraformersmc.campanion.client.renderer.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.terraformersmc.campanion.client.model.entity.SleepingBagModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.render.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public enum SleepingBagFeatureRenderer {
	INSTANCE;

	private static final ResourceLocation TEXTURE = new ResourceLocation(Campanion.MOD_ID, "textures/entity/sleeping_bag.png");

	private final SleepingBagModel<Player> model = new SleepingBagModel<>();
	private final HumanoidModel<Player> bipedModel = new HumanoidModel<>(LayerDefinition.create(HumanoidModel.createMesh(new CubeDeformation(1/16F), 0), 64, 64).bakeRoot(), RenderType::entityCutoutNoCull);

	SleepingBagFeatureRenderer() {
		this.bipedModel.setAllVisible(false);
		this.bipedModel.head.visible = true;
	}

	public void render(AbstractClientPlayer entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int light, int color) {
		Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();

		this.bipedModel.young = entity.isBaby();
		this.model.young = entity.isBaby();

		float size = entity.getEyeHeight(Pose.STANDING) - 0.1F;
		double theta = Math.toRadians(entity.getViewYRot(tickDelta));
		matrices.translate(size*-Math.sin(theta), entity instanceof RemotePlayer ? -0.55D : 0.15D, size*Math.cos(theta));

		matrices.mulPose(Vector3f.YP.rotationDegrees(270 - entity.getViewYRot(tickDelta)));
		matrices.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
		matrices.mulPose(Vector3f.YP.rotationDegrees(270.0F));

		matrices.scale(-1.0F, -1.0F, 1.0F);
		matrices.scale(0.9375F, 0.9375F, 0.9375F);
		matrices.translate(0.0D, -1.5010000467300415D, 0.0D);

		if(entity != camera.getEntity() || camera.isDetached()) {
			this.bipedModel.prepareMobModel(entity, 0, 0, tickDelta);
			this.bipedModel.setupAnim(entity, 0, 0, entity.tickCount + tickDelta, 0, 0);
			boolean isInvisible = !entity.isInvisible();
			boolean isInvisibleToPlayer = !isInvisible && !entity.isInvisibleTo(Minecraft.getInstance().player);
			RenderType renderLayer = this.getRenderLayer(entity, isInvisible, isInvisibleToPlayer);
			if (renderLayer != null) {
				VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
				this.bipedModel.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, isInvisibleToPlayer ? 0.15F : 1.0F);
			}
		}

		this.model.prepareMobModel(entity, 0, 0, tickDelta);
		this.model.setupAnim(entity, 0, 0, entity.tickCount + tickDelta, 0, 0);
		VertexConsumer buffer = vertexConsumerProvider.getBuffer(this.model.renderType(TEXTURE));
		this.model.renderToBuffer(matrices, buffer, light, OverlayTexture.NO_OVERLAY, ((color >> 16) & 255) / 255F, ((color >> 8) & 255) / 255F, (color & 255) / 255F, 1.0F);
	}

	protected RenderType getRenderLayer(AbstractClientPlayer entity, boolean showBody, boolean translucent) {
		ResourceLocation identifier = entity.getSkinTextureLocation();
		if (translucent) {
			return RenderType.entityTranslucent(identifier);
		} else if (showBody) {
			return this.model.renderType(identifier);
		} else {
			return entity.isCurrentlyGlowing() ? RenderType.outline(identifier) : null;
		}
	}

}
