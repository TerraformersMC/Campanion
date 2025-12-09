package com.terraformersmc.campanion.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.LootContext;

import java.util.Optional;

public class KilledWithStoneCriterion extends SimpleCriterionTrigger<KilledWithStoneCriterion.Conditions> {

	public void trigger(ServerPlayer player, Entity entity, int count) {
		LootContext lootContext = EntityPredicate.createContext(player, entity);
		this.trigger(player, (conditions) -> conditions.matches(player, lootContext, count));
	}

	@Override
	public Codec<Conditions> codec() {
		return Conditions.CODEC;
	}

	public record Conditions(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entityPredicate, MinMaxBounds.Ints skips) implements SimpleCriterionTrigger.SimpleInstance {
		public static final Codec<KilledWithStoneCriterion.Conditions> CODEC = RecordCodecBuilder.create((instance) -> {
			return instance.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(KilledWithStoneCriterion.Conditions::player), EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity").forGetter(KilledWithStoneCriterion.Conditions::entityPredicate), MinMaxBounds.Ints.CODEC.fieldOf("skips").forGetter(KilledWithStoneCriterion.Conditions::skips)).apply(instance, KilledWithStoneCriterion.Conditions::new);
		});

		public boolean matches(ServerPlayer player, LootContext lootContext, int count) {
			return this.skips.matches(count) && this.entityPredicate.map(ep -> ep.matches(lootContext)).orElse(true);
		}
	}
}
