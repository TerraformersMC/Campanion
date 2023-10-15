package com.terraformersmc.campanion.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.terraformersmc.campanion.client.renderer.entity.feature.BackpackFeatureRenderer;
import com.terraformersmc.campanion.client.renderer.entity.feature.SleepingBagFeatureRenderer;
import com.terraformersmc.campanion.item.SleepingBagItem;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.item.DyeableLeatherItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerEntityRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
	public MixinPlayerEntityRenderer(EntityRendererProvider.Context ctx, PlayerModel<AbstractClientPlayer> model, float shadowSize) {
		super(ctx, model, shadowSize);
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	public void onConstructor(EntityRendererProvider.Context ctx, boolean slim, CallbackInfo info) {
		addLayer(new BackpackFeatureRenderer<>(this));
	}

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void render(AbstractClientPlayer entity, float yaw, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo info) {
		SleepingBagItem.getUsingStack(entity).ifPresent(stack -> {
			matrices.pushPose();

			SleepingBagFeatureRenderer.INSTANCE.render(entity, tickDelta, matrices, vertexConsumers, light, ((DyeableLeatherItem) stack.getItem()).getColor(stack));

			matrices.popPose();
			info.cancel();
		});
	}
}
