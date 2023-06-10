package fr.catcore.fdlink.config;

public class ConfigData {
    protected static int CONFIG_VERSION = 10;
    private int version = CONFIG_VERSION;
    private String discordToken = "";
    public MessageSender[] messageSenders = new MessageSender[]{};

    public class MessageSender {
        public String id = "";

        public MessageSender(String id) {
            this.id = id;
        }
    }

    public class DiscordSender extends MessageSender {
        public DiscordSender(String id) {
            super("discord:" + id);
        }
    }

    public class DiscordBotSender extends DiscordSender {
        public String channelId = "";

        public DiscordBotSender() {
            super("bot");
        }
    }

    public class DiscordWebhookSender extends DiscordSender {
        public String url = "";

        public DiscordWebhookSender() {
            super("webhook");
        }
    }

    public class MinecraftSender extends MessageSender {

        public MinecraftSender() {
            super("minecraft");
        }
    }
}
