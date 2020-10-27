package fr.arthurbambou.fdlink.config.manager;

import com.google.gson.*;
import fr.arthurbambou.fdlink.config.Config;
import fr.arthurbambou.fdlink.config.MainConfig;
import fr.arthurbambou.fdlink.config.MessageConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfigHandler {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final int CONFIG_VERSION = 2;

    private static File OLD_CONFIG_FILE;

    private static File CONFIG_FOLDER;
    private static File NEW_MAIN_FILE;
    private static File MESSAGE_CONFIG;

    private static void initPaths() {
        File gameConfig = null;
        try {
            gameConfig = FabricLoader.getInstance().getConfigDir().toFile();
        } catch (NoSuchMethodError e) {
            gameConfig = FabricLoader.getInstance().getConfigDirectory();
        }

        File GAME_CONFIG_FOLDER = gameConfig;
        System.out.println(GAME_CONFIG_FOLDER.toPath().toAbsolutePath().toString());

        OLD_CONFIG_FILE = new File(GAME_CONFIG_FOLDER, "fdlink.json");

        CONFIG_FOLDER = new File(GAME_CONFIG_FOLDER, "fdlink");
        NEW_MAIN_FILE = new File(CONFIG_FOLDER, "fdlink.json");
        MESSAGE_CONFIG = new File(CONFIG_FOLDER, "messages.json");
    }

    public static ConfigHolder getConfig() {
        initPaths();

        ConfigHolder configHolder = null;
        if (OLD_CONFIG_FILE.exists()) {
            try (InputStreamReader fileReader = new InputStreamReader(new FileInputStream(OLD_CONFIG_FILE), StandardCharsets.UTF_8)) {
                JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);
                jsonObject.addProperty("version", -1);
                configHolder = parseConfig(jsonObject);
            } catch (IOException e) {
                e.printStackTrace();
            }
            OLD_CONFIG_FILE.delete();
        } else {
            if (NEW_MAIN_FILE.exists()) {
                try (InputStreamReader mainFileReader = new InputStreamReader(new FileInputStream(NEW_MAIN_FILE), StandardCharsets.UTF_8)) {
                    JsonObject jsonObject = new JsonObject();
                    JsonObject mainObject = gson.fromJson(mainFileReader, JsonObject.class);
                    int version = mainObject.get("version").getAsInt();
                    if (version < 2) configHolder = parseConfig(mainObject);
                    else {
                        mainObject.remove("version");
                        jsonObject.addProperty("version", version);
                        jsonObject.add("main", mainObject);
                        if (MESSAGE_CONFIG.exists()) {
                            try (InputStreamReader messageFileReader = new InputStreamReader(new FileInputStream(MESSAGE_CONFIG), StandardCharsets.UTF_8)) {
                                jsonObject.add("messages", gson.fromJson(messageFileReader, JsonObject.class));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            JsonObject messageConfig = (JsonObject) gson.toJsonTree(new MessageConfig());
                            try (FileWriter fileWriter = new FileWriter(MESSAGE_CONFIG)) {
                                fileWriter.write(gson.toJson(messageConfig));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            jsonObject.add("messages", messageConfig);
                        }

                        configHolder = parseConfig(jsonObject);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Config config = new Config();

                if (MESSAGE_CONFIG.exists()) {
                    try (InputStreamReader messageFileReader = new InputStreamReader(new FileInputStream(MESSAGE_CONFIG), StandardCharsets.UTF_8)) {
                        config.messageConfig = gson.fromJson(messageFileReader, MessageConfig.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                configHolder = new ConfigHolder(config, null);
            }
        }
        if (configHolder != null) {
            saveConfig(configHolder);
        }
        return configHolder;
    }

    private static ConfigHolder parseConfig(JsonObject jsonObject) {
        int version = jsonObject.get("version").getAsInt();
        while (version < CONFIG_VERSION) {
            int i = version + 1;
            System.out.println("version: " + version);
            jsonObject = ConfigUpgrader.values()[i].upgrade(jsonObject);
            version = jsonObject.get("version").getAsInt();
        }
        Config config = new Config();

        JsonObject mainObject = jsonObject.getAsJsonObject("main");

        List<String> chatChannels = new ArrayList<>();
        for (JsonElement jsonElement : readList(
                mainObject.getAsJsonArray("chatChannels"))) {
            if (jsonElement.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();

                Number number = jsonPrimitive.getAsNumber();
                chatChannels.add(String.valueOf(number.longValue()));
            }
        }
        config.mainConfig.chatChannels = chatChannels;

        List<String> logChannels = new ArrayList<>();
        for (JsonElement jsonElement : readList(
                mainObject.getAsJsonArray("logChannels"))) {
            if (jsonElement.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();

                Number number = jsonPrimitive.getAsNumber();
                logChannels.add(String.valueOf(number.longValue()));
            }
        }
        config.mainConfig.logChannels = logChannels;

        List<MainConfig.EmojiEntry> emojiEntryList = new ArrayList<>();
        for (JsonElement jsonElement : readList(
                mainObject.getAsJsonArray("emojiMap"))) {
            if (jsonElement.isJsonObject()) {
                JsonObject emojiEntryJson = jsonElement.getAsJsonObject();
                if (!emojiEntryJson.has("name")) emojiEntryJson.addProperty("name", "");
                if (!emojiEntryJson.has("id")) emojiEntryJson.addProperty("id", "");

                String name = emojiEntryJson.get("name").getAsString();
                String id = emojiEntryJson.get("id").getAsString();

                emojiEntryList.add(new MainConfig.EmojiEntry(name, id));
            }
        }
        config.mainConfig.emojiMap = emojiEntryList;

        JsonObject discordToMinecraft = mainObject.getAsJsonObject("discordToMinecraft");
        config.mainConfig.discordToMinecraft = gson.fromJson(discordToMinecraft.toString(), MainConfig.DiscordToMinecraft.class);

        JsonObject minecraftToDiscord = mainObject.getAsJsonObject("minecraftToDiscord");
        config.mainConfig.minecraftToDiscord = gson.fromJson(minecraftToDiscord.toString(), MainConfig.MinecraftToDiscord.class);

        JsonObject messageObject = jsonObject.getAsJsonObject("messages");
        config.messageConfig = gson.fromJson(messageObject.toString(), MessageConfig.class);


        String token = mainObject.get("token").getAsString();

        return new ConfigHolder(config, token);
    }

    private static List<JsonElement> readList(JsonArray jsonArray) {
        List<JsonElement> jsonObjectList = new ArrayList<>();

        Iterator<JsonElement> iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            jsonObjectList.add(iterator.next());
        }

        return jsonObjectList;
    }

    private static void saveMainConfig(JsonObject jsonObject) {
        if (!jsonObject.has("token")) jsonObject.addProperty("token", "");
        if (!jsonObject.has("version")) jsonObject.addProperty("version", CONFIG_VERSION);

        if (!CONFIG_FOLDER.exists()) CONFIG_FOLDER.mkdirs();
        try (FileWriter fileWriter = new FileWriter(NEW_MAIN_FILE)) {
            fileWriter.write(gson.toJson(jsonObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveMessageConfig(JsonObject jsonObject) {
        if (!CONFIG_FOLDER.exists()) CONFIG_FOLDER.mkdirs();

        try (FileWriter fileWriter = new FileWriter(MESSAGE_CONFIG)) {
            fileWriter.write(gson.toJson(jsonObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveConfig(ConfigHolder configHolder) {
        JsonObject mainObject = (JsonObject) gson.toJsonTree(configHolder.config.mainConfig);
        mainObject.addProperty("version", CONFIG_VERSION);
        if (configHolder.token != null) mainObject.addProperty("token", configHolder.token);

        saveMainConfig(mainObject);
        saveMessageConfig((JsonObject) gson.toJsonTree(configHolder.config.messageConfig));
    }

    public static class ConfigHolder {
        private Config config;
        private String token;

        protected ConfigHolder(Config config, String token) {
            this.config = config;
            this.token = token;
        }

        public Config getConfig() {
            return config;
        }

        public String getToken() {
            return token;
        }
    }
}
