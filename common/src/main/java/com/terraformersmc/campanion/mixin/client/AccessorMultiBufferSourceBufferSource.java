package com.terraformersmc.campanion.mixin.client;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MultiBufferSource.BufferSource.class)
public interface AccessorMultiBufferSourceBufferSource {

	@Invoker("endBatch")
	void callEndBatch(RenderType pRenderType, BufferBuilder pBuilder);
}
