package com.campanion.data;

import com.campanion.Campanion;
import com.campanion.block.CampanionBlocks;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.LootConditionConsumingBuilder;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
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
		Set<Block> dropsNothing = Sets.newHashSet(CampanionBlocks.ROPE_BRIDGE_PLANKS);
		for (Block block : CampanionBlocks.getBlocks().values()) {
			if (!dropsNothing.contains(block)) {
				registerForSelfDrop(block);
			}
		}

		this.register(CampanionBlocks.WHITE_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.ORANGE_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.MAGENTA_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.LIGHT_BLUE_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.YELLOW_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.LIME_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.PINK_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.GRAY_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.LIGHT_GRAY_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.CYAN_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.PURPLE_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.BLUE_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.BROWN_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.GREEN_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.RED_TENT_SIDE, Items.STRING);
		this.register(CampanionBlocks.BLACK_TENT_SIDE, Items.STRING);

		this.register(CampanionBlocks.WHITE_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.ORANGE_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.MAGENTA_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.LIGHT_BLUE_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.YELLOW_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.LIME_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.PINK_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.GRAY_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.LIGHT_GRAY_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.CYAN_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.PURPLE_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.BLUE_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.BROWN_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.GREEN_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.RED_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.BLACK_TENT_TOP, Items.STRING);

		this.register(CampanionBlocks.WHITE_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.ORANGE_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.MAGENTA_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.LIGHT_BLUE_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.YELLOW_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.LIME_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.PINK_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.GRAY_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.LIGHT_GRAY_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.CYAN_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.PURPLE_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.BLUE_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.BROWN_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.GREEN_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.RED_TOPPED_TENT_POLE, Items.STRING);
		this.register(CampanionBlocks.BLACK_TOPPED_TENT_POLE, Items.STRING);

		this.register(CampanionBlocks.WHITE_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.ORANGE_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.MAGENTA_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.LIGHT_BLUE_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.YELLOW_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.LIME_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.PINK_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.GRAY_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.LIGHT_GRAY_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.CYAN_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.PURPLE_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.BLUE_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.BROWN_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.GREEN_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.RED_FLAT_TENT_TOP, Items.STRING);
		this.register(CampanionBlocks.BLACK_FLAT_TENT_TOP, Items.STRING);

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
