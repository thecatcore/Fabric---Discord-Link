package fr.arthurbambou.fdlink.compat_1_16;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MessagePacket;

import java.util.UUID;

public class MessagePacket1_16 implements MessagePacket {

    private Message message;
    private MessageType messageType;
    private UUID uuid;

    public MessagePacket1_16(Message message, MessageType messageType, UUID uuid) {
        this.message = message;
        this.messageType = messageType;
        this.uuid = uuid;
    }

    @Override
    public Message getMessage() {
        return this.message;
    }

    @Override
    public MessageType getMessageType() {
        return this.messageType;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }
}
