package fr.arthurbambou.fdlink.discordstuff;

import com.vdurmont.emoji.EmojiParser;
import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.discord.Commands;
import fr.arthurbambou.fdlink.versionhelpers.CrossVersionHandler;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.ClickEvent;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DiscordBot {
    public static final Logger LOGGER = LogManager.getLogger();
    private MessageReceivedListener messageCreateListener;
    protected MinecraftToDiscordHandler minecraftToDiscordHandler = null;

    protected FDLink.Config config;
    public boolean hasChatChannels;
    public boolean hasLogChannels;
    protected MessageReceivedEvent messageCreateEvent;
    protected boolean hasReceivedMessage;
    public String lastMessageD;
    protected static List<String> lastMessageMs = new ArrayList<>();
    protected JDA api = null;
    protected long startTime;
    protected boolean stopping = false;

    public DiscordBot(String token, FDLink.Config config) {
        this.lastMessageD = "null";

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
        try {
            this.api = JDABuilder.createDefault(token).setActivity(Activity.playing(this.config.discordToMinecraft.commandPrefix + "commands")).build();
        } catch (LoginException error) {
            error.printStackTrace();
        }
        this.messageCreateListener = new MessageReceivedListener(this);
        this.api.addEventListener(messageCreateListener);
        this.minecraftToDiscordHandler = new MinecraftToDiscordHandler(this);
    }

    public void serverStarting() {
        if (this.config.minecraftToDiscord.chatChannels.serverStartingMessage) sendToChatChannels(config.minecraftToDiscord.messages.serverStarting);
        if (this.config.minecraftToDiscord.logChannels.serverStartingMessage) sendToLogChannels(config.minecraftToDiscord.messages.serverStarting);
    }

    public void serverStarted() {
        startTime = System.currentTimeMillis();
        if (this.config.minecraftToDiscord.chatChannels.serverStartMessage) sendToChatChannels(config.minecraftToDiscord.messages.serverStarted);
        if (this.config.minecraftToDiscord.logChannels.serverStartMessage) sendToLogChannels(config.minecraftToDiscord.messages.serverStarted);
    }

    public void serverStopping() {
        this.api.removeEventListener(this.messageCreateListener);
        this.stopping = true;
        if (this.config.minecraftToDiscord.chatChannels.serverStoppingMessage) sendToChatChannels(config.minecraftToDiscord.messages.serverStopping);
        if (this.config.minecraftToDiscord.logChannels.serverStoppingMessage) sendToLogChannels(config.minecraftToDiscord.messages.serverStopping);
    }

    public void serverStopped() {
        if (this.config.minecraftToDiscord.chatChannels.serverStopMessage || this.config.minecraftToDiscord.logChannels.serverStopMessage) {
            ArrayList<CompletableFuture<Message>> requests = new ArrayList<>();
            if(this.config.minecraftToDiscord.chatChannels.serverStopMessage && this.hasChatChannels) requests.addAll(sendToChatChannels(config.minecraftToDiscord.messages.serverStopped, requests));
            if(this.config.minecraftToDiscord.logChannels.serverStopMessage && this.hasLogChannels) requests.addAll(sendToLogChannels(config.minecraftToDiscord.messages.serverStopped, requests));

            for (CompletableFuture<Message> request : requests){
                while (!request.isDone()) {
                    if (this.config.minecraftToDiscord.general.enableDebugLogs) LOGGER.info("Request is not done yet!");
                }
            }
        }
        this.api.shutdown();
    }

    public void serverTick(MinecraftServer server) {
        int playerNumber = server.getPlayerCount();
        int maxPlayer = server.getMaxPlayerCount();
        if (this.hasReceivedMessage) {
            for (Commands command : Commands.values()) {
                if (this.messageCreateEvent.getMessage().getContentRaw().toLowerCase().equals(this.config.discordToMinecraft.commandPrefix + command.name().toLowerCase())) {
                    this.hasReceivedMessage = command.execute(server, this.messageCreateEvent, this.startTime);
                    return;
                }
            }
            this.lastMessageD = this.config.discordToMinecraft.message
                    .replace("%player", this.messageCreateEvent.getAuthor().getName());
            String string_message = EmojiParser.parseToAliases(this.messageCreateEvent.getMessage().getContentRaw());
            for (FDLink.Config.EmojiEntry emojiEntry : this.config.emojiMap) {
                string_message = string_message.replace("<" + emojiEntry.id + ">", emojiEntry.name);
            }
            if (this.config.minecraftToDiscord.chatChannels.minecraftToDiscordTag || this.config.minecraftToDiscord.logChannels.minecraftToDiscordTag) {
                for (User user : this.api.getUserCache()) {
                    TextChannel serverChannel = (TextChannel) this.api.getTextChannels().toArray()[0];
                    Guild discordServer = serverChannel.getGuild();
                    String string_discriminator = "";
                    if (this.config.minecraftToDiscord.chatChannels.minecraftToDiscordDiscriminator || this.config.minecraftToDiscord.logChannels.minecraftToDiscordDiscriminator){
                        string_discriminator = "#" + user.getDiscriminator();
                    }
                    string_message = string_message.replace("<@!" + user.getId() + ">", "@" + user.getName() + string_discriminator);
//                        if (user.(discordServer).isPresent() && this.config.discordToMinecraft.pingLongVersion) {
//                            string_message = string_message.replace("@" + user.getName(), "@" + user.getDisplayName(discordServer) + "(" + user.getName() + string_discriminator + ")");
//                        }
                }
            }
            Style style = Style.EMPTY;
            if (!this.messageCreateEvent.getMessage().getAttachments().isEmpty()) {
                this.lastMessageD = this.lastMessageD.replace("%message", string_message + " (Click to open attachment URL)");
                style = style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, this.messageCreateEvent.getMessage().getAttachments().get(0).getUrl()));
            } else {
                this.lastMessageD = this.lastMessageD.replace("%message", string_message);
            }
            CrossVersionHandler.sendMessageToChat(server, this.lastMessageD, style);

            this.hasReceivedMessage = false;
        }
        if ((this.hasChatChannels || this.hasLogChannels) && (this.config.minecraftToDiscord.chatChannels.customChannelDescription ||  this.config.minecraftToDiscord.logChannels.customChannelDescription) && ((System.currentTimeMillis()/1000) % 300 == 0)) {
            int totalUptimeSeconds = (int) (System.currentTimeMillis() - this.startTime) / 1000;
            final int uptimeH = totalUptimeSeconds / 3600 ;
            final int uptimeM = (totalUptimeSeconds % 3600) / 60;
            final int uptimeS = totalUptimeSeconds % 60;

            if(this.config.minecraftToDiscord.chatChannels.customChannelDescription){
                for (String id : this.config.chatChannels) {
//                        this.api.getTextChannelById(id).getTopic() = String.format(
//                             "Playercount : %d/%d,\n" +
//                                     "Uptime : %dh %dm %ds",
//                             playerNumber, maxPlayer, uptimeH, uptimeM, uptimeS
//                    );
                }
            }
            if(this.config.minecraftToDiscord.logChannels.customChannelDescription){
                for (String id : this.config.logChannels) {
//                        this.api.getServerTextChannelById(id).ifPresent(channel ->
//                               channel.updateTopic(String.format(
//                             "Playercount : %d/%d,\n" +
//                                     "Uptime : %dh %dm %ds",
//                             playerNumber, maxPlayer, uptimeH, uptimeM, uptimeS
//                    )));
                }
            }
        }
    }

    public void sendMessage(fr.arthurbambou.fdlink.versionhelpers.minecraft.Message message) {
        if (this.minecraftToDiscordHandler != null && !this.stopping) this.minecraftToDiscordHandler.handleTexts(message);
    }

