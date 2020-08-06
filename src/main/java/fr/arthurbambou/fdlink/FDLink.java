package fr.arthurbambou.fdlink;

import com.google.gson.*;
import fr.arthurbambou.fdlink.discordstuff.DiscordBot;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
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
				config = DefaultConfig;
				JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);
				if (jsonObject.get("token") != null) {
					config.token = jsonObject.get("token").getAsString();
				}
				if (jsonObject.get("chatChannels") != null) {
					config.chatChannels = gson.fromJson(jsonObject.get("chatChannels").toString(), List.class);
				}
				if (jsonObject.get("logChannels") != null) {
					config.logChannels = gson.fromJson(jsonObject.get("logChannels").toString(), List.class);
				}
				if (jsonObject.get("discordToMinecraft") != null) {
					JsonObject jsonToMinecraft = jsonObject.getAsJsonObject("discordToMinecraft");
					if (jsonToMinecraft.get("message") != null) {
						config.discordToMinecraft.message = jsonToMinecraft.get("message").getAsString();
					}
					if (jsonToMinecraft.get("commandPrefix") != null) {
						config.discordToMinecraft.commandPrefix = jsonToMinecraft.get("commandPrefix").getAsString();
					}
					if (jsonToMinecraft.get("pingLongVersion") != null) {
						config.discordToMinecraft.pingLongVersion = jsonToMinecraft.get("pingLongVersion").getAsBoolean();
					}
				}
				if (jsonObject.get("minecraftToDiscord") != null) {
					JsonObject minecraftToDiscord = jsonObject.getAsJsonObject("minecraftToDiscord");
					if (minecraftToDiscord.get("general") != null) {
						JsonObject general = minecraftToDiscord.getAsJsonObject("general");
						if (general.get("enableDebugLogs") != null) {
							config.minecraftToDiscord.general.enableDebugLogs = general.get("enableDebugLogs").getAsBoolean();
						}
					}
					if (minecraftToDiscord.get("messages") != null) {
						JsonObject messages = minecraftToDiscord.getAsJsonObject("messages");
						if (messages.get("serverStarting") != null) {
							config.minecraftToDiscord.messages.serverStarting = messages.get("serverStarting").getAsString();
						}
						if (messages.get("serverStarted") != null) {
							config.minecraftToDiscord.messages.serverStarted = messages.get("serverStarted").getAsString();
						}
						if (messages.get("serverStopped") != null) {
							config.minecraftToDiscord.messages.serverStopped = messages.get("serverStopped").getAsString();
						}
						if (messages.get("serverStopping") != null) {
							config.minecraftToDiscord.messages.serverStopping = messages.get("serverStopping").getAsString();
						}
						readConfigMessage(config.minecraftToDiscord.messages.playerMessage, "playerMessage", messages);
						readConfigMessage(config.minecraftToDiscord.messages.playerJoined, "playerJoined", messages);
						readConfigMessage(config.minecraftToDiscord.messages.playerJoinedRenamed, "playerJoinedRenamed", messages);
						readConfigMessage(config.minecraftToDiscord.messages.playerLeft, "playerLeft", messages);
						readConfigMessage(config.minecraftToDiscord.messages.advancementTask, "advancementTask", messages);
						readConfigMessage(config.minecraftToDiscord.messages.advancementChallenge, "advancementChallenge", messages);
						readConfigMessage(config.minecraftToDiscord.messages.advancementGoal, "advancementGoal", messages);
						if (messages.get("deathMsgPrefix") != null) {
							config.minecraftToDiscord.messages.deathMsgPrefix = messages.get("deathMsgPrefix").getAsString();
						}
						if (messages.get("deathMsgPostfix") != null) {
							config.minecraftToDiscord.messages.deathMsgPostfix = messages.get("deathMsgPostfix").getAsString();
						}
					}
					if (minecraftToDiscord.get("chatChannels") != null) {
						config.minecraftToDiscord.chatChannels = gson.fromJson(minecraftToDiscord.get("chatChannels").toString(), Config.MinecraftToDiscordChatChannel.class);
					}
					if (minecraftToDiscord.get("logChannels") != null) {
						config.minecraftToDiscord.logChannels = gson.fromJson(minecraftToDiscord.get("logChannels").toString(), Config.MinecraftToDiscordLogChannel.class);
					}
				}
				if (jsonObject.get("emojiMap") != null) {
					Iterator<JsonElement> jsonArray = jsonObject.getAsJsonArray("emojiMap").iterator();
					List<Config.EmojiEntry> emojiEntries = new ArrayList<>();
					while (jsonArray.hasNext()) {
						emojiEntries.add(gson.fromJson(jsonArray.next().toString(), Config.EmojiEntry.class));
					}
					config.emojiMap = emojiEntries;
				}
				return saveConfig(config);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private static void readConfigMessage(Config.MinecraftToDiscordMessage.ConfigMessage configMessage, String name, JsonObject jsonObject) {
		if (jsonObject.get(name) != null) {
			if (jsonObject.get(name).isJsonObject()) {
				JsonObject jsonObject1 = jsonObject.getAsJsonObject(name);
				if (jsonObject1.get("customMessage") != null) {
					configMessage.customMessage = jsonObject1.get("customMessage").getAsString();
				}
				if (jsonObject1.get("useCustomMessage") != null) {
					configMessage.useCustomMessage = jsonObject1.get("useCustomMessage").getAsBoolean();
				}
			} else {
				configMessage = new Config.MinecraftToDiscordMessage.ConfigMessage(jsonObject.get(name).getAsString());
			}
		}
	}

	public static class Config {
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

		public static class MinecraftToDiscordMessage {
			public ConfigMessage playerMessage = new ConfigMessage("<%player> %message");
			public String serverStarting = "Server is starting!";
			public String serverStarted = "Server Started.";
			public String serverStopping = "Server is stopping!";
			public String serverStopped = "Server Stopped.";
			public ConfigMessage playerJoined = new ConfigMessage("%player joined the game");
			public ConfigMessage playerJoinedRenamed = new ConfigMessage("%new (formerly known as %old) joined the game");
			public ConfigMessage playerLeft = new ConfigMessage("%player left the game");
			public ConfigMessage advancementTask = new ConfigMessage("%player has made the advancement %advancement");
			public ConfigMessage advancementChallenge = new ConfigMessage("%player has completed the challenge %advancement");
			public ConfigMessage advancementGoal = new ConfigMessage("%player has reached the goal %advancement");
			public String deathMsgPrefix = "";
			public String deathMsgPostfix = "";

			public static class ConfigMessage {
				public String customMessage;
				public boolean useCustomMessage = true;

				protected ConfigMessage(String customMessage, boolean useCustomMessage) {
					this.customMessage = customMessage;
					this.useCustomMessage = useCustomMessage;
				}

				protected ConfigMessage(String customMessage) {
					this.customMessage = customMessage;
				}
			}
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
