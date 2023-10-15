package com.terraformersmc.campanion.advancement.criterion;

import com.google.gson.JsonObject;
import com.terraformersmc.campanion.Campanion;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class KilledWithStoneCriterion extends SimpleCriterionTrigger<KilledWithStoneCriterion.Conditions> {
	public static final ResourceLocation ID = new ResourceLocation(Campanion.MOD_ID, "killed_with_stone");

	@Override
	public @NotNull ResourceLocation getId() {
		return ID;
	}

	public void trigger(ServerPlayer player, Entity entity, int count) {
		this.trigger(player, (conditions) -> conditions.matches(player, entity, count));
	}

	@Override
	protected @NotNull Conditions createInstance(JsonObject json, @NotNull ContextAwarePredicate predicate, @NotNull DeserializationContext deserializer) {
		return new KilledWithStoneCriterion.Conditions(EntityPredicate.fromJson(json.get("entity")), MinMaxBounds.Ints.fromJson(json.get("skips")), predicate);
	}

	public static class Conditions extends AbstractCriterionTriggerInstance {
		private final EntityPredicate entity;
		private final MinMaxBounds.Ints skips;

		public Conditions(EntityPredicate entity, MinMaxBounds.Ints skips, ContextAwarePredicate predicate) {
			super(KilledWithStoneCriterion.ID, predicate);
			this.entity = entity;
			this.skips = skips;
		}

		public static KilledWithStoneCriterion.Conditions create(EntityPredicate entity, MinMaxBounds.Ints count, ContextAwarePredicate predicate) {
			return new KilledWithStoneCriterion.Conditions(entity, count, predicate);
		}

		public boolean matches(ServerPlayer player, Entity entity, int count) {
			return this.skips.matches(count) && this.entity.matches(player, entity);
		}

		@Override
		public @NotNull JsonObject serializeToJson(@NotNull SerializationContext serializer) {
			JsonObject json = new JsonObject();
			json.add("entity", this.entity.serializeToJson());
			json.add("skips", this.skips.serializeToJson());
			return json;
		}
	}
}
