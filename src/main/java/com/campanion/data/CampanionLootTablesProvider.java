package com.campanion.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.loot.LootManager;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class CampanionLootTablesProvider implements DataProvider {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private final DataGenerator root;
	private final List<Pair<Supplier<Consumer<BiConsumer<Identifier, LootTable.Builder>>>, LootContextType>> lootTypeGenerators;

	public CampanionLootTablesProvider(DataGenerator dataGenerator) {
		this.lootTypeGenerators = ImmutableList.of(Pair.of(CampanionBlockLootTableGenerator::new, LootContextTypes.BLOCK));
		this.root = dataGenerator;
	}

	private static Path getOutput(Path rootOutput, Identifier lootTableId) {
		return rootOutput.resolve("data/" + lootTableId.getNamespace() + "/loot_tables/" + lootTableId.getPath() + ".json");
	}

	@Override
	public void run(DataCache dataCache) {
		Path path = this.root.getOutput();
		Map<Identifier, LootTable> map = Maps.newHashMap();
		this.lootTypeGenerators.forEach((pair) -> pair.getFirst().get().accept((id, builder) -> {
			if (map.put(id, builder.withType(pair.getSecond()).create()) != null) {
				throw new IllegalStateException("Duplicate loot table " + id);
			}
		}));
		LootContextType var10002 = LootContextTypes.GENERIC;
		LootTableReporter lootTableReporter = new LootTableReporter(var10002, (id) -> null, map::get);
		Set<Identifier> set = Sets.difference(LootTables.getAll(), map.keySet());

		for (Identifier id : set) {
			lootTableReporter.report("Missing built-in table: " + id);
		}

		map.forEach((identifierx, lootTable) -> {
			Path path2 = getOutput(path, identifierx);

			try {
				DataProvider.writeToPath(GSON, dataCache, LootManager.toJson(lootTable), path2);
			} catch (IOException var6) {
				LOGGER.error("Couldn't save loot table {}", path2, var6);
			}

		});
	}

	@Override
	public String getName() {
		return "Campanion Loot Tables";
	}
}
