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
        if (event.getAuthor().isBot() && this.discordBot.config.ignoreBots) return;
        if (!this.discordBot.hasChatChannels) return;
//        if (event.getAuthor().isYourself()) return;
        if (!this.discordBot.config.chatChannels.contains(event.getChannel().getId())) return;
        if (event.getMessage().getType() != MessageType.DEFAULT) return;
        if (!this.discordBot.lastMessageMs.isEmpty()) {
            if (event.getMessage().getContentRaw().equals(this.discordBot.lastMessageMs.get(0))) {
                this.discordBot.lastMessageMs.remove(0);
                return;
            }
        }
        this.discordBot.messageCreateEvent = event;
        this.discordBot.hasReceivedMessage = true;
        super.onMessageReceived(event);
    }
}
