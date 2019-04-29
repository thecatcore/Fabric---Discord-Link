package fr.arthurbambou.fblink.discordstuff;

import fr.arthurbambou.fblink.FBLink;
import fr.arthurbambou.fblink.discordstuff.commands.PlayerList;
import fr.arthurbambou.fblink.discordstuff.enums.EmojiTranslater;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.StringTextComponent;
import net.minecraft.util.SystemUtil;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.Collection;

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

        if (token == "") {
            System.out.println("[FDLink] Please add a bot token to the config file !");
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

        if (this.hasChatChannels)
            for (int a = 0; a < this.config.chatChannels.size(); a++)
                this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(config.minecraftToDiscordMessage.serverStarting);

        if (this.hasLogChannels)
            for (int a = 0; a < this.config.logChannels.size(); a++)
                this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(config.minecraftToDiscordMessage.serverStarting);

        this.api.addMessageCreateListener((event -> {
            if (event.getMessageAuthor().isBotOwner() && this.config.ignoreBots) return;
            if (!hasChatChannels) return;
            if (event.getMessageAuthor().isYourself()) return;
            if (!this.config.chatChannels.contains(event.getChannel().getIdAsString())) return;
            this.messageCreateEvent = event;
            this.hasReceivedaMessage = true;
        }));

//        this.api.addMessageCreateListener(new PlayerList());

        ServerStartCallback.EVENT.register((minecraftServer1 -> {
            startTime = minecraftServer1.getServerStartTime();
            if (this.hasChatChannels)
                for (int a = 0; a < this.config.chatChannels.size(); a++)
                    this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(config.minecraftToDiscordMessage.serverStarted);

            if (this.hasLogChannels)
                for (int a = 0; a < this.config.logChannels.size(); a++)
                    this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(config.minecraftToDiscordMessage.serverStarted);
        }));

        ServerStopCallback.EVENT.register((server -> {
            if (this.hasChatChannels)
                for (int a = 0; a < this.config.chatChannels.size(); a++)
                    this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(config.minecraftToDiscordMessage.serverStopped);

            if (this.hasLogChannels)
                for (int a = 0; a < this.config.logChannels.size(); a++)
                    this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(config.minecraftToDiscordMessage.serverStopped);


            this.api.disconnect();
        }));

        ServerTickCallback.EVENT.register((server -> {
            int playerNumber = server.getPlayerManager().getPlayerList().size();
            int maxPlayer = server.getPlayerManager().getMaxPlayerCount();
            String ip = server.getServerIp();
            int uptimeS = (int)(SystemUtil.getMeasuringTimeMs() - this.startTime) / 1000;
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
                        playerlist = playerlist.substring(0,a);
                    }
                    this.messageCreateEvent.getChannel().sendMessage("Players : " + server.getPlayerManager().getPlayerList().size()+"/" + server.getPlayerManager().getMaxPlayerCount() + "\n\n" + playerlist);
                }
                this.lastMessageD = this.config.discordToMinecraft
                        .replace("%player",this.messageCreateEvent.getMessageAuthor().getDisplayName())
                        .replace("%message",this.messageCreateEvent.getMessageContent());

                this.messageCreateEvent.getChannel().sendMessage(this.lastMessageD.replace(":", "d"));
                for (EmojiTranslater emoji : EmojiTranslater.values()) {
                    this.lastMessageD = this.lastMessageD.replace(emoji.discordID, emoji.minecraftID);
                }
                server.getPlayerManager().sendToAll(new StringTextComponent(this.lastMessageD));

                this.hasReceivedaMessage = false;
            }
            if (this.hasChatChannels && this.config.customChannelDescription) {
                for (int a = 0; a < this.config.chatChannels.size(); a++) {
                    ServerTextChannel channel = this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get();
                    String topic =
//                            "ip : " + ip + ",\n" +
                            "player count : " + playerNumber + "/" + maxPlayer +
                            ",\nuptime : " + uptimeH + " h " + uptimeM + " min " + uptimeS + " second";
                    channel.updateTopic(topic);
                }
            }
        }));
    }

    public void sendMessage(String string) {
        if (string.equals(this.lastMessageD)) { return; }
        else {
            if (string.startsWith("<")) {
//                String username = string.split(">")[0].replace("<", "");
//                if (!this.api.getCachedUsersByName(username).isEmpty()) {
//                    User[] users = (User[]) this.api.getCachedUsersByName(username).toArray();
//                    string.replace(username, users[0].getMentionTag());
//                }
                if (this.config.MCtoDiscordTag) {
                    for (User user : this.api.getCachedUsers()) {
                        ServerChannel serverChannel = (ServerChannel) this.api.getServerChannels().toArray()[0];
                        Server server = serverChannel.getServer();
                        if (string.contains(user.getName()) || string.contains(user.getDisplayName(server))) {
                            string = string.replace(user.getName(), user.getMentionTag());
                            string = string.replace(user.getDisplayName(server), user.getMentionTag());
                            break;
                        }
                        if (string.contains(user.getName().toLowerCase()) || string.contains(user.getDisplayName(server).toLowerCase())) {
                            string = string.replace(user.getName().toLowerCase(), user.getMentionTag());
                            string = string.replace(user.getDisplayName(server).toLowerCase(), user.getMentionTag());
                            break;
                        }
                    }
                }
                if (this.hasChatChannels)
                    for (int a = 0; a < this.config.chatChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(string);
                if (this.hasLogChannels)
                    for (int a = 0; a < this.config.logChannels.size(); a++)
                        if (!this.config.chatChannels.contains(this.config.logChannels.get(a))) {
                            this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(string);
                        }
            } else if (string.contains("left")) {
                if (this.hasChatChannels)
                    for (int a = 0; a < this.config.chatChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(string);
                if (this.hasLogChannels)
                    for (int a = 0; a < this.config.logChannels.size(); a++)
                        if (!this.config.chatChannels.contains(this.config.logChannels.get(a))) {
                            this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(string);
                        }
            } else if (string.contains("joined")) {
                if (this.hasChatChannels)
                    for (int a = 0; a < this.config.chatChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(string);
                if (this.hasLogChannels)
                    for (int a = 0; a < this.config.logChannels.size(); a++)
                        if (!this.config.chatChannels.contains(this.config.logChannels.get(a))) {
                            this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(string);
                        }
            } else if (string.contains("advancement")) {
                if (this.hasChatChannels)
                    for (int a = 0; a < this.config.chatChannels.size(); a++)
                        this.api.getServerTextChannelById(this.config.chatChannels.get(a)).get().sendMessage(string);
                if (this.hasLogChannels)
                    for (int a = 0; a < this.config.logChannels.size(); a++)
                        if (!this.config.chatChannels.contains(this.config.logChannels.get(a))) {
                            this.api.getServerTextChannelById(this.config.logChannels.get(a)).get().sendMessage(string);
                        }
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
