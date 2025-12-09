package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.item.PlaceableTentItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.function.Supplier;

public class C2SRotateHeldItem implements CustomPacketPayload{
	public static final C2SRotateHeldItem INSTANCE = new C2SRotateHeldItem();
	public static final CustomPacketPayload.Type<C2SRotateHeldItem> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Campanion.MOD_ID, "rotate_held_item"));
	public static final StreamCodec<RegistryFriendlyByteBuf, C2SRotateHeldItem> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	private C2SRotateHeldItem(){

	}

	public static void handle(Supplier<MinecraftServer> server, ServerPlayer player, C2SRotateHeldItem packet) {
		ItemStack stack = player.getMainHandItem();
		if (stack.getItem() instanceof PlaceableTentItem && stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).contains("Blocks")) {
			CustomData.update(DataComponents.CUSTOM_DATA, stack, customData -> {
				for (Tag block : customData.getList("Blocks", 10)) {
					CompoundTag tag = (CompoundTag) block;
					BlockPos off = NbtUtils.readBlockPos(tag, "Pos").orElse(BlockPos.ZERO);
					BlockState state = NbtUtils.readBlockState(player.level().holderLookup(Registries.BLOCK), tag.getCompound("BlockState"));
					CompoundTag data = tag.getCompound("BlockEntityData");

					BlockPos rotatedPos = StructureTemplate.transform(off, Mirror.NONE, Rotation.CLOCKWISE_90, BlockPos.ZERO);
					tag.put("Pos", NbtUtils.writeBlockPos(rotatedPos));

					BlockState rotatedState = state.rotate(Rotation.CLOCKWISE_90);
					tag.put("BlockState", NbtUtils.writeBlockState(rotatedState));

					if (!data.isEmpty()) {
						BlockEntity be = BlockEntity.loadStatic(rotatedPos, state, data, player.registryAccess());
						if (be != null) {
							// TODO - Can't find equivalent
//							be.applyRotation(BlockRotation.CLOCKWISE_90);
							tag.put("BlockEntityData", be.saveWithoutMetadata(player.registryAccess()));
						}
					}
				}
			});
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
