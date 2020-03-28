//package com.campanion.client.model;
//
//import net.minecraft.client.render.VertexConsumerProvider;
//import net.minecraft.client.render.entity.feature.FeatureRenderer;
//import net.minecraft.client.render.entity.feature.FeatureRendererContext;
//import net.minecraft.client.render.entity.model.BipedEntityModel;
//import net.minecraft.client.util.math.MatrixStack;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.PlayerEntity;
//
//public class BackpackFeatureRenderer extends FeatureRenderer {
//
//	public BackpackFeatureRenderer(FeatureRendererContext context) {
//		super(context);
//	}
//
//	@Override
//	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Entity entity, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
//		((BipedEntityModel)this.getContextModel()).setAttributes(bipedEntityModel);
//		bipedEntityModel.animateModel(livingEntity, f, g, h);
//		this.setVisible(bipedEntityModel, equipmentSlot);
//		bipedEntityModel.setAngles(livingEntity, f, g, i, j, k);
//	}
//}
