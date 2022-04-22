package com.terraformersmc.campanion.client.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
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
	public void draw(RenderLayer layer) {
		try {
			BufferBuilder buffer = this.layerBuffers.getOrDefault(layer, this.fallbackBuffer);
			boolean bl = Objects.equals(this.currentLayer, layer.asOptional());
			if ((bl || buffer != this.fallbackBuffer) && this.activeConsumers.remove(buffer)) {
				if (buffer.isBuilding()) {
					buffer.sortFrom(0, 0, 0);

					buffer.end();
					layer.startDrawing();

					RenderSystem.enableBlend();
					RenderSystem.defaultBlendFunc();
//					RenderSystem.defaultAlphaFunc(); may require shader change

					BufferRenderer.draw(buffer);

					RenderSystem.disableBlend();

					layer.endDrawing();
				}
				if (bl) {
					this.currentLayer = Optional.empty();
				}
			}
		} catch (NoSuchFieldError e) {
			if (FabricLoader.getInstance().isModLoaded("optifabric")) {
				LOGGER.error("ERROR likely due to OptiFine - Could not find required field:", e);
			} else {
				throw e;
			}
		}
	}
}
