package fr.arthurbambou.fdlink.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public enum ConfigUpgrader {

    // version: -1 -> 0
    PRE_VERSIONED_TO_V0((jsonObject) -> {
        if (!jsonObject.has("token")) jsonObject.addProperty("token", "");

        if (!jsonObject.has("chatChannels")) jsonObject.add("chatChannels", new JsonArray());
        if (!jsonObject.get("chatChannels").isJsonArray()) {
            jsonObject.remove("chatChannels");
            jsonObject.add("chatChannels", new JsonArray());
        }

        if (!jsonObject.has("logChannels")) jsonObject.add("logChannels", new JsonArray());
        if (!jsonObject.get("logChannels").isJsonArray()) {
            jsonObject.remove("logChannels");
            jsonObject.add("logChannels", new JsonArray());
        }

        if (!jsonObject.has("discordToMinecraft")) jsonObject.add("discordToMinecraft", new JsonObject());
        JsonObject discordToMinecraft = jsonObject.getAsJsonObject("discordToMinecraft");
        if (!discordToMinecraft.has("message")) discordToMinecraft.addProperty("message", "[%player] %message");
        if (!discordToMinecraft.has("commandPrefix")) discordToMinecraft.addProperty("commandPrefix", "!");
        if (!discordToMinecraft.has("pingLongVersion")) discordToMinecraft.addProperty("pingLongVersion", false);

        if (!jsonObject.has("minecraftToDiscord")) jsonObject.add("minecraftToDiscord", new JsonObject());
        JsonObject minecraftToDiscord = jsonObject.getAsJsonObject("minecraftToDiscord");
        if (!minecraftToDiscord.has("general")) minecraftToDiscord.add("general", new JsonObject());
        if (!minecraftToDiscord.getAsJsonObject("general").has("enableDebugLogs")) minecraftToDiscord.getAsJsonObject("general").addProperty("enableDebugLogs", false);

        if (!minecraftToDiscord.has("messages")) minecraftToDiscord.add("messages", new JsonObject());
        JsonObject message = minecraftToDiscord.getAsJsonObject("messages");
        if (!message.has("serverStarting")) message.addProperty("serverStarting", "Server is starting!");
        if (!message.has("serverStarted")) message.addProperty("serverStarted", "Server Started.");
        if (!message.has("serverStopped")) message.addProperty("serverStopped", "Server Stopped.");
        if (!message.has("serverStopping")) message.addProperty("serverStopping", "Server is stopping!");

        String fieldName = "playerMessage";
        String fieldValue = "<%player> %message";
        if (!message.has(fieldName)) message.add(fieldName, new JsonObject());
        if (!message.getAsJsonObject(fieldName).has("customMessage")) message.getAsJsonObject(fieldName).addProperty("customMessage", fieldValue);
        if (!message.getAsJsonObject(fieldName).has("useCustomMessage")) message.getAsJsonObject(fieldName).addProperty("useCustomMessage", true);

        fieldName = "playerJoined";
        fieldValue = "%player joined the game";
        if (!message.has(fieldName)) message.add(fieldName, new JsonObject());
        if (!message.getAsJsonObject(fieldName).has("customMessage")) message.getAsJsonObject(fieldName).addProperty("customMessage", fieldValue);
        if (!message.getAsJsonObject(fieldName).has("useCustomMessage")) message.getAsJsonObject(fieldName).addProperty("useCustomMessage", true);

        fieldName = "playerJoinedRenamed";
        fieldValue = "%new (formerly known as %old) joined the game";
        if (!message.has(fieldName)) message.add(fieldName, new JsonObject());
        if (!message.getAsJsonObject(fieldName).has("customMessage")) message.getAsJsonObject(fieldName).addProperty("customMessage", fieldValue);
        if (!message.getAsJsonObject(fieldName).has("useCustomMessage")) message.getAsJsonObject(fieldName).addProperty("useCustomMessage", true);

        fieldName = "playerLeft";
        fieldValue = "%player left the game";
        if (!message.has(fieldName)) message.add(fieldName, new JsonObject());
        if (!message.getAsJsonObject(fieldName).has("customMessage")) message.getAsJsonObject(fieldName).addProperty("customMessage", fieldValue);
        if (!message.getAsJsonObject(fieldName).has("useCustomMessage")) message.getAsJsonObject(fieldName).addProperty("useCustomMessage", true);

        fieldName = "advancementTask";
        fieldValue = "%player has made the advancement %advancement";
        if (!message.has(fieldName)) message.add(fieldName, new JsonObject());
        if (!message.getAsJsonObject(fieldName).has("customMessage")) message.getAsJsonObject(fieldName).addProperty("customMessage", fieldValue);
        if (!message.getAsJsonObject(fieldName).has("useCustomMessage")) message.getAsJsonObject(fieldName).addProperty("useCustomMessage", true);

        fieldName = "advancementChallenge";
        fieldValue = "%player has completed the challenge %advancement";
        if (!message.has(fieldName)) message.add(fieldName, new JsonObject());
        if (!message.getAsJsonObject(fieldName).has("customMessage")) message.getAsJsonObject(fieldName).addProperty("customMessage", fieldValue);
        if (!message.getAsJsonObject(fieldName).has("useCustomMessage")) message.getAsJsonObject(fieldName).addProperty("useCustomMessage", true);

        fieldName = "advancementGoal";
        fieldValue = "%player has reached the goal %advancement";
        if (!message.has(fieldName)) message.add(fieldName, new JsonObject());
        if (!message.getAsJsonObject(fieldName).has("customMessage")) message.getAsJsonObject(fieldName).addProperty("customMessage", fieldValue);
        if (!message.getAsJsonObject(fieldName).has("useCustomMessage")) message.getAsJsonObject(fieldName).addProperty("useCustomMessage", true);

        if (!message.has("deathMsgPrefix")) message.addProperty("deathMsgPrefix", "");
        if (!message.has("deathMsgPostfix")) message.addProperty("deathMsgPostfix", "");

        if (!minecraftToDiscord.has("chatChannels")) minecraftToDiscord.add("chatChannels", new JsonObject());
        JsonObject chatChannels = minecraftToDiscord.getAsJsonObject("chatChannels");
        if (!chatChannels.has("commandPrefix")) chatChannels.addProperty("commandPrefix", "-");
        if (!chatChannels.has("allowDiscordCommands")) chatChannels.addProperty("allowDiscordCommands", false);
        if (!chatChannels.has("serverStartingMessage")) chatChannels.addProperty("serverStartingMessage", true);
        if (!chatChannels.has("serverStartMessage")) chatChannels.addProperty("serverStartMessage", true);
        if (!chatChannels.has("serverStopMessage")) chatChannels.addProperty("serverStopMessage", true);
        if (!chatChannels.has("serverStoppingMessage")) chatChannels.addProperty("serverStoppingMessage", true);
        if (!chatChannels.has("customChannelDescription")) chatChannels.addProperty("customChannelDescription", false);
        if (!chatChannels.has("minecraftToDiscordTag")) chatChannels.addProperty("minecraftToDiscordTag", false);
        if (!chatChannels.has("minecraftToDiscordDiscriminator")) chatChannels.addProperty("minecraftToDiscordDiscriminator", false);
        if (!chatChannels.has("playerMessages")) chatChannels.addProperty("playerMessages", true);
        if (!chatChannels.has("joinAndLeaveMessages")) chatChannels.addProperty("joinAndLeaveMessages", true);
        if (!chatChannels.has("advancementMessages")) chatChannels.addProperty("advancementMessages", true);
        if (!chatChannels.has("challengeMessages")) chatChannels.addProperty("challengeMessages", true);
        if (!chatChannels.has("goalMessages")) chatChannels.addProperty("goalMessages", true);
        if (!chatChannels.has("deathMessages")) chatChannels.addProperty("deathMessages", true);
        if (!chatChannels.has("sendMeCommand")) chatChannels.addProperty("sendMeCommand", true);
        if (!chatChannels.has("sendSayCommand")) chatChannels.addProperty("sendSayCommand", true);
        if (!chatChannels.has("adminMessages")) chatChannels.addProperty("adminMessages", false);

        if (!minecraftToDiscord.has("logChannels")) minecraftToDiscord.add("logChannels", new JsonObject());
        chatChannels = minecraftToDiscord.getAsJsonObject("logChannels");
        if (!chatChannels.has("commandPrefix")) chatChannels.addProperty("commandPrefix", "-");
        if (!chatChannels.has("allowDiscordCommands")) chatChannels.addProperty("allowDiscordCommands", false);
        if (!chatChannels.has("serverStartingMessage")) chatChannels.addProperty("serverStartingMessage", true);
        if (!chatChannels.has("serverStartMessage")) chatChannels.addProperty("serverStartMessage", true);
        if (!chatChannels.has("serverStopMessage")) chatChannels.addProperty("serverStopMessage", true);
        if (!chatChannels.has("serverStoppingMessage")) chatChannels.addProperty("serverStoppingMessage", true);
        if (!chatChannels.has("customChannelDescription")) chatChannels.addProperty("customChannelDescription", false);
        if (!chatChannels.has("minecraftToDiscordTag")) chatChannels.addProperty("minecraftToDiscordTag", false);
        if (!chatChannels.has("minecraftToDiscordDiscriminator")) chatChannels.addProperty("minecraftToDiscordDiscriminator", false);
        if (!chatChannels.has("playerMessages")) chatChannels.addProperty("playerMessages", true);
        if (!chatChannels.has("joinAndLeaveMessages")) chatChannels.addProperty("joinAndLeaveMessages", true);
        if (!chatChannels.has("advancementMessages")) chatChannels.addProperty("advancementMessages", true);
        if (!chatChannels.has("challengeMessages")) chatChannels.addProperty("challengeMessages", true);
        if (!chatChannels.has("goalMessages")) chatChannels.addProperty("goalMessages", true);
        if (!chatChannels.has("deathMessages")) chatChannels.addProperty("deathMessages", true);
        if (!chatChannels.has("sendMeCommand")) chatChannels.addProperty("sendMeCommand", true);
        if (!chatChannels.has("sendSayCommand")) chatChannels.addProperty("sendSayCommand", true);
        if (!chatChannels.has("adminMessages")) chatChannels.addProperty("adminMessages", false);

        if (!jsonObject.has("emojiMap")) jsonObject.add("emojiMap", new JsonArray());
        if (!jsonObject.get("emojiMap").isJsonArray()) {
            jsonObject.remove("emojiMap");
            jsonObject.add("emojiMap", new JsonArray());
        }

        if (!jsonObject.has("ignoreBots")) jsonObject.addProperty("ignoreBots", true);

        jsonObject.remove("version");
        jsonObject.addProperty("version", 0);

        return jsonObject;
    }),
    V0_TO_V1(jsonObject -> {
        if (!jsonObject.has("minecraftToDiscord")) jsonObject.add("minecraftToDiscord", new JsonObject());
        JsonObject minecraftToDiscord = jsonObject.getAsJsonObject("minecraftToDiscord");

        if (!minecraftToDiscord.has("messages")) minecraftToDiscord.add("messages", new JsonObject());
        JsonObject message = minecraftToDiscord.getAsJsonObject("messages");

        message.addProperty("channelDescription", "Playercount : %playercount/%maxplayercount,\n Uptime : %uptime");

        jsonObject.remove("version");
        jsonObject.addProperty("version", 1);

        return jsonObject;
    });

    private Upgrader upgrader;

    ConfigUpgrader(Upgrader upgrader) {
        this.upgrader = upgrader;
    }

    public JsonObject upgrade(JsonObject object) {
        return this.upgrader.upgrade(object);
    }

    protected interface Upgrader {
        JsonObject upgrade(JsonObject jsonObject);
    }
}
