package fr.arthurbambou.fdlink.versionhelpers.minecraft;

import java.util.UUID;

public interface MessagePacket {

    Message getMessage();

    MessageType getMessageType();

    UUID getUUID();

    enum MessageType {
        CHAT,
        SYSTEM,
        INFO;
    }
}
