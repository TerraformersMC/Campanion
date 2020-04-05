package com.terraformersmc.campanion.client.renderer.entity;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;

public class EmptyRenderer<T extends Entity> extends EntityRenderer<T> {
    public EmptyRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public boolean shouldRender(T entity, Frustum visibleRegion, double cameraX, double cameraY, double cameraZ) {
        return false;
    }

    @Override
    public Identifier getTexture(T entity) {
        return null;
    }
}
