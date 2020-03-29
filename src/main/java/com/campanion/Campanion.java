package com.campanion;

import com.campanion.block.CampanionBlocks;
import com.campanion.blockentity.CampanionBlockEntities;
import com.campanion.config.CampanionConfigManager;
import com.campanion.entity.CampanionEntities;
import com.campanion.item.BackpackItem;
import com.campanion.item.CampanionItems;
import com.campanion.network.C2SEmptyBackpack;
import com.campanion.network.S2CClearBackpackHeldItem;
import com.campanion.sound.CampanionSoundEvents;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.registry.Registry;

import java.util.Objects;

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

		registerServerboundPackets();
		registerBackpackHandler();
	}

	public static void registerServerboundPackets() {
		ServerSidePacketRegistry.INSTANCE.register(C2SEmptyBackpack.ID, C2SEmptyBackpack::onPacket);
	}

	//Maybe move to a mixin (PlayerInventory#setCursorStack)
	public static void registerBackpackHandler() {
		ServerTickCallback.EVENT.register(e -> {
			for (ServerWorld world : e.getWorlds()) {
				for (ServerPlayerEntity player : world.getPlayers()) {
					ItemStack cursorItem = player.inventory.getCursorStack();
					if(cursorItem.getItem() instanceof BackpackItem && cursorItem.hasTag() && Objects.requireNonNull(cursorItem.getTag()).contains("Inventory", 10)) {
						ItemScatterer.spawn(player.world, player.getBlockPos().add(0, player.getEyeHeight(player.getPose()), 0), BackpackItem.getItems(cursorItem));
						cursorItem.getTag().remove("Inventory");
						player.networkHandler.sendPacket(S2CClearBackpackHeldItem.createPacket());
					}
				}
			}
		});
	}

}
