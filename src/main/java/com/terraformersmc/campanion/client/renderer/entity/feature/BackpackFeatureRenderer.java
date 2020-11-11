package com.terraformersmc.campanion.client.renderer.entity.feature;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.client.model.entity.backpack.CampingPackEntityModel;
import com.terraformersmc.campanion.client.model.entity.backpack.DayPackEntityModel;
import com.terraformersmc.campanion.client.model.entity.backpack.HikingPackEntityModel;
import com.terraformersmc.campanion.item.BackpackItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.Objects;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class BackpackFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

	public BackpackFeatureRenderer(FeatureRendererContext<T, M> context) {
		super(context);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
		ItemStack stack = entity.getEquippedStack(EquipmentSlot.CHEST);
		if (stack.getItem() instanceof BackpackItem) {
			Type type = Type.values()[((BackpackItem) stack.getItem()).type.ordinal()];
			AnimalModel model = Objects.requireNonNull(type.createModel());

			//Should always be true
			if (model instanceof BipedEntityModel && this.getContextModel() instanceof BipedEntityModel) {
				((BipedEntityModel) this.getContextModel()).setAttributes((BipedEntityModel) model);
			} else {
				this.getContextModel().copyStateTo(model);
			}

			model.animateModel(entity, limbAngle, limbDistance, tickDelta);
			model.setAngles(entity, limbAngle, limbDistance, customAngle, headYaw, headPitch);
			VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, model.getLayer(type.getTexture()), false, stack.hasGlint());
			model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public enum Type {
		DAY_PACK("day_pack", DayPackEntityModel::new),
		CAMPING_PACK("camping_pack", CampingPackEntityModel::new),
		HIKING_PACK("hiking_pack", HikingPackEntityModel::new);

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
