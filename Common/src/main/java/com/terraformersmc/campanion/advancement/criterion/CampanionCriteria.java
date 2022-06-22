package com.terraformersmc.campanion.advancement.criterion;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.mixin.InvokerCriteria;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.resources.ResourceLocation;

public class CampanionCriteria {
	public static final PlayerTrigger SLEPT_IN_SLEEPING_BAG = register(new PlayerTrigger(new ResourceLocation(Campanion.MOD_ID, "slept_in_sleeping_bag")));
	public static final CountCriterion STONE_SKIPS = register(new CountCriterion(new ResourceLocation(Campanion.MOD_ID, "stone_skips")));
	public static final KilledWithStoneCriterion KILLED_WITH_STONE = register(new KilledWithStoneCriterion());

	private static <T extends CriterionTrigger<?>> T register(T criterion) {
		return InvokerCriteria.callRegister(criterion);
	}

	public static void loadClass() {

	}
}
