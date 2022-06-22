package com.terraformersmc.campanion.advancement.criterion;

import com.google.gson.JsonObject;
import com.terraformersmc.campanion.Campanion;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.SerializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public class KilledWithStoneCriterion extends SimpleCriterionTrigger<KilledWithStoneCriterion.Conditions> {
	public static final ResourceLocation ID = new ResourceLocation(Campanion.MOD_ID, "killed_with_stone");

	@Override
	public ResourceLocation getId() {
		return ID;
	}

	public void trigger(ServerPlayer player, Entity entity, int count) {
		this.trigger(player, (conditions) -> conditions.matches(player, entity, count));
	}

	@Override
	protected Conditions createInstance(JsonObject json, EntityPredicate.Composite entityPredicate, DeserializationContext deserializer) {
		return new KilledWithStoneCriterion.Conditions(EntityPredicate.fromJson(json.get("entity")), MinMaxBounds.Ints.fromJson(json.get("skips")), entityPredicate);
	}

	public static class Conditions extends AbstractCriterionTriggerInstance {
		private final EntityPredicate entity;
		private final MinMaxBounds.Ints skips;

		public Conditions(EntityPredicate entity, MinMaxBounds.Ints skips, EntityPredicate.Composite entityPredicate) {
			super(KilledWithStoneCriterion.ID, entityPredicate);
			this.entity = entity;
			this.skips = skips;
		}

		public static KilledWithStoneCriterion.Conditions create(EntityPredicate entity, MinMaxBounds.Ints count, EntityPredicate.Composite entityPredicate) {
			return new KilledWithStoneCriterion.Conditions(entity, count, entityPredicate);
		}

		public boolean matches(ServerPlayer player, Entity entity, int count) {
			return this.skips.matches(count) && this.entity.matches(player, entity);
		}

		@Override
		public JsonObject serializeToJson(SerializationContext serializer) {
			JsonObject json = new JsonObject();
			json.add("entity", this.entity.serializeToJson());
			json.add("skips", this.skips.serializeToJson());
			return json;
		}
	}
}
