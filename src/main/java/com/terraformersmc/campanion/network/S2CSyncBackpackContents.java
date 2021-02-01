package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;

public class S2CSyncBackpackContents {
	public static final Identifier ID = new Identifier(Campanion.MOD_ID, "clear_held_item");

	public static Packet<?> createPacket(DefaultedList<ItemStack> stacks) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeShort(stacks.size());
		for (ItemStack stack : stacks) {
			buf.writeItemStack(stack);
		}
		return ServerPlayNetworking.createS2CPacket(ID, buf);
	}

	@Environment(EnvType.CLIENT)
	public static void onPacket(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf buffer, PacketSender sender) {
		int size = buffer.readShort();
		List<ItemStack> stacks = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			stacks.add(buffer.readItemStack());
		}
		client.execute(() -> {
			if (client.player != null) {
				DefaultedList<ItemStack> list = ((BackpackStorePlayer) client.player).getBackpackStacks();
				list.clear();
				list.addAll(stacks);
			}
		});
	}
}
