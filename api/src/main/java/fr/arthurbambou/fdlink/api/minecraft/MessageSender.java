package fr.arthurbambou.fdlink.api.minecraft;

import fr.arthurbambou.fdlink.api.minecraft.style.Style;

public interface MessageSender {

    void sendMessageToChat(MinecraftServer server, String message, Style style);
}
