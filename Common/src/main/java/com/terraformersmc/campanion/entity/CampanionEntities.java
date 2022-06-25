package com.terraformersmc.campanion.entity;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.item.SpearItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.LinkedHashMap;
import java.util.Map;

public class CampanionEntities {

	private static final Map<ResourceLocation, EntityType<?>> ENTITY_TYPES = new LinkedHashMap<>();

	public static final EntityType<SpearEntity> WOODEN_SPEAR = add("wooden_spear", createSpear(CampanionItems.WOODEN_SPEAR));
	public static final EntityType<SpearEntity> STONE_SPEAR = add("stone_spear", createSpear(CampanionItems.STONE_SPEAR));
	public static final EntityType<SpearEntity> IRON_SPEAR = add("iron_spear", createSpear(CampanionItems.IRON_SPEAR));
	public static final EntityType<SpearEntity> GOLDEN_SPEAR = add("golden_spear", createSpear(CampanionItems.GOLDEN_SPEAR));
	public static final EntityType<SpearEntity> DIAMOND_SPEAR = add("diamond_spear", createSpear(CampanionItems.DIAMOND_SPEAR));
	public static final EntityType<SpearEntity> NETHERITE_SPEAR = add("netherite_spear", createSpear(CampanionItems.NETHERITE_SPEAR));

	public static final EntityType<GrapplingHookEntity> GRAPPLING_HOOK = add("grappling_hook", EntityType.Builder.<GrapplingHookEntity>of((type, world) -> new GrapplingHookEntity(world), MobCategory.MISC).noSave().noSummon().sized(0.3F, 0.3F));

	public static final EntityType<LawnChairEntity> LAWN_CHAIR = add("lawn_chair", EntityType.Builder.<LawnChairEntity>of((type, world) -> new LawnChairEntity(world), MobCategory.MISC));

	public static final EntityType<SkippingStoneEntity> THROWING_STONE = add("skipping_stone", EntityType.Builder.<SkippingStoneEntity>createNothing(MobCategory.MISC).noSave().noSummon().sized(0.25F, 0.25F));

	public static final EntityType<FlareEntity> FLARE = add("flare_thrown", EntityType.Builder.<FlareEntity>createNothing(MobCategory.MISC).noSave().noSummon().sized(0.25F, 0.25F));

//	public static void register() {
//		for (ResourceLocation id : ENTITY_TYPES.keySet()) {
//			Registry.register(Registry.ENTITY_TYPE, id, ENTITY_TYPES.get(id));
//		}
//	}

	public static Map<ResourceLocation, EntityType<?>> getEntityTypes() {
		return ENTITY_TYPES;
	}

	private static <T extends Entity> EntityType<T> add(String name, EntityType.Builder<T> builder) {
		ResourceLocation id = new ResourceLocation(Campanion.MOD_ID, name);
		var type = builder.build(name);
		ENTITY_TYPES.put(id, type);
		return type;
	}

	private static EntityType.Builder<SpearEntity> createSpear(SpearItem item) {
		return EntityType.Builder.<SpearEntity>of((entity, world) -> new SpearEntity(entity, world, item), MobCategory.MISC).sized(0.5F, 0.5F);
	}
}
