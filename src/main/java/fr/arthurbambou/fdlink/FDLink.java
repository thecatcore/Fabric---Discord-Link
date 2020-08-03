package fr.arthurbambou.fdlink;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.arthurbambou.fdlink.discordstuff.DiscordBot;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FDLink implements DedicatedServerModInitializer {

	private static ConfigManager configManager;
	private static DiscordBot discordBot;
	public static Logger LOGGER = LogManager.getLogger("FDlink");

	@Override
	public void onInitializeServer() {
		configManager = new ConfigManager();
		discordBot = new DiscordBot(configManager.init(), configManager.config);
		configManager.config.token = "";
	}

	public static void regenConfig() {
		configManager.regenConfig();
		discordBot = new DiscordBot(configManager.init(), configManager.config);
		configManager.config.token = "";
	}

	public static DiscordBot getDiscordBot() {
		return discordBot;
	}

	protected class ConfigManager {
		private File CONFIG_PATH = FabricLoader.getInstance().getConfigDir().toFile();

		private final Gson DEFAULT_GSON = new GsonBuilder().setPrettyPrinting().create();

		private File configFile;
		private String configFilename = "fdlink";
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
			if (!instanceConfig.token.equals(DefaultConfig.token)) {
				return instanceConfig.token;
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
			try (InputStreamReader fileReader = new InputStreamReader(new FileInputStream(configFile), StandardCharsets.UTF_8)) {
				config = gson.fromJson(fileReader, Config.class);
				if (config.token == null) {
					config.token = DefaultConfig.token;
				}
				if (config.chatChannels == null) {
					config.chatChannels = DefaultConfig.chatChannels;
				}
				if (config.logChannels == null) {
					config.logChannels = DefaultConfig.logChannels;
				}
				if (config.discordToMinecraft == null) {
					config.discordToMinecraft = DefaultConfig.discordToMinecraft;
				}
				if (config.minecraftToDiscord == null) {
					config.minecraftToDiscord = DefaultConfig.minecraftToDiscord;
				}
				if (config.minecraftToDiscord.general == null) {
					config.minecraftToDiscord.general = DefaultConfig.minecraftToDiscord.general;
				}
				if (config.minecraftToDiscord.messages == null) {
					config.minecraftToDiscord.messages = DefaultConfig.minecraftToDiscord.messages;
				}
				if (config.minecraftToDiscord.messages.serverStarted == null) {
					config.minecraftToDiscord.messages.serverStarted = DefaultConfig.minecraftToDiscord.messages.serverStarted;
				}
				if (config.minecraftToDiscord.messages.serverStarting == null) {
					config.minecraftToDiscord.messages.serverStarting = DefaultConfig.minecraftToDiscord.messages.serverStarting;
				}
				if (config.minecraftToDiscord.messages.serverStopping == null) {
					config.minecraftToDiscord.messages.serverStopping = DefaultConfig.minecraftToDiscord.messages.serverStopping;
				}
				if (config.minecraftToDiscord.messages.serverStopped == null) {
					config.minecraftToDiscord.messages.serverStopped = DefaultConfig.minecraftToDiscord.messages.serverStopped;
				}
				if (config.minecraftToDiscord.messages.playerJoined == null) {
					config.minecraftToDiscord.messages.playerJoined = DefaultConfig.minecraftToDiscord.messages.playerJoined;
				}
				if (config.minecraftToDiscord.messages.playerLeft == null) {
					config.minecraftToDiscord.messages.playerLeft = DefaultConfig.minecraftToDiscord.messages.playerLeft;
				}
				if (config.minecraftToDiscord.messages.advancementTask == null) {
					config.minecraftToDiscord.messages.advancementTask = DefaultConfig.minecraftToDiscord.messages.advancementTask;
				}
				if (config.minecraftToDiscord.messages.advancementChallenge == null) {
					config.minecraftToDiscord.messages.advancementChallenge = DefaultConfig.minecraftToDiscord.messages.advancementChallenge;
				}
				if (config.minecraftToDiscord.messages.advancementGoal == null) {
					config.minecraftToDiscord.messages.advancementGoal = DefaultConfig.minecraftToDiscord.messages.advancementGoal;
				}
				if (config.minecraftToDiscord.messages.playerJoinedRenamed == null) {
					config.minecraftToDiscord.messages.playerJoinedRenamed = DefaultConfig.minecraftToDiscord.messages.playerJoinedRenamed;
				}
				if (config.minecraftToDiscord.messages.deathMsgPrefix == null) {
					config.minecraftToDiscord.messages.deathMsgPrefix = DefaultConfig.minecraftToDiscord.messages.deathMsgPrefix;
				}
				if (config.minecraftToDiscord.messages.deathMsgPostfix == null) {
					config.minecraftToDiscord.messages.deathMsgPostfix = DefaultConfig.minecraftToDiscord.messages.deathMsgPostfix;
				}
				if (config.minecraftToDiscord.chatChannels == null) {
					config.minecraftToDiscord.chatChannels = DefaultConfig.minecraftToDiscord.chatChannels;
				}
				if (config.minecraftToDiscord.logChannels == null) {
					config.minecraftToDiscord.logChannels = DefaultConfig.minecraftToDiscord.logChannels;
				}
				if (config.emojiMap == null) {
					config.emojiMap = DefaultConfig.emojiMap;
				}
				return saveConfig(config);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	public class Config {
		private String token = "";
		public List<String> chatChannels = new ArrayList<String>();
		public List<String> logChannels = new ArrayList<String>();
		public boolean ignoreBots = true;
		public MinecraftToDiscord minecraftToDiscord = new MinecraftToDiscord();
		public DiscordToMinecraft discordToMinecraft = new DiscordToMinecraft();
		public List<EmojiEntry> emojiMap = new ArrayList<>();

		public Config() {
			emojiMap.add(new EmojiEntry("example_name", ":example_id:22222222"));
			emojiMap.add(new EmojiEntry("example_name2", ":example_id2:22222222"));
		}

		public class EmojiEntry {
			public String name;
			public String id;

			public EmojiEntry(String name, String id) {
				this.name = name;
				this.id = id;
			}
		}

		public class MinecraftToDiscord {
			public MinecraftToDiscordGeneral general = new MinecraftToDiscordGeneral();
			public MinecraftToDiscordMessage messages = new MinecraftToDiscordMessage();
			public MinecraftToDiscordChatChannel chatChannels = new MinecraftToDiscordChatChannel();
			public MinecraftToDiscordLogChannel logChannels = new MinecraftToDiscordLogChannel();
		}

		public class MinecraftToDiscordGeneral {
			public boolean enableDebugLogs = false;
		}

		public class MinecraftToDiscordMessage {
			public String serverStarting = "Server is starting!";
			public String serverStarted = "Server Started.";
			public String serverStopping = "Server is stopping!";
			public String serverStopped = "Server Stopped.";
			public String playerJoined = "%player joined the game";
			public String playerJoinedRenamed = "%new (formerly known as %old) joined the game";
			public String playerLeft = "%player left the game";
			public String advancementTask = "%player has made the advancement %advancement";
			public String advancementChallenge = "%player has completed the challenge %advancement";
			public String advancementGoal = "%player has reached the goal %advancement";
			public String deathMsgPrefix = "";
			public String deathMsgPostfix = "";
		}
    
		public class MinecraftToDiscordChatChannel {
      		public String commandPrefix = "-";
			public boolean allowDiscordCommands = false;
			public boolean serverStartingMessage = true;
			public boolean serverStartMessage = true;
			public boolean serverStopMessage = true;
			public boolean serverStoppingMessage = true;
			public boolean customChannelDescription = false;
			public boolean minecraftToDiscordTag = false;
			public boolean minecraftToDiscordDiscriminator = false;
			public boolean playerMessages = true;
			public boolean joinAndLeaveMessages = true;
			public boolean advancementMessages = true;
			public boolean challengeMessages = true;
			public boolean goalMessages = true;
			public boolean deathMessages = true;
			public boolean sendMeCommand = true;
			public boolean sendSayCommand = true;
			public boolean adminMessages = false;
		}

		public class MinecraftToDiscordLogChannel {
			public boolean serverStartingMessage = true;
			public boolean serverStartMessage = true;
			public boolean serverStopMessage = true;
			public boolean serverStoppingMessage = true;
			public boolean customChannelDescription = false;
			public boolean minecraftToDiscordTag = false;
			public boolean minecraftToDiscordDiscriminator = false;
			public boolean playerMessages = false;
			public boolean joinAndLeaveMessages = true;
			public boolean advancementMessages = false;
			public boolean challengeMessages = false;
			public boolean goalMessages = false;
			public boolean deathMessages = true;
			public boolean sendMeCommand = true;
			public boolean sendSayCommand = true;
			public boolean adminMessages = true;
		}

		public class DiscordToMinecraft {
			public boolean pingLongVersion = false;
			public String message = "[%player] %message";
			public String commandPrefix = "!";
		}
	}
}
