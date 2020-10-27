package fr.arthurbambou.fdlink.config;

import java.util.ArrayList;
import java.util.List;

public class MainConfig {
    public List<String> chatChannels = new ArrayList<String>();
    public List<String> logChannels = new ArrayList<String>();
    public boolean ignoreBots = true;
    public MainConfig.MinecraftToDiscord minecraftToDiscord = new MainConfig.MinecraftToDiscord();
    public MainConfig.DiscordToMinecraft discordToMinecraft = new MainConfig.DiscordToMinecraft();
    public List<MainConfig.EmojiEntry> emojiMap = new ArrayList<>();

    public MainConfig() {
        emojiMap.add(new MainConfig.EmojiEntry("example_name", ":example_id:22222222"));
        emojiMap.add(new MainConfig.EmojiEntry("example_name2", ":example_id2:22222222"));
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
        public MainConfig.MinecraftToDiscordGeneral general = new MainConfig.MinecraftToDiscordGeneral();
        public MainConfig.MinecraftToDiscordChatChannel chatChannels = new MainConfig.MinecraftToDiscordChatChannel();
        public MainConfig.MinecraftToDiscordLogChannel logChannels = new MainConfig.MinecraftToDiscordLogChannel();
    }

    public class MinecraftToDiscordGeneral {
        public boolean enableDebugLogs = false;
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
    }
}
