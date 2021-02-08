package fr.arthurbambou.fdlink.discordstuff;

import fr.arthurbambou.fdlink.config.Config;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;

import java.util.UUID;

public interface MessageSender {

    void serverStarting();

    void serverStarted();

    void serverStopping();

    void serverStopped();

    void sendMessage(Message message);

    class MinecraftMessage {
        private final String[] message;
        private final Type type;
        private final Sender messageSender;
        private UUID author = null;

        public MinecraftMessage(String message, Type type) {
            this.message = new String[]{message};
            this.type = type;
            this.messageSender = null;
        }

        public MinecraftMessage(String message, Sender messageSender) {
            this.message = new String[]{message};
            this.type = Type.CUSTOM;
            this.messageSender = messageSender;
        }

        public MinecraftMessage(String message1, String message2, Type type) {
            this.message = new String[]{message1, message2};
            this.type = type;
            this.messageSender = null;
        }

        public MinecraftMessage(String message1, String message2, String message3, Type type) {
            this.message = new String[]{message1, message2, message3};
            this.type = type;
            this.messageSender = null;
        }

        public MinecraftMessage(String message1, String message2, Sender messageSender) {
            this.message = new String[]{message1, message2};
            this.type = Type.CUSTOM;
            this.messageSender = messageSender;
        }

        public String[] getMessages() {
            return message;
        }

        public String getMessage() {
            return message[0];
        }

        public Type getType() {
            return type;
        }

        public Sender getMessageSender() {
            return messageSender;
        }

        public UUID getAuthor() {
            return author;
        }

        public MinecraftMessage setAuthor(UUID author) {
            this.author = author;
            return this;
        }

        enum Type {
            CHAT,
            TEAM_CHAT,
            CHAT_COMMAND,
            ME,
            SAY,
            ADVANCEMENT_TASK,
            ADVANCEMENT_CHALLENGE,
            ADVANCEMENT_GOAL,
            ADMIN,
            JOIN_RENAMED,
            JOIN,
            LEAVE,
            DEATH,
            TELLRAW,
            ACHIEVEMENT,
            STRING_OLD,
            CUSTOM
        }

        interface Sender {
            void sendMessage(String message, MessageSender messageSender, Config config);
        }
    }
}
