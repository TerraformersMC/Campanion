package com.terraformersmc.campanion.client.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TentPreviewImmediate extends MultiBufferSource.BufferSource {

	public static final TentPreviewImmediate STORAGE = new TentPreviewImmediate(new TentPreviewBufferBuilder(256), ImmutableMap.of());
	public static final Logger LOGGER = LogManager.getLogger();

	private final TentPreviewBufferBuilder builder;

	private TentPreviewImmediate(TentPreviewBufferBuilder fallbackBuffer, Map<RenderType, BufferBuilder> layerBuffers) {
		super(fallbackBuffer, layerBuffers);
		this.builder = fallbackBuffer;
	}

	public void setApplyModifiers(boolean applyModifiers) {
		this.builder.setApplyModifier(applyModifiers);
	}

	@Override
	public @NotNull VertexConsumer getBuffer(@NotNull RenderType type) {
		//We always render as translucent, so might as well enforce it.
		return super.getBuffer(RenderType.translucent());
	}
}
