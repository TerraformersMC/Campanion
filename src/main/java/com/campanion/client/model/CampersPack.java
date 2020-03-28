//package com.campanion.client.model;
//
//import net.minecraft.client.model.ModelBase;
//import net.minecraft.client.model.ModelRenderer;
//import net.minecraft.entity.Entity;
//
///**
// * ModelPlayer - Either Mojang or a mod author
// * Created using Tabula 7.0.100
// */
//public class CampersPack extends ModelBase {
//    public ModelRenderer field_78115_e;
//    public ModelRenderer base;
//    public ModelRenderer thing_bottom;
//
//    public CampersPack() {
//        this.textureWidth = 24;
//        this.textureHeight = 24;
//        this.thing_bottom = new ModelRenderer(this, 0, 16);
//        this.thing_bottom.setRotationPoint(0.0F, 9.9F, 0.8F);
//        this.thing_bottom.addBox(-3.0F, -0.3F, -1.5F, 6, 3, 3, 0.0F);
//        this.field_78115_e = new ModelRenderer(this, 28, 16);
//        this.field_78115_e.setRotationPoint(0.0F, 0.0F, 0.0F);
//        this.field_78115_e.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
//        this.base = new ModelRenderer(this, 0, 0);
//        this.base.setRotationPoint(0.0F, 0.0F, 3.0F);
//        this.base.addBox(-3.5F, 0.1F, -1.0F, 8, 10, 4, 0.0F);
//        this.base.addChild(this.thing_bottom);
//        this.field_78115_e.addChild(this.base);
//    }
//
//    @Override
//    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
//        this.field_78115_e.render(f5);
//    }
//
//    /**
//     * This is a helper function from Tabula to set the rotation of model parts
//     */
//    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
//        modelRenderer.rotateAngleX = x;
//        modelRenderer.rotateAngleY = y;
//        modelRenderer.rotateAngleZ = z;
//    }
//}
