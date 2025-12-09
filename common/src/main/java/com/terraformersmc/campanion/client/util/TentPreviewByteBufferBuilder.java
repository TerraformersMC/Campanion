package com.terraformersmc.campanion.client.util;

import com.mojang.blaze3d.vertex.ByteBufferBuilder;

public class TentPreviewByteBufferBuilder extends ByteBufferBuilder {
    public TentPreviewByteBufferBuilder(int initialCapacity) {
        super(initialCapacity);
    }

    private float r;
	private float g;
	private float b;
	private float a;

    public void setApplyModifier(boolean applyModifier) {
        this.r = applyModifier ? 1F : 1F;
        this.g = applyModifier ? 0.1F : 1F;
        this.b = applyModifier ? 0.1F : 1F;
        this.a = 0.5F;
    }

	public float r(){
		return this.r;
	}

	public float g(){
		return this.g;
	}

	public float b(){
		return this.b;
	}

	public float a(){
		return this.a;
	}
}
