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
//public class HikersPack extends ModelBase {
//    public ModelRenderer field_78115_e;
//    public ModelRenderer base;
//    public ModelRenderer thing_bottom;
//    public ModelRenderer thing_left;
//    public ModelRenderer thing_right;
//
//    public HikersPack() {
//        this.textureWidth = 32;
//        this.textureHeight = 48;
//        this.thing_left = new ModelRenderer(this, 14, 26);
//        this.thing_left.setRotationPoint(4.0F, 1.6F, 0.0F);
//        this.thing_left.addBox(0.0F, 0.0F, 0.0F, 3, 8, 3, -0.2F);
//        this.thing_bottom = new ModelRenderer(this, 0, 17);
//        this.thing_bottom.setRotationPoint(0.0F, 10.0F, -0.4F);
//        this.thing_bottom.addBox(-4.5F, 0.0F, 0.0F, 9, 4, 4, -0.4F);
//        this.base = new ModelRenderer(this, 0, 0);
//        this.base.setRotationPoint(0.0F, 0.0F, 3.0F);
//        this.base.addBox(-4.5F, 0.1F, -1.0F, 9, 11, 5, 0.0F);
//        this.field_78115_e = new ModelRenderer(this, 38, 16);
//        this.field_78115_e.setRotationPoint(0.0F, 0.0F, 0.0F);
//        this.field_78115_e.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
//        this.thing_right = new ModelRenderer(this, 0, 26);
//        this.thing_right.setRotationPoint(-4.0F, 1.6F, 0.0F);
//        this.thing_right.addBox(-3.0F, 0.0F, 0.0F, 3, 8, 3, -0.2F);
//        this.base.addChild(this.thing_left);
//        this.base.addChild(this.thing_bottom);
//        this.field_78115_e.addChild(this.base);
//        this.base.addChild(this.thing_right);
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
