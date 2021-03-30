package fr.arthurbambou.fdlink.config.manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public enum ConfigUpgrader {

    // version: -1 -> 0
    // config file moved from config/fdlink.json to config/fdlink/fdlink.json
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
        else if (message.get(fieldName).isJsonPrimitive()) {
            String msg = message.get(fieldName).getAsString();
            message.remove(fieldName);
            message.add(fieldName, new JsonObject());
            message.getAsJsonObject(fieldName).addProperty("cutomMessage", msg);
        }
        if (!message.getAsJsonObject(fieldName).has("customMessage")) message.getAsJsonObject(fieldName).addProperty("customMessage", fieldValue);
        if (!message.getAsJsonObject(fieldName).has("useCustomMessage")) message.getAsJsonObject(fieldName).addProperty("useCustomMessage", true);

        fieldName = "playerJoined";
        fieldValue = "%player joined the game";
        if (!message.has(fieldName)) message.add(fieldName, new JsonObject());
        else if (message.get(fieldName).isJsonPrimitive()) {
            String msg = message.get(fieldName).getAsString();
            message.remove(fieldName);
            message.add(fieldName, new JsonObject());
            message.getAsJsonObject(fieldName).addProperty("cutomMessage", msg);
        }
        if (!message.getAsJsonObject(fieldName).has("customMessage")) message.getAsJsonObject(fieldName).addProperty("customMessage", fieldValue);
        if (!message.getAsJsonObject(fieldName).has("useCustomMessage")) message.getAsJsonObject(fieldName).addProperty("useCustomMessage", true);

        fieldName = "playerJoinedRenamed";
        fieldValue = "%new (formerly known as %old) joined the game";
        if (!message.has(fieldName)) message.add(fieldName, new JsonObject());
        else if (message.get(fieldName).isJsonPrimitive()) {
            String msg = message.get(fieldName).getAsString();
            message.remove(fieldName);
            message.add(fieldName, new JsonObject());
            message.getAsJsonObject(fieldName).addProperty("cutomMessage", msg);
        }
        if (!message.getAsJsonObject(fieldName).has("customMessage")) message.getAsJsonObject(fieldName).addProperty("customMessage", fieldValue);
        if (!message.getAsJsonObject(fieldName).has("useCustomMessage")) message.getAsJsonObject(fieldName).addProperty("useCustomMessage", true);

        fieldName = "playerLeft";
        fieldValue = "%player left the game";
        if (!message.has(fieldName)) message.add(fieldName, new JsonObject());
        else if (message.get(fieldName).isJsonPrimitive()) {
            String msg = message.get(fieldName).getAsString();
            message.remove(fieldName);
            message.add(fieldName, new JsonObject());
            message.getAsJsonObject(fieldName).addProperty("cutomMessage", msg);
        }
        if (!message.getAsJsonObject(fieldName).has("customMessage")) message.getAsJsonObject(fieldName).addProperty("customMessage", fieldValue);
        if (!message.getAsJsonObject(fieldName).has("useCustomMessage")) message.getAsJsonObject(fieldName).addProperty("useCustomMessage", true);

        fieldName = "advancementTask";
        fieldValue = "%player has made the advancement %advancement";
        if (!message.has(fieldName)) message.add(fieldName, new JsonObject());
        else if (message.get(fieldName).isJsonPrimitive()) {
            String msg = message.get(fieldName).getAsString();
            message.remove(fieldName);
            message.add(fieldName, new JsonObject());
            message.getAsJsonObject(fieldName).addProperty("cutomMessage", msg);
        }
        if (!message.getAsJsonObject(fieldName).has("customMessage")) message.getAsJsonObject(fieldName).addProperty("customMessage", fieldValue);
        if (!message.getAsJsonObject(fieldName).has("useCustomMessage")) message.getAsJsonObject(fieldName).addProperty("useCustomMessage", true);

        fieldName = "advancementChallenge";
        fieldValue = "%player has completed the challenge %advancement";
        if (!message.has(fieldName)) message.add(fieldName, new JsonObject());
        else if (message.get(fieldName).isJsonPrimitive()) {
            String msg = message.get(fieldName).getAsString();
            message.remove(fieldName);
            message.add(fieldName, new JsonObject());
            message.getAsJsonObject(fieldName).addProperty("cutomMessage", msg);
        }
        if (!message.getAsJsonObject(fieldName).has("customMessage")) message.getAsJsonObject(fieldName).addProperty("customMessage", fieldValue);
        if (!message.getAsJsonObject(fieldName).has("useCustomMessage")) message.getAsJsonObject(fieldName).addProperty("useCustomMessage", true);

        fieldName = "advancementGoal";
        fieldValue = "%player has reached the goal %advancement";
        if (!message.has(fieldName)) message.add(fieldName, new JsonObject());
        else if (message.get(fieldName).isJsonPrimitive()) {
            String msg = message.get(fieldName).getAsString();
            message.remove(fieldName);
            message.add(fieldName, new JsonObject());
            message.getAsJsonObject(fieldName).addProperty("cutomMessage", msg);
        }
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
    // v0 -> v1
    // + channel description in messages
    V0_TO_V1(jsonObject -> {
        if (!jsonObject.has("minecraftToDiscord")) jsonObject.add("minecraftToDiscord", new JsonObject());
        JsonObject minecraftToDiscord = jsonObject.getAsJsonObject("minecraftToDiscord");

        if (!minecraftToDiscord.has("messages")) minecraftToDiscord.add("messages", new JsonObject());
        JsonObject message = minecraftToDiscord.getAsJsonObject("messages");

        message.addProperty("channelDescription", "Playercount : %playercount/%maxplayercount,\n Uptime : %uptime");

        jsonObject.remove("version");
        jsonObject.addProperty("version", 1);

        return jsonObject;
    }),
    // v1 -> v2
    // split of the config between fdlink.json and messages.json
    V1_TO_V2(jsonObject -> {
        JsonObject newJsonObject = new JsonObject();
        JsonObject newMessages = new JsonObject();
        if (jsonObject.has("minecraftToDiscord")) {
            JsonObject minecraftToDiscord = jsonObject.getAsJsonObject("minecraftToDiscord");
            JsonObject message = new JsonObject();
            if (minecraftToDiscord.has("messages")) {
                message = (JsonObject) minecraftToDiscord.remove("messages");
            }

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

            if (!message.has("channelDescription")) message.addProperty("channelDescription", "Playercount : %playercount/%maxplayercount,\n Uptime : %uptime");

            newMessages.add("minecraftToDiscord", message);
        }
        if (jsonObject.has("discordToMinecraft")) {
            JsonObject oldDiscordToMinecraft = jsonObject.getAsJsonObject("discordToMinecraft");
            JsonObject newDiscordToMinecraft = new JsonObject();
            if (oldDiscordToMinecraft.has("message")) {
                newDiscordToMinecraft.addProperty("message", oldDiscordToMinecraft.get("message").getAsString());
                oldDiscordToMinecraft.remove("message");
            } else newDiscordToMinecraft.addProperty("message", "[%player] %message");
            if (oldDiscordToMinecraft.has("commandPrefix")) {
                newDiscordToMinecraft.addProperty("commandPrefix", oldDiscordToMinecraft.get("commandPrefix").getAsString());
                oldDiscordToMinecraft.remove("commandPrefix");
            } else newDiscordToMinecraft.addProperty("commandPrefix", "!");
            newMessages.add("discordToMinecraft", newDiscordToMinecraft);
        }
        jsonObject.remove("version");

        newJsonObject.add("main", jsonObject);
        newJsonObject.add("messages", newMessages);

        newJsonObject.addProperty("version", 2);

        return newJsonObject;
    }),
    V2_TO_V3(jsonObject -> {

        jsonObject.getAsJsonObject("main").addProperty("activityUpdateInterval", 120);

        JsonObject messageJson = jsonObject.getAsJsonObject("messages");

        JsonObject discordOnly = new JsonObject();

        if (messageJson.getAsJsonObject("discordToMinecraft").has("commandPrefix")) {
            discordOnly.addProperty("commandPrefix",
                    messageJson.getAsJsonObject("discordToMinecraft").remove("commandPrefix").getAsString());
        } else {
            discordOnly.addProperty("commandPrefix", "!");
        }

        JsonArray array = new JsonArray();
        array.add("!commands");
        array.add("%playercount / %maxplayercount");
        array.add("on %ip");
        array.add("%uptime_D day(s), %uptime_H hour(s), %uptime_M minute(s) and %uptime_S second(s)");

        discordOnly.add("botActivities", array);

        messageJson.add("discord", discordOnly);

        jsonObject.remove("version");
        jsonObject.addProperty("version", 3);

        return jsonObject;
    }),
    V3_TO_V4(jsonObject -> {
        jsonObject.getAsJsonObject("main").getAsJsonObject("minecraftToDiscord").getAsJsonObject("chatChannels").addProperty("atATellRaw", false);
        jsonObject.getAsJsonObject("main").getAsJsonObject("minecraftToDiscord").getAsJsonObject("logChannels").addProperty("atATellRaw", false);

        jsonObject.getAsJsonObject("messages").getAsJsonObject("minecraftToDiscord").addProperty("atATellRaw", "%message");

        jsonObject.remove("version");
        jsonObject.addProperty("version", 4);

        return jsonObject;
    }),
    V4_TO_V5(jsonObject -> {
        JsonObject messages = jsonObject.getAsJsonObject("messages");
        JsonObject minecraftToDiscord = messages.getAsJsonObject("minecraftToDiscord");

        JsonObject configMessageObject = new JsonObject();
        configMessageObject.addProperty("customMessage", "* %author %message");
        configMessageObject.addProperty("useCustomMessage", true);
        minecraftToDiscord.add("meMessage", configMessageObject);

        configMessageObject = new JsonObject();
        configMessageObject.addProperty("customMessage", "[%author: %message]");
        configMessageObject.addProperty("useCustomMessage", true);
        minecraftToDiscord.add("adminMessage", configMessageObject);

        configMessageObject = new JsonObject();
        configMessageObject.addProperty("customMessage", "[%author] %message");
        configMessageObject.addProperty("useCustomMessage", true);
        minecraftToDiscord.add("sayMessage", configMessageObject);

        jsonObject.remove("version");
        jsonObject.addProperty("version", 5);

        return jsonObject;
    }),
    V5_TO_V6(jsonObject -> {
        jsonObject.getAsJsonObject("main").addProperty("webhookURL", "");

        jsonObject.remove("version");
        jsonObject.addProperty("version", 6);

        return jsonObject;
    }),
    V6_TO_V7(jsonObject -> {
        JsonObject messageConfig = new JsonObject();
        messageConfig.addProperty("customMessage", "%player has just earned the achievement %achievement");
        messageConfig.addProperty("useCustomMessage", true);

        jsonObject.getAsJsonObject("messages").getAsJsonObject("minecraftToDiscord").add("achievement", messageConfig);

        jsonObject.getAsJsonObject("main").getAsJsonObject("minecraftToDiscord")
                .getAsJsonObject("chatChannels").addProperty("achievementMessages", true);
        jsonObject.getAsJsonObject("main").getAsJsonObject("minecraftToDiscord")
                .getAsJsonObject("logChannels").addProperty("achievementMessages", true);

        jsonObject.remove("version");
        jsonObject.addProperty("version", 7);

        return jsonObject;
    }),
    V7_TO_V8(jsonObject -> {
        JsonObject messageConfig = new JsonObject();
        messageConfig.addProperty("customMessage", "%team <%player> %message");
        messageConfig.addProperty("useCustomMessage", true);

        jsonObject.getAsJsonObject("messages").getAsJsonObject("minecraftToDiscord").add("teamPlayerMessage", messageConfig);

        jsonObject.getAsJsonObject("main").getAsJsonObject("minecraftToDiscord")
                .getAsJsonObject("chatChannels").addProperty("teamPlayerMessages", true);
        jsonObject.getAsJsonObject("main").getAsJsonObject("minecraftToDiscord")
                .getAsJsonObject("logChannels").addProperty("teamPlayerMessages", false);

        jsonObject.remove("version");
        jsonObject.addProperty("version", 8);

        return jsonObject;
    }),
    V8_TO_V9(jsonObject -> {
        JsonObject webhookSettings = new JsonObject();

        JsonObject webhookMentions = new JsonObject();
        webhookMentions.addProperty("everyone", false);
        webhookMentions.addProperty("roles", false);
        webhookMentions.addProperty("users", true);

        if (jsonObject.getAsJsonObject("main").has("webhookURL")) {
            webhookSettings.addProperty("url", jsonObject.getAsJsonObject("main").get("webhookURL").getAsString());
            jsonObject.getAsJsonObject("main").remove("webhookURL");
        } else {
            webhookSettings.addProperty("url", "");
        }

        webhookSettings.add("mentions", webhookMentions);

        jsonObject.getAsJsonObject("main").add("webhook", webhookSettings);

        jsonObject.remove("version");
        jsonObject.addProperty("version", 9);

        return jsonObject;
    });

    private final Upgrader upgrader;

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
