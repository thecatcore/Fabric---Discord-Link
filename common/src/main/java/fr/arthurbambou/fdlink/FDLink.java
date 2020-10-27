package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.config.manager.ConfigHandler;
import fr.arthurbambou.fdlink.discordstuff.DiscordBot;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FDLink implements DedicatedServerModInitializer {

	private static DiscordBot discordBot;
	public static Logger LOGGER = LogManager.getLogger("FDlink");
	private static boolean loaded = false;

	@Override
	public void onInitializeServer() {
		initialize();
	}

	private static void initialize() {
		ConfigHandler.ConfigHolder configHolder = ConfigHandler.getConfig();
		discordBot = new DiscordBot(configHolder.getToken(), configHolder.getConfig());
		loaded = true;
	}

	public static void regenConfig() {
		ConfigHandler.ConfigHolder configHolder = ConfigHandler.getConfig();
		discordBot = new DiscordBot(configHolder.getToken(), configHolder.getConfig());
	}

	public static DiscordBot getDiscordBot() {
		if (!loaded) initialize();
		return discordBot;
	}
}
