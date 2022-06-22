package com.terraformersmc.campanion;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.blockentity.CampanionBlockEntities;
import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.recipe.CampanionRecipeSerializers;
import com.terraformersmc.campanion.sound.CampanionSoundEvents;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod(Campanion.MOD_ID)
public class CampanionForge {
    
    public CampanionForge() {
        Campanion.init();

		FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
		IEventBus modEventBus = context.getModEventBus();

		modEventBus.addListener((RegisterEvent event) -> {
			event.register(ForgeRegistries.Keys.SOUND_EVENTS, createHelperConsumer(CampanionSoundEvents.getSounds()));
			event.register(ForgeRegistries.Keys.ITEMS, createHelperConsumer(CampanionItems.getItems()));
			event.register(ForgeRegistries.Keys.BLOCKS, createHelperConsumer(CampanionBlocks.getBlocks()));
			event.register(ForgeRegistries.Keys.ITEMS, createHelperConsumer(CampanionBlocks.getItemBlocks()));
			event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, createHelperConsumer(CampanionBlockEntities.getBlockEntityTypes()));
			event.register(ForgeRegistries.Keys.ENTITY_TYPES, createHelperConsumer(CampanionEntities.getEntityTypes()));
			event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, createHelperConsumer(CampanionRecipeSerializers.getRecipeSerializers()));
		});
	}

	private <T> Consumer<RegisterEvent.RegisterHelper<T>> createHelperConsumer(Map<ResourceLocation, T> map) {
		return helper -> map.forEach(helper::register);
	}
}
