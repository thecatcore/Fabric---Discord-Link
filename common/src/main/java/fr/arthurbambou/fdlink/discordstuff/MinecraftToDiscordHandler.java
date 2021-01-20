package fr.arthurbambou.fdlink.discordstuff;

import fr.arthurbambou.fdlink.config.Config;
import fr.arthurbambou.fdlink.config.MainConfig;
import fr.arthurbambou.fdlink.discordstuff.todiscord.MinecraftToDiscordFunction;
import fr.arthurbambou.fdlink.versionhelpers.CompatText;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

public final class MinecraftToDiscordHandler {

    private final JDA api;
    private final DiscordBot discordBot;
    private final Config config;
    private final List<MessageHandler> TEXT_HANDLERS = new ArrayList<>();

    public MinecraftToDiscordHandler(DiscordBot discordBot) {
        this.api = discordBot.api;
        this.discordBot = discordBot;
        this.config = discordBot.config;

        // Chat messages
        registerTextHandler(new TextHandler("chat.type.text", text -> {
            String playerName = getArgAsString(text.getArgs()[0]).replaceAll("§[b0931825467adcfeklmnor]", "");
            String message = getArgAsString(text.getArgs()[1]).replaceAll("§[b0931825467adcfeklmnor]", "");
            String chatMessage = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            String logMessage = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            String chatPlayerName = "";
            String logPlayerName = "";
            String chatCompleteMessage;
            String logCompleteMessage;
            if (this.config.mainConfig.minecraftToDiscord.chatChannels.allowDiscordCommands && message.startsWith(this.config.mainConfig.minecraftToDiscord.chatChannels.commandPrefix)){
                return new MessageSender.MinecraftMessage(message, MessageSender.MinecraftMessage.Type.CHAT_COMMAND);
            } else {
                chatPlayerName = adaptUsernameToDiscord(playerName);
                logPlayerName = adaptUsernameToDiscord(playerName);
                for (MainConfig.EmojiEntry emojiEntry : this.config.mainConfig.emojiMap) {
                    message = message.replaceAll(emojiEntry.name, "<" + emojiEntry.id + ">");
                }
                if(!this.config.mainConfig.minecraftToDiscord.chatChannels.minecraftToDiscordTag){
                    chatMessage = message;
                }
                if(!this.config.mainConfig.minecraftToDiscord.logChannels.minecraftToDiscordTag){
                    logMessage = message;
                }
                if (this.config.mainConfig.minecraftToDiscord.chatChannels.minecraftToDiscordTag ||  this.config.mainConfig.minecraftToDiscord.logChannels.minecraftToDiscordTag) {
                    for (User user : this.api.getUserCache()) {
                        TextChannel serverChannel = (TextChannel) this.api.getTextChannels().toArray()[0];
                        Guild server = serverChannel.getGuild();
                        message = message
                                .replaceAll("@" + user.getName(), "<@!" + user.getId() + ">")
                                .replaceAll("@" + user.getName(), "<@!" + user.getId() + ">")
                                .replaceAll("@" + user.getName().toLowerCase(), "<@!" + user.getId() + ">")
                                .replaceAll("@" + user.getName().toLowerCase(), "<@!" + user.getId() + ">");
//                        if (user.getNickname(server).isPresent()) {
//                            message = message
//                                    .replaceAll("@" + user.getNickname(server).get(), user.getAsTag())
//                                    .replaceAll("@" + user.getNickname(server).get().toLowerCase(), user.getAsTag());
//                        }
                    }
                    if(this.config.mainConfig.minecraftToDiscord.chatChannels.minecraftToDiscordTag){
                        chatMessage = message;
                    }
                    if(this.config.mainConfig.minecraftToDiscord.logChannels.minecraftToDiscordTag){
                        logMessage = message;
                    }
                }
                if (this.config.messageConfig.minecraftToDiscord.playerMessage.useCustomMessage) {
                    chatCompleteMessage = this.config.messageConfig.minecraftToDiscord.playerMessage.customMessage.replace("%player", chatPlayerName).replace("%message", chatMessage);
                    logCompleteMessage = this.config.messageConfig.minecraftToDiscord.playerMessage.customMessage.replace("%player", logPlayerName).replace("%message", logMessage);
                } else {
                    chatCompleteMessage = "<" + chatPlayerName + "> " + chatMessage;
                    logCompleteMessage = "<" + logPlayerName + "> " + logMessage;
                }
                return new MessageSender.MinecraftMessage(chatCompleteMessage, logCompleteMessage, MessageSender.MinecraftMessage.Type.CHAT);
            }
        }));

        registerTextHandler(new TextHandler("chat.type.team.text", text -> {
            String teamName = adaptUsernameToDiscord(getArgAsString(text.getArgs()[0]).replaceAll("§[b0931825467adcfeklmnor]", ""));
            String playerName = adaptUsernameToDiscord(getArgAsString(text.getArgs()[1]).replaceAll("§[b0931825467adcfeklmnor]", ""));
            String message = getArgAsString(text.getArgs()[2]).replaceAll("§[b0931825467adcfeklmnor]", "");
            String chatMessage = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            String logMessage = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            String chatCompleteMessage;
            String logCompleteMessage;
            if (this.config.mainConfig.minecraftToDiscord.chatChannels.allowDiscordCommands && message.startsWith(this.config.mainConfig.minecraftToDiscord.chatChannels.commandPrefix)){
                return new MessageSender.MinecraftMessage(message, MessageSender.MinecraftMessage.Type.CHAT_COMMAND);
            } else {
                for (MainConfig.EmojiEntry emojiEntry : this.config.mainConfig.emojiMap) {
                    message = message.replaceAll(emojiEntry.name, "<" + emojiEntry.id + ">");
                }
                if(!this.config.mainConfig.minecraftToDiscord.chatChannels.minecraftToDiscordTag){
                    chatMessage = message;
                }
                if(!this.config.mainConfig.minecraftToDiscord.logChannels.minecraftToDiscordTag){
                    logMessage = message;
                }
                if (this.config.mainConfig.minecraftToDiscord.chatChannels.minecraftToDiscordTag ||  this.config.mainConfig.minecraftToDiscord.logChannels.minecraftToDiscordTag) {
                    for (User user : this.api.getUserCache()) {
                        TextChannel serverChannel = (TextChannel) this.api.getTextChannels().toArray()[0];
                        Guild server = serverChannel.getGuild();
                        message = message
                                .replaceAll("@" + user.getName(), "<@!" + user.getId() + ">")
                                .replaceAll("@" + user.getName(), "<@!" + user.getId() + ">")
                                .replaceAll("@" + user.getName().toLowerCase(), "<@!" + user.getId() + ">")
                                .replaceAll("@" + user.getName().toLowerCase(), "<@!" + user.getId() + ">");
//                        if (user.getNickname(server).isPresent()) {
//                            message = message
//                                    .replaceAll("@" + user.getNickname(server).get(), user.getAsTag())
//                                    .replaceAll("@" + user.getNickname(server).get().toLowerCase(), user.getAsTag());
//                        }
                    }
                    if(this.config.mainConfig.minecraftToDiscord.chatChannels.minecraftToDiscordTag){
                        chatMessage = message;
                    }
                    if(this.config.mainConfig.minecraftToDiscord.logChannels.minecraftToDiscordTag){
                        logMessage = message;
                    }
                }
                if (this.config.messageConfig.minecraftToDiscord.teamPlayerMessage.useCustomMessage) {
                    chatCompleteMessage = this.config.messageConfig.minecraftToDiscord.teamPlayerMessage.customMessage
                            .replace("%player", playerName)
                            .replace("%team", teamName)
                            .replace("%message", chatMessage);
                    logCompleteMessage = this.config.messageConfig.minecraftToDiscord.teamPlayerMessage.customMessage
                            .replace("%player", playerName)
                            .replace("%team", teamName)
                            .replace("%message", logMessage);
                } else {
                    chatCompleteMessage = teamName + " <" + playerName + "> " + chatMessage;
                    logCompleteMessage = teamName + " <" + playerName + "> " + logMessage;
                }
                return new MessageSender.MinecraftMessage(chatCompleteMessage, logCompleteMessage, MessageSender.MinecraftMessage.Type.TEAM_CHAT);
            }
        }));

        // /me command
        registerTextHandler(new TextHandler("chat.type.emote", text -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.meMessage.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.meMessage.customMessage
                        .replace("%author", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%message", getArgAsString(text.getArgs()[1]));
            }
            return new MessageSender.MinecraftMessage(message, MessageSender.MinecraftMessage.Type.ME);
        }));

