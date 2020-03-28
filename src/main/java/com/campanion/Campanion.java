package com.campanion;

import com.campanion.block.CampanionBlocks;
import com.campanion.blockentity.CampanionBlockEntities;
import com.campanion.config.CampanionConfigManager;
import com.campanion.entity.CampanionEntities;
import com.campanion.item.CampanionItems;
import com.campanion.sound.CampanionSoundEvents;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Campanion implements ModInitializer {

	public static final String MOD_ID = "campanion";
	public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

	@Override
	public void onInitialize() {
		register();
	}

	public static void register() {
		CampanionConfigManager.initializeConfig();

		CampanionSoundEvents.register();
		CampanionItems.register();
		CampanionBlocks.register();
		CampanionBlockEntities.register();
		CampanionEntities.register();

		FabricItemGroupBuilder.create(new Identifier(MOD_ID, "items")).icon(() -> Items.CAMPFIRE.asItem().getStackForRender()).appendItems(stacks -> Registry.ITEM.forEach(item -> {
			if (Registry.ITEM.getId(item).getNamespace().equals(MOD_ID)) {
				item.appendStacks(item.getGroup(), (DefaultedList<ItemStack>) stacks);
			}
		})).build();
	}

}
