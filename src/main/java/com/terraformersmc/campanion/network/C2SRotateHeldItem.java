package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.item.PlaceableTentItem;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.structure.Structure;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class C2SRotateHeldItem {
	public static final Identifier ID = new Identifier(Campanion.MOD_ID, "rotate_held_item");

	public static Packet<?> createPacket() {
		return ClientPlayNetworking.createC2SPacket(ID, new PacketByteBuf(Unpooled.buffer()));
	}

	public static void onPacket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler networkHandler, PacketByteBuf buffer, PacketSender sender) {
		server.execute(() -> {
			ItemStack stack = player.getMainHandStack();
			if (stack.getItem() instanceof PlaceableTentItem && stack.getOrCreateNbt().contains("Blocks", 9)) {
				for (NbtElement block : stack.getOrCreateNbt().getList("Blocks", 10)) {
					NbtCompound tag = (NbtCompound) block;
					BlockPos off = NbtHelper.toBlockPos(tag.getCompound("Pos"));
					BlockState state = NbtHelper.toBlockState(tag.getCompound("BlockState"));
					NbtCompound data = tag.getCompound("BlockEntityData");

					BlockPos rotatedPos = Structure.transformAround(off, BlockMirror.NONE, BlockRotation.CLOCKWISE_90, BlockPos.ORIGIN);
					tag.put("Pos", NbtHelper.fromBlockPos(rotatedPos));

					BlockState rotatedState = state.rotate(BlockRotation.CLOCKWISE_90);
					tag.put("BlockState", NbtHelper.fromBlockState(rotatedState));

					if (!data.isEmpty()) {
						BlockEntity be = BlockEntity.createFromNbt(rotatedPos, state, data);
						if (be != null) {
							// TODO - Can't find equivalent
//							be.applyRotation(BlockRotation.CLOCKWISE_90);
							tag.put("BlockEntityData", be.createNbt());
						}
					}
				}
			}
		});
	}

}
