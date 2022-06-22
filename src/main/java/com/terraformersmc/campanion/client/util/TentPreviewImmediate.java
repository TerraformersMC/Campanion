package com.terraformersmc.campanion.client.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
	public void endBatch(RenderType layer) {
		try {
			BufferBuilder buffer = this.fixedBuffers.getOrDefault(layer, this.builder);
			boolean bl = Objects.equals(this.lastState, layer.asOptional());
			if ((bl || buffer != this.builder) && this.startedBuffers.remove(buffer)) {
				if (buffer.building()) {
					buffer.setQuadSortOrigin(0, 0, 0);

					buffer.end();
					layer.setupRenderState();

					RenderSystem.enableBlend();
					RenderSystem.defaultBlendFunc();
//					RenderSystem.defaultAlphaFunc(); may require shader change

					BufferUploader.end(buffer);

					RenderSystem.disableBlend();

					layer.clearRenderState();
				}
				if (bl) {
					this.lastState = Optional.empty();
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
