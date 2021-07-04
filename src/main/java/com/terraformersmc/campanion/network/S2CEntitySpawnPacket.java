package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.entity.AdditionalSpawnDataEntity;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class S2CEntitySpawnPacket {
	public static final Identifier ID = new Identifier(Campanion.MOD_ID, "spawn_entity");

	public static Packet<?> createPacket(Entity entity) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeVarInt(Registry.ENTITY_TYPE.getRawId(entity.getType()));
		buf.writeUuid(entity.getUuid());
		buf.writeVarInt(entity.getId());
		buf.writeDouble(entity.getX());
		buf.writeDouble(entity.getY());
		buf.writeDouble(entity.getZ());
		buf.writeByte(MathHelper.floor(entity.getPitch() * 256.0F / 360.0F));
		buf.writeByte(MathHelper.floor(entity.getYaw() * 256.0F / 360.0F));
		if (entity instanceof AdditionalSpawnDataEntity) {
			((AdditionalSpawnDataEntity) entity).writeToBuffer(buf);
		}
		return ServerPlayNetworking.createS2CPacket(ID, buf);
	}

	@Environment(EnvType.CLIENT)
	public static void onPacket(MinecraftClient client, ClientPlayNetworkHandler networkHandler, PacketByteBuf buffer, PacketSender sender) {
		EntityType<?> type = Registry.ENTITY_TYPE.get(buffer.readVarInt());
		UUID entityUUID = buffer.readUuid();
		int entityID = buffer.readVarInt();
		double x = buffer.readDouble();
		double y = buffer.readDouble();
		double z = buffer.readDouble();
		float pitch = (buffer.readByte() * 360) / 256.0F;
		float yaw = (buffer.readByte() * 360) / 256.0F;
		ClientWorld world = MinecraftClient.getInstance().world;
		Entity entity = type.create(world);
		if (entity instanceof AdditionalSpawnDataEntity) {
			((AdditionalSpawnDataEntity) entity).readFromBuffer(buffer);
		}
		client.execute(() -> {
			if (world != null && entity != null) {
				entity.setPosition(x, y, z);
				entity.updateTrackedPosition(x, y, z);
				entity.setPitch(pitch);
				entity.getYaw(yaw);
				entity.setId(entityID);
				entity.setUuid(entityUUID);
				world.addEntity(entityID, entity);
			}
		});
	}
}
