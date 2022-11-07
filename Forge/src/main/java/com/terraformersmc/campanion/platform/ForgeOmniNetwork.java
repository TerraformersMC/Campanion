package com.terraformersmc.campanion.platform;

import com.mojang.datafixers.util.Pair;
import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.platform.services.OmniNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgeOmniNetwork implements OmniNetwork {

	private static final String PROTOCOL_VERSION = "1";
	private static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
		new ResourceLocation(Campanion.MOD_ID, "main"),
		() -> PROTOCOL_VERSION,
		PROTOCOL_VERSION::equals,
		PROTOCOL_VERSION::equals
	);

	private int incrementId = 0;

	private Map<Class, Pair<Function<FriendlyByteBuf, ?>, S2CHandler<?>>> clientHandlers = new HashMap<>();

	@Override
	public Packet<?> toVanillaPacket(Object packet, PacketType type) {
		NetworkDirection direction = switch (type) {
			case PLAY_S2C -> NetworkDirection.PLAY_TO_CLIENT;
			case PLAY_C2S -> NetworkDirection.PLAY_TO_SERVER;
		};
		return NETWORK.toVanillaPacket(packet, direction);
	}

	@Override
	public void sendToServer(Object packet) {
		NETWORK.sendToServer(packet);
	}

	@Override
	public void sendToPlayer(Object packet, ServerPlayer player) {
		NETWORK.send(PacketDistributor.PLAYER.with(() -> player), packet);
	}

	@Override
	public void sendToAllInDimension(Object packet, ServerLevel level) {
		NETWORK.send(PacketDistributor.DIMENSION.with(level::dimension), packet);

	}

	@Override
	public void sendToAllAround(Object packet, ServerLevel level, BlockPos pos) {
		NETWORK.send(PacketDistributor.TRACKING_CHUNK.with(() -> level.getChunkAt(pos)), packet);
	}

	@Override
	public <P> void registerClientBound(Class<P> clazz, BiConsumer<P, FriendlyByteBuf> encoder) {
		NETWORK.registerMessage(this.getNextId(), clazz, encoder, buf -> this.getHandlerFor(clazz).getFirst().apply(buf), (packet, supplier) -> {
			NetworkEvent.Context context = supplier.get();
			context.enqueueWork(() -> this.getHandlerFor(clazz).getSecond().handle(() -> Minecraft::getInstance, packet));
			context.setPacketHandled(true);
		});
	}

	@Override
	public <P> void registerClientBoundHandler(Class<P> clazz, Function<FriendlyByteBuf, P> decoder, S2CHandler<P> handler) {
		this.clientHandlers.put(clazz, Pair.of(decoder, handler));
	}

	@OnlyIn(Dist.CLIENT)
	private <P> Pair<Function<FriendlyByteBuf, P>, S2CHandler<P>> getHandlerFor(Class<P> clazz) {
		Pair<Function<FriendlyByteBuf, ?>, S2CHandler<?>> pair = this.clientHandlers.get(clazz);
		return Pair.of((Function<FriendlyByteBuf, P>)pair.getFirst(), (S2CHandler<P>)pair.getSecond());
	}

	@Override
	public <P> void registerServerBound(Class<P> clazz, BiConsumer<P, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, P> decoder, C2SHandler<P> handler) {
		NETWORK.registerMessage(this.getNextId(), clazz, encoder, decoder, (packet, supplier) -> {
			NetworkEvent.Context context = supplier.get();
			context.enqueueWork(() -> {
				ServerPlayer sender = context.getSender();
				handler.handle(() -> sender.server, sender, packet);
			});
			context.setPacketHandled(true);
		});
	}



	private int getNextId() {
		return this.incrementId++;
	}
}
