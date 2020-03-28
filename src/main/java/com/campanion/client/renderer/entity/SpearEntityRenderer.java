package com.campanion.client.renderer.entity;

import com.campanion.entity.SpearEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SpearEntityRenderer extends EntityRenderer<SpearEntity> {
	public static final Identifier SKIN = new Identifier("textures/entity/trident.png");
	private final SpearEntityModel model = new SpearEntityModel();

	public SpearEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
		super(entityRenderDispatcher);
	}

	public void render(SpearEntity spear, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
		SpearEntityModel model = new SpearEntityModel();
		matrixStack.push();
		matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.lerp(g, spear.prevYaw, spear.yaw) - 90.0F));
		matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.lerp(g, spear.prevPitch, spear.pitch) + 90.0F));
		VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexConsumerProvider, model.getLayer(this.getTexture(spear)), false, spear.method_23751());
		model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStack.pop();
		super.render(spear, f, g, matrixStack, vertexConsumerProvider, i);
	}

	public Identifier getTexture(SpearEntity spear) {
		return SKIN;
	}
}
