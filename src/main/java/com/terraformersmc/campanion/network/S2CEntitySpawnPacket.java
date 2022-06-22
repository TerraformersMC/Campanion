package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.entity.AdditionalSpawnDataEntity;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import java.util.UUID;

public class S2CEntitySpawnPacket {
	public static final ResourceLocation ID = new ResourceLocation(Campanion.MOD_ID, "spawn_entity");

	public static Packet<?> createPacket(Entity entity) {
		FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
		buf.writeVarInt(Registry.ENTITY_TYPE.getId(entity.getType()));
		buf.writeUUID(entity.getUUID());
		buf.writeVarInt(entity.getId());
		buf.writeDouble(entity.getX());
		buf.writeDouble(entity.getY());
		buf.writeDouble(entity.getZ());
		buf.writeByte(Mth.floor(entity.getXRot() * 256.0F / 360.0F));
		buf.writeByte(Mth.floor(entity.getYRot() * 256.0F / 360.0F));
		if (entity instanceof AdditionalSpawnDataEntity) {
			((AdditionalSpawnDataEntity) entity).writeToBuffer(buf);
		}
		return ServerPlayNetworking.createS2CPacket(ID, buf);
	}

	@Environment(EnvType.CLIENT)
	public static void onPacket(Minecraft client, ClientPacketListener networkHandler, FriendlyByteBuf buffer, PacketSender sender) {
		EntityType<?> type = Registry.ENTITY_TYPE.byId(buffer.readVarInt());
		UUID entityUUID = buffer.readUUID();
		int entityID = buffer.readVarInt();
		double x = buffer.readDouble();
		double y = buffer.readDouble();
		double z = buffer.readDouble();
		float pitch = (buffer.readByte() * 360) / 256.0F;
		float yaw = (buffer.readByte() * 360) / 256.0F;
		ClientLevel world = Minecraft.getInstance().level;
		Entity entity = type.create(world);
		if (entity instanceof AdditionalSpawnDataEntity) {
			((AdditionalSpawnDataEntity) entity).readFromBuffer(buffer);
		}
		client.execute(() -> {
			if (world != null && entity != null) {
				entity.setPos(x, y, z);
				entity.setPacketCoordinates(x, y, z);
				entity.setXRot(pitch);
				entity.getViewYRot(yaw);
				entity.setId(entityID);
				entity.setUUID(entityUUID);
				world.putNonPlayerEntity(entityID, entity);
			}
		});
	}
}
