package fr.arthurbambou.fdlink.versionhelpers;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style;

public interface MessageSender {

    void sendMessageToChat(MinecraftServer server, String message, Style style);
}
