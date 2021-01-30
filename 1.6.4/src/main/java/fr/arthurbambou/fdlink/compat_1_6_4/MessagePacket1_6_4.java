package fr.arthurbambou.fdlink.compat_1_6_4;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MessagePacket;

import java.util.UUID;

public class MessagePacket1_6_4 implements MessagePacket {

    private Message message;

    public MessagePacket1_6_4(Message message) {
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
