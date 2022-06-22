package com.terraformersmc.campanion.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class GrapplingHookEntityModel extends Model {

    private final ModelPart base;

    public GrapplingHookEntityModel(ModelPart root) {
        super(RenderType::entitySolid);

        this.base = root.getChild("base");
    }

	public GrapplingHookEntityModel() {
		this(getTexturedModelData().bakeRoot());
	}

	public static LayerDefinition getTexturedModelData() {
		MeshDefinition modelData = HumanoidModel.createMesh(new CubeDeformation(1/16F), 0);
		PartDefinition root = modelData.getRoot();

		CubeListBuilder base = CubeListBuilder.create().texOffs(12, 0).addBox(-2.5F, -5.0F, -2.5F, 5, 5, 5, new CubeDeformation(0.0F));
		CubeListBuilder main = CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -8.0F, -1.5F, 3, 16, 3, new CubeDeformation(0.0F));
		CubeListBuilder hookBase1 = CubeListBuilder.create().texOffs(12, 10).addBox(-1.5F, 0.0F, 0.0F, 3, 6, 3, new CubeDeformation(-0.3F));
		CubeListBuilder hookBase2 = CubeListBuilder.create().texOffs(12, 10).addBox(-1.5F, 0.0F, 0.0F, 3, 6, 3, new CubeDeformation(-0.3F));
		CubeListBuilder hookBase3 = CubeListBuilder.create().texOffs(12, 10).addBox(-1.5F, 0.0F, 0.0F, 3, 6, 3, new CubeDeformation(-0.3F));
		CubeListBuilder hookBase4 = CubeListBuilder.create().texOffs(12, 10).addBox(-1.5F, 0.0F, 0.0F, 3, 6, 3, new CubeDeformation(-0.3F));
		CubeListBuilder tip1 = CubeListBuilder.create().texOffs(12, 19).addBox(0.0F, 0.0F, 0.0F, 3, 3, 3, new CubeDeformation(-0.3F));
		CubeListBuilder tip2 = CubeListBuilder.create().texOffs(12, 19).addBox(0.0F, 0.0F, 0.0F, 3, 3, 3, new CubeDeformation(-0.3F));
		CubeListBuilder tip3 = CubeListBuilder.create().texOffs(12, 19).addBox(0.0F, 0.0F, 0.0F, 3, 3, 3, new CubeDeformation(-0.3F));
		CubeListBuilder tip4 = CubeListBuilder.create().texOffs(12, 19).addBox(0.0F, 0.0F, 0.0F, 3, 3, 3, new CubeDeformation(-0.3F));

		PartDefinition baseData = root.addOrReplaceChild("base", base, PartPose.ZERO);
		PartDefinition mainData = baseData.addOrReplaceChild("main", main, PartPose.offset(0.0F, -5.0F, 0.0F));
		PartDefinition hookBaseData1 =  mainData.addOrReplaceChild("hook_base1", hookBase1, PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 1.9198621771937625F, 0.0F, -0.0F));
		PartDefinition hookBaseData2 = mainData.addOrReplaceChild("hook_base2", hookBase2, PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, -1.2217304763960306F, 1.5707963267948966F, -3.141592653589793F));
		PartDefinition hookBaseData3 = mainData.addOrReplaceChild("hook_base3", hookBase3, PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, -1.2217304763960306F, 0.0F, -3.141592653589793F));
		PartDefinition hookBaseData4 = mainData.addOrReplaceChild("hook_base4", hookBase4, PartPose.offsetAndRotation(0.0F, -5.0F, 0.0F, 1.9198621771937625F, -1.5707963267948966F, 0.0F));
		hookBaseData1.addOrReplaceChild("tip1", tip1, PartPose.offsetAndRotation(-1.5F, 6.0F, 0.6F, -3.141592653589793F, -0.0F, -0.0F));
		hookBaseData2.addOrReplaceChild("tip2", tip2, PartPose.offsetAndRotation(-1.5F, 3.0F, 0.6F, -3.141592653589793F, 0.0F, 1.5707963267948966F));
		hookBaseData3.addOrReplaceChild("tip3", tip3, PartPose.offsetAndRotation(1.5F, 3.0F, 0.6F, -3.141592653589793F, 0.0F, -3.141592653589793F));
		hookBaseData4.addOrReplaceChild("tip4", tip4, PartPose.offsetAndRotation(-1.5F, 6.0F, 0.6F, -1.5707963267948966F, 0.0F, -1.5707963267948966F));

		return LayerDefinition.create(modelData, 32, 32);
	}

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.base.render(matrices, vertexConsumer, light, overlay);
    }
}
