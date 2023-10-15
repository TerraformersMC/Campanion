package com.terraformersmc.campanion.network;

import com.terraformersmc.campanion.item.PlaceableTentItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.function.Supplier;

public class C2SRotateHeldItem {
	public static void handle(Supplier<MinecraftServer> server, ServerPlayer player, C2SRotateHeldItem packet) {
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
	}

}
