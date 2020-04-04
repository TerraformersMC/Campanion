package com.campanion.blockentity;

import com.campanion.Campanion;
import com.campanion.block.CampanionBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.campanion.block.CampanionBlocks.*;

public class CampanionBlockEntities {

	private static final Map<Identifier, BlockEntityType<? extends BlockEntity>> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

	public static final BlockEntityType<RopeBridgePlanksBlockEntity> ROPE_BRIDGE_PLANK = add("rope_bridge_planks", RopeBridgePlanksBlockEntity::new, CampanionBlocks.ROPE_BRIDGE_PLANKS);
	public static final BlockEntityType<RopeBridgePostBlockEntity> ROPE_BRIDGE_POST = add("rope_bridge_post", RopeBridgePostBlockEntity::new, CampanionBlocks.ROPE_BRIDGE_ANCHOR);

	public static final BlockEntityType<LawnChairBlockEntity> LAWN_CHAIR = add("lawn_chair", LawnChairBlockEntity::new, WHITE_LAWN_CHAIR,
		ORANGE_LAWN_CHAIR, MAGENTA_LAWN_CHAIR, LIGHT_BLUE_LAWN_CHAIR, YELLOW_LAWN_CHAIR, LIME_LAWN_CHAIR, PINK_LAWN_CHAIR,GRAY_LAWN_CHAIR, LIGHT_GRAY_LAWN_CHAIR,
		CYAN_LAWN_CHAIR, PURPLE_LAWN_CHAIR, BLUE_LAWN_CHAIR, BROWN_LAWN_CHAIR, GREEN_LAWN_CHAIR, RED_LAWN_CHAIR, BLACK_LAWN_CHAIR
	);

	public static final BlockEntityType<TentPartBlockEntity> TENT_PART = add("tent_part", TentPartBlockEntity::new,
		TENT_POLE, WHITE_TENT_SIDE, ORANGE_TENT_SIDE, MAGENTA_TENT_SIDE, LIGHT_BLUE_TENT_SIDE, YELLOW_TENT_SIDE, LIME_TENT_SIDE,
		PINK_TENT_SIDE, GRAY_TENT_SIDE, LIGHT_GRAY_TENT_SIDE, CYAN_TENT_SIDE, PURPLE_TENT_SIDE, BLUE_TENT_SIDE, BROWN_TENT_SIDE,
		GREEN_TENT_SIDE, RED_TENT_SIDE, BLACK_TENT_SIDE, WHITE_TENT_TOP, ORANGE_TENT_TOP, MAGENTA_TENT_TOP, LIGHT_BLUE_TENT_TOP,
		YELLOW_TENT_TOP, LIME_TENT_TOP, PINK_TENT_TOP, GRAY_TENT_TOP, LIGHT_GRAY_TENT_TOP, CYAN_TENT_TOP, PURPLE_TENT_TOP, BLUE_TENT_TOP,
		BROWN_TENT_TOP, GREEN_TENT_TOP, RED_TENT_TOP, BLACK_TENT_TOP, WHITE_TENT_TOP_POLE, ORANGE_TENT_TOP_POLE, MAGENTA_TENT_TOP_POLE,
		LIGHT_BLUE_TENT_TOP_POLE, YELLOW_TENT_TOP_POLE, LIME_TENT_TOP_POLE, PINK_TENT_TOP_POLE, GRAY_TENT_TOP_POLE, LIGHT_GRAY_TENT_TOP_POLE,
		CYAN_TENT_TOP_POLE, PURPLE_TENT_TOP_POLE, BLUE_TENT_TOP_POLE, BROWN_TENT_TOP_POLE, GREEN_TENT_TOP_POLE, RED_TENT_TOP_POLE,
		BLACK_TENT_TOP_POLE, WHITE_TENT_TOP_FLAT, ORANGE_TENT_TOP_FLAT, MAGENTA_TENT_TOP_FLAT, LIGHT_BLUE_TENT_TOP_FLAT, YELLOW_TENT_TOP_FLAT,
		LIME_TENT_TOP_FLAT, PINK_TENT_TOP_FLAT, GRAY_TENT_TOP_FLAT, LIGHT_GRAY_TENT_TOP_FLAT, CYAN_TENT_TOP_FLAT, PURPLE_TENT_TOP_FLAT,
		BLUE_TENT_TOP_FLAT, BROWN_TENT_TOP_FLAT, GREEN_TENT_TOP_FLAT, RED_TENT_TOP_FLAT, BLACK_TENT_TOP_FLAT
	);

	private static <T extends BlockEntity> BlockEntityType<T> add(String name, Supplier<? extends T> supplier, Block... blocks) {
		return add(name, BlockEntityType.Builder.create(supplier, blocks));
	}

	private static <T extends BlockEntity> BlockEntityType<T> add(String name, BlockEntityType.Builder<T> builder) {
		return add(name, builder.build(null));
	}

	private static <T extends BlockEntity> BlockEntityType<T> add(String name, BlockEntityType<T> blockEntityType) {
		BLOCK_ENTITY_TYPES.put(new Identifier(Campanion.MOD_ID, name), blockEntityType);
		return blockEntityType;
	}

	public static void register() {
		for (Identifier id : BLOCK_ENTITY_TYPES.keySet()) {
			Registry.register(Registry.BLOCK_ENTITY_TYPE, id, BLOCK_ENTITY_TYPES.get(id));
		}
	}

	public static Map<Identifier, BlockEntityType<? extends BlockEntity>> getBlockEntityTypes() {
		return BLOCK_ENTITY_TYPES;
	}

}