/*     public List<CompletableFuture<Message>> sendToAllChannels(String message) {
        List<CompletableFuture<Message>> requests = new ArrayList<>();
        if (this.hasLogChannels) {
            requests.add(sendToLogChannels(message));
        }
        requests.add(sendToChatChannels(message));
        return requests;
    } */

    public List<CompletableFuture<Message>> sendToLogChannels(String message) {
        return this.sendToLogChannels(message, new ArrayList<>());
    }

    public List<CompletableFuture<Message>> sendToChatChannels(String message) {
        return this.sendToChatChannels(message, new ArrayList<>());
    }

    /**
     * This method will no longer send to chat channel as fallback if no log channel is present since log channels have their own config now
     * @param message the message to send
     * @return
     */
    public List<CompletableFuture<Message>> sendToLogChannels(String message, List<CompletableFuture<Message>> list) {
        if (this.hasLogChannels && this.api != null) {
            for (String id : this.config.logChannels) {
                try {
                    this.api = this.api.awaitReady();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TextChannel channel = this.api.getTextChannelById(id);
                list.add(channel.sendMessage(message).submit());
                lastMessageMs.add(message);
            }
        }
        return list;
    }

    public List<CompletableFuture<Message>> sendToChatChannels(String message, List<CompletableFuture<Message>> list) {
        if (this.hasChatChannels && this.api != null) {
            for (String id : this.config.chatChannels) {
                try {
                    this.api = this.api.awaitReady();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TextChannel channel = this.api.getTextChannelById(id);
                while (channel == null) {
                    channel = this.api.getTextChannelById(id);
                }
                list.add(channel.sendMessage(message).submit());
                lastMessageMs.add(message);
            }
        }
        return list;
    }
}
