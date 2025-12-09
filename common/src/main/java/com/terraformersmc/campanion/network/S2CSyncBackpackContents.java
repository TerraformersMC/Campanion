package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.backpack.BackpackStorePlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public record S2CSyncBackpackContents(NonNullList<ItemStack> stacks) implements CustomPacketPayload {
	public static final Type<S2CSyncBackpackContents> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Campanion.MOD_ID, "sync_backpack_contents"));
	public static final StreamCodec<RegistryFriendlyByteBuf, S2CSyncBackpackContents> STREAM_CODEC = StreamCodec.of(S2CSyncBackpackContents::encode, S2CSyncBackpackContents::decode);

	public static void encode(RegistryFriendlyByteBuf buf, S2CSyncBackpackContents packet) {
		buf.writeShort(packet.stacks.size());
		for (ItemStack stack : packet.stacks) {
			//buf.writeItem(stack);
			ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, stack);
		}
	}

	public static S2CSyncBackpackContents decode(RegistryFriendlyByteBuf buf) {
		int size = buf.readShort();
		NonNullList<ItemStack> stacks = NonNullList.create();
		for (int i = 0; i < size; i++) {
			//stacks.add(buf.readItem());
			stacks.add(ItemStack.OPTIONAL_STREAM_CODEC.decode(buf));
		}
		return new S2CSyncBackpackContents(stacks);
	}

	public static void handle(Supplier<Supplier<Minecraft>> client, S2CSyncBackpackContents packet) {
		LocalPlayer player = client.get().get().player;
		if (player != null) {
			NonNullList<ItemStack> list = ((BackpackStorePlayer) player).getBackpackStacks();
			list.clear();
			list.addAll(packet.stacks);
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
