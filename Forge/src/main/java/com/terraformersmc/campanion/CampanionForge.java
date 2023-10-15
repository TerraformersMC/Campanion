package com.terraformersmc.campanion;

import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.blockentity.CampanionBlockEntities;
import com.terraformersmc.campanion.client.BridgePlanksUnbakedGeometry;
import com.terraformersmc.campanion.data.ForgeDataGenerators;
import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.recipe.CampanionRecipeSerializers;
import com.terraformersmc.campanion.sound.CampanionSoundEvents;
import com.terraformersmc.campanion.stat.CampanionStats;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Map;
import java.util.function.Consumer;

@Mod(Campanion.MOD_ID)
public class CampanionForge {
    
    public CampanionForge() {
        Campanion.init();

		FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
		IEventBus modEventBus = context.getModEventBus();

		modEventBus.addListener(ForgeDataGenerators::gatherDataGens);

		modEventBus.addListener((RegisterEvent event) -> {
			event.register(ForgeRegistries.Keys.SOUND_EVENTS, createHelperConsumer(CampanionSoundEvents.getSounds()));
			event.register(ForgeRegistries.Keys.ITEMS, createHelperConsumer(CampanionItems.getItems()));
			event.register(ForgeRegistries.Keys.BLOCKS, createHelperConsumer(CampanionBlocks.getBlocks()));
			event.register(ForgeRegistries.Keys.ITEMS, createHelperConsumer(CampanionBlocks.getItemBlocks()));
			event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, createHelperConsumer(CampanionBlockEntities.getBlockEntityTypes()));
			event.register(ForgeRegistries.Keys.ENTITY_TYPES, createHelperConsumer(CampanionEntities.getEntityTypes()));
			event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, createHelperConsumer(CampanionRecipeSerializers.getRecipeSerializers()));
		});

		modEventBus.addListener((FMLCommonSetupEvent event) -> {
			CampanionBlocks.registerItemBlocks();
			Campanion.registerDispenserBehavior();
			CampanionStats.loadClass();
		});

		CampanionForgeClient.registerEvents(modEventBus);
		modEventBus.addListener((FMLClientSetupEvent event) -> CampanionForgeClient.init());

		modEventBus.addListener((ModelEvent.RegisterGeometryLoaders event) ->
			event.register("bridge_planks", (IGeometryLoader<BridgePlanksUnbakedGeometry>) (jsonObject, deserializationContext) -> new BridgePlanksUnbakedGeometry())
		);
	}

	private <T> Consumer<RegisterEvent.RegisterHelper<T>> createHelperConsumer(Map<ResourceLocation, T> map) {
		return helper -> map.forEach(helper::register);
	}
}
