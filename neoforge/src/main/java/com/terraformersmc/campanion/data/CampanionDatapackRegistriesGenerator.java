package com.terraformersmc.campanion.data;

import com.terraformersmc.campanion.Campanion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CampanionDatapackRegistriesGenerator extends DatapackBuiltinEntriesProvider {

	public CampanionDatapackRegistriesGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, RegistrySetBuilder datapackEntriesBuilder) {
		super(output, registries, datapackEntriesBuilder, Set.of(Campanion.MOD_ID));
	}
}
