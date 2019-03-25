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
    private long channelID = 559063516510683151L;
    private MessageCreateEvent messageCreateEvent;
    private boolean hasReceivedaMessage;
    private DiscordApi api;

    public DiscordBot(String token, FBLink.Config config) {
        if (token == null) {
            FBLink.regenConfig();
            return;
        }

        if (token == "") {
            return;
        }
        this.config = config;

        this.api = new DiscordApiBuilder().setToken(token).login().join();

        this.api.addMessageCreateListener((event -> {
            if (event.getMessageAuthor().isYourself()) return;
            this.messageCreateEvent = event;
            this.hasReceivedaMessage = true;
//
//            api.getServerTextChannelById(channelID).get().sendMessage("Message received : [" + event.getMessageAuthor().getDiscriminatedName() + "] " + event.getMessageContent());
//            MinecraftClient.getInstance().getServer().getPlayerManager().getPlayerList().forEach((serverPlayerEntity -> {
//                serverPlayerEntity.sendChatMessage(new StringTextComponent(
//                        this.config.discordToMinecraft.substring(0,3).replace("%s",event.getMessageAuthor().getDisplayName()) +
//                                this.config.discordToMinecraft.substring(4,6).replace("%s",event.getMessage().toString())), ChatMessageType.CHAT);
//            }));
        }));

        ServerStartCallback.EVENT.register((minecraftServer1 -> {
            this.api.getServerTextChannelById(channelID).get().sendMessage("Server Started");
        }));

        ServerStopCallback.EVENT.register((server -> {
            this.api.getServerTextChannelById(channelID).get().sendMessage("Server Stoped");
        }));

        ServerTickCallback.EVENT.register((server -> {
            if (this.hasReceivedaMessage) {
                server.getPlayerManager().sendToAll(new StringTextComponent(
                        this.config.discordToMinecraft
                                .replace("%player",this.messageCreateEvent.getMessageAuthor().getDisplayName())
                                .replace("%message",this.messageCreateEvent.getMessageContent())));
                this.hasReceivedaMessage = false;
            }
        }));
    }

    public void sendMessage(String string) {
        if (string.contains("]") && string.contains("[")) return;
        this.api.getServerTextChannelById(channelID).get().sendMessage(string);
    }
}
