package fr.arthurbambou.fdlink;

import com.google.gson.*;
import fr.arthurbambou.fdlink.config.ConfigHandler;
import fr.arthurbambou.fdlink.discordstuff.DiscordBot;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
