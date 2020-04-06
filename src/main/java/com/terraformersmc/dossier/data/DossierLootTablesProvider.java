package com.terraformersmc.dossier.data;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.datafixers.util.Pair;
import com.terraformersmc.dossier.util.DefaultedHashMap;
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

public class DossierLootTablesProvider implements DataProvider, Consumer<Runnable> {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
	private final DataGenerator generator;
	public final Map<LootContextType, List<Pair<Identifier, LootTable.Builder>>> lootTables = new DefaultedHashMap<>(Lists.newArrayList());
	protected final List<Pair<Supplier<Consumer<BiConsumer<Identifier, LootTable.Builder>>>, LootContextType>> lootTypeGenerators;
	private Runnable onRun;

	public DossierLootTablesProvider(DataGenerator generator) {
		this.generator = generator;
		this.lootTypeGenerators = Lists.newArrayList();
	}

	private static Path getOutput(Path rootOutput, Identifier lootTableId) {
		return rootOutput.resolve("data/" + lootTableId.getNamespace() + "/loot_tables/" + lootTableId.getPath() + ".json");
	}

	@Override
	public void run(DataCache cache) {
		Path path = this.generator.getOutput();
		Map<Identifier, LootTable> map = Maps.newHashMap();
		this.onRun.run();
		this.lootTables.forEach((type, tables) -> tables.forEach(table -> {
			Identifier id = table.getFirst();
			LootTable.Builder builder = table.getSecond();
			if (map.put(id, builder.withType(type).create()) != null) {
				throw new IllegalStateException("Duplicate loot table " + id);
			}
		}));
		LootContextType genericType = LootContextTypes.GENERIC;
		LootTableReporter lootTableReporter = new LootTableReporter(genericType, (id) -> null, map::get);
		Set<Identifier> missingBuiltIns = Sets.difference(LootTables.getAll(), map.keySet());

		for (Identifier id : missingBuiltIns) {
			lootTableReporter.report("Missing built-in table: " + id);
		}

		map.forEach((identifierx, lootTable) -> {
			Path path2 = getOutput(path, identifierx);

			try {
				DataProvider.writeToPath(GSON, cache, LootManager.toJson(lootTable), path2);
			} catch (IOException var6) {
				LOGGER.error("Couldn't save loot table {}", path2, var6);
			}

		});
	}

	@Override
	public String getName() {
		return "Dossier Loot Tables";
	}

	@Override
	public void accept(Runnable onRun) {
		this.onRun = onRun;
	}
}
