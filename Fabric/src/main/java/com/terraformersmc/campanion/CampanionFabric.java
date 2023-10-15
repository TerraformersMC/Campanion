package com.terraformersmc.campanion;

import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.blockentity.CampanionBlockEntities;
import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.recipe.CampanionRecipeSerializers;
import com.terraformersmc.campanion.sound.CampanionSoundEvents;
import com.terraformersmc.campanion.stat.CampanionStats;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class CampanionFabric implements ModInitializer {

	@Override
	public void onInitialize() {
		Campanion.init();
		Campanion.registerDispenserBehavior();
		CampanionStats.loadClass();

		register(BuiltInRegistries.SOUND_EVENT, CampanionSoundEvents.getSounds());
		register(BuiltInRegistries.ITEM, CampanionItems.getItems());
		register(BuiltInRegistries.BLOCK, CampanionBlocks.getBlocks());
		register(BuiltInRegistries.ITEM, CampanionBlocks.getItemBlocks());
		CampanionBlocks.registerItemBlocks();
		register(BuiltInRegistries.BLOCK_ENTITY_TYPE, CampanionBlockEntities.getBlockEntityTypes());
		register(BuiltInRegistries.ENTITY_TYPE, CampanionEntities.getEntityTypes());
		register(BuiltInRegistries.RECIPE_SERIALIZER, CampanionRecipeSerializers.getRecipeSerializers());

	}

	private <T> void register(Registry<T> registry, Map<ResourceLocation, T> registered) {
		registered.forEach((location, object) -> Registry.register(registry, location, object));
	}
}
