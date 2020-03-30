package com.campanion.advancement.criterion;

import com.campanion.Campanion;
import com.campanion.mixin.AccessorCriterions;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.advancement.criterion.LocationArrivalCriterion;
import net.minecraft.util.Identifier;

public class CampanionCriteria {
	public static final LocationArrivalCriterion SLEPT_IN_SLEEPING_BAG = register(new LocationArrivalCriterion(new Identifier(Campanion.MOD_ID, "slept_in_sleeping_bag")));

	private static <T extends Criterion<?>> T register(T criterion) {
		return AccessorCriterions.callRegister(criterion);
	}

	public static void loadClass() {

	}
}
