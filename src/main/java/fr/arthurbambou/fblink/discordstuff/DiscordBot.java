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
    private MinecraftServer minecraftServer;

    public DiscordBot(String token, FBLink.Config config) {
        if (token == null) {
            FBLink.regenConfig();
            return;
        }

        if (token == "") {
            return;
        }
        this.config = config;

        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        api.addMessageCreateListener((event -> {
            System.out.println(event.getMessageAuthor().getDisplayName() + ":" + event.getMessage().toString());
            this.minecraftServer.getPlayerManager().getPlayerList().forEach((serverPlayerEntity -> {
                serverPlayerEntity.sendChatMessage(new StringTextComponent(
                        this.config.discordToMinecraft.substring(0,3).replace("%s",event.getMessageAuthor().getDisplayName()) +
                                this.config.discordToMinecraft.substring(4,6).replace("%s",event.getMessage().toString())), ChatMessageType.CHAT);
            }));
        }));

        ServerStartCallback.EVENT.register((minecraftServer1 -> {
            api.getServerTextChannelById(channelID).get().sendMessage("Server Started");
        }));

        ServerStopCallback.EVENT.register((server -> {
            api.getServerTextChannelById(channelID).get().sendMessage("Server Stoped");
        }));

        ServerTickCallback.EVENT.register((server -> {
            this.minecraftServer = server;
        }));
    }
}
