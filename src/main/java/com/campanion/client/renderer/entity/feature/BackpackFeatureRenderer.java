package com.campanion.client.renderer.entity.feature;

import com.campanion.Campanion;
import com.campanion.client.model.entity.backpack.DayPackEntityModel;
import com.campanion.item.BackpackItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class BackpackFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

	public BackpackFeatureRenderer(FeatureRendererContext<T, M> context) {
		super(context);
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float limbAngle, float limbDistance, float h, float customAngle, float headYaw, float headPitch) {
		ItemStack stack = livingEntity.getEquippedStack(EquipmentSlot.CHEST);
		if (stack.getItem() instanceof BackpackItem) {
			Type type = Type.values()[((BackpackItem) stack.getItem()).type.ordinal()];
			AnimalModel model = type.createModel();
			matrixStack.push();
			matrixStack.translate(0.0D, 0.0D, 0.125D);
			this.getContextModel().copyStateTo(model);
			model.setAngles(livingEntity, limbAngle, limbDistance, customAngle, headYaw, headPitch);
			VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexConsumerProvider, model.getLayer(type.getTexture()), false, stack.hasEnchantmentGlint());
			model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
			matrixStack.pop();
		}
	}

	public enum Type {
		DAY_PACK("day_pack", () -> new DayPackEntityModel()),
		CAMPING_PACK("camping_pack", () -> new DayPackEntityModel()),
		HIKING_PACK("hiking_pack", () -> new DayPackEntityModel());

		private Identifier texture;
		private Supplier<AnimalModel> modelSupplier;

		Type(String name, Supplier<AnimalModel> modelSupplier) {
			this.texture = new Identifier(Campanion.MOD_ID, "textures/entity/backpack/" + name + ".png");
			this.modelSupplier = modelSupplier;
		}

		public Identifier getTexture() {
			return texture;
		}

		public AnimalModel createModel() {
			return modelSupplier.get();
		}
	}
}
