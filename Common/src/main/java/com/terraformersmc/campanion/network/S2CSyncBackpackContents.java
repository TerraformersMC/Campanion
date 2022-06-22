package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;
import java.util.List;

public class S2CSyncBackpackContents {
	public static final ResourceLocation ID = new ResourceLocation(Campanion.MOD_ID, "clear_held_item");

	public static Packet<?> createPacket(NonNullList<ItemStack> stacks) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeShort(stacks.size());
		for (ItemStack stack : stacks) {
			buf.writeItem(stack);
		}
		return ServerPlayNetworking.createS2CPacket(ID, buf);
	}

	@Environment(EnvType.CLIENT)
	public static void onPacket(Minecraft client, ClientPacketListener networkHandler, FriendlyByteBuf buffer, PacketSender sender) {
		int size = buffer.readShort();
		List<ItemStack> stacks = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			stacks.add(buffer.readItem());
		}
		client.execute(() -> {
			if (client.player != null) {
				NonNullList<ItemStack> list = ((BackpackStorePlayer) client.player).getBackpackStacks();
				list.clear();
				list.addAll(stacks);
			}
		});
	}
}
