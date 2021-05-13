package fr.arthurbambou.fdlink.api.discord.handlers;

import fr.arthurbambou.fdlink.api.config.Config;
import fr.arthurbambou.fdlink.api.discord.MinecraftMessage;
import fr.arthurbambou.fdlink.api.discord.MinecraftToDiscordFunction;
import fr.arthurbambou.fdlink.api.minecraft.Message;

public abstract class MessageHandler {
    private final MinecraftToDiscordFunction minecraftToDiscordFunction;

    public MessageHandler(MinecraftToDiscordFunction minecraftToDiscordFunction) {
        this.minecraftToDiscordFunction = minecraftToDiscordFunction;
    }

    public MinecraftMessage handle(Message text, Config config) {
        return this.minecraftToDiscordFunction.handleText(text, config);
    }

    public abstract boolean match(Message message);
}
