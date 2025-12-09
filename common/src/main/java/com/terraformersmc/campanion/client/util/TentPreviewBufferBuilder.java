package com.terraformersmc.campanion.client.util;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.util.FastColor;

public class TentPreviewBufferBuilder extends BufferBuilder {
	private final TentPreviewByteBufferBuilder builder;

	public TentPreviewBufferBuilder(TentPreviewByteBufferBuilder builder, VertexFormat.Mode mode, VertexFormat format) {
        super(builder, mode, format);
		this.builder = builder;
    }

    @Override
    public VertexConsumer setColor(int red, int green, int blue, int alpha) {
        return super.setColor((int)(red*this.builder.r()), (int)(green*this.builder.g()), (int)(blue*this.builder.b()), (int)(alpha*this.builder.a()));
    }

	@Override
	public void addVertex(float pX, float pY, float pZ, int pColor, float pU, float pV, int pPackedOverlay, int pPackedLight, float pNormalX, float pNormalY, float pNormalZ) {
		super.addVertex(pX, pY, pZ, FastColor.ARGB32.multiply(pColor, FastColor.ARGB32.colorFromFloat(this.builder.a(), this.builder.r(), this.builder.g(), this.builder.b())), pU, pV, pPackedOverlay, pPackedLight, pNormalX, pNormalY, pNormalZ);
	}
}
