package fr.arthurbambou.fdlink.api.discord;

import fr.arthurbambou.fdlink.api.minecraft.Message;

public interface MessageSender {

    void serverStarting();

    void serverStarted();

    void serverStopping();

    void serverStopped();

    void sendMessage(Message message);
}
