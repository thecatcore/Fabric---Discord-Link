package fr.arthurbambou.fdlink.api.discord.handlers;

import fr.arthurbambou.fdlink.api.discord.MinecraftToDiscordFunction;
import fr.arthurbambou.fdlink.api.minecraft.Message;

public class CommandHandler extends MessageHandler {
    private final String commandName;

    public CommandHandler(String commandName, MinecraftToDiscordFunction minecraftToDiscordFunction) {
        super(minecraftToDiscordFunction);
        this.commandName = commandName;
    }

    @Override
    public boolean match(Message message) {
        if (message.getTextType() == Message.TextType.COMMAND) {
            return this.commandName.equals(message.getCommandName());
        }
        return false;
    }
}
