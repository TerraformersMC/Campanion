package com.terraformersmc.campanion.client.model.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;

import java.util.Collections;

/**
 * SleepingBagModel - Either Mojang or a mod author
 * Created using Tabula 7.0.100
 */
public class SleepingBagModel<T extends LivingEntity> extends BipedEntityModel<T> {

    public SleepingBagModel() {
        super(1/16F, 0F, 64, 64);
        this.head = new ModelPart(this, 16, 16);

        ModelPart base = new ModelPart(this, 0, 0);
        base.addCuboid(-7.0F, 0, -2.8F, 14, 25, 6, 0.0F);

        ModelPart headPiece = new ModelPart(this, 37, 28);
        headPiece.addCuboid(-5.0F, -9.5F, 0.2F, 10, 10, 3, 0.0F);

        ModelPart bump = new ModelPart(this, 0, 31);
        bump.addCuboid(-5.0F, 0.0F, -4.3F, 10, 23, 2, 0.0F);

        this.head.addChild(base);
        this.head.addChild(headPiece);
        this.head.addChild(bump);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return Collections.emptyList();
    }
}

