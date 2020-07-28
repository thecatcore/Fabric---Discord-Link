package fr.arthurbambou.fdlink.discordstuff;

import com.vdurmont.emoji.EmojiParser;
import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.discordstuff.todiscord.MinecraftToDiscordHandler;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents.ServerStopping;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.*;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DiscordBot {
    public static final Logger LOGGER = LogManager.getLogger();
    private MinecraftToDiscordHandler minecraftToDiscordHandler = null;

    private FDLink.Config config;
    public boolean hasChatChannels;
    public boolean hasLogChannels;
    private MessageCreateEvent messageCreateEvent;
    private boolean hasReceivedMessage;
    public String lastMessageD;
    private DiscordApi api = null;
    private long startTime;
    private int ticks;

    public DiscordBot(String token, FDLink.Config config) {
        this.ticks = 0;
        this.lastMessageD = "";
        if (token == null) {
            FDLink.regenConfig();
            return;
        }

        if (token.isEmpty()) {
            LOGGER.error("[FDLink] Please add a bot token to the config file!");
            return;
        }

        if (config.chatChannels.isEmpty()) {
            LOGGER.info("[FDLink] Please add a game chat channel to the config file!");
            this.hasChatChannels = false;
        } else {
            this.hasChatChannels = true;
        }

        if (config.logChannels.isEmpty()) {
            LOGGER.info("[FDLink] Please add a log channel to the config file!");
            this.hasLogChannels = false;
        } else {
            this.hasLogChannels = true;
        }

        if (!this.hasLogChannels && !this.hasChatChannels) return;

        config.logChannels.removeIf(id -> config.chatChannels.contains(id));

        this.config = config;
        this.api = new DiscordApiBuilder().setToken(token).login().join();
        MessageCreateListener messageCreateListener = (event -> {
            if (event.getMessageAuthor().isBotUser() && this.config.ignoreBots) return;
            if (!this.hasChatChannels) return;
            if (event.getMessageAuthor().isYourself()) return;
            if (!this.config.chatChannels.contains(event.getChannel().getIdAsString())) return;
            this.messageCreateEvent = event;
            this.hasReceivedMessage = true;
        });
        this.api.addMessageCreateListener(messageCreateListener);
        this.minecraftToDiscordHandler = new MinecraftToDiscordHandler(this.api, this, this.config);

        if (this.config.minecraftToDiscord.chatChannels.serverStartingMessage || this.config.minecraftToDiscord.logChannels.serverStartingMessage) {
            ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> {
                if (this.config.minecraftToDiscord.chatChannels.serverStoppingMessage) sendToChatChannels(config.minecraftToDiscord.messages.serverStarting);
                if (this.config.minecraftToDiscord.logChannels.serverStoppingMessage) sendToLogChannels(config.minecraftToDiscord.messages.serverStarting);
            });
        }

        if (this.config.minecraftToDiscord.chatChannels.serverStartMessage || this.config.minecraftToDiscord.logChannels.serverStartMessage) {
            ServerLifecycleEvents.SERVER_STARTED.register((server -> {
                startTime = server.getServerStartTime();
                if (this.config.minecraftToDiscord.chatChannels.serverStartMessage) sendToChatChannels(config.minecraftToDiscord.messages.serverStarted);
                if (this.config.minecraftToDiscord.logChannels.serverStartMessage) sendToLogChannels(config.minecraftToDiscord.messages.serverStarted);
            }));
        }
        ServerLifecycleEvents.SERVER_STOPPING.register(minecraftServer -> {
            this.api.removeListener(MessageCreateListener.class, messageCreateListener);
            if (this.config.minecraftToDiscord.chatChannels.serverStoppingMessage) sendToChatChannels(config.minecraftToDiscord.messages.serverStopping);
            if (this.config.minecraftToDiscord.logChannels.serverStoppingMessage) sendToLogChannels(config.minecraftToDiscord.messages.serverStopping);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register((server -> {
            if (this.config.minecraftToDiscord.chatChannels.serverStopMessage || this.config.minecraftToDiscord.logChannels.serverStopMessage) {
                List<CompletableFuture<Message>> requests = new ArrayList<>();
                if(this.config.minecraftToDiscord.chatChannels.serverStopMessage) requests.add(sendToChatChannels(config.minecraftToDiscord.messages.serverStopped));
                if(this.config.minecraftToDiscord.logChannels.serverStopMessage) requests.add(sendToLogChannels(config.minecraftToDiscord.messages.serverStopped));
                for (CompletableFuture<Message> request : requests) {
                    while (!request.isDone()) {
                        if (this.config.minecraftToDiscord.general.enableDebugLogs) LOGGER.info("Request is not done yet!");
                    }
                }
            }
            this.api.disconnect();
        }));

        ServerTickEvents.START_SERVER_TICK.register((server -> {
            this.ticks++;
            int playerNumber = server.getPlayerManager().getPlayerList().size();
            int maxPlayer = server.getPlayerManager().getMaxPlayerCount();
            if (this.hasReceivedMessage) {
                if (this.messageCreateEvent.getMessageContent().startsWith("!playerlist")) {
                    StringBuilder playerlist = new StringBuilder();
                    for (PlayerEntity playerEntity : server.getPlayerManager().getPlayerList()) {
                        playerlist.append(playerEntity.getName().getString()).append("\n");
                    }
                    if (playerlist.toString().endsWith("\n")) {
                        int a = playerlist.lastIndexOf("\n");
                        playerlist = new StringBuilder(playerlist.substring(0, a));
                    }
                    this.messageCreateEvent.getChannel().sendMessage("Players : " + server.getPlayerManager().getPlayerList().size() + "/" + server.getPlayerManager().getMaxPlayerCount() + "\n\n" + playerlist);
                }
                this.lastMessageD = this.config.discordToMinecraft.message
                        .replace("%player", this.messageCreateEvent.getMessageAuthor().getDisplayName());
                String string_message = EmojiParser.parseToAliases(this.messageCreateEvent.getMessageContent());
                for (FDLink.Config.EmojiEntry emojiEntry : this.config.emojiMap) {
                    string_message = string_message.replace("<" + emojiEntry.id + ">", emojiEntry.name);
                }
                if (this.config.minecraftToDiscord.chatChannels.minecraftToDiscordTag || this.config.minecraftToDiscord.logChannels.minecraftToDiscordTag) {
                    for (User user : this.api.getCachedUsers()) {
                        ServerChannel serverChannel = (ServerChannel) this.api.getServerChannels().toArray()[0];
                        Server discordServer = serverChannel.getServer();
                        String string_discriminator = "";
                        if (this.config.minecraftToDiscord.chatChannels.minecraftToDiscordDiscriminator || this.config.minecraftToDiscord.logChannels.minecraftToDiscordDiscriminator){
                            string_discriminator = "#" + user.getDiscriminator();
                        }
                        string_message = string_message.replace("<@!" + user.getIdAsString() + ">", "@" + user.getDisplayName(discordServer) + string_discriminator);
                        if (user.getNickname(discordServer).isPresent() && this.config.discordToMinecraft.pingLongVersion) {
                            string_message = string_message.replace("@" + user.getName(), "@" + user.getDisplayName(discordServer) + "(" + user.getName() + string_discriminator + ")");
                        }
                    }
                }
                Style style = Style.EMPTY;
                if (!this.messageCreateEvent.getMessageAttachments().isEmpty()) {
                    this.lastMessageD = this.lastMessageD.replace("%message", string_message + " (Click to open attachment URL)");
                    style = style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, this.messageCreateEvent.getMessageAttachments().get(0).getUrl().toString()));
                } else {
                    this.lastMessageD = this.lastMessageD.replace("%message", string_message);
                }
                server.getPlayerManager().sendToAll(new GameMessageS2CPacket(new LiteralText(this.lastMessageD).setStyle(style), MessageType.CHAT, UUID.randomUUID()));

                this.hasReceivedMessage = false;
            }
            if (this.hasChatChannels && (this.config.minecraftToDiscord.chatChannels.customChannelDescription ||  this.config.minecraftToDiscord.logChannels.customChannelDescription) && this.ticks >= 200) {
                this.ticks = 0;
                int totalUptimeSeconds = (int) (Util.getMeasuringTimeMs() - this.startTime) / 1000;
                final int uptimeH = totalUptimeSeconds / 3600 ;
                final int uptimeM = (totalUptimeSeconds % 3600) / 60;
                final int uptimeS = totalUptimeSeconds % 60;

                for (String id : this.config.chatChannels) {
                    this.api.getServerTextChannelById(id).ifPresent(channel ->
                            channel.updateTopic(String.format(
                            "player count : %d/%d,\n" +
                                    "uptime : %d h %d min %d second",
                            playerNumber, maxPlayer, uptimeH, uptimeM, uptimeS
                    )));
                }
            }
        }));
    }

    public void sendMessage(Text text) {
        if (this.minecraftToDiscordHandler != null) this.minecraftToDiscordHandler.handleTexts(text);
    }

    public List<CompletableFuture<Message>> sendToAllChannels(String message) {
        List<CompletableFuture<Message>> requests = new ArrayList<>();
        if (this.hasLogChannels) {
            requests.add(sendToLogChannels(message));
        }
        requests.add(sendToChatChannels(message));
        return requests;
    }

    /**
     * This method will send to chat channel as fallback if no log channel is present
     * @param message the message to send
     * @return
     */
    public CompletableFuture<Message> sendToLogChannels(String message) {
        if (this.hasLogChannels) {
            for (String id : this.config.logChannels) {
                Optional<ServerTextChannel> channel = this.api.getServerTextChannelById(id);
                if (channel.isPresent()) {
                    return channel.get().sendMessage(message);
                }
            }
        }
        return null;
    }

    public CompletableFuture<Message> sendToChatChannels(String message) {
        if (this.hasChatChannels) {
            for (String id : this.config.chatChannels) {
                Optional<ServerTextChannel> channel = this.api.getServerTextChannelById(id);
                if (channel.isPresent()) {
                    return channel.get().sendMessage(message);
                }
            }
        }
        return null;
    }
}
