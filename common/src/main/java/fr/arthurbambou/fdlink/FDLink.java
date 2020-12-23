package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.config.manager.ConfigHandler;
import fr.arthurbambou.fdlink.discordstuff.DiscordBot;
import fr.arthurbambou.fdlink.discordstuff.DiscordWebhook;
import fr.arthurbambou.fdlink.discordstuff.MessageSender;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FDLink implements DedicatedServerModInitializer {

	private static DiscordBot messageReceiver;
	private static MessageSender messageSender;
	public static Logger LOGGER = LogManager.getLogger("FDlink");
	private static boolean loaded = false;

	@Override
	public void onInitializeServer() {
		initialize();
	}

	private static void initialize() {
		ConfigHandler.ConfigHolder configHolder = ConfigHandler.getConfig();
		messageReceiver = new DiscordBot(configHolder.getToken(), configHolder.getConfig());
		if (configHolder.getConfig().mainConfig.webhookURL.isEmpty()) {
			messageSender = messageReceiver;
		} else {
			LOGGER.info("Found a webhook URL, using Webhook instead of Bot to send message.");
			messageSender = new DiscordWebhook(configHolder.getConfig().mainConfig.webhookURL, configHolder.getConfig(), messageReceiver);
		}
		loaded = true;
	}

	public static void regenConfig() {
		ConfigHandler.ConfigHolder configHolder = ConfigHandler.getConfig();
		messageReceiver = new DiscordBot(configHolder.getToken(), configHolder.getConfig());
		if (configHolder.getConfig().mainConfig.webhookURL.isEmpty()) {
			messageSender = messageReceiver;
		} else {
			messageSender = new DiscordWebhook(configHolder.getConfig().mainConfig.webhookURL, configHolder.getConfig(), messageReceiver);
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
