package com.terraformersmc.campanion.platform.rendering;

import com.terraformersmc.campanion.platform.services.rendering.BlockModelCreatedData;
import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.List;

public record ForgeBlockModelCreatedData(List<BakedQuad> quads) implements BlockModelCreatedData { }
