package com.terraformersmc.campanion.advancement.criterion;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.NumberRange;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class CountCriterion extends AbstractCriterion<CountCriterion.Conditions> {
	public final Identifier id;

	public CountCriterion(Identifier id) {
		this.id = id;
	}

	public Identifier getId() {
		return id;
	}

	public CountCriterion.Conditions conditionsFromJson(JsonObject json, JsonDeserializationContext context) {
		NumberRange.IntRange count = NumberRange.IntRange.fromJson(json.get("count"));
		return new CountCriterion.Conditions(id, count);
	}

	public void trigger(ServerPlayerEntity player, int count) {
		this.test(player.getAdvancementTracker(), (conditions) -> conditions.matches(count));
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final NumberRange.IntRange count;

		public Conditions(Identifier id, NumberRange.IntRange count) {
			super(id);
			this.count = count;
		}

		public static CountCriterion.Conditions create(Identifier id, NumberRange.IntRange count) {
			return new CountCriterion.Conditions(id, count);
		}

		public boolean matches(int count) {
			return this.count.test(count);
		}

		public JsonElement toJson() {
			JsonObject json = new JsonObject();
			json.add("count", this.count.toJson());
			return json;
		}
	}
}
