package fr.arthurbambou.fblink.discordstuff;

import com.vdurmont.emoji.EmojiParser;
import fr.arthurbambou.fblink.FBLink;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.SystemUtil;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.NoSuchElementException;

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

        if (token == "" || token.isEmpty()) {
            System.out.println("[FDLink] Please add a bot token to the config file!");
            return;
        }

        if (config.chatChannels.size() == 0) {
            System.out.println("[FDLink] Please add a game chat channel to the config file!");
            this.hasChatChannels = false;
        } else {
            this.hasChatChannels = true;
        }

        if (config.logChannels.size() == 0) {
            System.out.println("[FDLink] Please add a log channel to the config file!");
            this.hasLogChannels = false;
        } else {
            this.hasLogChannels = true;
        }

        if (!this.hasLogChannels && !this.hasChatChannels) return;

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

        if (this.hasChatChannels)
            for (int a = 0; a < this.config.chatChannels.size(); a++)
                this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(this.config.minecraftToDiscord.messages.serverStarting);

        if (this.hasLogChannels)
            for (int a = 0; a < this.config.logChannels.size(); a++)
                this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(this.config.minecraftToDiscord.messages.serverStarting);

        ServerStartCallback.EVENT.register((minecraftServer1 -> {
            try {
                startTime = minecraftServer1.getServerStartTime();
                if (this.hasChatChannels)
                    for (int a = 0; a < this.config.chatChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(config.minecraftToDiscord.messages.serverStarted);

                if (this.hasLogChannels)
                    for (int a = 0; a < this.config.logChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(config.minecraftToDiscord.messages.serverStarted);

            } catch (NoSuchElementException error) {
                System.out.println(error);
            }
        }));

        ServerStopCallback.EVENT.register((server -> {
            try {
                if (this.hasChatChannels)
                    for (int a = 0; a < this.config.chatChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(config.minecraftToDiscord.messages.serverStopped);

                if (this.hasLogChannels)
                    for (int a = 0; a < this.config.logChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(config.minecraftToDiscord.messages.serverStopped);


                this.api.disconnect();
            } catch (NoSuchElementException error) {
                System.out.println(error);
            }
        }));

        ServerTickCallback.EVENT.register((server -> {
            try {
                int playerNumber = server.getPlayerManager().getPlayerList().size();
                int maxPlayer = server.getPlayerManager().getMaxPlayerCount();
                String ip = server.getServerIp();
                int uptimeS = (int) (SystemUtil.getMeasuringTimeMs() - this.startTime) / 1000;
                int uptimeM = uptimeS / 60;
                int uptimeH = uptimeM / 60;
                uptimeM = uptimeM - (uptimeH * 60);
                uptimeS = uptimeS - (uptimeH * 60 * 60) - (uptimeM * 60);
                if (this.hasReceivedaMessage) {
                    if (this.messageCreateEvent.getMessageContent().startsWith("!playlist")) {
                        String playerlist = "";
                        for (PlayerEntity playerEntity : server.getPlayerManager().getPlayerList()) {
                            playerlist = playerlist + playerEntity.getName().getString() + "\n";
                        }
                        if (playerlist.endsWith("\n")) {
                            int a = playerlist.lastIndexOf("\n");
                            playerlist = playerlist.substring(0, a);
                        }
                        this.messageCreateEvent.getChannel().sendMessage("Players : " + server.getPlayerManager().getPlayerList().size() + "/" + server.getPlayerManager().getMaxPlayerCount() + "\n\n" + playerlist);
                    }
                    this.lastMessageD = this.config.discordToMinecraft
                            .replace("%player", this.messageCreateEvent.getMessageAuthor().getDisplayName())
                            .replace("%message", EmojiParser.parseToAliases(this.messageCreateEvent.getMessageContent()));
                    server.getPlayerManager().sendToAll(new TextComponent(this.lastMessageD));

                    this.hasReceivedaMessage = false;
                }
                if (this.hasChatChannels && this.config.minecraftToDiscord.booleans.customChannelDescription) {
                    for (int a = 0; a < this.config.chatChannels.size(); a++) {
                        ServerTextChannel channel = this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get();
                        String topic =
                                "player count : " + playerNumber + "/" + maxPlayer +
                                ",\nuptime : " + uptimeH + " h " + uptimeM + 
                                " min " + uptimeS + " second";
                        channel.updateTopic(topic);
                    }
                }
            } catch (NoSuchElementException error) {
                System.out.println(error);
            }
        }));
    }

    public void sendMessage(String string) {
        try {
            try {
                if (string.equals(this.lastMessageD)) {
                    return;
                } else {
                    if (string.startsWith("<") && this.config.minecraftToDiscord.booleans.PlayerMessages) {
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
                        if (this.hasChatChannels)
                            for (int a = 0; a < this.config.chatChannels.size(); a++)
                                this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(EmojiParser.parseToUnicode(string));
                        if (this.hasLogChannels)
                            for (int a = 0; a < this.config.logChannels.size(); a++)
                                if (!this.config.chatChannels.contains(this.config.logChannels.get(a))) {
                                    this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(EmojiParser.parseToUnicode(string));
                                }
                    } else if (string.contains("left") && this.config.minecraftToDiscord.booleans.JoinAndLeftMessages) {
                        if (this.hasChatChannels)
                            for (int a = 0; a < this.config.chatChannels.size(); a++)
                                this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(string);
                        if (this.hasLogChannels)
                            for (int a = 0; a < this.config.logChannels.size(); a++)
                                if (!this.config.chatChannels.contains(this.config.logChannels.get(a))) {
                                    this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(string);
                                }
                    } else if (string.contains("joined") && this.config.minecraftToDiscord.booleans.JoinAndLeftMessages) {
                        if (this.hasChatChannels)
                            for (int a = 0; a < this.config.chatChannels.size(); a++)
                                this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(string);
                        if (this.hasLogChannels)
                            for (int a = 0; a < this.config.logChannels.size(); a++)
                                if (!this.config.chatChannels.contains(this.config.logChannels.get(a))) {
                                    this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(string);
                                }
                    } else if (string.contains("advancement") && this.config.minecraftToDiscord.booleans.AdvancementMessages) {
                        if (this.hasChatChannels)
                            for (int a = 0; a < this.config.chatChannels.size(); a++)
                                this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(string);
                        if (this.hasLogChannels)
                            for (int a = 0; a < this.config.logChannels.size(); a++)
                                if (!this.config.chatChannels.contains(this.config.logChannels.get(a))) {
                                    this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(string);
                                }
                    } else if (string.startsWith("[") && this.config.minecraftToDiscord.booleans.LogMessages) {
                        if (this.hasChatChannels && !this.hasLogChannels)
                            for (int a = 0; a < this.config.chatChannels.size(); a++)
                                this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(string);
                        if (this.hasLogChannels)
                            for (int a = 0; a < this.config.logChannels.size(); a++)
                                this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(string);
                    } else {
                        for (String deathMethod : Lists.DEATH_LIST) {
                            if (string.contains(deathMethod) && this.config.minecraftToDiscord.booleans.DeathMessages) {
                                if (this.hasChatChannels)
                                    for (int a = 0; a < this.config.chatChannels.size(); a++)
                                        this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(string);
                                if (this.hasLogChannels)
                                    for (int a = 0; a < this.config.logChannels.size(); a++)
                                        this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(string);
                            }
                        }
                    }
                }
            } catch (NullPointerException error) {
                System.out.println(error);
            }
        } catch (NoSuchElementException error) {
            System.out.println(error);
        }
    }
}
