package fr.arthurbambou.fdlink.discordstuff.todiscord;

import fr.arthurbambou.fdlink.discordstuff.MessageSender;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;

public interface MinecraftToDiscordFunction {

    MessageSender.MinecraftMessage handleText(Message text);
}
