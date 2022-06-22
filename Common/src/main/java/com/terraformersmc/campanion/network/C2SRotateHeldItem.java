package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.item.PlaceableTentItem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class C2SRotateHeldItem {
	public static final ResourceLocation ID = new ResourceLocation(Campanion.MOD_ID, "rotate_held_item");

	public static Packet<?> createPacket() {
		return ClientPlayNetworking.createC2SPacket(ID, new FriendlyByteBuf(Unpooled.buffer()));
	}

	public static void onPacket(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl networkHandler, FriendlyByteBuf buffer, PacketSender sender) {
		server.execute(() -> {
			ItemStack stack = player.getMainHandItem();
			if (stack.getItem() instanceof PlaceableTentItem && stack.getOrCreateTag().contains("Blocks", 9)) {
				for (Tag block : stack.getOrCreateTag().getList("Blocks", 10)) {
					CompoundTag tag = (CompoundTag) block;
					BlockPos off = NbtUtils.readBlockPos(tag.getCompound("Pos"));
					BlockState state = NbtUtils.readBlockState(tag.getCompound("BlockState"));
					CompoundTag data = tag.getCompound("BlockEntityData");

					BlockPos rotatedPos = StructureTemplate.transform(off, Mirror.NONE, Rotation.CLOCKWISE_90, BlockPos.ZERO);
					tag.put("Pos", NbtUtils.writeBlockPos(rotatedPos));

					BlockState rotatedState = state.rotate(Rotation.CLOCKWISE_90);
					tag.put("BlockState", NbtUtils.writeBlockState(rotatedState));

					if (!data.isEmpty()) {
						BlockEntity be = BlockEntity.loadStatic(rotatedPos, state, data);
						if (be != null) {
							// TODO - Can't find equivalent
//							be.applyRotation(BlockRotation.CLOCKWISE_90);
							tag.put("BlockEntityData", be.saveWithoutMetadata());
						}
					}
				}
			}
		});
	}

}
