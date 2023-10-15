package com.terraformersmc.campanion.client.renderer.entity;

import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class EmptyRenderer<T extends Entity> extends EntityRenderer<T> {
	public EmptyRenderer(EntityRendererProvider.Context factoryCtx) {
		super(factoryCtx);
	}

	@Override
	public boolean shouldRender(@NotNull T entity, @NotNull Frustum visibleRegion, double cameraX, double cameraY, double cameraZ) {
		return false;
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull T entity) {
		return null;
	}
}
