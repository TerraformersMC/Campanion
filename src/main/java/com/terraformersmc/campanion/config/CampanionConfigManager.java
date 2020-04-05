package com.terraformersmc.campanion.config;

import com.terraformersmc.campanion.Campanion;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;

public class CampanionConfigManager {
	private static File file;
	private static CampanionConfig config;

	private static void prepareBiomeConfigFile() {
		if (file != null) {
			return;
		}
		file = new File(FabricLoader.getInstance().getConfigDirectory(), Campanion.MOD_ID + ".json");
	}

	public static CampanionConfig initializeConfig() {
		if (config != null) {
			return config;
		}

		config = new CampanionConfig();
		load();

		return config;
	}

	private static void load() {
		prepareBiomeConfigFile();

		try {
			if (!file.exists()) {
				save();
			}
			if (file.exists()) {
				BufferedReader br = new BufferedReader(new FileReader(file));

				config = Campanion.GSON.fromJson(br, CampanionConfig.class);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't load Campanion configuration file; reverting to defaults");
			e.printStackTrace();
		}
	}

	public static void save() {
		prepareBiomeConfigFile();

		String jsonString = Campanion.GSON.toJson(config);

		try (FileWriter fileWriter = new FileWriter(file)) {
			fileWriter.write(jsonString);
		} catch (IOException e) {
			System.err.println("Couldn't save Campanion configuration file");
			e.printStackTrace();
		}
	}

	public static CampanionConfig getConfig() {
		return config;
	}
}
