package com.terraformersmc.campanion.client.util;

import com.google.common.collect.ImmutableSortedMap;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.terraformersmc.campanion.mixin.client.AccessorMultiBufferSourceBufferSource;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.SequencedMap;

public class TentPreviewImmediate extends MultiBufferSource.BufferSource {

	public static final TentPreviewImmediate STORAGE = new TentPreviewImmediate(new TentPreviewByteBufferBuilder(256), ImmutableSortedMap.of());
	public static final Logger LOGGER = LogManager.getLogger();

	private final TentPreviewByteBufferBuilder builder;

	private TentPreviewImmediate(TentPreviewByteBufferBuilder fallbackBuffer, SequencedMap<RenderType, ByteBufferBuilder> layerBuffers) {
		super(fallbackBuffer, layerBuffers);
		this.builder = fallbackBuffer;
	}

	public void setApplyModifiers(boolean applyModifiers) {
		this.builder.setApplyModifier(applyModifiers);
	}

	@Override
	public VertexConsumer getBuffer(RenderType type) {
		RenderType pRenderType = RenderType.translucent();
		TentPreviewBufferBuilder bufferbuilder = (TentPreviewBufferBuilder)this.startedBuilders.get(pRenderType);
		if (bufferbuilder != null && !pRenderType.canConsolidateConsecutiveGeometry()) {
			((AccessorMultiBufferSourceBufferSource)this).callEndBatch(pRenderType, bufferbuilder);
			bufferbuilder = null;
		}

		if (bufferbuilder != null) {
			return bufferbuilder;
		} else {
			TentPreviewByteBufferBuilder bytebufferbuilder = (TentPreviewByteBufferBuilder)this.fixedBuffers.get(pRenderType);
			if (bytebufferbuilder != null) {
				bufferbuilder = new TentPreviewBufferBuilder(bytebufferbuilder, pRenderType.mode(), pRenderType.format());
			} else {
				if (this.lastSharedType != null) {
					this.endBatch(this.lastSharedType);
				}

				bufferbuilder = new TentPreviewBufferBuilder((TentPreviewByteBufferBuilder)this.sharedBuffer, pRenderType.mode(), pRenderType.format());
				this.lastSharedType = pRenderType;
			}

			this.startedBuilders.put(pRenderType, bufferbuilder);
			return bufferbuilder;
		}
	}
}
