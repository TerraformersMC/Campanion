package com.terraformersmc.campanion.item;

import com.terraformersmc.campanion.blockentity.TentPartBlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.CustomData;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public abstract class PlaceableTentItem extends Item {
	public PlaceableTentItem(Properties settings) {
		super(settings);
	}

	public boolean hasBlocks(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).contains("Blocks");
	}

	public BlockPos getSize(ItemStack stack) {
		return NbtUtils.readBlockPos(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).getUnsafe(), "TentSize").orElse(BlockPos.ZERO);
	}

	public ListTag getBlocks(ItemStack stack) {
		return stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).getUnsafe().getList("Blocks", 10);
	}

	public abstract void onPlaceTent(ItemStack stack);

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
		ItemStack stack = user.getItemInHand(hand);
		HitResult result = user.pick(10, 0, true);
		if (result instanceof BlockHitResult && result.getType() == HitResult.Type.BLOCK) {
			BlockPos base = ((BlockHitResult) result).getBlockPos().above();
			if (!world.isClientSide && getErrorPosition(world, base, stack).isEmpty()) {
				BlockPos tentSize = getSize(stack);
				traverseBlocks(world, stack, (pos, state, tag) -> {
					BlockPos off = base.offset(pos);

					world.setBlockAndUpdate(off, state);
					BlockEntity entity = world.getBlockEntity(off);
					if (entity != null && !tag.isEmpty()) {
						tag.putInt("x", off.getX());
						tag.putInt("y", off.getY());
						tag.putInt("z", off.getZ());
						entity.loadWithComponents(tag, world.registryAccess());
						entity.setChanged();
					}
					if (entity instanceof TentPartBlockEntity) {
						((TentPartBlockEntity) entity).setLinkedPos(base);
						((TentPartBlockEntity) entity).setTentSize(tentSize);
						entity.setChanged();
					}

				});
				onPlaceTent(stack);
			}

			return new InteractionResultHolder<>(InteractionResult.CONSUME, stack);
		}

		return super.use(world, user, hand);
	}

	public List<BlockPos> getErrorPosition(LevelReader world, BlockPos pos, ItemStack stack) {
		List<BlockPos> list = new ArrayList<>();
		if (hasBlocks(stack)) {
			Vec3 changeSize = Vec3.atLowerCornerOf(NbtUtils.readBlockPos(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).getUnsafe(),"TentSize").orElse(BlockPos.ZERO)).add(-1, -1, -1).scale(1 / 2F);
			for (int x = Mth.floor(-changeSize.x); x <= Mth.floor(changeSize.x); x++) {
				for (int y = -1; y <= 2 * changeSize.y(); y++) {
					for (int z = Mth.floor(-changeSize.z); z <= Mth.floor(changeSize.z); z++) {
						BlockPos blockPos = new BlockPos(pos.offset(x, y, z));
						if (y != -1 == !world.getBlockState(blockPos).canBeReplaced()) {
							list.add(blockPos);
						}
					}
				}
			}
		}
		return list;
	}

	public void traverseBlocks(Level world, ItemStack stack, TriConsumer<BlockPos, BlockState, CompoundTag> consumer) {
		if (hasBlocks(stack)) {
			for (Tag block : getBlocks(stack)) {
				CompoundTag tag = (CompoundTag) block;
				BlockPos off = NbtUtils.readBlockPos(tag, "Pos").orElse(BlockPos.ZERO);
				BlockState state = NbtUtils.readBlockState(world.holderLookup(Registries.BLOCK), tag.getCompound("BlockState"));
				CompoundTag data = tag.getCompound("BlockEntityData");
				data.remove("x");
				data.remove("y");
				data.remove("z");
				consumer.accept(off, state, data);
			}
		}
	}
}
