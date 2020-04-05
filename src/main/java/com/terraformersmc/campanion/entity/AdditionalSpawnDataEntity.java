package com.terraformersmc.campanion.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.PacketByteBuf;

public interface AdditionalSpawnDataEntity {
    void writeToBuffer(PacketByteBuf buffer);
    void readFromBuffer(PacketByteBuf buffer);
}
