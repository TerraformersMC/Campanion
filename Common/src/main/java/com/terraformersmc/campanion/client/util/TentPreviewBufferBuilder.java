package com.terraformersmc.campanion.client.util;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.NotNull;

public class TentPreviewBufferBuilder extends BufferBuilder {
	public TentPreviewBufferBuilder(int initialCapacity) {
		super(initialCapacity);
	}

	private float r;
	private float g;
	private float b;
	private float a;

	public void setApplyModifier(boolean applyModifier) {
		this.r = 1F;
		this.g = applyModifier ? 0.1F : 1F;
		this.b = applyModifier ? 0.1F : 1F;
		this.a = 0.5F;
	}

	@Override
	public @NotNull VertexConsumer color(int red, int green, int blue, int alpha) {
		return super.color((int) (red * this.r), (int) (green * this.g), (int) (blue * this.b), (int) (alpha * this.a));
	}

	@Override
	public void vertex(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int overlay, int light, float normalX, float normalY, float normalZ) {
		super.vertex(x, y, z, red * this.r, green * this.g, blue * this.b, alpha * this.a, u, v, overlay, light, normalX, normalY, normalZ);
	}
}
