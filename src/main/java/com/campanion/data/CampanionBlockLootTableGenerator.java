package com.campanion.data;

import com.campanion.Campanion;
import com.campanion.block.CampanionBlocks;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.condition.LootConditionConsumingBuilder;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CampanionBlockLootTableGenerator implements Consumer<BiConsumer<Identifier, LootTable.Builder>> {

	private final Map<Identifier, LootTable.Builder> lootTables = Maps.newHashMap();

	private static <T> T addSurvivesExplosionLootCondition(LootConditionConsumingBuilder<T> lootConditionConsumingBuilder) {
		return lootConditionConsumingBuilder.withCondition(SurvivesExplosionLootCondition.builder());
	}

	private static LootTable.Builder create(ItemConvertible itemConvertible) {
		return LootTable.builder().withPool(addSurvivesExplosionLootCondition(LootPool.builder().withRolls(ConstantLootTableRange.create(1)).withEntry(ItemEntry.builder(itemConvertible))));
	}

	@Override
	public void accept(BiConsumer<Identifier, LootTable.Builder> biConsumer) {
		this.registerForSelfDrop(CampanionBlocks.ROPE_BRIDGE_POST);
		this.registerForSelfDrop(CampanionBlocks.TENT_POLE);

		this.registerForSelfDrop(CampanionBlocks.WHITE_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.ORANGE_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.MAGENTA_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.YELLOW_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.LIME_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.PINK_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.GRAY_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.CYAN_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.PURPLE_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.BLUE_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.BROWN_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.GREEN_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.RED_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.BLACK_LAWN_CHAIR);
		this.registerForSelfDrop(CampanionBlocks.LEATHER_TANNER);


		this.registerTentDrop(CampanionBlocks.WHITE_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.ORANGE_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.MAGENTA_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.LIGHT_BLUE_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.YELLOW_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.LIME_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.PINK_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.GRAY_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.LIGHT_GRAY_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.CYAN_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.PURPLE_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.BLUE_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.BROWN_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.GREEN_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.RED_TENT_SIDE);
		this.registerTentDrop(CampanionBlocks.BLACK_TENT_SIDE);

		this.registerTentDrop(CampanionBlocks.WHITE_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.ORANGE_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.MAGENTA_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.LIGHT_BLUE_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.YELLOW_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.LIME_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.PINK_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.GRAY_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.LIGHT_GRAY_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.CYAN_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.PURPLE_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.BLUE_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.BROWN_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.GREEN_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.RED_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.BLACK_TENT_TOP);

		this.registerTentDrop(CampanionBlocks.WHITE_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.ORANGE_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.MAGENTA_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.LIGHT_BLUE_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.YELLOW_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.LIME_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.PINK_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.GRAY_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.LIGHT_GRAY_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.CYAN_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.PURPLE_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.BLUE_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.BROWN_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.GREEN_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.RED_TOPPED_TENT_POLE);
		this.registerTentDrop(CampanionBlocks.BLACK_TOPPED_TENT_POLE);

		this.registerTentDrop(CampanionBlocks.WHITE_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.ORANGE_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.MAGENTA_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.LIGHT_BLUE_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.YELLOW_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.LIME_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.PINK_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.GRAY_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.LIGHT_GRAY_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.CYAN_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.PURPLE_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.BLUE_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.BROWN_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.GREEN_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.RED_FLAT_TENT_TOP);
		this.registerTentDrop(CampanionBlocks.BLACK_FLAT_TENT_TOP);

		Set<Identifier> set = Sets.newHashSet();

		for (Block block : Registry.BLOCK) {
			if (!Registry.BLOCK.getId(block).getNamespace().equals(Campanion.MOD_ID)) {
				continue;
			}
			Identifier lootTableId = block.getDropTableId();
			if (lootTableId != LootTables.EMPTY && set.add(lootTableId)) {
				LootTable.Builder builder5 = this.lootTables.remove(lootTableId);
				if (builder5 == null) {
					throw new IllegalStateException(String.format("Missing loot table '%s' for '%s'", lootTableId, Registry.BLOCK.getId(block)));
				}

				biConsumer.accept(lootTableId, builder5);
			}
		}

		if (!this.lootTables.isEmpty()) {
			throw new IllegalStateException("Created block loot tables for non-blocks: " + this.lootTables.keySet());
		}
	}

	public void registerTentDrop(Block block) {
		this.register(block, create(Items.STRING).withFunction(SetCountLootFunction.builder(UniformLootTableRange.between(5F, 5F))));
	}

	public void register(Block block, ItemConvertible loot) {
		this.register(block, create(loot));
	}

	public void registerForSelfDrop(Block block) {
		this.register(block, block);
	}

	private void register(Block block, LootTable.Builder builder) {
		this.lootTables.put(block.getDropTableId(), builder);
	}
}
