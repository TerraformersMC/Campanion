package com.terraformersmc.campanion.block;

import com.terraformersmc.campanion.blockentity.TentPartBlockEntity;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.item.TentBagItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class BaseTentBlock extends Block implements EntityBlock {

	private static final Map<Class<?>, Map<DyeColor, BlockState>> TENT_PART_COLOR_MAP = new HashMap<>();

	private final DyeColor color;

	public BaseTentBlock(Properties settings, DyeColor color) {
		super(settings);
		this.color = color;
		if (this.color != null) {
			TENT_PART_COLOR_MAP.computeIfAbsent(this.getClass(), aClass -> new HashMap<>()).put(this.color, this.defaultBlockState());
		}
	}

	@Override
	public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter view, @NotNull BlockPos pos) {
		return true;
	}

	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, Level world, @NotNull BlockPos pos, Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		ItemStack stack = player.getItemInHand(hand);
		if (!world.isClientSide && stack.getItem() instanceof DyeItem && blockEntity instanceof TentPartBlockEntity && this.color != null) {
			DyeColor stackColor = ((DyeItem) stack.getItem()).getDyeColor();
			TentPartBlockEntity tentPart = (TentPartBlockEntity) blockEntity;

			Vec3 changeSize = Vec3.atLowerCornerOf(tentPart.getSize()).add(-1, -1, -1).scale(1 / 2F);

			for (int x = Mth.floor(-changeSize.x); x <= Mth.floor(changeSize.x); x++) {
				for (int y = 0; y <= 2 * changeSize.y(); y++) {
					for (int z = Mth.floor(-changeSize.z); z <= Mth.floor(changeSize.z); z++) {
						BlockPos off = tentPart.getLinkedPos().offset(x, y, z);
						BlockState offState = world.getBlockState(off);
						if (offState.getBlock() instanceof BaseTentBlock && TENT_PART_COLOR_MAP.containsKey(offState.getBlock().getClass())) {
							BlockState newState = TENT_PART_COLOR_MAP.get(offState.getBlock().getClass()).get(stackColor);
							for (Property property : newState.getProperties()) {
								newState = newState.setValue(property, offState.getValue(property));
							}
							world.setBlockAndUpdate(off, newState);
						}
					}
				}
			}
		}
		return super.use(state, world, pos, player, hand, hit);
	}

	@Override
	public void onRemove(BlockState state, @NotNull Level world, @NotNull BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock().getClass() == newState.getBlock().getClass()) {
			return;
		}
		super.onRemove(state, world, pos, newState, moved);
	}

	@Override
	public void playerWillDestroy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, Player player) {
		int slotIndex = -1;
		for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
			ItemStack stack = player.getInventory().getItem(i);
			if (stack.getItem() == CampanionItems.TENT_BAG && TentBagItem.isEmpty(stack)) {
				slotIndex = i;
			}
		}
		BlockEntity be = world.getBlockEntity(pos);
		if (slotIndex != -1 && be instanceof TentPartBlockEntity) {
			TentPartBlockEntity tentPart = (TentPartBlockEntity) be;

			Vec3 changeSize = Vec3.atLowerCornerOf(tentPart.getSize()).add(-1, -1, -1).scale(1 / 2F);

			ItemStack out = new ItemStack(CampanionItems.TENT_BAG);
			ListTag list = new ListTag();
			for (int x = Mth.floor(-changeSize.x); x <= Mth.floor(changeSize.x); x++) {
				for (int y = 0; y <= 2 * changeSize.y(); y++) {
					for (int z = Mth.floor(-changeSize.z); z <= Mth.floor(changeSize.z); z++) {
						BlockPos off = tentPart.getLinkedPos().offset(x, y, z);
						if (world.isEmptyBlock(off)) {
							continue;
						}
						CompoundTag tag = new CompoundTag();
						tag.put("Pos", NbtUtils.writeBlockPos(new BlockPos(x, y, z)));
						tag.put("BlockState", NbtUtils.writeBlockState(world.getBlockState(off)));
						BlockEntity entity = world.getBlockEntity(off);
						if (entity != null) {
							tag.put("BlockEntityData", entity.saveWithId());
						}
						list.add(tag);
						world.removeBlockEntity(off); // if we want block entities to drop items, remove this line
						world.setBlock(off, Blocks.AIR.defaultBlockState(), 1 | 2 | 16);
					}
				}
			}
			out.getOrCreateTag().put("Blocks", list);
			out.getOrCreateTag().put("TentSize", NbtUtils.writeBlockPos(tentPart.getSize()));


			player.getInventory().setItem(slotIndex, out);
		}

		super.playerWillDestroy(world, pos, state, player);
	}


	@Override
	public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
		return new TentPartBlockEntity(pos, state);
	}

	public static VoxelShape createDiagonals(int heightStart, int lengthStart, boolean bothSides) {
		double size = 2D;
		VoxelShape shape = Shapes.empty();
		for (double d = 0; d < heightStart; d += size) {
			shape = Shapes.or(shape, box(0, heightStart - d - size, lengthStart - d - size, 16, heightStart - d + size, lengthStart - d + size));
			if (bothSides) {
				shape = Shapes.or(shape, box(0, heightStart - d - size, lengthStart + d - size, 16, heightStart - d + size, lengthStart + d + size));
			}
		}
		return shape;
	}

	public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[]{shape, Shapes.empty()};

		int times = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = Shapes.or(buffer[1], Shapes.box(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
			buffer[0] = buffer[1];
			buffer[1] = Shapes.empty();
		}

		return buffer[0];
	}
}
