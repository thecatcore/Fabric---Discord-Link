package fr.arthurbambou.fblink.discordstuff;

import com.vdurmont.emoji.EmojiParser;
import fr.arthurbambou.fblink.FBLink;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.SystemUtil;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

public class DiscordBot {

    private FBLink.Config config;
    private boolean hasChatChannels;
    private boolean hasLogChannels;
    private MessageCreateEvent messageCreateEvent;
    private boolean hasReceivedaMessage;
    private String lastMessageD;
    private DiscordApi api;
    private long startTime;

    public DiscordBot(String token, FBLink.Config config) {
        this.lastMessageD = "";
        if (token == null) {
            FBLink.regenConfig();
            return;
        }

        if (token.isEmpty()) {
            System.out.println("[FDLink] Please add a bot token to the config file!");
            return;
        }

        if (config.chatChannels.isEmpty()) {
            System.out.println("[FDLink] Please add a game chat channel to the config file!");
            this.hasChatChannels = false;
        } else {
            this.hasChatChannels = true;
        }

        if (config.logChannels.isEmpty()) {
            System.out.println("[FDLink] Please add a log channel to the config file!");
            this.hasLogChannels = false;
        } else {
            this.hasLogChannels = true;
        }

        if (!this.hasLogChannels && !this.hasChatChannels) return;

        config.logChannels.removeIf(id -> config.chatChannels.contains(id));

        this.config = config;
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
        api.addMessageCreateListener((event -> {
            if (event.getMessageAuthor().isBotUser() && this.config.ignoreBots) return;
            if (!this.hasChatChannels) return;
            if (event.getMessageAuthor().isYourself()) return;
            if (!this.config.chatChannels.contains(event.getChannel().getIdAsString())) return;
            this.messageCreateEvent = event;
            this.hasReceivedaMessage = true;
        }));
        this.api = api;

        sendToAllChannels(this.config.minecraftToDiscord.messages.serverStarting);

        ServerStartCallback.EVENT.register((server -> {
            startTime = server.getServerStartTime();
            sendToAllChannels(this.config.minecraftToDiscord.messages.serverStarted);
        }));

        ServerStopCallback.EVENT.register((server -> {
            sendToAllChannels(config.minecraftToDiscord.messages.serverStopped);
            this.api.disconnect();
        }));

        ServerTickCallback.EVENT.register((server -> {
            int playerNumber = server.getPlayerManager().getPlayerList().size();
            int maxPlayer = server.getPlayerManager().getMaxPlayerCount();
            if (this.hasReceivedaMessage) {
                if (this.messageCreateEvent.getMessageContent().startsWith("!playlist")) {
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
                this.lastMessageD = this.config.discordToMinecraft
                        .replace("%player", this.messageCreateEvent.getMessageAuthor().getDisplayName())
                        .replace("%message", EmojiParser.parseToAliases(this.messageCreateEvent.getMessageContent()));
                server.getPlayerManager().sendToAll(new LiteralText(this.lastMessageD));

                this.hasReceivedaMessage = false;
            }
            if (this.hasChatChannels && this.config.minecraftToDiscord.booleans.customChannelDescription) {
                int totalUptimeSeconds = (int) (SystemUtil.getMeasuringTimeMs() - this.startTime) / 1000;
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

    public void sendMessage(Text text, MessageType messageType) {
        try {
            if (text.asString().equals(this.lastMessageD)) {
                return;
            }

            if (MessageType.CHAT.equals(messageType) && this.config.minecraftToDiscord.booleans.PlayerMessages) {
                String string = text.asString();
                if (this.config.minecraftToDiscord.booleans.MCtoDiscordTag) {
                    for (User user : this.api.getCachedUsers()) {
                        ServerChannel serverChannel = (ServerChannel) this.api.getServerChannels().toArray()[0];
                        Server server = serverChannel.getServer();
                        string = string.replace(user.getName(), user.getMentionTag());
                        string = string.replace(user.getDisplayName(server), user.getMentionTag());
                        string = string.replace(user.getName().toLowerCase(), user.getMentionTag());
                        string = string.replace(user.getDisplayName(server).toLowerCase(), user.getMentionTag());
                    }
                }
                sendToAllChannels(string);
            } else if (((TranslatableText) text).getKey().equals("multiplayer.player.left") && this.config.minecraftToDiscord.booleans.JoinAndLeftMessages) {
                sendToAllChannels(text.asString());
            } else if (((TranslatableText) text).getKey().startsWith("multiplayer.player.joined") && this.config.minecraftToDiscord.booleans.JoinAndLeftMessages) {
                sendToAllChannels(text.asString());
            } else if (((TranslatableText) text).getKey().startsWith("chat.type.advancement.") && this.config.minecraftToDiscord.booleans.AdvancementMessages) {
                sendToAllChannels(text.formatted(Formatting.RESET).asString());
            } else if (((TranslatableText) text).getKey().startsWith("commands.") && this.config.minecraftToDiscord.booleans.LogMessages) {
                if (this.hasLogChannels) {
                    sendToLogChannels(text.asString());
                } else {
                    sendToChatChannels(text.asString());
                }
            } else if (((TranslatableText) text).getKey().startsWith("death.") && this.config.minecraftToDiscord.booleans.DeathMessages) {
                sendToAllChannels(text.asString());
            }
        } catch (NullPointerException error) {
            error.printStackTrace();
        }
    }

    private void sendToAllChannels(String message) {
        sendToChatChannels(message);
        sendToLogChannels(message);
    }

    private void sendToLogChannels(String message) {
        if (this.hasLogChannels) {
            for (String id : this.config.logChannels) {
                this.api.getServerTextChannelById(id).ifPresent(channel -> channel.sendMessage(message));
            }
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
