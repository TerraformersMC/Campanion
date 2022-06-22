package com.terraformersmc.campanion.client.renderer.entity.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.terraformersmc.campanion.client.model.entity.backpack.CampingPackEntityModel;
import com.terraformersmc.campanion.client.model.entity.backpack.DayPackEntityModel;
import com.terraformersmc.campanion.client.model.entity.backpack.HikingPackEntityModel;
import com.terraformersmc.campanion.item.BackpackItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import java.util.Objects;
import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public class BackpackFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

	public BackpackFeatureRenderer(RenderLayerParent<T, M> context) {
		super(context);
	}

	@Override
	public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
		ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
		if (stack.getItem() instanceof BackpackItem) {
			Type type = Type.values()[((BackpackItem) stack.getItem()).type.ordinal()];
			AgeableListModel model = Objects.requireNonNull(type.createModel());

			//Should always be true
			if (model instanceof HumanoidModel && this.getParentModel() instanceof HumanoidModel) {
				((HumanoidModel) this.getParentModel()).copyPropertiesTo((HumanoidModel) model);
			} else {
				this.getParentModel().copyPropertiesTo(model);
			}

			model.prepareMobModel(entity, limbAngle, limbDistance, tickDelta);
			model.setupAnim(entity, limbAngle, limbDistance, customAngle, headYaw, headPitch);
			VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(vertexConsumers, model.renderType(type.getTexture()), false, stack.hasFoil());
			model.renderToBuffer(matrices, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}

	public enum Type {
		DAY_PACK("day_pack", DayPackEntityModel::new),
		CAMPING_PACK("camping_pack", CampingPackEntityModel::new),
		HIKING_PACK("hiking_pack", HikingPackEntityModel::new);

		private ResourceLocation texture;
		private Supplier<AgeableListModel> modelSupplier;

		Type(String name, Supplier<AgeableListModel> modelSupplier) {
			this.texture = new ResourceLocation(Campanion.MOD_ID, "textures/entity/backpack/" + name + ".png");
			this.modelSupplier = modelSupplier;
		}

		public ResourceLocation getTexture() {
			return texture;
		}

		public AgeableListModel createModel() {
			return modelSupplier.get();
		}
	}
}
