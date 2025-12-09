package com.terraformersmc.campanion.platform.rendering;

import com.terraformersmc.campanion.platform.services.rendering.BlockModelCreatedData;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;

public record FabricBlockModelCreatedData(Mesh mesh) implements BlockModelCreatedData { }
