package fr.arthurbambou.fdlink.versionhelpers;

import net.fabricmc.loader.api.SemanticVersion;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Style;

public interface MessageSender {

    boolean isCompatibleWithVersion(SemanticVersion semanticVersion);

    void sendMessageToChat(MinecraftServer server, String message, Style style);
}
