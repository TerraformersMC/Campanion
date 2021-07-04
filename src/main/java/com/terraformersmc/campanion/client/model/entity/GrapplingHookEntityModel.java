package com.terraformersmc.campanion.client.model.entity;

import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class GrapplingHookEntityModel extends Model {

    private final ModelPart base;

    public GrapplingHookEntityModel(ModelPart root) {
        super(RenderLayer::getEntitySolid);

        this.base = root.getChild("base");
    }

	public GrapplingHookEntityModel() {
		this(getTexturedModelData().createModel());
	}

	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = BipedEntityModel.getModelData(new Dilation(1/16F), 0);
		ModelPartData root = modelData.getRoot();

		ModelPartBuilder base = ModelPartBuilder.create().uv(12, 0).cuboid(-2.5F, -5.0F, -2.5F, 5, 5, 5, new Dilation(0.0F));
		ModelPartBuilder main = ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, -8.0F, -1.5F, 3, 16, 3, new Dilation(0.0F));
		ModelPartBuilder hookBase1 = ModelPartBuilder.create().uv(12, 10).cuboid(-1.5F, 0.0F, 0.0F, 3, 6, 3, new Dilation(-0.3F));
		ModelPartBuilder hookBase2 = ModelPartBuilder.create().uv(12, 10).cuboid(-1.5F, 0.0F, 0.0F, 3, 6, 3, new Dilation(-0.3F));
		ModelPartBuilder hookBase3 = ModelPartBuilder.create().uv(12, 10).cuboid(-1.5F, 0.0F, 0.0F, 3, 6, 3, new Dilation(-0.3F));
		ModelPartBuilder hookBase4 = ModelPartBuilder.create().uv(12, 10).cuboid(-1.5F, 0.0F, 0.0F, 3, 6, 3, new Dilation(-0.3F));
		ModelPartBuilder tip1 = ModelPartBuilder.create().uv(12, 19).cuboid(0.0F, 0.0F, 0.0F, 3, 3, 3, new Dilation(-0.3F));
		ModelPartBuilder tip2 = ModelPartBuilder.create().uv(12, 19).cuboid(0.0F, 0.0F, 0.0F, 3, 3, 3, new Dilation(-0.3F));
		ModelPartBuilder tip3 = ModelPartBuilder.create().uv(12, 19).cuboid(0.0F, 0.0F, 0.0F, 3, 3, 3, new Dilation(-0.3F));
		ModelPartBuilder tip4 = ModelPartBuilder.create().uv(12, 19).cuboid(0.0F, 0.0F, 0.0F, 3, 3, 3, new Dilation(-0.3F));

		ModelPartData baseData = root.addChild("base", base, ModelTransform.NONE);
		ModelPartData mainData = baseData.addChild("main", main, ModelTransform.pivot(0.0F, -5.0F, 0.0F));
		ModelPartData hookBaseData1 =  mainData.addChild("hook_base1", hookBase1, ModelTransform.of(0.0F, -5.0F, 0.0F, 1.9198621771937625F, 0.0F, -0.0F));
		ModelPartData hookBaseData2 = mainData.addChild("hook_base2", hookBase2, ModelTransform.of(0.0F, -5.0F, 0.0F, -1.2217304763960306F, 1.5707963267948966F, -3.141592653589793F));
		ModelPartData hookBaseData3 = mainData.addChild("hook_base3", hookBase3, ModelTransform.of(0.0F, -5.0F, 0.0F, -1.2217304763960306F, 0.0F, -3.141592653589793F));
		ModelPartData hookBaseData4 = mainData.addChild("hook_base4", hookBase4, ModelTransform.of(0.0F, -5.0F, 0.0F, 1.9198621771937625F, -1.5707963267948966F, 0.0F));
		hookBaseData1.addChild("tip1", tip1, ModelTransform.of(-1.5F, 6.0F, 0.6F, -3.141592653589793F, -0.0F, -0.0F));
		hookBaseData2.addChild("tip2", tip2, ModelTransform.of(-1.5F, 3.0F, 0.6F, -3.141592653589793F, 0.0F, 1.5707963267948966F));
		hookBaseData3.addChild("tip3", tip3, ModelTransform.of(1.5F, 3.0F, 0.6F, -3.141592653589793F, 0.0F, -3.141592653589793F));
		hookBaseData4.addChild("tip4", tip4, ModelTransform.of(-1.5F, 6.0F, 0.6F, -1.5707963267948966F, 0.0F, -1.5707963267948966F));

		return TexturedModelData.of(modelData, 32, 32);
	}

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.base.render(matrices, vertexConsumer, light, overlay);
    }
}
