package com.campanion.client.model.entity.backpack;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

public class BaseBackpackModel<T extends LivingEntity> extends BipedEntityModel<T> {
    protected BaseBackpackModel(int textureWidth, int textureHeight) {
        super(1/16F, 0, textureWidth, textureHeight);
        this.torso = new ModelPart(this, 16, 16);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.torso);
    }
}
