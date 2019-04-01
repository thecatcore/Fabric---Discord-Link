package fr.arthurbambou.fblink.discordstuff;

import fr.arthurbambou.fblink.FBLink;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.sortme.ChatMessageType;
import net.minecraft.text.StringTextComponent;
import net.minecraft.text.TextComponent;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class DiscordBot {

    private FBLink.Config config;
//    private long channelID = 559063516510683151L;
    private boolean hasChatChannels;
    private boolean hasLogChannels;
    private MessageCreateEvent messageCreateEvent;
    private boolean hasReceivedaMessage;
    private String lastMessageD;
    private DiscordApi api;

    public DiscordBot(String token, FBLink.Config config) {
        this.lastMessageD = "";
        if (token == null) {
            FBLink.regenConfig();
            return;
        }

        if (token == "") {
            return;
        }

        if (config.chatChannels.size() == 0) {
            this.hasChatChannels = false;
        } else {
            this.hasChatChannels = true;
        }

        if (config.logChannels.size() == 0) {
            this.hasLogChannels = false;
        } else {
            this.hasLogChannels = true;
        }

        if (!hasLogChannels && !hasChatChannels) return;

        this.config = config;

        this.api = new DiscordApiBuilder().setToken(token).login().join();

        this.api.addMessageCreateListener((event -> {
            if (!hasChatChannels) return;
            if (event.getMessageAuthor().isYourself()) return;
            if (!this.config.chatChannels.contains(event.getChannel().getIdAsString())) return;
            this.messageCreateEvent = event;
            this.hasReceivedaMessage = true;
        }));

        ServerStartCallback.EVENT.register((minecraftServer1 -> {
            if (this.hasChatChannels)
                for (int a = 0; a < this.config.chatChannels.size(); a++)
                    this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage("Server Started");

            if (this.hasLogChannels)
                for (int a = 0; a < this.config.logChannels.size(); a++)
                    this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage("Server Started");
        }));

        ServerStopCallback.EVENT.register((server -> {
            if (this.hasChatChannels)
                for (int a = 0; a < this.config.chatChannels.size(); a++)
                    this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage("Server Stoped");

            if (this.hasLogChannels)
                for (int a = 0; a < this.config.logChannels.size(); a++)
                    this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage("Server Stoped");
        }));

        ServerTickCallback.EVENT.register((server -> {
            if (this.hasReceivedaMessage) {
                this.lastMessageD = this.config.discordToMinecraft
                        .replace("%player",this.messageCreateEvent.getMessageAuthor().getDisplayName())
                        .replace("%message",this.messageCreateEvent.getMessageContent());
                server.getPlayerManager().sendToAll(new StringTextComponent(this.lastMessageD));

                this.hasReceivedaMessage = false;
            }
        }));
    }

    public void sendMessage(String string) {
        if (string.equals(this.lastMessageD)) { return; }
        else {
            if (string.startsWith("<")) {
                if (this.hasChatChannels)
                    for (int a = 0; a < this.config.chatChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(string);
                if (this.hasLogChannels)
                    for (int a = 0; a < this.config.logChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(string);
            } else if (string.contains("left")) {
                if (this.hasChatChannels)
                    for (int a = 0; a < this.config.chatChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(string);
                if (this.hasLogChannels)
                    for (int a = 0; a < this.config.logChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(string);
            } else if (string.contains("joined")) {
                if (this.hasChatChannels)
                    for (int a = 0; a < this.config.chatChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(string);
                if (this.hasLogChannels)
                    for (int a = 0; a < this.config.logChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(string);
            } else if (string.contains("advancement")) {
                if (this.hasChatChannels)
                    for (int a = 0; a < this.config.chatChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(string);
                if (this.hasLogChannels)
                    for (int a = 0; a < this.config.logChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(string);
            } else if (string.startsWith("[")) {
                if (this.hasChatChannels && !this.hasLogChannels)
                    for (int a = 0; a < this.config.chatChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(string);
                if (this.hasLogChannels)
                    for (int a = 0; a < this.config.logChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(string);
            }
        }
    }
}
