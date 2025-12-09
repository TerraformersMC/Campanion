package com.terraformersmc.campanion;

import com.terraformersmc.campanion.advancement.criterion.CampanionCriteria;
import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.blockentity.CampanionBlockEntities;
import com.terraformersmc.campanion.client.BridgePlanksUnbakedGeometry;
import com.terraformersmc.campanion.data.ForgeDataGenerators;
import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.item.CampanionCreativeModeTabs;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.recipe.CampanionRecipeSerializers;
import com.terraformersmc.campanion.sound.CampanionSoundEvents;
import com.terraformersmc.campanion.stat.CampanionStats;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stats;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.Map;
import java.util.function.Consumer;

@Mod(Campanion.MOD_ID)
public class CampanionForge {
	private static IEventBus modEventBus;
    
    public CampanionForge(IEventBus modEventBus, ModContainer container) {
		CampanionForge.modEventBus = modEventBus;
        Campanion.init();

		modEventBus.addListener(ForgeDataGenerators::gatherDataGens);

		modEventBus.addListener((RegisterEvent event) -> {
			event.register(Registries.SOUND_EVENT, createHelperConsumer(CampanionSoundEvents.getSounds()));
			event.register(Registries.ITEM, createHelperConsumer(CampanionItems.getItems()));
			event.register(Registries.BLOCK, createHelperConsumer(CampanionBlocks.getBlocks()));
			event.register(Registries.ITEM, createHelperConsumer(CampanionBlocks.getItemBlocks()));
			event.register(Registries.BLOCK_ENTITY_TYPE, createHelperConsumer(CampanionBlockEntities.getBlockEntityTypes()));
			event.register(Registries.ENTITY_TYPE, createHelperConsumer(CampanionEntities.getEntityTypes()));
			event.register(Registries.RECIPE_SERIALIZER, createHelperConsumer(CampanionRecipeSerializers.getRecipeSerializers()));
			event.register(Registries.CREATIVE_MODE_TAB, createHelperConsumer(CampanionCreativeModeTabs.getTabs()));
			event.register(Registries.CUSTOM_STAT, createHelperConsumer(CampanionStats.getStats()));
			event.register(Registries.TRIGGER_TYPE, createHelperConsumer(CampanionCriteria.getTriggers()));
		});

		modEventBus.addListener((FMLCommonSetupEvent event) -> {
			CampanionBlocks.registerItemBlocks();
			Campanion.registerDispenserBehavior();
			//CampanionStats.loadClass();
			CampanionStats.getFORMATTERS().forEach(Stats.CUSTOM::get);
		});

		CampanionForgeClient.registerEvents(modEventBus);
		modEventBus.addListener((FMLClientSetupEvent event) -> CampanionForgeClient.init());

		modEventBus.addListener((ModelEvent.RegisterGeometryLoaders event) ->
			event.register(ResourceLocation.fromNamespaceAndPath(Campanion.MOD_ID, "bridge_planks"), (IGeometryLoader<BridgePlanksUnbakedGeometry>) (jsonObject, deserializationContext) -> {
				return new BridgePlanksUnbakedGeometry();
			})
		);
		modEventBus.addListener((BuildCreativeModeTabContentsEvent event) -> {
			CampanionItems.getTabsToItems().get(event.getTabKey()).forEach(event::accept);
			CampanionBlocks.getTabsToBlocks().get(event.getTabKey()).forEach(event::accept);
		});
	}

	public static IEventBus getModEventBus() {
		return modEventBus;
	}

	private <T> Consumer<RegisterEvent.RegisterHelper<T>> createHelperConsumer(Map<ResourceLocation, T> map) {
		return helper -> map.forEach(helper::register);
	}
}
