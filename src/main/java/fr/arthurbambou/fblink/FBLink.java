package fr.arthurbambou.fblink;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.arthurbambou.fblink.discordstuff.DiscordBot;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FBLink implements DedicatedServerModInitializer {

	private static ConfigManager configManager;
	private static DiscordBot discordBot;

	@Override
	public void onInitializeServer() {
		configManager = new ConfigManager();
		discordBot = new DiscordBot(configManager.init(), configManager.config);
	}

	public static void regenConfig() {
		configManager.regenConfig();
		discordBot = new DiscordBot(configManager.init(), configManager.config);
	}

	protected class ConfigManager {
		private File CONFIG_PATH = FabricLoader.INSTANCE.getConfigDirectory();

		private final Gson DEFAULT_GSON = new GsonBuilder().setPrettyPrinting().create();

		private File configFile;
		private String configFilename = "fblink";
		private Gson gson = DEFAULT_GSON;
		private Config DefaultConfig = new Config();

		private Config config;

		protected String init() {
			configFile = new File(CONFIG_PATH, configFilename + (configFilename.endsWith(".json") ? "" : ".json"));
			if (!configFile.exists()) {
				return saveConfig(DefaultConfig);
			}
			return loadConfig();
		}

		private String saveConfig(Config instanceConfig) {
			try (FileWriter fileWriter = new FileWriter(configFile)) {
				fileWriter.write(gson.toJson(instanceConfig));
				config = instanceConfig;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return DefaultConfig.token;
		}

		public String regenConfig() {
			try (FileWriter fileWriter = new FileWriter(configFile)) {
				fileWriter.write(gson.toJson(DefaultConfig));
				config = DefaultConfig;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return DefaultConfig.token;
		}

		public String loadConfig() {
			try (FileReader fileReader = new FileReader(configFile)) {
				config = gson.fromJson(fileReader, Config.class);
				return config.token;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public class Config {
		private String token = "";
		public String discordToMinecraft = "[%s] %s";
	}
}
