package com.terraformersmc.campanion.advancement.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class CountCriterion extends SimpleCriterionTrigger<CountCriterion.Conditions> {
	public final ResourceLocation id;

	public CountCriterion(ResourceLocation id) {
		this.id = id;
	}

	@Override
	public @NotNull ResourceLocation getId() {
		return id;
	}

	public void trigger(ServerPlayer player, int count) {
		this.trigger(player, (conditions) -> conditions.matches(count));
	}

	@Override
	protected @NotNull Conditions createInstance(JsonObject json, @NotNull ContextAwarePredicate predicate, @NotNull DeserializationContext deserializer) {
		MinMaxBounds.Ints count = MinMaxBounds.Ints.fromJson(json.get("count"));
		return new CountCriterion.Conditions(id, count, predicate);
	}

	public static class Conditions extends AbstractCriterionTriggerInstance {
		private final MinMaxBounds.Ints count;

		public Conditions(ResourceLocation id, MinMaxBounds.Ints count, ContextAwarePredicate entityPredicate) {
			super(id, entityPredicate);
			this.count = count;
		}

		public static CountCriterion.Conditions create(ResourceLocation id, MinMaxBounds.Ints count, ContextAwarePredicate predicate) {
			return new CountCriterion.Conditions(id, count, predicate);
		}

		public boolean matches(int count) {
			return this.count.matches(count);
		}

		@Override
		public @NotNull JsonObject serializeToJson(@NotNull SerializationContext serializer) {
			JsonObject json = new JsonObject();
			json.add("count", this.count.serializeToJson());
			return json;
		}
	}
}