        // /say command
        registerTextHandler(new TextHandler("chat.type.announcement", text -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.sayMessage.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.sayMessage.customMessage
                        .replace("%author", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%message", getArgAsString(text.getArgs()[1]));
            }
            return new MessageSender.MinecraftMessage(message, MessageSender.MinecraftMessage.Type.SAY);
        }));

        // Advancement task
        registerTextHandler(new TextHandler("chat.type.advancement.task", text -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.advancementTask.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.advancementTask.customMessage
                        .replace("%player", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%advancement", getArgAsString(text.getArgs()[1]));
            }
            return new MessageSender.MinecraftMessage(message, MessageSender.MinecraftMessage.Type.ADVANCEMENT_TASK);
        }));

        // Advancement challenge
        registerTextHandler(new TextHandler("chat.type.advancement.challenge", text -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.advancementChallenge.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.advancementChallenge.customMessage
                        .replace("%player", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%advancement", getArgAsString(text.getArgs()[1]));
            }
            return new MessageSender.MinecraftMessage(message, MessageSender.MinecraftMessage.Type.ADVANCEMENT_CHALLENGE);
        }));

        // Advancement goal
        registerTextHandler(new TextHandler("chat.type.advancement.goal", text -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.advancementGoal.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.advancementGoal.customMessage
                        .replace("%player", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%advancement", getArgAsString(text.getArgs()[1]));
            }
            return new MessageSender.MinecraftMessage(message, MessageSender.MinecraftMessage.Type.ADVANCEMENT_GOAL);
        }));

        // Admin commands
        registerTextHandler(new TextHandler("chat.type.admin", text -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.adminMessage.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.adminMessage.customMessage
                        .replace("%author", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%message", getArgAsString(text.getArgs()[1]));
            }
            return new MessageSender.MinecraftMessage(message, MessageSender.MinecraftMessage.Type.ADMIN);
        }));

        // Player join server with new username
        registerTextHandler(new TextHandler("multiplayer.player.joined.renamed", text -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.playerJoinedRenamed.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.playerJoinedRenamed.customMessage
                        .replace("%new", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%old", adaptUsernameToDiscord(getArgAsString(text.getArgs()[1])));
            }
            return new MessageSender.MinecraftMessage(message, MessageSender.MinecraftMessage.Type.JOIN_RENAMED);
        }));

        // Player join server
        registerTextHandler(new TextHandler("multiplayer.player.joined", text -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.playerJoined.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.playerJoined.customMessage
                        .replace("%player", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])));
            }
            return new MessageSender.MinecraftMessage(message, MessageSender.MinecraftMessage.Type.JOIN);
        }));

        // Player leave server
        registerTextHandler(new TextHandler("multiplayer.player.left", text -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.playerLeft.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.playerLeft.customMessage
                        .replace("%player", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])));
            }
            return new MessageSender.MinecraftMessage(message, MessageSender.MinecraftMessage.Type.LEAVE);
        }));

        // Death messages
        registerTextHandler(new TextHandler("death.", text -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            return new MessageSender.MinecraftMessage(this.config.messageConfig.minecraftToDiscord.deathMsgPrefix + message + this.config.messageConfig.minecraftToDiscord.deathMsgPostfix, MessageSender.MinecraftMessage.Type.DEATH);
        }));

        registerTextHandler(new CommandHandler("tellraw", text -> {
            String message = text.getMessage();
            String source = adaptUsernameToDiscord(text.getSource());

            String stringMessage = this.config.messageConfig.minecraftToDiscord.atATellRaw
                    .replace("%message", message)
                    .replace("%source", source);

            return new MessageSender.MinecraftMessage(stringMessage, MessageSender.MinecraftMessage.Type.TELLRAW);
        }));

        // Old versions achievement
        registerTextHandler(new TextHandler("chat.type.achievement", text -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.achievement.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.achievement.customMessage
                        .replace("%player", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%achievement", getArgAsString(text.getArgs()[1]));
            }
            return new MessageSender.MinecraftMessage(message, MessageSender.MinecraftMessage.Type.ACHIEVEMENT);
        }));

        // Old versions
        registerTextHandler(new StringHandler(message -> {
            String text = message.getMessage().replaceAll("§[b0931825467adcfeklmnor]","");
            return new MessageSender.MinecraftMessage(text, MessageSender.MinecraftMessage.Type.STRING_OLD);
        }));
    }

    public static String adaptUsernameToDiscord(String string) {
        return string.replaceAll("§[b0931825467adcfeklmnor]", "")
                .replaceAll("([_`~*>])", "\\\\$1");
    }

    public void registerTextHandler(MessageHandler messageHandler) {
        this.TEXT_HANDLERS.add(messageHandler);
    }

    public static String getArgAsString(Object arg) {
        if (arg instanceof CompatText) {
            return ((CompatText) arg).getMessage();
        }
        return (String) arg;
    }

    public MessageSender.MinecraftMessage handleTexts(Message text) {
        if (this.api == null || (!this.discordBot.hasChatChannels && !this.discordBot.hasLogChannels && this.config.mainConfig.webhookURL.isEmpty())) return null;
        Message.MessageObjectType objectType = text.getType();
        String message = text.getMessage();
        if (message.equals(this.discordBot.lastMessageD)) return null;
        for (MessageHandler messageHandler : TEXT_HANDLERS) {
            if (messageHandler.match(text)) {
                return messageHandler.handle(text);
            }
        }
        if (this.config.mainConfig.minecraftToDiscord.general.enableDebugLogs) {
            if (text.getTextType() == Message.TextType.TRANSLATABLE) {
                DiscordBot.LOGGER.error("[FDLink] Unhandled text \"{}\":{}", text.getKey(), text.getMessage());
            } else {
                DiscordBot.LOGGER.error("[FDLink] Unhandled text \"{}\"", text.getMessage());
            }
        }
        return null;
    }

    public abstract static class MessageHandler {
        private final MinecraftToDiscordFunction minecraftToDiscordFunction;

        public MessageHandler(MinecraftToDiscordFunction minecraftToDiscordFunction) {
            this.minecraftToDiscordFunction = minecraftToDiscordFunction;
        }

        public MessageSender.MinecraftMessage handle(Message text) {
            return this.minecraftToDiscordFunction.handleText(text);
        }

        public abstract boolean match(Message message);
    }

    public static class TextHandler extends MessageHandler {
        private String key;
        public TextHandler(String key, MinecraftToDiscordFunction minecraftToDiscordFunction) {
            super(minecraftToDiscordFunction);
            this.key = key;
        }

        public boolean match(Message text) {
            if (text.getTextType() == Message.TextType.TRANSLATABLE) {
                return text.getKey().startsWith(this.key);
            }
            return false;
        }
    }

    public static class CommandHandler extends MessageHandler {
        private String commandName;

        public CommandHandler(String commandName, MinecraftToDiscordFunction minecraftToDiscordFunction) {
            super(minecraftToDiscordFunction);
            this.commandName = commandName;
        }

        @Override
        public boolean match(Message message) {
            if (message.getTextType() == Message.TextType.COMMAND) {
                return this.commandName.equals(message.getCommandName());
            }
            return false;
        }
    }

    public static class StringHandler extends MessageHandler {

        public StringHandler(MinecraftToDiscordFunction minecraftToDiscordFunction) {
            super(minecraftToDiscordFunction);
        }

        @Override
        public boolean match(Message message) {
            return message.getType() == Message.MessageObjectType.STRING;
        }
    }
}