package com.terraformersmc.campanion.data;

import com.terraformersmc.campanion.advancement.criterion.CampanionCriteria;
import com.terraformersmc.campanion.advancement.criterion.CountCriterion;
import com.terraformersmc.campanion.advancement.criterion.KilledWithStoneCriterion;
import com.terraformersmc.campanion.item.CampanionItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class CampanionAdvancementsGenerator extends AdvancementProvider {
	public CampanionAdvancementsGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
		super(output, registries, existingFileHelper, List.of(new CampanionAdventureAdvancements(), new CampanionHusbandryAdvancements()));
	}

	public static class CampanionAdventureAdvancements implements AdvancementGenerator{
		public static final ResourceLocation ADVENTURE_ROOT = ResourceLocation.withDefaultNamespace("adventure/root");
		public static final ResourceLocation SLEEP_IN_BED = ResourceLocation.withDefaultNamespace("adventure/sleep_in_bed");

		@Override
		public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> pWriter, ExistingFileHelper existingFileHelper) {
			Advancement.Builder.advancement()
				.parent(ADVENTURE_ROOT)
				.display(CampanionItems.SKIPPING_STONE, Component.translatable("advancements.campanion.adventure.skip_stone.title"), Component.translatable("advancements.campanion.adventure.skip_stone.description"), (ResourceLocation)null, AdvancementType.TASK, true, true, false)
				.addCriterion("skips", CampanionCriteria.STONE_SKIPS.createCriterion(new CountCriterion.Conditions(Optional.empty(), MinMaxBounds.Ints.atLeast(6))))
				.save(pWriter, "campanion:adventure/skip_stone");
			Advancement.Builder.advancement()
				.parent(ADVENTURE_ROOT)
				.display(CampanionItems.SKIPPING_STONE, Component.translatable("advancements.campanion.adventure.kill_with_stone_after_skips.title"), Component.translatable("advancements.campanion.adventure.kill_with_stone_after_skips.description"), (ResourceLocation)null, AdvancementType.CHALLENGE, true, true, false)
				.addCriterion("skips", CampanionCriteria.KILLED_WITH_STONE.createCriterion(new KilledWithStoneCriterion.Conditions(Optional.empty(), Optional.empty(), MinMaxBounds.Ints.atLeast(6))))
				.save(pWriter, "campanion:adventure/kill_with_stone_after_skips");
			Advancement.Builder.advancement()
				.parent(SLEEP_IN_BED)
				.display(CampanionItems.SLEEPING_BAG, Component.translatable("advancements.campanion.adventure.sleep_in_sleeping_bag.title"), Component.translatable("advancements.campanion.adventure.sleep_in_sleeping_bag.description"), (ResourceLocation)null, AdvancementType.TASK, true, true, false)
				.addCriterion("slept_in_sleeping_bag", CampanionCriteria.SLEPT_IN_SLEEPING_BAG.createCriterion(new PlayerTrigger.TriggerInstance(Optional.empty())))
				.save(pWriter, "campanion:adventure/sleep_in_sleeping_bag");
		}
	}

	public static class CampanionHusbandryAdvancements implements AdvancementGenerator{
		public static final ResourceLocation HUSBANDRY_ROOT = ResourceLocation.withDefaultNamespace("husbandry/root");

		@Override
		public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> pWriter, ExistingFileHelper existingFileHelper) {
			AdvancementHolder cookMarshmallow = Advancement.Builder.advancement()
				.parent(HUSBANDRY_ROOT)
				.display(CampanionItems.COOKED_MARSHMALLOW_ON_A_STICK, Component.translatable("advancements.campanion.husbandry.cook_marshmallow.title"), Component.translatable("advancements.campanion.husbandry.cook_marshmallow.description"), (ResourceLocation) null, AdvancementType.TASK, true, true, false)
				.addCriterion("cooked_marshmallow", InventoryChangeTrigger.TriggerInstance.hasItems(CampanionItems.COOKED_MARSHMALLOW))
				.save(pWriter, "campanion:husbandry/cook_marshmallow");
			Advancement.Builder.advancement()
				.parent(cookMarshmallow)
				.display(CampanionItems.SMORE, Component.translatable("advancements.campanion.husbandry.eat_smore.title"), Component.translatable("advancements.campanion.husbandry.eat_smore.description"), (ResourceLocation)null, AdvancementType.TASK, true, true, false)
				.addCriterion("eat_smore", ConsumeItemTrigger.TriggerInstance.usedItem(CampanionItems.SMORE))
				.save(pWriter, "campanion:husbandry/eat_smore");

		}
	}
}
