package com.terraformersmc.campanion.platform;
import com.terraformersmc.campanion.platform.services.OmniNetwork;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.function.Function;

public class FabricOmniNetwork implements OmniNetwork {

	public FabricOmniNetwork() {
	}

	@Override
	public Packet<?> toVanillaPacket(CustomPacketPayload packet, PacketType type) {
		Function<CustomPacketPayload, Packet<?>> creator = switch (type) {
			case PLAY_C2S -> ClientPlayNetworking::createC2SPacket;
			case PLAY_S2C -> ServerPlayNetworking::createS2CPacket;
		};

		return creator.apply(packet);
	}

	@Override
	public void sendToServer(CustomPacketPayload packet) {
		ClientPlayNetworking.send(packet);
	}

	@Override
	public void sendToPlayer(CustomPacketPayload packet, ServerPlayer player) {
		ServerPlayNetworking.send(player, packet);
	}

	@Override
	public void sendToAllInDimension(CustomPacketPayload packet, ServerLevel level) {
		for (ServerPlayer player : level.players()) {
			ServerPlayNetworking.send(player, packet);
		}

	}

	@Override
	public void sendToAllAround(CustomPacketPayload packet, ServerLevel level, BlockPos pos) {
		final LevelChunk chunk = level.getChunkAt(pos);
		((ServerChunkCache)chunk.getLevel().getChunkSource()).chunkMap.getPlayers(chunk.getPos(), false).forEach(player ->
			ServerPlayNetworking.send(player, packet)
		);
	}

	@Override
	public <P extends CustomPacketPayload> void registerClientBound(CustomPacketPayload.Type<P> clazz, StreamCodec<RegistryFriendlyByteBuf, P> encoder) {
		PayloadTypeRegistry.playS2C().register(clazz, encoder);
	}

	@Override
	public <P extends CustomPacketPayload> void registerClientBoundHandler(CustomPacketPayload.Type<P> clazz, StreamCodec<RegistryFriendlyByteBuf, P> streamCodec, S2CHandler<P> handler) {
		ClientPlayNetworking.registerGlobalReceiver(clazz, (packet, context) -> {
			context.client().execute(() -> handler.handle(() -> context::client, packet));
		});
	}

	@Override
	public <P extends CustomPacketPayload> void registerServerBound(CustomPacketPayload.Type<P> clazz, StreamCodec<RegistryFriendlyByteBuf, P> streamCodec, C2SHandler<P> handler) {
		PayloadTypeRegistry.playC2S().register(clazz, streamCodec);
		ServerPlayNetworking.registerGlobalReceiver(clazz, (packet, context) -> {
			context.server().execute(() -> handler.handle(context::server, context.player(), packet));
		});
	}

}
