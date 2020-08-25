package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.Campanion;
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

import java.util.Objects;

public class S2CClearBackpackHeldItem {
	public static final Identifier ID = new Identifier(Campanion.MOD_ID, "clear_held_item");

	public static Packet<?> createPacket() {
		return ServerSidePacketRegistry.INSTANCE.toPacket(ID, new PacketByteBuf(Unpooled.buffer()));
	}

	@Environment(EnvType.CLIENT)
	public static void onPacket(PacketContext context, PacketByteBuf byteBuf) {
		context.getTaskQueue().execute(() -> {
			@SuppressWarnings("resource")
			ItemStack stack = Objects.requireNonNull(MinecraftClient.getInstance().player).inventory.getCursorStack();
			if (stack.getItem() instanceof BackpackItem && stack.hasTag()) {
				stack.getTag().remove("Inventory");
			}
		});
	}
}
