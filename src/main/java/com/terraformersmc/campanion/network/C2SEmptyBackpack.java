package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.integration.Curios;
import com.terraformersmc.campanion.item.BackpackItem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class C2SEmptyBackpack {
	public static final Identifier ID = new Identifier(Campanion.MOD_ID, "open_backpack");

	public static Packet<?> createPacket() {
		return ClientSidePacketRegistry.INSTANCE.toPacket(ID, new PacketByteBuf(Unpooled.buffer()));
	}

	public static void onPacket(PacketContext context, PacketByteBuf byteBuf) {
		context.getTaskQueue().execute(() -> {
			PlayerEntity player = context.getPlayer();

			ItemStack stack = Curios.getBackpackStack(player);
			if (stack.isEmpty()) {
				stack = player.getEquippedStack(EquipmentSlot.CHEST);
			}
			if (stack.getItem() instanceof BackpackItem) {
				BackpackItem.Type type = ((BackpackItem) stack.getItem()).type;
				player.openHandledScreen(type.createFactory(stack));
			}
		});
	}
}
