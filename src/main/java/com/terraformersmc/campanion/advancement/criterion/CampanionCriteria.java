package com.terraformersmc.campanion.advancement.criterion;

import com.terraformersmc.campanion.Campanion;
import com.terraformersmc.campanion.mixin.InvokerCriterions;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.LocationArrivalCriterion;
import net.minecraft.util.Identifier;

public class CampanionCriteria {
	public static final LocationArrivalCriterion SLEPT_IN_SLEEPING_BAG = register(new LocationArrivalCriterion(new Identifier(Campanion.MOD_ID, "slept_in_sleeping_bag")));
	public static final CountCriterion STONE_SKIPS = register(new CountCriterion(new Identifier(Campanion.MOD_ID, "stone_skips")));
	public static final KilledWithStoneCriterion KILLED_WITH_STONE = register(new KilledWithStoneCriterion());

	private static <T extends Criterion<?>> T register(T criterion) {
		return InvokerCriterions.callRegister(criterion);
	}

	public static void loadClass() {

	}
}
