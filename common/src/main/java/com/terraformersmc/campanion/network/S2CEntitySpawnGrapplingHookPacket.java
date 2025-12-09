package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.entity.GrapplingHookEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.UUID;

public record S2CEntitySpawnGrapplingHookPacket(
	UUID uuid,
	int id,
	double x, double y, double z,
	int xRot, int yRot,
	boolean hasGrapplingPlayer,
	int grapplingPlayerId
) implements CustomPacketPayload{
	public static final CustomPacketPayload.Type<S2CEntitySpawnGrapplingHookPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Campanion.MOD_ID, "entity_spawn_grappling_hook"));
	public static final StreamCodec<RegistryFriendlyByteBuf, S2CEntitySpawnGrapplingHookPacket> STREAM_CODEC = StreamCodec.of(S2CEntitySpawnGrapplingHookPacket::encode, S2CEntitySpawnGrapplingHookPacket::decode);


	public S2CEntitySpawnGrapplingHookPacket(GrapplingHookEntity entity) {
		this(
			entity.getUUID(), entity.getId(),
			entity.getX(), entity.getY(), entity.getZ(),
			Mth.floor(entity.getXRot() * 256.0F / 360.0F),
			Mth.floor(entity.getYRot() * 256.0F / 360.0F),
			entity.getPlayer() != null,
			entity.getPlayer() == null ? -1 : entity.getPlayer().getId()
		);
	}

	public static void encode(FriendlyByteBuf buf, S2CEntitySpawnGrapplingHookPacket packet) {
		buf.writeUUID(packet.uuid());
		buf.writeVarInt(packet.id());
		buf.writeDouble(packet.x());
		buf.writeDouble(packet.y());
		buf.writeDouble(packet.z());
		buf.writeByte(packet.xRot);
		buf.writeByte(packet.yRot);
		buf.writeBoolean(packet.hasGrapplingPlayer);
		buf.writeVarInt(packet.grapplingPlayerId);
	}

	public static S2CEntitySpawnGrapplingHookPacket decode(FriendlyByteBuf buffer) {
		return new S2CEntitySpawnGrapplingHookPacket(
			buffer.readUUID(),
			buffer.readVarInt(),
			buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
			buffer.readByte(), buffer.readByte(),
			buffer.readBoolean(), buffer.readVarInt()
		);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
