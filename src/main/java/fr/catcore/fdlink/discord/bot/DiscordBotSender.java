package fr.catcore.fdlink.discord.bot;

import fr.catcore.fdlink.api.DiscordSender;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class DiscordBotSender extends DiscordSender {
    private final String channelId;
    private final JDA bot;
    protected DiscordBotSender(JDA bot, String channelId) {
        this.channelId = channelId;
        this.bot = bot;
    }

    @Override
    public void sendMessage(String message, String author) {
        TextChannel channel = this.bot.getTextChannelById(this.channelId);
        if (channel != null) {
            channel.sendMessage(message);
        }
    }
}
