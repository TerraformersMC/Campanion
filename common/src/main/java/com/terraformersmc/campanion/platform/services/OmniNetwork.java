package com.terraformersmc.campanion.platform.services;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Supplier;

public interface OmniNetwork {

	Packet<?> toVanillaPacket(CustomPacketPayload packet, PacketType type);

	//C2S
	void sendToServer(CustomPacketPayload packet);

	//S2C
	void sendToPlayer(CustomPacketPayload packet, ServerPlayer player);
	void sendToAllInDimension(CustomPacketPayload packet, ServerLevel level);
	void sendToAllAround(CustomPacketPayload packet, ServerLevel level, BlockPos pos);

	<P extends CustomPacketPayload> void registerClientBound(CustomPacketPayload.Type<P> clazz, StreamCodec<RegistryFriendlyByteBuf, P> encoder);

	<P extends CustomPacketPayload> void registerClientBoundHandler(CustomPacketPayload.Type<P> clazz, StreamCodec<RegistryFriendlyByteBuf, P> streamCodec, S2CHandler<P> handler);

	<P extends CustomPacketPayload> void registerServerBound(CustomPacketPayload.Type<P> clazz, StreamCodec<RegistryFriendlyByteBuf, P> streamCodec, C2SHandler<P> handler);

	interface C2SHandler<P extends CustomPacketPayload> {
		void handle(Supplier<MinecraftServer> server, ServerPlayer player, P packet);
	}

	interface S2CHandler<P extends CustomPacketPayload> {
		void handle(Supplier<Supplier<Minecraft>> client, P packet);
	}

	enum PacketType {
		PLAY_C2S, PLAY_S2C,
	}
}
