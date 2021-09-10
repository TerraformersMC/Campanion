package com.terraformersmc.campanion.advancement.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CountCriterion extends AbstractCriterion<CountCriterion.Conditions> {
	public final Identifier id;

	public CountCriterion(Identifier id) {
		this.id = id;
	}

	@Override
	public Identifier getId() {
		return id;
	}

	public void trigger(ServerPlayerEntity player, int count) {
		this.trigger(player, (conditions) -> conditions.matches(count));
	}

	@Override
	protected Conditions conditionsFromJson(JsonObject json, EntityPredicate.Extended entityPredicate, AdvancementEntityPredicateDeserializer deserializer) {
		NumberRange.IntRange count = NumberRange.IntRange.fromJson(json.get("count"));
		return new CountCriterion.Conditions(id, count, entityPredicate);
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final NumberRange.IntRange count;

		public Conditions(Identifier id, NumberRange.IntRange count, EntityPredicate.Extended entityPredicate) {
			super(id, entityPredicate);
			this.count = count;
		}

		public static CountCriterion.Conditions create(Identifier id, NumberRange.IntRange count, EntityPredicate.Extended entityPredicate) {
			return new CountCriterion.Conditions(id, count, entityPredicate);
		}

		public boolean matches(int count) {
			return this.count.test(count);
		}

		@Override
		public JsonObject toJson(AdvancementEntityPredicateSerializer serializer) {
			JsonObject json = new JsonObject();
			json.add("count", this.count.toJson());
			return json;
		}
	}
}
