package com.campanion.entity;

import com.campanion.Campanion;
import com.campanion.item.CampanionItems;
import com.campanion.item.SpearItem;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.LinkedHashMap;
import java.util.Map;

public class CampanionEntities {

	private static final Map<Identifier, EntityType<?>> ENTITY_TYPES = new LinkedHashMap<>();

	public static final EntityType<SpearEntity> WOODEN_SPEAR = add("wooden_spear", createSpear(CampanionItems.WOODEN_SPEAR));
	public static final EntityType<SpearEntity> STONE_SPEAR = add("stone_spear", createSpear(CampanionItems.STONE_SPEAR));
	public static final EntityType<SpearEntity> IRON_SPEAR = add("iron_spear", createSpear(CampanionItems.IRON_SPEAR));
	public static final EntityType<SpearEntity> GOLDEN_SPEAR = add("golden_spear", createSpear(CampanionItems.GOLDEN_SPEAR));
	public static final EntityType<SpearEntity> DIAMOND_SPEAR = add("diamond_spear", createSpear(CampanionItems.DIAMOND_SPEAR));

	public static final EntityType<GrapplingHookEntity> GRAPPLING_HOOK = add("grappling_hook", FabricEntityTypeBuilder.<GrapplingHookEntity>create(EntityCategory.MISC, (type, world) -> new GrapplingHookEntity(world)).disableSaving().disableSummon().size(EntityDimensions.fixed(0.3F, 0.3F)).build());

	public static final EntityType<LawnChairEntity> LAWN_CHAIR = add("lawn_chair", FabricEntityTypeBuilder.<LawnChairEntity>create(EntityCategory.MISC, (type, world) -> new LawnChairEntity(world)).build());

	public static final EntityType<SkippingStoneEntity> THROWING_STONE = add("skipping_stone", FabricEntityTypeBuilder.<SkippingStoneEntity>create(EntityCategory.MISC).disableSaving().disableSummon().size(EntityDimensions.fixed(0.25F,0.25F)).build());

	public static void register() {
		for (Identifier id : ENTITY_TYPES.keySet()) {
			Registry.register(Registry.ENTITY_TYPE, id, ENTITY_TYPES.get(id));
		}
	}

	private static <T extends EntityType<?>> T add(String name, T type) {
		Identifier id = new Identifier(Campanion.MOD_ID, name);
		ENTITY_TYPES.put(id, type);
		return type;
	}

	private static EntityType<SpearEntity> createSpear(SpearItem item) {
		return FabricEntityTypeBuilder.<SpearEntity>create(EntityCategory.MISC, (entity, world) -> new SpearEntity(entity, world, item)).size(EntityDimensions.fixed(0.5F, 0.5F)).build();
	}
}
