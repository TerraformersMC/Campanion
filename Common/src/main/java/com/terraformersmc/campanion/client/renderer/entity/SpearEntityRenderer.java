package com.terraformersmc.campanion.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.client.model.entity.SpearEntityModel;
import com.terraformersmc.campanion.entity.SpearEntity;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;

public class SpearEntityRenderer extends EntityRenderer<SpearEntity> {
	private static final Map<EntityType<?>, ResourceLocation> TEXTURES = new HashMap<>();
	private final SpearEntityModel model = new SpearEntityModel();

	public SpearEntityRenderer(EntityRendererProvider.Context factoryCtx) {
		super(factoryCtx);
	}

	@Override
	public void render(SpearEntity spear, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
		matrixStack.pushPose();
		matrixStack.mulPose(Vector3f.YP.rotationDegrees(Mth.lerp(g, spear.yRotO, spear.getYRot()) - 90.0F));
		matrixStack.mulPose(Vector3f.ZP.rotationDegrees(Mth.lerp(g, spear.xRotO, spear.getXRot()) + 90.0F));
		VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(vertexConsumerProvider, model.renderType(this.getTextureLocation(spear)), false, spear.isEnchanted());
		model.renderToBuffer(matrixStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.scale(2.0F, -2.0F, -2.0F);
		matrixStack.popPose();
		super.render(spear, f, g, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	public ResourceLocation getTextureLocation(SpearEntity spear) {
		return getTexture(spear.getType());
	}

	public static ResourceLocation getTexture(EntityType<?> type) {
		if (!TEXTURES.containsKey(type)) {
			TEXTURES.put(type, new ResourceLocation(Campanion.MOD_ID, "textures/entity/spear/" + Registry.ENTITY_TYPE.getKey(type).getPath() + ".png"));
		}
		return TEXTURES.get(type);
	}
}
