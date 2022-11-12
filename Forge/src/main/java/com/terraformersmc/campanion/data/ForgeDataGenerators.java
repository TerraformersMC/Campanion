package com.terraformersmc.campanion.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;

public class ForgeDataGenerators {
	public static void gatherDataGens(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();

		CampanionBlockTagsGenerator blockTagsGenerator = new CampanionBlockTagsGenerator(generator);
		generator.addProvider(event.includeServer(), blockTagsGenerator);
		generator.addProvider(event.includeServer(), new CampanionItemTagsGenerator(generator, blockTagsGenerator));
		generator.addProvider(event.includeServer(), new CampanionLootTablesGenerator(generator));
		generator.addProvider(event.includeServer(), new CampanionRecipesGenerator(generator));
	}

}
