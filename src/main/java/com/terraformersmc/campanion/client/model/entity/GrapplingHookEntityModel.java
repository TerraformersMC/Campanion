package com.terraformersmc.campanion.client.model.entity;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

public class GrapplingHookEntityModel extends Model {

    private final ModelPart base;

    public GrapplingHookEntityModel() {
        super(RenderLayer::getEntitySolid);
        this.textureWidth = 32;
        this.textureHeight = 32;

        this.base = new ModelPart(this, 12, 0);
        base.addCuboid(-2.5F, -5.0F, -2.5F, 5, 5, 5, 0.0F);
        
        ModelPart main = new ModelPart(this, 0, 0);
        main.setPivot(0.0F, -5.0F, 0.0F);
        main.addCuboid(-1.5F, -8.0F, -1.5F, 3, 16, 3, 0.0F);


        ModelPart hookBase1 = new ModelPart(this, 12, 10);
        hookBase1.setPivot(0.0F, -5.0F, 0.0F);
        hookBase1.addCuboid(-1.5F, 0.0F, 0.0F, 3, 6, 3, -0.3F);
        setRotateAngle(hookBase1, 1.9198621771937625F, 0.0F, -0.0F);

        ModelPart hookBase2 = new ModelPart(this, 12, 10);
        hookBase2.setPivot(0.0F, -5.0F, 0.0F);
        hookBase2.addCuboid(-1.5F, 0.0F, 0.0F, 3, 6, 3, -0.3F);
        setRotateAngle(hookBase2, -1.2217304763960306F, 1.5707963267948966F, -3.141592653589793F);

        ModelPart hookBase3 = new ModelPart(this, 12, 10);
        hookBase3.setPivot(0.0F, -5.0F, 0.0F);
        hookBase3.addCuboid(-1.5F, 0.0F, 0.0F, 3, 6, 3, -0.3F);
        setRotateAngle(hookBase3, -1.2217304763960306F, 0.0F, -3.141592653589793F);

        ModelPart hookBase4 = new ModelPart(this, 12, 10);
        hookBase4.setPivot(0.0F, -5.0F, 0.0F);
        hookBase4.addCuboid(-1.5F, 0.0F, 0.0F, 3, 6, 3, -0.3F);
        setRotateAngle(hookBase4, 1.9198621771937625F, -1.5707963267948966F, 0.0F);


        ModelPart tip1 = new ModelPart(this, 12, 19);
        tip1.setPivot(-1.5F, 6.0F, 0.6F);
        tip1.addCuboid(0.0F, 0.0F, 0.0F, 3, 3, 3, -0.3F);
        setRotateAngle(tip1, -3.141592653589793F, -0.0F, -0.0F);

        ModelPart tip2 = new ModelPart(this, 12, 19);
        tip2.setPivot(-1.5F, 3.0F, 0.6F);
        tip2.addCuboid(0.0F, 0.0F, 0.0F, 3, 3, 3, -0.3F);
        setRotateAngle(tip2, -3.141592653589793F, 0.0F, 1.5707963267948966F);

        ModelPart tip3 = new ModelPart(this, 12, 19);
        tip3.setPivot(1.5F, 3.0F, 0.6F);
        tip3.addCuboid(0.0F, 0.0F, 0.0F, 3, 3, 3, -0.3F);
        setRotateAngle(tip3, -3.141592653589793F, 0.0F, -3.141592653589793F);

        ModelPart tip4 = new ModelPart(this, 12, 19);
        tip4.setPivot(-1.5F, 6.0F, 0.6F);
        tip4.addCuboid(0.0F, 0.0F, 0.0F, 3, 3, 3, -0.3F);
        setRotateAngle(tip4, -1.5707963267948966F, 0.0F, -1.5707963267948966F);

        this.base.addChild(main);
        main.addChild(hookBase1);
        main.addChild(hookBase2);
        main.addChild(hookBase3);
        main.addChild(hookBase4);
        hookBase1.addChild(tip1);
        hookBase2.addChild(tip2);
        hookBase3.addChild(tip3);
        hookBase4.addChild(tip4);
    }

    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.pitch = x;
        modelRenderer.yaw = y;
        modelRenderer.roll = z;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.base.render(matrices, vertexConsumer, light, overlay);
    }
}
