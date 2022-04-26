package com.terraformersmc.campanion.client.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class TentPreviewImmediate extends VertexConsumerProvider.Immediate {

	public static final TentPreviewImmediate STORAGE = new TentPreviewImmediate(new TentPreviewBufferBuilder(256), ImmutableMap.of());
	public static final Logger LOGGER = LogManager.getLogger();

	private final TentPreviewBufferBuilder builder;

	private TentPreviewImmediate(TentPreviewBufferBuilder fallbackBuffer, Map<RenderLayer, BufferBuilder> layerBuffers) {
		super(fallbackBuffer, layerBuffers);
		this.builder = fallbackBuffer;
	}

	public void setApplyModifiers(boolean applyModifiers) {
		this.builder.setApplyModifier(applyModifiers);
	}

	@Override
	public VertexConsumer getBuffer(RenderLayer renderLayer) {
		return super.getBuffer(RenderLayer.getTranslucent());
	}
}
