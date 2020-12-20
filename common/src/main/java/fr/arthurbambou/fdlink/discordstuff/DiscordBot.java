package fr.arthurbambou.fdlink.discordstuff;

import com.vdurmont.emoji.EmojiParser;
import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.config.Config;
import fr.arthurbambou.fdlink.config.MainConfig;
import fr.arthurbambou.fdlink.discord.Commands;
import fr.arthurbambou.fdlink.versionhelpers.CrossVersionHandler;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.ClickEvent;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DiscordBot {
    public static final Logger LOGGER = LogManager.getLogger();
    private MessageReceivedListener messageCreateListener;
    protected MinecraftToDiscordHandler minecraftToDiscordHandler = null;

    protected Config config;
    public boolean hasChatChannels;
    public boolean hasLogChannels;
    protected MessageReceivedEvent messageCreateEvent;
    protected boolean hasReceivedMessage;
    public String lastMessageD;
    protected static List<String> lastMessageMs = new ArrayList<>();
    protected JDA api = null;
    protected long startTime;
    protected boolean stopping = false;
    // This is when we scheduled the next channel topic update. Should happen every five minutes.
    private long nextChannelTopicUpdateTimeMilliseconds = Long.MIN_VALUE;

    private boolean firstTick = true;
    private boolean updatedActivity = false;

    public DiscordBot(String token, Config config) {
        this.lastMessageD = "null";

        if (token == null) {
            FDLink.regenConfig();
            return;
        }

        if (token.isEmpty()) {
            LOGGER.error("[FDLink] Please add a bot token to the config file!");
            return;
        }

        if (config.mainConfig.chatChannels.isEmpty()) {
            this.hasChatChannels = false;
        } else {
            this.hasChatChannels = true;
        }

        if (config.mainConfig.logChannels.isEmpty()) {
            this.hasLogChannels = false;
        } else {
            this.hasLogChannels = true;
        }

        if (!this.hasLogChannels && !this.hasChatChannels) {
            LOGGER.error("[FDLink] Please add either a game chat or a log channel to the config file (or both)!");
            return;
        }

        config.mainConfig.logChannels.removeIf(id -> config.mainConfig.chatChannels.contains(id));

        this.config = config;
        try {
            this.api = JDABuilder.createDefault(token).setActivity(Activity.playing("Minecraft")).build();
        } catch (LoginException error) {
            error.printStackTrace();
        }
        if (this.api != null) {
            this.messageCreateListener = new MessageReceivedListener(this);
            this.api.addEventListener(messageCreateListener);
            this.minecraftToDiscordHandler = new MinecraftToDiscordHandler(this);
        }
    }

    public void serverStarting() {
        if (this.api == null) return;
        if (this.config.mainConfig.minecraftToDiscord.chatChannels.serverStartingMessage) sendToChatChannels(config.messageConfig.minecraftToDiscord.serverStarting);
        if (this.config.mainConfig.minecraftToDiscord.logChannels.serverStartingMessage) sendToLogChannels(config.messageConfig.minecraftToDiscord.serverStarting);
    }

    public void serverStarted() {
        if (this.api == null) return;
        startTime = System.currentTimeMillis();
        if (this.config.mainConfig.minecraftToDiscord.chatChannels.serverStartMessage) sendToChatChannels(config.messageConfig.minecraftToDiscord.serverStarted);
        if (this.config.mainConfig.minecraftToDiscord.logChannels.serverStartMessage) sendToLogChannels(config.messageConfig.minecraftToDiscord.serverStarted);
    }

    public void serverStopping() {
        if (this.api == null) return;
        this.api.removeEventListener(this.messageCreateListener);
        this.stopping = true;
        if (this.config.mainConfig.minecraftToDiscord.chatChannels.serverStoppingMessage) sendToChatChannels(config.messageConfig.minecraftToDiscord.serverStopping);
        if (this.config.mainConfig.minecraftToDiscord.logChannels.serverStoppingMessage) sendToLogChannels(config.messageConfig.minecraftToDiscord.serverStopping);
    }

    public void serverStopped() {
        if (this.api == null) return;
        if (this.config.mainConfig.minecraftToDiscord.chatChannels.serverStopMessage || this.config.mainConfig.minecraftToDiscord.logChannels.serverStopMessage) {
            ArrayList<CompletableFuture<Message>> requests = new ArrayList<>();
            if(this.config.mainConfig.minecraftToDiscord.chatChannels.serverStopMessage && this.hasChatChannels) requests.addAll(sendToChatChannels(config.messageConfig.minecraftToDiscord.serverStopped, requests));
            if(this.config.mainConfig.minecraftToDiscord.logChannels.serverStopMessage && this.hasLogChannels) requests.addAll(sendToLogChannels(config.messageConfig.minecraftToDiscord.serverStopped, requests));

            for (CompletableFuture<Message> request : requests){
                while (!request.isDone()) {
                    if (this.config.mainConfig.minecraftToDiscord.general.enableDebugLogs) LOGGER.info("Request is not done yet!");
                }
            }
        }
        this.api.shutdown();
    }

    public void serverTick(MinecraftServer server) {
        if (this.api == null) return;
        int playerNumber = server.getPlayerCount();
        int maxPlayer = server.getMaxPlayerCount();
        int totalUptimeSeconds = (int) (System.currentTimeMillis() - this.startTime) / 1000;
        final int uptimeD = totalUptimeSeconds / 86400;
        final int uptimeH = (totalUptimeSeconds % 86400) / 3600;
        final int uptimeM = (totalUptimeSeconds % 3600) / 60;
        final int uptimeS = totalUptimeSeconds % 60;
        String ip = server.getIp();
        if (this.updatedActivity) this.updatedActivity = ((int)(System.currentTimeMillis()/1000) % this.config.mainConfig.activityUpdateInterval) == 0;
        if ((((int)(System.currentTimeMillis()/1000) % this.config.mainConfig.activityUpdateInterval) == 0 && !this.updatedActivity) || this.firstTick) {
            String[] possibleActivities = this.config.messageConfig.discord.botActivities;
            int rand = new Random().nextInt(possibleActivities.length);
            String selected = possibleActivities[rand];
            selected = selected
                    .replace("%playercount", String.valueOf(playerNumber))
                    .replace("%maxplayercount", String.valueOf(maxPlayer))
                    .replace("%uptime_D", String.valueOf(uptimeD))
                    .replace("%uptime_H", String.valueOf(uptimeH))
                    .replace("%uptime_M", String.valueOf(uptimeM))
                    .replace("%uptime_S", String.valueOf(uptimeS))
                    .replace("%ip", ip);
            this.api.getPresence().setActivity(Activity.playing(selected));
            this.updatedActivity = true;
        }
        if (this.hasReceivedMessage) {
            for (Commands command : Commands.values()) {
                if (this.messageCreateEvent.getMessage().getContentRaw().toLowerCase().equals(this.config.messageConfig.discord.commandPrefix + command.name().toLowerCase())) {
                    this.hasReceivedMessage = command.execute(server, this.messageCreateEvent, this.startTime);
                    return;
                }
            }
            //Author name will always be populated, so use it as the default.
            String playerName = messageCreateEvent.getAuthor().getName();
            //Look for a member and nickname and use that instead.
            if(messageCreateEvent.getMember() != null && messageCreateEvent.getMember().getNickname() != null) {
                playerName = messageCreateEvent.getMember().getNickname();
            }
            this.lastMessageD = this.config.messageConfig.discordToMinecraft.message
                    .replace("%player", playerName);
            String string_message = EmojiParser.parseToAliases(this.messageCreateEvent.getMessage().getContentRaw());
            for (MainConfig.EmojiEntry emojiEntry : this.config.mainConfig.emojiMap) {
                string_message = string_message.replace("<" + emojiEntry.id + ">", emojiEntry.name);
            }
            if (this.config.mainConfig.minecraftToDiscord.chatChannels.minecraftToDiscordTag || this.config.mainConfig.minecraftToDiscord.logChannels.minecraftToDiscordTag) {
               for (User user : this.api.getUserCache()) {
                    TextChannel serverChannel = (TextChannel) this.api.getTextChannels().toArray()[0];
                    Guild discordServer = serverChannel.getGuild();
                    String string_discriminator = "";
                    if (this.config.mainConfig.minecraftToDiscord.chatChannels.minecraftToDiscordDiscriminator || this.config.mainConfig.minecraftToDiscord.logChannels.minecraftToDiscordDiscriminator){
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

        if ((this.hasChatChannels || this.hasLogChannels) && (this.config.mainConfig.minecraftToDiscord.chatChannels.customChannelDescription ||  this.config.mainConfig.minecraftToDiscord.logChannels.customChannelDescription) && (System.currentTimeMillis() > this.nextChannelTopicUpdateTimeMilliseconds)) {
            this.nextChannelTopicUpdateTimeMilliseconds = System.currentTimeMillis() + 5 * 60 * 1000; // Five minutes from now.

            final String topic = this.config.messageConfig.minecraftToDiscord.channelDescription
                    .replace("%playercount", String.valueOf(playerNumber))
                    .replace("%maxplayercount", String.valueOf(maxPlayer))
                    .replace("%uptime", uptimeD + "d " + uptimeH + "h " + uptimeM + "m " + uptimeS + "s")
                    .replace("%motd", server.getMotd())
                    .replace("%uptime_d", String.valueOf(uptimeD))
                    .replace("%uptime_h", String.valueOf(uptimeH))
                    .replace("%uptime_m", String.valueOf(uptimeM))
                    .replace("%uptime_s", String.valueOf(uptimeS));

            if (this.config.mainConfig.minecraftToDiscord.chatChannels.customChannelDescription){
                for (String id : this.config.mainConfig.chatChannels) {
                    this.updateChannelTopic(id, topic);
                }
            }
            if (this.config.mainConfig.minecraftToDiscord.logChannels.customChannelDescription){
                for (String id : this.config.mainConfig.logChannels) {
                    this.updateChannelTopic(id, topic);
                }
            }
        }

        if (this.firstTick) this.firstTick = false;
    }

    private void updateChannelTopic(String channelId, String topic) {
        TextChannel channel = this.api.getTextChannelById(channelId);
        if (channel != null) {
            try {
                channel.getManager().setTopic(topic).queue();
            }
            catch (InsufficientPermissionException e) {
                LOGGER.error(String.format("Failed to set the channel topic for channel %s. Check that the bot has the <Manage Channels> permission, or else disable custom channel descriptions.", channelId), e);
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
            for (String id : this.config.mainConfig.logChannels) {
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
            for (String id : this.config.mainConfig.chatChannels) {
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
