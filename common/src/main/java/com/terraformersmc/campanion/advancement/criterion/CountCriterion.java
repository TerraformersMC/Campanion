package com.terraformersmc.campanion.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class CountCriterion extends SimpleCriterionTrigger<CountCriterion.Conditions> {

	public CountCriterion() {
	}

	public void trigger(ServerPlayer player, int count) {
		this.trigger(player, (conditions) -> conditions.matches(count));
	}

	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}

	public record Conditions(Optional<ContextAwarePredicate> player, MinMaxBounds.Ints count) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<CountCriterion.Conditions> CODEC = RecordCodecBuilder.create((instance) -> {
			return instance.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(CountCriterion.Conditions::player), MinMaxBounds.Ints.CODEC.fieldOf("count").forGetter(CountCriterion.Conditions::count)).apply(instance, CountCriterion.Conditions::new);
		});

		public boolean matches(int count) {
			return this.count.matches(count);
		}
	}
}
