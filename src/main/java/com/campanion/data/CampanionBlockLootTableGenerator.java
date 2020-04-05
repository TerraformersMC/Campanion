package com.campanion.data;

import com.campanion.Campanion;
import com.campanion.block.CampanionBlocks;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantLootTableRange;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionConsumingBuilder;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.item.EnchantmentPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CampanionBlockLootTableGenerator implements Consumer<BiConsumer<Identifier, LootTable.Builder>> {
	private static final LootCondition.Builder NEEDS_SILK_TOUCH;
	private static final LootCondition.Builder DOESNT_NEED_SILK_TOUCH;
	private static final LootCondition.Builder NEEDS_SHEARS;
	private static final LootCondition.Builder NEEDS_SILK_TOUCH_SHEARS;
	private static final LootCondition.Builder DOESNT_NEED_SILK_TOUCH_SHEARS;
	private static final Set<Item> ALWAYS_DROPPED_FROM_EXPLOSION;
	private static final float[] SAPLING_DROP_CHANCES_FROM_LEAVES;
	private static final float[] JUNGLE_SAPLING_DROP_CHANCES_FROM_LEAVES;

	static {
		NEEDS_SILK_TOUCH = MatchToolLootCondition.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, NumberRange.IntRange.atLeast(1))));
		DOESNT_NEED_SILK_TOUCH = NEEDS_SILK_TOUCH.invert();
		NEEDS_SHEARS = MatchToolLootCondition.builder(ItemPredicate.Builder.create().item(Items.SHEARS));
		NEEDS_SILK_TOUCH_SHEARS = NEEDS_SHEARS.withCondition(NEEDS_SILK_TOUCH);
		DOESNT_NEED_SILK_TOUCH_SHEARS = NEEDS_SILK_TOUCH_SHEARS.invert();
		ALWAYS_DROPPED_FROM_EXPLOSION = Stream.of(Blocks.DRAGON_EGG, Blocks.BEACON, Blocks.CONDUIT, Blocks.SKELETON_SKULL, Blocks.WITHER_SKELETON_SKULL, Blocks.PLAYER_HEAD, Blocks.ZOMBIE_HEAD, Blocks.CREEPER_HEAD, Blocks.DRAGON_HEAD, Blocks.SHULKER_BOX, Blocks.BLACK_SHULKER_BOX, Blocks.BLUE_SHULKER_BOX, Blocks.BROWN_SHULKER_BOX, Blocks.CYAN_SHULKER_BOX, Blocks.GRAY_SHULKER_BOX, Blocks.GREEN_SHULKER_BOX, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.LIME_SHULKER_BOX, Blocks.MAGENTA_SHULKER_BOX, Blocks.ORANGE_SHULKER_BOX, Blocks.PINK_SHULKER_BOX, Blocks.PURPLE_SHULKER_BOX, Blocks.RED_SHULKER_BOX, Blocks.WHITE_SHULKER_BOX, Blocks.YELLOW_SHULKER_BOX).map(ItemConvertible::asItem).collect(ImmutableSet.toImmutableSet());
		SAPLING_DROP_CHANCES_FROM_LEAVES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
		JUNGLE_SAPLING_DROP_CHANCES_FROM_LEAVES = new float[]{0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F};
	}

	private final Map<Identifier, LootTable.Builder> lootTables = Maps.newHashMap();

	private static <T> T addSurvivesExplosionLootCondition(ItemConvertible itemConvertible, LootConditionConsumingBuilder<T> lootConditionConsumingBuilder) {
		return !ALWAYS_DROPPED_FROM_EXPLOSION.contains(itemConvertible.asItem()) ? lootConditionConsumingBuilder.withCondition(SurvivesExplosionLootCondition.builder()) : lootConditionConsumingBuilder.getThis();
	}

	private static LootTable.Builder create(ItemConvertible itemConvertible) {
		return LootTable.builder().withPool(addSurvivesExplosionLootCondition(itemConvertible, LootPool.builder().withRolls(ConstantLootTableRange.create(1)).withEntry(ItemEntry.builder(itemConvertible))));
	}

	@Override
	public void accept(BiConsumer<Identifier, LootTable.Builder> biConsumer) {
		Set<Block> dropsNothing = Sets.newHashSet(CampanionBlocks.ROPE_BRIDGE_PLANKS);
		for (Block block : CampanionBlocks.getBlocks().values()) {
			if (!dropsNothing.contains(block)) {
				registerForSelfDrop(block);
			}
		}

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
