package fr.arthurbambou.fdlink.compat_1_5_2;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MessagePacket;

import java.util.UUID;

public class MessagePacket1_5_2 implements MessagePacket {

    private Message message;

    public MessagePacket1_5_2(Message message) {
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
