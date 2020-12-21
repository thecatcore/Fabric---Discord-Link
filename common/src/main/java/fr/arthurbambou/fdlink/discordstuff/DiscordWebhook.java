package fr.arthurbambou.fdlink.discordstuff;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import fr.arthurbambou.fdlink.config.Config;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;

public class DiscordWebhook implements MessageSender {

    protected final Config config;
    protected WebhookClient webhookClient;

    public DiscordWebhook(String webhookURL, Config config) {
        this.config = config;

        WebhookClientBuilder builder = new WebhookClientBuilder(webhookURL); // or id, token
        builder.setThreadFactory((job) -> {
            Thread thread = new Thread(job);
            thread.setName("Hello");
            thread.setDaemon(true);
            return thread;
        });
        builder.setWait(true);
        this.webhookClient = builder.build();
    }

    public void sendMessage(String author, String message) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.setUsername(author);
        if (!author.equals("server")) builder.setAvatarUrl("https://minotar.net/avatar/" + author);
        builder.setContent(message);

        this.webhookClient.send(builder.build());
    }

    @Override
    public void serverStarting() {

    }

    @Override
    public void serverStarted() {

    }

    @Override
    public void serverStopping() {

    }

    @Override
    public void serverStopped() {

    }

    @Override
    public void sendMessage(Message message) {

    }
}
