package com.terraformersmc.campanion.client.renderer.entity.feature;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.client.model.entity.SleepingBagModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public enum SleepingBagFeatureRenderer {
	INSTANCE;

	private static final Identifier TEXTURE = new Identifier(Campanion.MOD_ID, "textures/entity/sleeping_bag.png");

	private final SleepingBagModel<PlayerEntity> model = new SleepingBagModel<>();
	private final BipedEntityModel<PlayerEntity> bipedModel = new BipedEntityModel<>(TexturedModelData.of(BipedEntityModel.getModelData(new Dilation(1/16F), 0), 64, 64).createModel(), RenderLayer::getEntityCutoutNoCull);

	SleepingBagFeatureRenderer() {
		this.bipedModel.setVisible(false);
		this.bipedModel.head.visible = true;
	}

	public void render(AbstractClientPlayerEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, int color) {
		Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();

		this.bipedModel.child = entity.isBaby();
		this.model.child = entity.isBaby();

		float size = entity.getEyeHeight(EntityPose.STANDING) - 0.1F;
		double theta = Math.toRadians(entity.getYaw(tickDelta));
		matrices.translate(size*-Math.sin(theta), entity instanceof OtherClientPlayerEntity ? -0.55D : 0.15D, size*Math.cos(theta));

		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(270 - entity.getYaw(tickDelta)));
		matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(90.0F));
		matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(270.0F));

		matrices.scale(-1.0F, -1.0F, 1.0F);
		matrices.scale(0.9375F, 0.9375F, 0.9375F);
		matrices.translate(0.0D, -1.5010000467300415D, 0.0D);

		if(entity != camera.getFocusedEntity() || camera.isThirdPerson()) {
			this.bipedModel.animateModel(entity, 0, 0, tickDelta);
			this.bipedModel.setAngles(entity, 0, 0, entity.age + tickDelta, 0, 0);
			boolean isInvisible = !entity.isInvisible();
			boolean isInvisibleToPlayer = !isInvisible && !entity.isInvisibleTo(MinecraftClient.getInstance().player);
			RenderLayer renderLayer = this.getRenderLayer(entity, isInvisible, isInvisibleToPlayer);
			if (renderLayer != null) {
				VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
				this.bipedModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, isInvisibleToPlayer ? 0.15F : 1.0F);
			}
		}

		this.model.animateModel(entity, 0, 0, tickDelta);
		this.model.setAngles(entity, 0, 0, entity.age + tickDelta, 0, 0);
		VertexConsumer buffer = vertexConsumerProvider.getBuffer(this.model.getLayer(TEXTURE));
		this.model.render(matrices, buffer, light, OverlayTexture.DEFAULT_UV, ((color >> 16) & 255) / 255F, ((color >> 8) & 255) / 255F, (color & 255) / 255F, 1.0F);
	}

	protected RenderLayer getRenderLayer(AbstractClientPlayerEntity entity, boolean showBody, boolean translucent) {
		Identifier identifier = entity.getSkinTexture();
		if (translucent) {
			return RenderLayer.getEntityTranslucent(identifier);
		} else if (showBody) {
			return this.model.getLayer(identifier);
		} else {
			return entity.isGlowing() ? RenderLayer.getOutline(identifier) : null;
		}
	}

}
