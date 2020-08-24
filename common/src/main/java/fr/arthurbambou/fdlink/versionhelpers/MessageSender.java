package fr.arthurbambou.fdlink.versionhelpers;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style;
import net.fabricmc.loader.api.SemanticVersion;

public interface MessageSender {

    boolean isCompatibleWithVersion(SemanticVersion semanticVersion);

    void sendMessageToChat(MinecraftServer server, String message, Style style);
}
