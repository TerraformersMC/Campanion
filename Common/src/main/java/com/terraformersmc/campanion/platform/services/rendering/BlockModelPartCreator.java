package com.terraformersmc.campanion.platform.services.rendering;

import java.util.List;

public interface BlockModelPartCreator {
	void beginQuad();

	BlockModelPartCreator position(float x, float y, float z);
	BlockModelPartCreator uv(float u, float v);
	BlockModelPartCreator colour(int colour);
	BlockModelPartCreator normal(float x, float y, float z);
	void finishVertex();

	void finishQuad();

	BlockModelCreatedData created();
}
