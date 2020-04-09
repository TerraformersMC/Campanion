package com.terraformersmc.campanion.advancement.criterion;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.terraformersmc.campanion.Campanion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.Entity;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class KilledWithStoneCriterion extends AbstractCriterion<KilledWithStoneCriterion.Conditions> {
	public static final Identifier ID = new Identifier(Campanion.MOD_ID, "killed_with_stone");

	@Override
	public Identifier getId() {
		return ID;
	}

	@Override
	public KilledWithStoneCriterion.Conditions conditionsFromJson(JsonObject json, JsonDeserializationContext context) {
		return new KilledWithStoneCriterion.Conditions(EntityPredicate.fromJson(json.get("entity")), NumberRange.IntRange.fromJson(json.get("skips")));
	}

	public void trigger(ServerPlayerEntity player, Entity entity, int count) {
		this.test(player.getAdvancementTracker(), (conditions) -> conditions.matches(player, entity, count));
	}

	public static class Conditions extends AbstractCriterionConditions {
		private final EntityPredicate entity;
		private final NumberRange.IntRange skips;

		public Conditions(EntityPredicate entity, NumberRange.IntRange skips) {
			super(KilledWithStoneCriterion.ID);
			this.entity = entity;
			this.skips = skips;
		}

		public static KilledWithStoneCriterion.Conditions create(EntityPredicate entity, NumberRange.IntRange count) {
			return new KilledWithStoneCriterion.Conditions(entity, count);
		}

		public boolean matches(ServerPlayerEntity player, Entity entity, int count) {
			return this.skips.test(count) && this.entity.test(player, entity);
		}

		@Override
		public JsonElement toJson() {
			JsonObject json = new JsonObject();
			json.add("entity", this.entity.toJson());
			json.add("skips", this.skips.toJson());
			return json;
		}
	}
}
