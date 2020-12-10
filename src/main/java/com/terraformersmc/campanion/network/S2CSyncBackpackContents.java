package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import com.terraformersmc.campanion.item.BackpackItem;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class S2CSyncBackpackContents {
	public static final Identifier ID = new Identifier(Campanion.MOD_ID, "clear_held_item");

	public static Packet<?> createPacket(DefaultedList<ItemStack> stacks) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeShort(stacks.size());
		for (ItemStack stack : stacks) {
			buf.writeItemStack(stack);
		}
		return ServerSidePacketRegistry.INSTANCE.toPacket(ID, buf);
	}

	@Environment(EnvType.CLIENT)
	public static void onPacket(PacketContext context, PacketByteBuf byteBuf) {
		int size = byteBuf.readShort();
		List<ItemStack> stacks = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			stacks.add(byteBuf.readItemStack());
		}
		context.getTaskQueue().execute(() -> {
			DefaultedList<ItemStack> list = ((BackpackStorePlayer) context.getPlayer()).getBackpackStacks();
			list.clear();
			list.addAll(stacks);
		});
	}
}
