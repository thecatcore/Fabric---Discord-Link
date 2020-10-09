package fr.arthurbambou.fdlink.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ConfigHandler {

    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private static final int CONFIG_VERSION = 0;

    private static final File OLD_CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDirectory(), "fdlink.json");

    private static final File CONFIG_FOLDER = new File(FabricLoader.getInstance().getConfigDirectory(), "fdlink");

    public static ConfigHolder getConfig() {
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
            if (new File(CONFIG_FOLDER, "fdlink.json").exists()) {
                try (InputStreamReader fileReader = new InputStreamReader(new FileInputStream(new File(CONFIG_FOLDER, "fdlink.json")), StandardCharsets.UTF_8)) {
                    JsonObject jsonObject = gson.fromJson(fileReader, JsonObject.class);
                    configHolder = parseConfig(jsonObject);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Config defaultConfig = new Config();
                saveConfig((JsonObject)gson.toJsonTree(defaultConfig));
                configHolder = new ConfigHolder(defaultConfig, null);
            }
        }
        return configHolder;
    }

    private static ConfigHolder parseConfig(JsonObject jsonObject) {
        int version = jsonObject.get("version").getAsInt();
        int i = version + 1;
        while (version < CONFIG_VERSION) {
            jsonObject = ConfigUpgrader.values()[i].upgrade(jsonObject);
            version = jsonObject.get("version").getAsInt();
        }
        saveConfig(jsonObject);
        Config config = new Config();

        List<String> chatChannels = new ArrayList<>();
        for (JsonElement jsonElement : readList(
                jsonObject.getAsJsonArray("chatChannels"))) {
            if (jsonElement.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();

                Number number = jsonPrimitive.getAsNumber();
                chatChannels.add(String.valueOf(number.longValue()));
            }
        }
        config.chatChannels = chatChannels;

        List<String> logChannels = new ArrayList<>();
        for (JsonElement jsonElement : readList(
                jsonObject.getAsJsonArray("logChannels"))) {
            if (jsonElement.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();

                Number number = jsonPrimitive.getAsNumber();
                logChannels.add(String.valueOf(number.longValue()));
            }
        }
        config.logChannels = logChannels;

        List<Config.EmojiEntry> emojiEntryList = new ArrayList<>();
        for (JsonElement jsonElement : readList(
                jsonObject.getAsJsonArray("emojiMap"))) {
            if (jsonElement.isJsonObject()) {
                JsonObject emojiEntryJson = jsonElement.getAsJsonObject();
                if (!emojiEntryJson.has("name")) emojiEntryJson.addProperty("name", "");
                if (!emojiEntryJson.has("id")) emojiEntryJson.addProperty("id", "");

                String name = emojiEntryJson.get("name").getAsString();
                String id = emojiEntryJson.get("id").getAsString();

                emojiEntryList.add(new Config.EmojiEntry(name, id));
            }
        }
        config.emojiMap = emojiEntryList;

        JsonObject discordToMinecraft = jsonObject.getAsJsonObject("discordToMinecraft");
        config.discordToMinecraft = gson.fromJson(discordToMinecraft.toString(), Config.DiscordToMinecraft.class);

        JsonObject minecraftToDiscord = jsonObject.getAsJsonObject("minecraftToDiscord");
        config.minecraftToDiscord = gson.fromJson(minecraftToDiscord.toString(), Config.MinecraftToDiscord.class);


        String token = jsonObject.get("token").getAsString();

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

    private static void saveConfig(JsonObject jsonObject) {
        if (!jsonObject.has("token")) jsonObject.addProperty("token", "");
        if (!jsonObject.has("version")) jsonObject.addProperty("version", CONFIG_VERSION);

        if (!CONFIG_FOLDER.exists()) CONFIG_FOLDER.mkdirs();
        try (FileWriter fileWriter = new FileWriter(new File(CONFIG_FOLDER, "fdlink.json"))) {
            fileWriter.write(gson.toJson(jsonObject));
        } catch (IOException e) {
            e.printStackTrace();
        }
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
