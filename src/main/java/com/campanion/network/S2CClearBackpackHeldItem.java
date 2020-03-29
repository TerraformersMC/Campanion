package com.campanion.network;

import com.campanion.Campanion;
import com.campanion.item.BackpackItem;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.Objects;
import java.util.UUID;

public class S2CClearBackpackHeldItem {
    public static final Identifier ID = new Identifier(Campanion.MOD_ID, "clear_held_item");

    public static Packet<?> createPacket() {
        return ServerSidePacketRegistry.INSTANCE.toPacket(ID, new PacketByteBuf(Unpooled.buffer()));
    }

    @Environment(EnvType.CLIENT)
    public static void onPacket(PacketContext context, PacketByteBuf byteBuf) {
        context.getTaskQueue().execute(() -> {
            ItemStack stack = Objects.requireNonNull(MinecraftClient.getInstance().player).inventory.getCursorStack();
            if(stack.getItem() instanceof BackpackItem && stack.hasTag()) {
                stack.getTag().remove("Inventory");
            }
        });
    }
}