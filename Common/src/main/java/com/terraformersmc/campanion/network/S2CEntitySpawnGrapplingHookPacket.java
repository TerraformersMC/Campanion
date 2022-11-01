package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.entity.CampanionEntities;
import com.terraformersmc.campanion.entity.GrapplingHookEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;
import java.util.function.Supplier;

public record S2CEntitySpawnGrapplingHookPacket(
	UUID uuid,
	int id,
	double x, double y, double z,
	int xRot, int yRot,
	boolean hasGrapplingPlayer,
	int grapplingPlayerId
) {

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

	public static void encode(S2CEntitySpawnGrapplingHookPacket packet, FriendlyByteBuf buf) {
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

	public static void handle(Supplier<Minecraft> minecraft, S2CEntitySpawnGrapplingHookPacket packet) {
		ClientLevel world = minecraft.get().level;
		GrapplingHookEntity entity = CampanionEntities.GRAPPLING_HOOK.create(world);
		if (world != null && entity != null) {
			entity.syncPacketPositionCodec(packet.x, packet.y, packet.z);
			entity.setPosRaw(packet.x, packet.y, packet.z);
			entity.setXRot(packet.xRot);
			entity.getViewYRot(packet.yRot);
			entity.setId(packet.id);
			entity.setUUID(packet.uuid);

			if(packet.hasGrapplingPlayer && world.getEntity(packet.grapplingPlayerId) instanceof Player player) {
				entity.setPlayer(player);
			}
			world.putNonPlayerEntity(packet.id, entity);
		}
	}
}
