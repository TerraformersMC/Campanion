package com.terraformersmc.campanion.entity;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.item.CampanionItems;
import com.terraformersmc.campanion.item.SpearItem;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
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

	public static final EntityType<GrapplingHookEntity> GRAPPLING_HOOK = add("grappling_hook", FabricEntityTypeBuilder.<GrapplingHookEntity>create(MobCategory.MISC, (type, world) -> new GrapplingHookEntity(world)).disableSaving().disableSummon().dimensions(EntityDimensions.fixed(0.3F, 0.3F)).build());

	public static final EntityType<LawnChairEntity> LAWN_CHAIR = add("lawn_chair", FabricEntityTypeBuilder.<LawnChairEntity>create(MobCategory.MISC, (type, world) -> new LawnChairEntity(world)).build());

	public static final EntityType<SkippingStoneEntity> THROWING_STONE = add("skipping_stone", FabricEntityTypeBuilder.<SkippingStoneEntity>create(MobCategory.MISC).disableSaving().disableSummon().dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build());

	public static final EntityType<FlareEntity> FLARE = add("flare_thrown", FabricEntityTypeBuilder.<FlareEntity>create(MobCategory.MISC).disableSaving().disableSummon().dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build());

	public static void register() {
		for (ResourceLocation id : ENTITY_TYPES.keySet()) {
			Registry.register(Registry.ENTITY_TYPE, id, ENTITY_TYPES.get(id));
		}
	}

	private static <T extends EntityType<?>> T add(String name, T type) {
		ResourceLocation id = new ResourceLocation(Campanion.MOD_ID, name);
		ENTITY_TYPES.put(id, type);
		return type;
	}

	private static EntityType<SpearEntity> createSpear(SpearItem item) {
		return FabricEntityTypeBuilder.<SpearEntity>create(MobCategory.MISC, (entity, world) -> new SpearEntity(entity, world, item)).dimensions(EntityDimensions.fixed(0.5F, 0.5F)).build();
	}
}
