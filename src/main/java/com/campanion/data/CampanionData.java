package com.campanion.data;

import net.minecraft.data.DataGenerator;

import java.nio.file.Path;
import java.util.Collection;

public class CampanionData {
	public static final boolean ENABLED = true;

	public static DataGenerator create(Path output, Collection<Path> inputs, boolean includeClient, boolean includeServer, boolean includeDev, boolean includeReports, boolean validate) {
		DataGenerator generator = new DataGenerator(output, inputs);
		if (includeServer) {
			generator.install(new CampanionBlockTagsProvider(generator));
			generator.install(new CampanionItemTagsProvider(generator));
			generator.install(new CampanionRecipesProvider(generator));
		}
		return generator;
	}
}
