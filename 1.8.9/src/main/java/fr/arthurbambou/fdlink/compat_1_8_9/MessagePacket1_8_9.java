package fr.arthurbambou.fdlink.compat_1_8_9;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MessagePacket;

import java.util.UUID;

public class MessagePacket1_8_9 implements MessagePacket {

    private Message message;

    public MessagePacket1_8_9(Message message) {
        this.message = message;
    }

    @Override
    public Message getMessage() {
        return this.message;
    }

    @Override
    public MessageType getMessageType() {
        return null;
    }

    @Override
    public UUID getUUID() {
        return null;
    }
}
