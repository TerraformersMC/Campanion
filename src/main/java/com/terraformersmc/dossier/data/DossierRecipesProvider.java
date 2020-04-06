package com.terraformersmc.dossier.data;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.criterion.ImpossibleCriterion;
import net.minecraft.data.DataCache;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public class DossierRecipesProvider implements DataProvider, Consumer<Consumer<Consumer<RecipeJsonProvider>>> {

	private Consumer<Consumer<RecipeJsonProvider>> onGenerate;

	private static final Logger LOGGER = LogManager.getLogger();
	private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();
	private final DataGenerator generator;

	public DossierRecipesProvider(DataGenerator generator) {
		this.generator = generator;
	}

	@Override
	public void run(DataCache cache) {
		Path path = this.generator.getOutput();
		Set<Identifier> set = Sets.newHashSet();
		this.generate((provider) -> {
			if (!set.add(provider.getRecipeId())) {
				throw new IllegalStateException("Duplicate recipe " + provider.getRecipeId());
			} else {
				this.saveRecipe(cache, provider.toJson(), path.resolve("data/" + provider.getRecipeId().getNamespace() + "/recipes/" + provider.getRecipeId().getPath() + ".json"));
				JsonObject advancementJson = provider.toAdvancementJson();
				if (advancementJson != null) {
					this.saveRecipeAdvancement(cache, advancementJson, path.resolve("data/" + provider.getRecipeId().getNamespace() + "/advancements/" + provider.getAdvancementId().getPath() + ".json"));
				}

			}
		});
		this.saveRecipeAdvancement(cache, Advancement.Task.create().criterion("impossible", new ImpossibleCriterion.Conditions()).toJson(), path.resolve("data/minecraft/advancements/recipes/root.json"));
	}

	@SuppressWarnings("UnstableApiUsage")
	private void saveRecipe(DataCache cache, JsonObject jsonObject, Path path) {
		try {
			String json = GSON.toJson(jsonObject);
			String string2 = SHA1.hashUnencodedChars(json).toString();
			if (!Objects.equals(cache.getOldSha1(path), string2) || !Files.exists(path)) {
				Files.createDirectories(path.getParent());
				BufferedWriter bufferedWriter = Files.newBufferedWriter(path);
				Throwable throwable = null;

				try {
					bufferedWriter.write(json);
				} catch (Throwable var17) {
					throwable = var17;
					throw var17;
				} finally {
					if (throwable != null) {
						try {
							bufferedWriter.close();
						} catch (Throwable var16) {
							throwable.addSuppressed(var16);
						}
					} else {
						bufferedWriter.close();
					}

				}
			}

			cache.updateSha1(path, string2);
		} catch (IOException var19) {
			LOGGER.error("Couldn't save recipe {}", path, var19);
		}

	}

	@SuppressWarnings("UnstableApiUsage")
	private void saveRecipeAdvancement(DataCache dataCache, JsonObject jsonObject, Path path) {
		try {
			String json = GSON.toJson(jsonObject);
			String hash = SHA1.hashUnencodedChars(json).toString();
			if (!Objects.equals(dataCache.getOldSha1(path), hash) || !Files.exists(path)) {
				Files.createDirectories(path.getParent());
				BufferedWriter writer = Files.newBufferedWriter(path);
				Throwable throwable = null;

				try {
					writer.write(json);
				} catch (Throwable throwable1) {
					throwable = throwable1;
					throw throwable1;
				} finally {
					if (throwable != null) {
						try {
							writer.close();
						} catch (Throwable throwable2) {
							throwable.addSuppressed(throwable2);
						}
					} else {
						writer.close();
					}

				}
			}

			dataCache.updateSha1(path, hash);
		} catch (IOException e) {
			LOGGER.error("Couldn't save recipe advancement {}", path, e);
		}

	}

	private void generate(Consumer<RecipeJsonProvider> consumer) {
		this.onGenerate.accept(consumer);
	}

	@Override
	public String getName() {
		return "Dossier Recipes";
	}

	@Override
	public void accept(Consumer<Consumer<RecipeJsonProvider>> onGenerate) {
		this.onGenerate = onGenerate;
	}
}
