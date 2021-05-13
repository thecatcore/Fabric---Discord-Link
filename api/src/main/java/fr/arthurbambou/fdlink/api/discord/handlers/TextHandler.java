package fr.arthurbambou.fdlink.api.discord.handlers;

import fr.arthurbambou.fdlink.api.discord.MinecraftToDiscordFunction;
import fr.arthurbambou.fdlink.api.minecraft.Message;

public class TextHandler extends MessageHandler {
    private final String key;
    public TextHandler(String key, MinecraftToDiscordFunction minecraftToDiscordFunction) {
        super(minecraftToDiscordFunction);
        this.key = key;
    }

    public boolean match(Message text) {
        if (text.getTextType() == Message.TextType.TRANSLATABLE) {
            return text.getKey().startsWith(this.key);
        }
        return false;
    }
}
