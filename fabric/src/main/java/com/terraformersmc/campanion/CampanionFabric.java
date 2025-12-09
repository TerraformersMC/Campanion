package com.terraformersmc.campanion;

import com.terraformersmc.campanion.advancement.criterion.CampanionCriteria;
import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.blockentity.CampanionBlockEntities;
import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.item.CampanionCreativeModeTabs;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.recipe.CampanionRecipeSerializers;
import com.terraformersmc.campanion.sound.CampanionSoundEvents;
import com.terraformersmc.campanion.stat.CampanionStats;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Collection;
import java.util.Map;

public class CampanionFabric implements ModInitializer {
    
    @Override
    public void onInitialize() {
        Campanion.init();
		Campanion.registerDispenserBehavior();
		//CampanionStats.loadClass();

		register(BuiltInRegistries.SOUND_EVENT, CampanionSoundEvents.getSounds());
		register(BuiltInRegistries.ITEM, CampanionItems.getItems());
		register(BuiltInRegistries.BLOCK, CampanionBlocks.getBlocks());
		register(BuiltInRegistries.ITEM, CampanionBlocks.getItemBlocks());
		CampanionBlocks.registerItemBlocks();
		register(BuiltInRegistries.BLOCK_ENTITY_TYPE, CampanionBlockEntities.getBlockEntityTypes());
		register(BuiltInRegistries.ENTITY_TYPE, CampanionEntities.getEntityTypes());
		register(BuiltInRegistries.RECIPE_SERIALIZER, CampanionRecipeSerializers.getRecipeSerializers());
		register(BuiltInRegistries.CREATIVE_MODE_TAB, CampanionCreativeModeTabs.getTabs());
		register(BuiltInRegistries.CUSTOM_STAT, CampanionStats.getStats());
		CampanionStats.getFORMATTERS().forEach(Stats.CUSTOM::get);
		register(BuiltInRegistries.TRIGGER_TYPES, CampanionCriteria.getTriggers());

		for(ResourceKey<CreativeModeTab> key : CampanionItems.getTabsToItems().keySet()){
			ItemGroupEvents.modifyEntriesEvent(key).register(entries -> {
				Collection<Item> items = CampanionItems.getTabsToItems().get(key);
				for(Item item : items){
					entries.accept(item);
				}
			});
		}
		for(ResourceKey<CreativeModeTab> key : CampanionBlocks.getTabsToBlocks().keySet()){
			ItemGroupEvents.modifyEntriesEvent(key).register(entries -> {
				Collection<Block> blocks = CampanionBlocks.getTabsToBlocks().get(key);
				for(Block block : blocks){
					entries.accept(block);
				}
			});
		}


    }

	private <T> void register(Registry<T> registry, Map<ResourceLocation, T> registered) {
		registered.forEach((location, object) -> Registry.register(registry, location, object));
	}
}
