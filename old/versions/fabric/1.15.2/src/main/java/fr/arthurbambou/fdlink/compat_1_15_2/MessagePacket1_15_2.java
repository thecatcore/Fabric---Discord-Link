package fr.arthurbambou.fdlink.compat_1_15_2;


import fr.arthurbambou.fdlink.api.minecraft.Message;
import fr.arthurbambou.fdlink.api.minecraft.MessagePacket;

import java.util.UUID;

public class MessagePacket1_15_2 implements MessagePacket {

    private Message message;

    public MessagePacket1_15_2(Message message) {
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
