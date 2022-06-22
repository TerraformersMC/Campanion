package com.terraformersmc.campanion.entity;


import net.minecraft.network.FriendlyByteBuf;

public interface AdditionalSpawnDataEntity {
	void writeToBuffer(FriendlyByteBuf buffer);

	void readFromBuffer(FriendlyByteBuf buffer);
}
