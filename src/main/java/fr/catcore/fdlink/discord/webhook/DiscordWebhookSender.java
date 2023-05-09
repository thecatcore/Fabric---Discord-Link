package fr.catcore.fdlink.discord.webhook;

import fr.catcore.fdlink.api.DiscordSender;

public class DiscordWebhookSender extends DiscordSender {
    private final String url;
    public DiscordWebhookSender(String url) {
        this.url = url;
    }
    @Override
    public void sendMessage(String message, String author) {

    }
}
