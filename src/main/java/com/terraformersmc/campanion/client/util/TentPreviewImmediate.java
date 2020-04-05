package com.terraformersmc.campanion.client.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class TentPreviewImmediate extends VertexConsumerProvider.Immediate {

    public static final TentPreviewImmediate STORAGE = new TentPreviewImmediate(new TentPreviewBufferBuilder(256), ImmutableMap.of());

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
        BufferBuilder buffer = this.layerBuffers.getOrDefault(layer, this.fallbackBuffer);
        boolean bl = Objects.equals(this.currentLayer, layer.asOptional());
        if ((bl || buffer != this.fallbackBuffer) && this.activeConsumers.remove(buffer)) {
            if (buffer.isBuilding()) {
                buffer.sortQuads(0, 0, 0);

                buffer.end();
                layer.startDrawing();

                RenderSystem.enableBlend();
                RenderSystem.defaultBlendFunc();
                RenderSystem.defaultAlphaFunc();

                BufferRenderer.draw(buffer);

                RenderSystem.disableBlend();

                layer.endDrawing();
            }
            if (bl) {
                this.currentLayer = Optional.empty();
            }
        }
    }
}
