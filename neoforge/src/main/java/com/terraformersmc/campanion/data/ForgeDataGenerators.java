package com.terraformersmc.campanion.data;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.damagesource.CampanionDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class ForgeDataGenerators {
	public static void gatherDataGens(GatherDataEvent event) {
		Campanion.LOG.info("Running data generation for Campanion (NeoForge)");
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		RegistrySetBuilder registrySetBuilder = new RegistrySetBuilder()
			.add(Registries.DAMAGE_TYPE, CampanionDamageTypes::bootstrap);
		CampanionDatapackRegistriesGenerator datapackRegistriesGenerator = generator.addProvider(event.includeServer(), new CampanionDatapackRegistriesGenerator(packOutput, lookupProvider, registrySetBuilder));
		lookupProvider = datapackRegistriesGenerator.getRegistryProvider();

		CampanionBlockTagsGenerator blockTagsGenerator = new CampanionBlockTagsGenerator(packOutput, lookupProvider, existingFileHelper);
		generator.addProvider(event.includeServer(), blockTagsGenerator);
		generator.addProvider(event.includeServer(), new CampanionItemTagsGenerator(packOutput, lookupProvider, blockTagsGenerator.contentsGetter(), existingFileHelper));
		generator.addProvider(event.includeServer(), new CampanionLootTablesGenerator(packOutput, lookupProvider));
		generator.addProvider(event.includeServer(), new CampanionRecipesGenerator(packOutput, lookupProvider));
		generator.addProvider(event.includeServer(), new CampanionDamageTypeTagsGenerator(packOutput, lookupProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), new CampanionAdvancementsGenerator(packOutput, lookupProvider, existingFileHelper));
	}

}
