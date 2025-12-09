package com.terraformersmc.campanion.advancement.criterion;

import com.terraformersmc.campanion.Campanion;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.resources.ResourceLocation;

import java.util.LinkedHashMap;
import java.util.Map;

public class CampanionCriteria {
	private static final Map<ResourceLocation, CriterionTrigger<?>> TRIGGERS = new LinkedHashMap<>();

	public static final PlayerTrigger SLEPT_IN_SLEEPING_BAG = register(ResourceLocation.fromNamespaceAndPath(Campanion.MOD_ID, "slept_in_sleeping_bag"), new PlayerTrigger());
	public static final CountCriterion STONE_SKIPS = register(ResourceLocation.fromNamespaceAndPath(Campanion.MOD_ID, "stone_skips"), new CountCriterion());
	public static final KilledWithStoneCriterion KILLED_WITH_STONE = register(ResourceLocation.fromNamespaceAndPath(Campanion.MOD_ID, "killed_with_stone"), new KilledWithStoneCriterion());

	private static <T extends CriterionTrigger<?>> T register(ResourceLocation pName, T criterion) {
		TRIGGERS.put(pName, criterion);
		return criterion;
	}

	public static void loadClass() {

	}

	public static Map<ResourceLocation, CriterionTrigger<?>> getTriggers() {
		return TRIGGERS;
	}
}
