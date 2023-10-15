package com.terraformersmc.campanion.platform.rendering;

import com.terraformersmc.campanion.platform.services.rendering.BlockModelCreatedData;
import com.terraformersmc.campanion.platform.services.rendering.BlockModelPartCreator;
import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.BlendMode;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;

public class FabricBlockModelPartCreator implements BlockModelPartCreator {
	private final RenderMaterial material;
	private final MeshBuilder meshBuilder;
	private final QuadEmitter emitter;

	private boolean buildingQuad;
	private int currentVertex = 0;

	public FabricBlockModelPartCreator() {
		Renderer renderer = RendererAccess.INSTANCE.getRenderer();

		this.meshBuilder = renderer.meshBuilder();
		this.emitter = this.meshBuilder.getEmitter();
		this.material = renderer.materialFinder().blendMode(BlendMode.CUTOUT).find();
		//this.material = renderer.materialFinder().blendMode(0, BlendMode.CUTOUT).find();
	}


	@Override
	public void beginQuad() {
		if (this.buildingQuad) {
			throw new RuntimeException("Tried to begin builder twice");
		}
		this.currentVertex = 0;
		this.buildingQuad = true;
		this.emitter.material(this.material);
	}

	@Override
	public BlockModelPartCreator position(float x, float y, float z) {
		this.emitter.pos(this.currentVertex, x, y, z);
		return this;
	}

	@Override
	public BlockModelPartCreator uv(float u, float v) {
		this.emitter.sprite(this.currentVertex, 0, u, v);
		return this;
	}

	@Override
	public BlockModelPartCreator colour(int colour) {
		this.emitter.spriteColor(this.currentVertex, 0, colour);
		return this;
	}

	@Override
	public BlockModelPartCreator normal(float x, float y, float z) {
		this.emitter.normal(this.currentVertex, x, y, z);
		return this;
	}

	@Override
	public void finishVertex() {
		this.currentVertex++;
		if (this.currentVertex > 4) {
			throw new RuntimeException("Tried finishing more than 4 vertices");
		}
	}

	@Override
	public void finishQuad() {
		if (!this.buildingQuad) {
			throw new RuntimeException("Tried to finish building without starting");
		}
		if (this.currentVertex != 4) {
			throw new RuntimeException("Tried to finish with " + this.currentVertex + " number of vertices");
		}
		this.buildingQuad = false;
		this.emitter.emit();
	}

	@Override
	public BlockModelCreatedData created() {
		return new FabricBlockModelCreatedData(this.meshBuilder.build());
	}
}
