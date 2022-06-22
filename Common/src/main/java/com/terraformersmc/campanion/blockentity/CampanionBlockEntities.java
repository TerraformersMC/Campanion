package com.terraformersmc.campanion.blockentity;

import com.mojang.datafixers.types.Type;
import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.block.CampanionBlocks;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.terraformersmc.campanion.platform.Services;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.terraformersmc.campanion.block.CampanionBlocks.*;

public class CampanionBlockEntities {

	private static final Map<ResourceLocation, BlockEntityType<? extends BlockEntity>> BLOCK_ENTITY_TYPES = new LinkedHashMap<>();

	public static final BlockEntityType<RopeBridgePlanksBlockEntity> ROPE_BRIDGE_PLANK = add("rope_bridge_planks", RopeBridgePlanksBlockEntity::new, CampanionBlocks.ROPE_BRIDGE_PLANKS);
	public static final BlockEntityType<RopeBridgePostBlockEntity> ROPE_BRIDGE_POST = add("rope_bridge_post", RopeBridgePostBlockEntity::new, CampanionBlocks.ROPE_BRIDGE_POST);

	public static final BlockEntityType<LawnChairBlockEntity> LAWN_CHAIR = add("lawn_chair", LawnChairBlockEntity::new, WHITE_LAWN_CHAIR,
		ORANGE_LAWN_CHAIR, MAGENTA_LAWN_CHAIR, LIGHT_BLUE_LAWN_CHAIR, YELLOW_LAWN_CHAIR, LIME_LAWN_CHAIR, PINK_LAWN_CHAIR,GRAY_LAWN_CHAIR, LIGHT_GRAY_LAWN_CHAIR,
		CYAN_LAWN_CHAIR, PURPLE_LAWN_CHAIR, BLUE_LAWN_CHAIR, BROWN_LAWN_CHAIR, GREEN_LAWN_CHAIR, RED_LAWN_CHAIR, BLACK_LAWN_CHAIR
	);

	public static final BlockEntityType<TentPartBlockEntity> TENT_PART = add("tent_part", TentPartBlockEntity::new,
		TENT_POLE, WHITE_TENT_SIDE, ORANGE_TENT_SIDE, MAGENTA_TENT_SIDE, LIGHT_BLUE_TENT_SIDE, YELLOW_TENT_SIDE, LIME_TENT_SIDE,
		PINK_TENT_SIDE, GRAY_TENT_SIDE, LIGHT_GRAY_TENT_SIDE, CYAN_TENT_SIDE, PURPLE_TENT_SIDE, BLUE_TENT_SIDE, BROWN_TENT_SIDE,
		GREEN_TENT_SIDE, RED_TENT_SIDE, BLACK_TENT_SIDE, WHITE_TENT_TOP, ORANGE_TENT_TOP, MAGENTA_TENT_TOP, LIGHT_BLUE_TENT_TOP,
		YELLOW_TENT_TOP, LIME_TENT_TOP, PINK_TENT_TOP, GRAY_TENT_TOP, LIGHT_GRAY_TENT_TOP, CYAN_TENT_TOP, PURPLE_TENT_TOP, BLUE_TENT_TOP,
		BROWN_TENT_TOP, GREEN_TENT_TOP, RED_TENT_TOP, BLACK_TENT_TOP, WHITE_TOPPED_TENT_POLE, ORANGE_TOPPED_TENT_POLE, MAGENTA_TOPPED_TENT_POLE,
		LIGHT_BLUE_TOPPED_TENT_POLE, YELLOW_TOPPED_TENT_POLE, LIME_TOPPED_TENT_POLE, PINK_TOPPED_TENT_POLE, GRAY_TOPPED_TENT_POLE, LIGHT_GRAY_TOPPED_TENT_POLE,
		CYAN_TOPPED_TENT_POLE, PURPLE_TOPPED_TENT_POLE, BLUE_TOPPED_TENT_POLE, BROWN_TOPPED_TENT_POLE, GREEN_TOPPED_TENT_POLE, RED_TOPPED_TENT_POLE,
		BLACK_TOPPED_TENT_POLE, WHITE_FLAT_TENT_TOP, ORANGE_FLAT_TENT_TOP, MAGENTA_FLAT_TENT_TOP, LIGHT_BLUE_FLAT_TENT_TOP, YELLOW_FLAT_TENT_TOP,
		LIME_FLAT_TENT_TOP, PINK_FLAT_TENT_TOP, GRAY_FLAT_TENT_TOP, LIGHT_GRAY_FLAT_TENT_TOP, CYAN_FLAT_TENT_TOP, PURPLE_FLAT_TENT_TOP,
		BLUE_FLAT_TENT_TOP, BROWN_FLAT_TENT_TOP, GREEN_FLAT_TENT_TOP, RED_FLAT_TENT_TOP, BLACK_FLAT_TENT_TOP
	);

	private static <T extends BlockEntity> BlockEntityType<T> add(String name, BiFunction<BlockPos, BlockState, T> factory, Block... blocks) {
		var creator = Services.PLATFORM.createBlockEntity(factory, blocks);
		Type<?> type = Util.fetchChoiceType(References.BLOCK_ENTITY, name);
		return add(name, creator.apply(type));
	}


	private static <T extends BlockEntity> BlockEntityType<T> add(String name, BlockEntityType<T> blockEntityType) {
		BLOCK_ENTITY_TYPES.put(new ResourceLocation(Campanion.MOD_ID, name), blockEntityType);
		return blockEntityType;
	}

//	public static void register() {
//		for (ResourceLocation id : BLOCK_ENTITY_TYPES.keySet()) {
//			Registry.register(Registry.BLOCK_ENTITY_TYPE, id, BLOCK_ENTITY_TYPES.get(id));
//		}
//	}

	public static Map<ResourceLocation, BlockEntityType<? extends BlockEntity>> getBlockEntityTypes() {
		return BLOCK_ENTITY_TYPES;
	}

}
