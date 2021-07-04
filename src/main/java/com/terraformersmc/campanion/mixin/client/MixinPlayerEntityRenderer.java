package com.terraformersmc.campanion.mixin.client;

import com.terraformersmc.campanion.client.renderer.entity.feature.BackpackFeatureRenderer;
import com.terraformersmc.campanion.client.renderer.entity.feature.SleepingBagFeatureRenderer;
import com.terraformersmc.campanion.item.SleepingBagItem;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.DyeableItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public MixinPlayerEntityRenderer(EntityRendererFactory.Context ctx, PlayerEntityModel<AbstractClientPlayerEntity> model, float shadowSize) {
		super(ctx, model, shadowSize);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	public void onConstructor(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo info) {
		addFeature(new BackpackFeatureRenderer<>(this));
	}

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void render(AbstractClientPlayerEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo info) {
		SleepingBagItem.getUsingStack(entity).ifPresent(stack -> {
			matrices.push();

			SleepingBagFeatureRenderer.INSTANCE.render(entity, tickDelta, matrices, vertexConsumers, light, ((DyeableItem)stack.getItem()).getColor(stack));

			matrices.pop();
			info.cancel();
		});
	}
}
