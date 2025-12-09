package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.Campanion;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import java.util.LinkedHashMap;
import java.util.Map;

public class CampanionCreativeModeTabs {
	private static final Map<ResourceLocation, CreativeModeTab> TABS = new LinkedHashMap<>();
	public static ResourceKey<CreativeModeTab> TAB_KEY = ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(Campanion.MOD_ID, "items"));

	public static final CreativeModeTab TAB = registerTab(TAB_KEY);

	private static CreativeModeTab registerTab(ResourceKey<CreativeModeTab> tabKey) {
		CreativeModeTab tab = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).title(Component.translatable("itemGroup.campanion.items")).icon(() -> {
			return CampanionItems.SMORE.asItem().getDefaultInstance();
		}).build();
		TABS.put(tabKey.location(), tab);
		return tab;
	}

	public static Map<ResourceLocation, CreativeModeTab> getTabs() {
		return TABS;
	}
}
