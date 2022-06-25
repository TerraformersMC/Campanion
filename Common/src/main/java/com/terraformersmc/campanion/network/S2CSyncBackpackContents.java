package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public record S2CSyncBackpackContents(NonNullList<ItemStack> stacks) {

	public static void encode(S2CSyncBackpackContents packet, FriendlyByteBuf buf) {
		buf.writeShort(packet.stacks.size());
		for (ItemStack stack : packet.stacks) {
			buf.writeItem(stack);
		}
	}

	public static S2CSyncBackpackContents decode(FriendlyByteBuf buf) {
		int size = buf.readShort();
		NonNullList<ItemStack> stacks = NonNullList.create();
		for (int i = 0; i < size; i++) {
			stacks.add(buf.readItem());
		}
		return new S2CSyncBackpackContents(stacks);
	}

	public static void handle(Supplier<Minecraft> client, S2CSyncBackpackContents packet) {
		LocalPlayer player = client.get().player;
		if (player != null) {
			NonNullList<ItemStack> list = ((BackpackStorePlayer) player).getBackpackStacks();
			list.clear();
			list.addAll(packet.stacks);
		}
	}
}
