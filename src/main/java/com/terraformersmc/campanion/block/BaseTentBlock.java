package com.terraformersmc.campanion.block;

import com.terraformersmc.campanion.blockentity.TentPartBlockEntity;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.item.TentBagItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class BaseTentBlock extends Block implements BlockEntityProvider {

	private static final Map<Class<?>, Map<DyeColor, BlockState>> TENT_PART_COLOR_MAP = new HashMap<>();

	private final DyeColor color;

	public BaseTentBlock(Settings settings, DyeColor color) {
		super(settings);
		this.color = color;
		if (this.color != null) {
			TENT_PART_COLOR_MAP.computeIfAbsent(this.getClass(), aClass -> new HashMap<>()).put(this.color, this.getDefaultState());
		}
	}

	@Override
	public boolean isTranslucent(BlockState state, BlockView view, BlockPos pos) {
		return true;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		BlockEntity blockEntity = world.getBlockEntity(pos);
		ItemStack stack = player.getStackInHand(hand);
		if (!world.isClient && stack.getItem() instanceof DyeItem && blockEntity instanceof TentPartBlockEntity && this.color != null) {
			DyeColor stackColor = ((DyeItem) stack.getItem()).getColor();
			TentPartBlockEntity tentPart = (TentPartBlockEntity) blockEntity;

			Vec3d changeSize = Vec3d.of(tentPart.getSize()).add(-1, -1, -1).multiply(1 / 2F);

			for (int x = MathHelper.floor(-changeSize.x); x <= MathHelper.floor(changeSize.x); x++) {
				for (int y = 0; y <= 2 * changeSize.getY(); y++) {
					for (int z = MathHelper.floor(-changeSize.z); z <= MathHelper.floor(changeSize.z); z++) {
						BlockPos off = tentPart.getLinkedPos().add(x, y, z);
						BlockState offState = world.getBlockState(off);
						if (offState.getBlock() instanceof BaseTentBlock && TENT_PART_COLOR_MAP.containsKey(offState.getBlock().getClass())) {
							BlockState newState = TENT_PART_COLOR_MAP.get(offState.getBlock().getClass()).get(stackColor);
							for (Property property : newState.getProperties()) {
								newState = newState.with(property, offState.get(property));
							}
							world.setBlockState(off, newState);
						}
					}
				}
			}
		}
		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock().getClass() == newState.getBlock().getClass()) {
			return;
		}
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		int slotIndex = -1;
		for (int i = 0; i < player.getInventory().size(); i++) {
			ItemStack stack = player.getInventory().getStack(i);
			if (stack.getItem() == CampanionItems.TENT_BAG && TentBagItem.isEmpty(stack)) {
				slotIndex = i;
			}
		}
		BlockEntity be = world.getBlockEntity(pos);
		if (slotIndex != -1 && be instanceof TentPartBlockEntity) {
			TentPartBlockEntity tentPart = (TentPartBlockEntity) be;

			Vec3d changeSize = Vec3d.of(tentPart.getSize()).add(-1, -1, -1).multiply(1 / 2F);

			ItemStack out = new ItemStack(CampanionItems.TENT_BAG);
			NbtList list = new NbtList();
			for (int x = MathHelper.floor(-changeSize.x); x <= MathHelper.floor(changeSize.x); x++) {
				for (int y = 0; y <= 2 * changeSize.getY(); y++) {
					for (int z = MathHelper.floor(-changeSize.z); z <= MathHelper.floor(changeSize.z); z++) {
						BlockPos off = tentPart.getLinkedPos().add(x, y, z);
						if (world.isAir(off)) {
							continue;
						}
						NbtCompound tag = new NbtCompound();
						tag.put("Pos", NbtHelper.fromBlockPos(new BlockPos(x, y, z)));
						tag.put("BlockState", NbtHelper.fromBlockState(world.getBlockState(off)));
						BlockEntity entity = world.getBlockEntity(off);
						if (entity != null) {
							tag.put("BlockEntityData", entity.createNbtWithId());
						}
						list.add(tag);
						world.removeBlockEntity(off); // if we want block entities to drop items, remove this line
						world.setBlockState(off, Blocks.AIR.getDefaultState(), 1 | 2 | 16);
					}
				}
			}
			out.getOrCreateNbt().put("Blocks", list);
			out.getOrCreateNbt().put("TentSize", NbtHelper.fromBlockPos(tentPart.getSize()));


			player.getInventory().setStack(slotIndex, out);
		}

		super.onBreak(world, pos, state, player);
	}


	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TentPartBlockEntity(pos, state);
	}

	public static VoxelShape createDiagonals(int heightStart, int lengthStart, boolean bothSides) {
		double size = 2D;
		VoxelShape shape = VoxelShapes.empty();
		for (double d = 0; d < heightStart; d += size) {
			shape = VoxelShapes.union(shape, createCuboidShape(0, heightStart - d - size, lengthStart - d - size, 16, heightStart - d + size, lengthStart - d + size));
			if (bothSides) {
				shape = VoxelShapes.union(shape, createCuboidShape(0, heightStart - d - size, lengthStart + d - size, 16, heightStart - d + size, lengthStart + d + size));
			}
		}
		return shape;
	}

	public static VoxelShape rotateShape(Direction from, Direction to, VoxelShape shape) {
		VoxelShape[] buffer = new VoxelShape[]{shape, VoxelShapes.empty()};

		int times = (to.getHorizontal() - from.getHorizontal() + 4) % 4;
		for (int i = 0; i < times; i++) {
			buffer[0].forEachBox((minX, minY, minZ, maxX, maxY, maxZ) -> buffer[1] = VoxelShapes.union(buffer[1], VoxelShapes.cuboid(1 - maxZ, minY, minX, 1 - minZ, maxY, maxX)));
			buffer[0] = buffer[1];
			buffer[1] = VoxelShapes.empty();
		}

		return buffer[0];
	}
}
