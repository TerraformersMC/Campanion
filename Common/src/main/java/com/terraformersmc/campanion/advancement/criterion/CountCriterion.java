package com.terraformersmc.campanion.advancement.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class CountCriterion extends SimpleCriterionTrigger<CountCriterion.Conditions> {
	public final ResourceLocation id;

	public CountCriterion(ResourceLocation id) {
		this.id = id;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	public void trigger(ServerPlayer player, int count) {
		this.trigger(player, (conditions) -> conditions.matches(count));
	}

	@Override
	protected Conditions createInstance(JsonObject json, EntityPredicate.Composite entityPredicate, DeserializationContext deserializer) {
		MinMaxBounds.Ints count = MinMaxBounds.Ints.fromJson(json.get("count"));
		return new CountCriterion.Conditions(id, count, entityPredicate);
	}

	public static class Conditions extends AbstractCriterionTriggerInstance {
		private final MinMaxBounds.Ints count;

		public Conditions(ResourceLocation id, MinMaxBounds.Ints count, EntityPredicate.Composite entityPredicate) {
			super(id, entityPredicate);
			this.count = count;
		}

		public static CountCriterion.Conditions create(ResourceLocation id, MinMaxBounds.Ints count, EntityPredicate.Composite entityPredicate) {
			return new CountCriterion.Conditions(id, count, entityPredicate);
		}

		public boolean matches(int count) {
			return this.count.matches(count);
		}

		@Override
		public JsonObject serializeToJson(SerializationContext serializer) {
			JsonObject json = new JsonObject();
			json.add("count", this.count.serializeToJson());
			return json;
		}
	}
}
