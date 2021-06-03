package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.api.discord.MessageSender;
import fr.arthurbambou.fdlink.config.manager.ConfigHandler;
import fr.arthurbambou.fdlink.discordstuff.DiscordBot;
import fr.arthurbambou.fdlink.discordstuff.DiscordWebhook;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FDLink implements DedicatedServerModInitializer {

	private static DiscordBot messageReceiver;
	private static MessageSender messageSender;
	public static Logger LOGGER = LogManager.getLogger("FDLink");
	public static Logger MESSAGE_LOGGER = LogManager.getLogger("Discord->Minecraft");
	private static boolean loaded = false;

	@Override
	public void onInitializeServer() {
		initialize();
	}

	private static void initialize() {
		ConfigHandler.ConfigHolder configHolder = ConfigHandler.getConfig();
		if (configHolder.getConfig().mainConfig.webhook.url.isEmpty()) {
			messageSender = new DiscordBot(configHolder.getToken(), configHolder.getConfig());
		} else {
			LOGGER.info("Found a webhook URL, using Webhook instead of Bot to send message.");
			if (configHolder.getConfig().mainConfig.chatChannels.isEmpty() && configHolder.getConfig().mainConfig.logChannels.isEmpty()) {
				LOGGER.warn("Unable to find any channel id, only Minecraft->Discord will work, add a channel id to the config if this wasn't intended.");
			}
			messageSender = new DiscordWebhook(configHolder.getConfig().mainConfig.webhook.url, configHolder.getConfig(), messageReceiver);
		}
		loaded = true;
	}

	public static void regenConfig() {
		ConfigHandler.ConfigHolder configHolder = ConfigHandler.getConfig();
		messageReceiver = new DiscordBot(configHolder.getToken(), configHolder.getConfig());
		if (configHolder.getConfig().mainConfig.webhook.url.isEmpty()) {
			messageSender = messageReceiver;
		} else {
			messageSender = new DiscordWebhook(configHolder.getConfig().mainConfig.webhook.url, configHolder.getConfig(), messageReceiver);
		}
	}

	public static MessageSender getMessageSender() {
		if (!loaded) initialize();
		return messageSender;
	}

	public static DiscordBot getMessageReceiver() {
		if (!loaded) initialize();
		return messageReceiver;
	}
}
