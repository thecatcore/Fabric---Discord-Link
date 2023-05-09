package fr.catcore.fdlink.api;

public abstract class MessageSender {

    protected MessageSender() {
    }

    public abstract void sendMessage(String message, String author);
}
