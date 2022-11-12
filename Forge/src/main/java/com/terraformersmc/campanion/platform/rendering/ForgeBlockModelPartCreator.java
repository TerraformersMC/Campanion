package com.terraformersmc.campanion.platform.rendering;

import com.terraformersmc.campanion.platform.services.rendering.BlockModelCreatedData;
import com.terraformersmc.campanion.platform.services.rendering.BlockModelPartCreator;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ForgeBlockModelPartCreator implements BlockModelPartCreator {

	private QuadBakingVertexConsumer builder;

	private int currentVertex = 0;

	private final List<BakedQuad> output = new ArrayList<>();

	@Override
	public void beginQuad() {
		if(this.builder != null) {
			throw new RuntimeException("Tried to begin builder twice");
		}
		//Why are we creating this every time?
		this.currentVertex = 0;
		this.builder = new QuadBakingVertexConsumer(this.output::add);

	}

	@Override
	public BlockModelPartCreator position(float x, float y, float z) {
		this.builder.vertex(x, y, z);
		return this;
	}

	@Override
	public BlockModelPartCreator uv(float u, float v) {
		this.builder.uv(u, v);
		return this;
	}

	@Override
	public BlockModelPartCreator colour(int colour) {
		float a = (colour >> 24) / 255F;
		float r = (colour >> 16) / 255F;
		float g = (colour >> 8) / 255F;
		float b = (colour) / 255F;
		this.builder.color(a, r, g, b);
		return this;
	}

	@Override
	public BlockModelPartCreator normal(float x, float y, float z) {
		this.builder.normal(x, y, z);
		return this;
	}

	@Override
	public void finishVertex() {
		this.currentVertex++;
		if(this.currentVertex > 4) {
			throw new RuntimeException("Tried finishing more than 4 vertices");
		}
		this.builder.endVertex();
	}

	@Override
	public void finishQuad() {
		if(this.builder == null) {
			throw new RuntimeException("Tried to finish building without starting");
		}
		if(this.currentVertex != 4) {
			throw new RuntimeException("Tried to finish with " + this.currentVertex + " number of vertices");
		}
		this.builder = null;
	}

	@Override
	public BlockModelCreatedData created() {
		return new ForgeBlockModelCreatedData(Collections.unmodifiableList(this.output));
	}
}
