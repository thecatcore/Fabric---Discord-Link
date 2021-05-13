package fr.arthurbambou.fdlink.api.discord;

import java.util.UUID;

public class MinecraftMessage {
    private final MessageSendability common;
    private final MessageSendability chat;
    private final MessageSendability log;
    private UUID author = null;
    private boolean searchForAuthor = false;

    public MinecraftMessage(MessageSendability common) {
        this.common = common;
        this.chat = null;
        this.log = null;
    }

    public MinecraftMessage(MessageSendability chat, MessageSendability log) {
        this.common = null;
        this.chat = chat;
        this.log = log;
    }

    public MinecraftMessage(MessageSendability messageSendability, Type type) {
        this.common = null;
        if (type == Type.CHAT) {
            this.chat = messageSendability;
            this.log = null;
        } else {
            this.chat = null;
            this.log = messageSendability;
        }
    }

    public static enum Type {
        CHAT,
        LOG;
    }

    public MessageSendability getCommon() {
        return common;
    }

    public MessageSendability getChat() {
        return chat;
    }

    public MessageSendability getLog() {
        return log;
    }

    public UUID getAuthor() {
        return author;
    }

    public MinecraftMessage setAuthor(UUID author) {
        this.author = author;
        return this;
    }

    public MinecraftMessage searchForAuthor() {
        this.searchForAuthor = true;
        return this;
    }

    public boolean doSearchForAuthor() {
        return this.searchForAuthor;
    }

    public static final class MessageSendability {
        private final String message;
        private final boolean canSend;
        private final boolean canSendChat;
        private final boolean canSendLog;

        public MessageSendability(String message, boolean canSend) {
            this.message = message;
            this.canSend = canSend;
            this.canSendChat = false;
            this.canSendLog = false;
        }

        public MessageSendability(String message, boolean canSendChat, boolean canSendLog) {
            this.message = message;
            this.canSend = false;
            this.canSendChat = canSendChat;
            this.canSendLog = canSendLog;
        }

        public String getMessage() {
            return message;
        }

        public boolean canSend() {
            return canSend;
        }

        public boolean canSendChat() {
            return canSendChat;
        }

        public boolean canSendLog() {
            return canSendLog;
        }
    }
}
