package fr.arthurbambou.fdlink.discordstuff;

import com.vdurmont.emoji.EmojiParser;
import fr.arthurbambou.fdlink.FDLink;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class DiscordBot {
    public static final Logger LOGGER = LogManager.getLogger();

    private FDLink.Config config;
    private boolean hasChatChannels;
    private boolean hasLogChannels;
    private MessageCreateEvent messageCreateEvent;
    private boolean hasReceivedMessage;
    private String lastMessageD;
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
        DiscordApi api1 = new DiscordApiBuilder().setToken(token).login().join();
        api1.addMessageCreateListener((event -> {
            if (event.getMessageAuthor().isBotUser() && this.config.ignoreBots) return;
            if (!this.hasChatChannels) return;
            if (event.getMessageAuthor().isYourself()) return;
            if (!this.config.chatChannels.contains(event.getChannel().getIdAsString())) return;
            this.messageCreateEvent = event;
            this.hasReceivedMessage = true;
        }));
        this.api = api1;
        api1 = null;

        if (this.config.minecraftToDiscord.booleans.serverStartingMessage) sendToAllChannels(this.config.minecraftToDiscord.messages.serverStarting);

        if (this.config.minecraftToDiscord.booleans.serverStartMessage) {
            ServerStartCallback.EVENT.register((server -> {
                startTime = server.getServerStartTime();
                sendToAllChannels(this.config.minecraftToDiscord.messages.serverStarted);
            }));
        }
        if (this.config.minecraftToDiscord.booleans.serverStopMessage) {
            ServerStopCallback.EVENT.register((server -> {
                sendToAllChannels(config.minecraftToDiscord.messages.serverStopped);
                this.api.disconnect();
                this.api = null;
            }));
        }

        ServerTickCallback.EVENT.register((server -> {
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
                if (this.config.minecraftToDiscord.booleans.minecraftToDiscordTag) {
                    for (User user : this.api.getCachedUsers()) {
                        ServerChannel serverChannel = (ServerChannel) this.api.getServerChannels().toArray()[0];
                        Server discordServer = serverChannel.getServer();
                        String string_discriminator = "";
                        if(this.config.minecraftToDiscord.booleans.minecraftToDiscordDiscriminator){
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
                server.getPlayerManager().sendToAll(new LiteralText(this.lastMessageD).setStyle(style));

                this.hasReceivedMessage = false;
            }
            if (this.hasChatChannels && this.config.minecraftToDiscord.booleans.customChannelDescription && this.ticks >= 200) {
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
        if (this.api == null || (!this.hasChatChannels && !this.hasLogChannels)) return;
        if (text.getString().equals(this.lastMessageD)) {
            return;
        }

        if (!(text instanceof TranslatableText)) {
            sendToLogChannels(text.getString());
            return;
        }

        String key = ((TranslatableText) text).getKey();
        String message = text.getString();
        message = message.replaceAll("§[b0931825467adcfeklmnor]", "");
        LOGGER.debug(this.config.toString());
        if (key.equals("chat.type.text") && this.config.minecraftToDiscord.booleans.playerMessages) {
            // Handle normal chat
            if (this.config.minecraftToDiscord.booleans.minecraftToDiscordTag) {
                for (User user : this.api.getCachedUsers()) {
                    ServerChannel serverChannel = (ServerChannel) this.api.getServerChannels().toArray()[0];
                    Server server = serverChannel.getServer();
                    message = message
                            .replace("@" + user.getName(), user.getMentionTag())
                            .replace("@" + user.getDisplayName(server), user.getMentionTag())
                            .replace("@" + user.getName().toLowerCase(), user.getMentionTag())
                            .replace("@" + user.getDisplayName(server).toLowerCase(), user.getMentionTag());
                    if (user.getNickname(server).isPresent()) {
                        message = message
                                .replace("@" + user.getNickname(server).get(), user.getMentionTag())
                                .replace("@" + user.getNickname(server).get().toLowerCase(), user.getMentionTag());
                    }
                }
            }
            sendToAllChannels(message);

        } else if (key.equals("chat.type.emote") || key.equals("chat.type.announcement") // Handling /me and /say command
                || (key.startsWith("multiplayer.player.") && this.config.minecraftToDiscord.booleans.joinAndLeftMessages)
                || (key.startsWith("chat.type.advancement.") && this.config.minecraftToDiscord.booleans.advancementMessages)
                || (key.startsWith("death.") && this.config.minecraftToDiscord.booleans.deathMessages)
        ) {
            sendToAllChannels(message);

        } else if (key.equals("chat.type.admin")) {
            sendToLogChannels(message);

        } else {
            LOGGER.info("[FDLink] Unhandled text \"{}\":{}", key, message);
        }
    }

    private void sendToAllChannels(String message) {
        if (this.hasLogChannels) {
            sendToLogChannels(message);
        }
        sendToChatChannels(message);
    }

    /**
     * This method will send to chat channel as fallback if no log channel is present
     * @param message the message to send
     */
    public void sendToLogChannels(String message) {
        if (this.hasLogChannels) {
            for (String id : this.config.logChannels) {
                this.api.getServerTextChannelById(id).ifPresent(channel -> channel.sendMessage(message));
            }
        } else {
            sendToChatChannels(message);
        }
    }

    private void sendToChatChannels(String message) {
        if (this.hasChatChannels) {
            for (String id : this.config.chatChannels) {
                this.api.getServerTextChannelById(id).ifPresent(channel -> channel.sendMessage(message));
            }
        }
    }
}
