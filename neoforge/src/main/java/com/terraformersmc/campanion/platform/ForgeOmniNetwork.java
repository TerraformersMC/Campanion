package com.terraformersmc.campanion.platform;

import com.terraformersmc.campanion.CampanionForge;
import com.terraformersmc.campanion.platform.services.OmniNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import java.util.function.Function;

public class ForgeOmniNetwork implements OmniNetwork {
	public static final String VERSION = "1.0";
	private static final PayloadRegistrar REGISTRAR = new PayloadRegistrar(VERSION);

	@Override
	public Packet<?> toVanillaPacket(CustomPacketPayload packet, PacketType type) {
		Function<CustomPacketPayload, Packet<?>> creator = switch (type) {
			case PLAY_S2C -> CustomPacketPayload::toVanillaClientbound;
			case PLAY_C2S -> CustomPacketPayload::toVanillaServerbound;
		};
		return creator.apply(packet);
	}

	@Override
	public void sendToServer(CustomPacketPayload packet) {
		PacketDistributor.sendToServer(packet);
	}

	@Override
	public void sendToPlayer(CustomPacketPayload packet, ServerPlayer player) {
		PacketDistributor.sendToPlayer(player, packet);
	}

	@Override
	public void sendToAllInDimension(CustomPacketPayload packet, ServerLevel level) {
		PacketDistributor.sendToPlayersInDimension(level, packet);

	}

	@Override
	public void sendToAllAround(CustomPacketPayload packet, ServerLevel level, BlockPos pos) {
		PacketDistributor.sendToPlayersTrackingChunk(level, new ChunkPos(pos), packet);
	}

	@Override
	public <P extends CustomPacketPayload> void registerClientBound(CustomPacketPayload.Type<P> clazz, StreamCodec<RegistryFriendlyByteBuf, P> encoder) {
		if(FMLEnvironment.dist != Dist.CLIENT){
			CampanionForge.getModEventBus().addListener((RegisterPayloadHandlersEvent event) -> {
				REGISTRAR.playToClient(clazz, encoder, (payload, context) -> {
				});
			});
		}
	}

	@Override
	public <P extends CustomPacketPayload> void registerClientBoundHandler(CustomPacketPayload.Type<P> clazz, StreamCodec<RegistryFriendlyByteBuf, P> streamCodec, S2CHandler<P> handler) {
		if(FMLEnvironment.dist == Dist.CLIENT){
			CampanionForge.getModEventBus().addListener((RegisterPayloadHandlersEvent event) -> {
				REGISTRAR.playToClient(clazz, streamCodec, (payload, context) -> {
					context.enqueueWork(() -> handler.handle(() -> Minecraft::getInstance, payload));
				});
			});
		}
	}

	@Override
	public <P extends CustomPacketPayload> void registerServerBound(CustomPacketPayload.Type<P> clazz, StreamCodec<RegistryFriendlyByteBuf, P> streamCodec, C2SHandler<P> handler) {
		CampanionForge.getModEventBus().addListener((RegisterPayloadHandlersEvent event) -> {
			REGISTRAR.playToServer(clazz, streamCodec, (payload, context) -> {
				context.enqueueWork(() -> handler.handle(context.player()::getServer, (ServerPlayer) context.player(), payload));
			});
		});
	}
}
