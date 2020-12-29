package fr.arthurbambou.fdlink.discordstuff;

import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageReceivedListener extends ListenerAdapter {

    private final DiscordBot discordBot;

    public MessageReceivedListener(DiscordBot discordBot) {
        this.discordBot = discordBot;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.isWebhookMessage()) return;
        if ((event.getAuthor().isBot() && this.discordBot.config.mainConfig.ignoreBots)
                || this.discordBot.api.getSelfUser().getId().equals(event.getAuthor().getId())) return;
        if (!this.discordBot.hasChatChannels) return;
        if (!this.discordBot.config.mainConfig.chatChannels.contains(event.getChannel().getId())) return;
        if (event.getMessage().getType() != MessageType.DEFAULT) return;
        if (!DiscordBot.lastMessageMs.isEmpty()) {
            if (event.getMessage().getContentRaw().equals(DiscordBot.lastMessageMs.get(0))) {
                DiscordBot.lastMessageMs.remove(0);
                return;
            }
        }

        this.discordBot.messageCreateEvent = event;
        this.discordBot.hasReceivedMessage = true;
        super.onMessageReceived(event);
    }
}
