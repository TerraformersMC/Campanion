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
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

public class C2SOpenBackpack {
	public static final ResourceLocation ID = new ResourceLocation(Campanion.MOD_ID, "open_backpack");

	public static Packet<?> createPacket() {
		return ClientPlayNetworking.createC2SPacket(ID, new FriendlyByteBuf(Unpooled.buffer()));
	}

	public static void onPacket(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl networkHandler, FriendlyByteBuf buffer, PacketSender sender) {
		server.execute(() -> {
			ItemStack stack = player.getItemBySlot(EquipmentSlot.CHEST);

			if (CampanionConfigManager.getConfig().isTrinketsBackpacksEnabled() &&
					FabricLoader.getInstance().isModLoaded("trinkets")) {
				TrinketComponent component = TrinketsApi.getTrinketComponent(player).orElse(null);

				if (component != null && component.isEquipped(itemStack -> itemStack.getItem() instanceof BackpackItem)) {
					stack = component.getEquipped(itemStack -> itemStack.getItem() instanceof BackpackItem).get(0).getB();
				}
			}

			if (stack.getItem() instanceof BackpackItem) {
				BackpackItem.Type type = ((BackpackItem) stack.getItem()).type;
				player.openMenu(new BackpackContainerFactory(type));
			}
		});
	}
}
