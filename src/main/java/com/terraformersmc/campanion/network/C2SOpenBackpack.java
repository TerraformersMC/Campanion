package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.backpack.BackpackContainerFactory;
import com.terraformersmc.campanion.config.CampanionConfigManager;
import com.terraformersmc.campanion.item.BackpackItem;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class C2SOpenBackpack {
	public static final Identifier ID = new Identifier(Campanion.MOD_ID, "open_backpack");

	public static Packet<?> createPacket() {
		return ClientPlayNetworking.createC2SPacket(ID, new PacketByteBuf(Unpooled.buffer()));
	}

	public static void onPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler networkHandler, PacketByteBuf buffer, PacketSender sender) {
		server.execute(() -> {
			ItemStack stack = player.getEquippedStack(EquipmentSlot.CHEST);

			if (CampanionConfigManager.getConfig().isTrinketsBackpacksEnabled() &&
					FabricLoader.getInstance().isModLoaded("trinkets")) {
				TrinketComponent component = TrinketsApi.getTrinketComponent(player).orElse(null);

				if (component != null && component.isEquipped(itemStack -> itemStack.getItem() instanceof BackpackItem)) {
					stack = component.getEquipped(itemStack -> itemStack.getItem() instanceof BackpackItem).get(0).getRight();
				}
			}

			if (stack.getItem() instanceof BackpackItem) {
				BackpackItem.Type type = ((BackpackItem) stack.getItem()).type;
				player.openHandledScreen(new BackpackContainerFactory(type));
			}
		});
	}
}
