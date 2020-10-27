package fr.arthurbambou.fdlink.config;

public class MessageConfig {

    public MinecraftToDiscord minecraftToDiscord = new MinecraftToDiscord();
    public DiscordToMinecraft discordToMinecraft = new DiscordToMinecraft();

    public static class MinecraftToDiscord {
        public ConfigMessage playerMessage = new ConfigMessage("<%player> %message");
        public String serverStarting = "Server is starting!";
        public String serverStarted = "Server Started.";
        public String serverStopping = "Server is stopping!";
        public String serverStopped = "Server Stopped.";
        public String channelDescription = "Playercount : %playercount/%maxplayercount,\n Uptime : %uptime";
        public ConfigMessage playerJoined = new ConfigMessage("%player joined the game");
        public ConfigMessage playerJoinedRenamed = new ConfigMessage("%new (formerly known as %old) joined the game");
        public ConfigMessage playerLeft = new ConfigMessage("%player left the game");
        public ConfigMessage advancementTask = new ConfigMessage("%player has made the advancement %advancement");
        public ConfigMessage advancementChallenge = new ConfigMessage("%player has completed the challenge %advancement");
        public ConfigMessage advancementGoal = new ConfigMessage("%player has reached the goal %advancement");
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

    public static class DiscordToMinecraft {
        public String message = "[%player] %message";
        public String commandPrefix = "!";
    }
}
