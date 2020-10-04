package fr.arthurbambou.fdlink.config;

import java.util.ArrayList;
import java.util.List;

public class Config {
    public List<String> chatChannels = new ArrayList<String>();
    public List<String> logChannels = new ArrayList<String>();
    public boolean ignoreBots = true;
    public Config.MinecraftToDiscord minecraftToDiscord = new Config.MinecraftToDiscord();
    public Config.DiscordToMinecraft discordToMinecraft = new Config.DiscordToMinecraft();
    public List<Config.EmojiEntry> emojiMap = new ArrayList<>();

    public Config() {
        emojiMap.add(new Config.EmojiEntry("example_name", ":example_id:22222222"));
        emojiMap.add(new Config.EmojiEntry("example_name2", ":example_id2:22222222"));
    }

    public static class EmojiEntry {
        public String name;
        public String id;

        public EmojiEntry(String name, String id) {
            this.name = name;
            this.id = id;
        }
    }

    public class MinecraftToDiscord {
        public Config.MinecraftToDiscordGeneral general = new Config.MinecraftToDiscordGeneral();
        public Config.MinecraftToDiscordMessage messages = new Config.MinecraftToDiscordMessage();
        public Config.MinecraftToDiscordChatChannel chatChannels = new Config.MinecraftToDiscordChatChannel();
        public Config.MinecraftToDiscordLogChannel logChannels = new Config.MinecraftToDiscordLogChannel();
    }

    public class MinecraftToDiscordGeneral {
        public boolean enableDebugLogs = false;
    }

    public static class MinecraftToDiscordMessage {
        public Config.MinecraftToDiscordMessage.ConfigMessage playerMessage = new Config.MinecraftToDiscordMessage.ConfigMessage("<%player> %message");
        public String serverStarting = "Server is starting!";
        public String serverStarted = "Server Started.";
        public String serverStopping = "Server is stopping!";
        public String serverStopped = "Server Stopped.";
        public Config.MinecraftToDiscordMessage.ConfigMessage playerJoined = new Config.MinecraftToDiscordMessage.ConfigMessage("%player joined the game");
        public Config.MinecraftToDiscordMessage.ConfigMessage playerJoinedRenamed = new Config.MinecraftToDiscordMessage.ConfigMessage("%new (formerly known as %old) joined the game");
        public Config.MinecraftToDiscordMessage.ConfigMessage playerLeft = new Config.MinecraftToDiscordMessage.ConfigMessage("%player left the game");
        public Config.MinecraftToDiscordMessage.ConfigMessage advancementTask = new Config.MinecraftToDiscordMessage.ConfigMessage("%player has made the advancement %advancement");
        public Config.MinecraftToDiscordMessage.ConfigMessage advancementChallenge = new Config.MinecraftToDiscordMessage.ConfigMessage("%player has completed the challenge %advancement");
        public Config.MinecraftToDiscordMessage.ConfigMessage advancementGoal = new Config.MinecraftToDiscordMessage.ConfigMessage("%player has reached the goal %advancement");
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
