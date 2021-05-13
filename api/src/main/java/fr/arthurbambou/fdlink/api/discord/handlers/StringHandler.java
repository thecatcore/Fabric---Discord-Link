package fr.arthurbambou.fdlink.api.discord.handlers;

import fr.arthurbambou.fdlink.api.discord.MinecraftToDiscordFunction;
import fr.arthurbambou.fdlink.api.minecraft.Message;

public class StringHandler extends MessageHandler {

    public StringHandler(MinecraftToDiscordFunction minecraftToDiscordFunction) {
        super(minecraftToDiscordFunction);
    }

    @Override
    public boolean match(Message message) {
        return message.getType() == Message.MessageObjectType.STRING;
    }
}
