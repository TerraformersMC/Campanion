package com.terraformersmc.campanion.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.terraformersmc.campanion.block.CampanionBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CampanionLootTablesGenerator extends LootTableProvider {

	public CampanionLootTablesGenerator(DataGenerator generator) {
		super(generator);
	}

	@Override
	protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
		return ImmutableList.of(Pair.of(CampanionBlockLoot::new, LootContextParamSets.BLOCK));
	}

	public static class CampanionBlockLoot extends BlockLoot {
		@Override
		protected void addTables() {
			this.dropSelf(CampanionBlocks.ROPE_BRIDGE_POST);
			this.dropSelf(CampanionBlocks.TENT_POLE);

			this.dropSelf(CampanionBlocks.WHITE_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.ORANGE_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.MAGENTA_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.LIGHT_BLUE_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.YELLOW_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.LIME_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.PINK_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.GRAY_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.LIGHT_GRAY_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.CYAN_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.PURPLE_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.BLUE_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.BROWN_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.GREEN_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.RED_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.BLACK_LAWN_CHAIR);
			this.dropSelf(CampanionBlocks.LEATHER_TANNER);

			this.addTentPartDrop(CampanionBlocks.WHITE_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.ORANGE_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.MAGENTA_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.LIGHT_BLUE_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.YELLOW_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.LIME_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.PINK_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.GRAY_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.LIGHT_GRAY_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.CYAN_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.PURPLE_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.BLUE_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.BROWN_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.GREEN_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.RED_TENT_SIDE);
			this.addTentPartDrop(CampanionBlocks.BLACK_TENT_SIDE);

			this.addTentPartDrop(CampanionBlocks.WHITE_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.ORANGE_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.MAGENTA_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.LIGHT_BLUE_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.YELLOW_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.LIME_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.PINK_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.GRAY_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.LIGHT_GRAY_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.CYAN_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.PURPLE_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.BLUE_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.BROWN_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.GREEN_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.RED_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.BLACK_TENT_TOP);

			this.addTentPartDrop(CampanionBlocks.WHITE_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.ORANGE_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.MAGENTA_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.LIGHT_BLUE_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.YELLOW_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.LIME_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.PINK_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.GRAY_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.LIGHT_GRAY_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.CYAN_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.PURPLE_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.BLUE_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.BROWN_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.GREEN_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.RED_TOPPED_TENT_POLE);
			this.addTentPartDrop(CampanionBlocks.BLACK_TOPPED_TENT_POLE);

			this.addTentPartDrop(CampanionBlocks.WHITE_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.ORANGE_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.MAGENTA_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.LIGHT_BLUE_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.YELLOW_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.LIME_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.PINK_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.GRAY_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.LIGHT_GRAY_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.CYAN_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.PURPLE_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.BLUE_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.BROWN_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.GREEN_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.RED_FLAT_TENT_TOP);
			this.addTentPartDrop(CampanionBlocks.BLACK_FLAT_TENT_TOP);
		}

		public void addTentPartDrop(Block block) {
			this.add(block, b -> createSingleItemTable(block).apply(SetItemCountFunction.setCount(ConstantValue.exactly(5F))));
		}

		@Override
		protected Iterable<Block> getKnownBlocks() {
			return CampanionBlocks.getBlocks().values();
		}
	}
}
