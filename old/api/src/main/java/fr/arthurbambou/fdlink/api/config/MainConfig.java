package fr.arthurbambou.fdlink.api.config;

import java.util.ArrayList;
import java.util.List;

public class MainConfig {
    public List<String> chatChannels = new ArrayList<String>();
    public List<String> logChannels = new ArrayList<String>();
    public WebhookSettings webhook = new WebhookSettings();
    public boolean ignoreBots = true;
    public int activityUpdateInterval = 120;
    public MinecraftToDiscord minecraftToDiscord = new MinecraftToDiscord();
    public DiscordToMinecraft discordToMinecraft = new DiscordToMinecraft();
    public List<EmojiEntry> emojiMap = new ArrayList<>();

    public MainConfig() {
        emojiMap.add(new EmojiEntry("example_name", ":example_id:22222222"));
        emojiMap.add(new EmojiEntry("example_name2", ":example_id2:22222222"));
    }

    public static class EmojiEntry {
        public String name;
        public String id;

        public EmojiEntry(String name, String id) {
            this.name = name;
            this.id = id;
        }
    }

    public static class WebhookSettings {
        public String url = "";
        public WebhookMentions mentions = new WebhookMentions();
    }

    public static class WebhookMentions {
        public boolean everyone = false;
        public boolean roles = false;
        public boolean users = true;
    }

    public static class MinecraftToDiscord {
        public MinecraftToDiscordGeneral general = new MinecraftToDiscordGeneral();
        public MinecraftToDiscordChatChannel chatChannels = new MinecraftToDiscordChatChannel();
        public MinecraftToDiscordLogChannel logChannels = new MinecraftToDiscordLogChannel();
    }

    public static class MinecraftToDiscordGeneral {
        public boolean enableDebugLogs = false;
    }

    public static class MinecraftToDiscordChatChannel {
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
        public boolean teamPlayerMessages = true;
        public boolean joinAndLeaveMessages = true;
        public boolean advancementMessages = true;
        public boolean challengeMessages = true;
        public boolean goalMessages = true;
        public boolean deathMessages = true;
        public boolean sendMeCommand = true;
        public boolean sendSayCommand = true;
        public boolean adminMessages = false;
        public boolean atATellRaw = false;
        public boolean achievementMessages = true;
    }

    public static class MinecraftToDiscordLogChannel {
        public boolean serverStartingMessage = true;
        public boolean serverStartMessage = true;
        public boolean serverStopMessage = true;
        public boolean serverStoppingMessage = true;
        public boolean customChannelDescription = false;
        public boolean minecraftToDiscordTag = false;
        public boolean minecraftToDiscordDiscriminator = false;
        public boolean playerMessages = false;
        public boolean teamPlayerMessages = false;
        public boolean joinAndLeaveMessages = true;
        public boolean advancementMessages = false;
        public boolean challengeMessages = false;
        public boolean goalMessages = false;
        public boolean deathMessages = true;
        public boolean sendMeCommand = true;
        public boolean sendSayCommand = true;
        public boolean adminMessages = true;
        public boolean atATellRaw = false;
        public boolean achievementMessages = true;
    }

    public static class DiscordToMinecraft {
        public boolean pingLongVersion = false;
    }
}
