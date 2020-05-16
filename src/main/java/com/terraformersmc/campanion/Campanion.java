package com.terraformersmc.campanion;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.terraformersmc.campanion.advancement.criterion.CampanionCriteria;
import com.terraformersmc.campanion.block.CampanionBlocks;
import com.terraformersmc.campanion.blockentity.CampanionBlockEntities;
import com.terraformersmc.campanion.config.CampanionConfigManager;
import com.terraformersmc.campanion.data.CampanionData;
import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.item.BackpackItem;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.mixin.InvokerEntity;
import com.terraformersmc.campanion.network.C2SEmptyBackpack;
import com.terraformersmc.campanion.network.C2SRotateHeldItem;
import com.terraformersmc.campanion.network.S2CClearBackpackHeldItem;
import com.terraformersmc.campanion.recipe.CampanionRecipeSerializers;
import com.terraformersmc.campanion.sound.CampanionSoundEvents;
import com.terraformersmc.campanion.stat.CampanionStats;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
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
		CampanionRecipeSerializers.register();

		CampanionCriteria.loadClass();
		CampanionStats.loadClass();

		FabricItemGroupBuilder.create(new Identifier(MOD_ID, "items")).icon(() -> CampanionItems.SMORE.asItem().getStackForRender()).appendItems(stacks -> Registry.ITEM.forEach(item -> {
			if (Registry.ITEM.getId(item).getNamespace().equals(MOD_ID)) {
				item.appendStacks(item.getGroup(), (DefaultedList<ItemStack>) stacks);
			}
		})).build();

		registerServerboundPackets();

		CampanionData.generate();
	}

	public static void registerServerboundPackets() {
		ServerSidePacketRegistry.INSTANCE.register(C2SEmptyBackpack.ID, C2SEmptyBackpack::onPacket);
		ServerSidePacketRegistry.INSTANCE.register(C2SRotateHeldItem.ID, C2SRotateHeldItem::onPacket);
	}
}
