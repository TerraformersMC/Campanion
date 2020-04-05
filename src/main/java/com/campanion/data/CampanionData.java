package com.campanion.data;

import net.minecraft.data.DataGenerator;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

public class CampanionData {

	public static DataGenerator create(Path output) {
		DataGenerator generator = new DataGenerator(output, Collections.emptyList());

		generator.install(new CampanionBlockTagsProvider(generator));
		generator.install(new CampanionItemTagsProvider(generator));
		generator.install(new CampanionRecipesProvider(generator));
		generator.install(new CampanionLootTablesProvider(generator));

		return generator;
	}
}
