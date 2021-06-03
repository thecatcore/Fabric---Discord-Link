package fr.arthurbambou.fdlink.discordstuff;

import fr.arthurbambou.fdlink.api.config.Config;
import fr.arthurbambou.fdlink.api.config.MainConfig;
import fr.arthurbambou.fdlink.api.discord.MessageHandler;
import fr.arthurbambou.fdlink.api.discord.MinecraftMessage;
import fr.arthurbambou.fdlink.api.discord.handlers.CommandHandler;
import fr.arthurbambou.fdlink.api.discord.handlers.StringHandler;
import fr.arthurbambou.fdlink.api.discord.handlers.TextHandler;
import fr.arthurbambou.fdlink.api.minecraft.Message;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public final class MinecraftToDiscordHandler implements MessageHandler {

    private final JDA api;
    private final DiscordBot discordBot;
    private final Config config;

    public MinecraftToDiscordHandler(DiscordBot discordBot) {
        this.api = discordBot.api;
        this.discordBot = discordBot;
        this.config = discordBot.config;

        // Chat messages
        MessageHandler.registerHandler(new TextHandler("chat.type.text", (text, config) -> {
            boolean webhookMode = !config.mainConfig.webhook.url.isEmpty();

            Object arg1 = text.getArgs()[0];
            String teamPrefix = "";
            String teamSuffix = "";
            String playerName = "";
            if (arg1 instanceof Message) {
                Message argMessage = (Message) arg1;
                if (argMessage.getSibblings().isEmpty()) {
                    playerName = argMessage.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
                } else if (argMessage.getSibblings().size() == 3) {
                    teamPrefix = argMessage.getSibblings().get(0).getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
                    playerName = argMessage.getSibblings().get(1).getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
                    teamSuffix = argMessage.getSibblings().get(2).getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
                }
            } else {
                playerName = getArgAsString(text.getArgs()[0]).replaceAll("§[b0931825467adcfeklmnor]", "");
            }
            String message = getArgAsString(text.getArgs()[1]).replaceAll("§[b0931825467adcfeklmnor]", "");
            String chatMessage = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            String logMessage = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            String chatPlayerName = "";
            String logPlayerName = "";
            String chatCompleteMessage;
            String logCompleteMessage;
            if (config.mainConfig.minecraftToDiscord.chatChannels.allowDiscordCommands && message.startsWith(config.mainConfig.minecraftToDiscord.chatChannels.commandPrefix)){
                return new MinecraftMessage(new MinecraftMessage.MessageSendability(message, true), MinecraftMessage.Type.CHAT).searchForAuthor();
            } else {
                chatPlayerName = adaptUsernameToDiscord(playerName);
                logPlayerName = adaptUsernameToDiscord(playerName);
                for (MainConfig.EmojiEntry emojiEntry : this.config.mainConfig.emojiMap) {
                    if (!emojiEntry.name.isEmpty()) {
                        message = message.replaceAll(emojiEntry.name, "<" + emojiEntry.id + ">");
                    }
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
                    chatCompleteMessage = this.config.messageConfig.minecraftToDiscord.playerMessage.customMessage
                            .replace("%teamprefix", teamPrefix)
                            .replace("%teamsuffix", teamSuffix)
                            .replace("%player", chatPlayerName)
                            .replace("%message", chatMessage);
                    logCompleteMessage = this.config.messageConfig.minecraftToDiscord.playerMessage.customMessage
                            .replace("%teamprefix", teamPrefix)
                            .replace("%teamsuffix", teamSuffix)
                            .replace("%player", logPlayerName)
                            .replace("%message", logMessage);
                } else {
                    chatCompleteMessage = "<" + chatPlayerName + "> " + chatMessage;
                    logCompleteMessage = "<" + logPlayerName + "> " + logMessage;
                }
                MinecraftMessage minecraftMessage = new MinecraftMessage(
                        new MinecraftMessage.MessageSendability(chatCompleteMessage, config.mainConfig.minecraftToDiscord.chatChannels.playerMessages),
                        new MinecraftMessage.MessageSendability(logCompleteMessage, config.mainConfig.minecraftToDiscord.logChannels.playerMessages))
                        .searchForAuthor();

                if (webhookMode) {
                    minecraftMessage = new MinecraftMessage(
                            new MinecraftMessage.MessageSendability(chatMessage, config.mainConfig.minecraftToDiscord.chatChannels.playerMessages),
                            new MinecraftMessage.MessageSendability(logMessage, config.mainConfig.minecraftToDiscord.logChannels.playerMessages))
                            .searchForAuthor();
                }

                if (text.hasAuthorUUID()) minecraftMessage.setAuthor(text.getAuthorUUID());

                return minecraftMessage;
            }
        }));

        MessageHandler.registerHandler(new TextHandler("chat.type.team.text", (text, config) -> {
            boolean webhookMode = !config.mainConfig.webhook.url.isEmpty();

            String teamName = adaptUsernameToDiscord(getArgAsString(text.getArgs()[0]).replaceAll("§[b0931825467adcfeklmnor]", ""));
            String playerName = adaptUsernameToDiscord(getArgAsString(text.getArgs()[1]).replaceAll("§[b0931825467adcfeklmnor]", ""));
            String message = getArgAsString(text.getArgs()[2]).replaceAll("§[b0931825467adcfeklmnor]", "");
            String chatMessage = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            String logMessage = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            String chatCompleteMessage;
            String logCompleteMessage;
            if (this.config.mainConfig.minecraftToDiscord.chatChannels.allowDiscordCommands && message.startsWith(this.config.mainConfig.minecraftToDiscord.chatChannels.commandPrefix)){
                return new MinecraftMessage(new MinecraftMessage.MessageSendability(message, true), MinecraftMessage.Type.CHAT).searchForAuthor();
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
                MinecraftMessage minecraftMessage = new MinecraftMessage(
                        new MinecraftMessage.MessageSendability(chatCompleteMessage, config.mainConfig.minecraftToDiscord.chatChannels.teamPlayerMessages),
                        new MinecraftMessage.MessageSendability(logCompleteMessage, config.mainConfig.minecraftToDiscord.logChannels.teamPlayerMessages))
                        .searchForAuthor();

                if (webhookMode) {
                    minecraftMessage = new MinecraftMessage(
                            new MinecraftMessage.MessageSendability(chatMessage, config.mainConfig.minecraftToDiscord.chatChannels.teamPlayerMessages),
                            new MinecraftMessage.MessageSendability(logMessage, config.mainConfig.minecraftToDiscord.logChannels.teamPlayerMessages))
                            .searchForAuthor();
                }

                if (text.hasAuthorUUID()) minecraftMessage.setAuthor(text.getAuthorUUID());

                return minecraftMessage;
            }
        }));

        // /me command
        MessageHandler.registerHandler(new TextHandler("chat.type.emote", (text, config) -> {
            boolean webhookMode = !config.mainConfig.webhook.url.isEmpty();

            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.meMessage.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.meMessage.customMessage
                        .replace("%author", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%message", getArgAsString(text.getArgs()[1]));
            }
            MinecraftMessage minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(message,
                    config.mainConfig.minecraftToDiscord.chatChannels.sendMeCommand,
                    config.mainConfig.minecraftToDiscord.logChannels.sendMeCommand
                    )).searchForAuthor();

            if (webhookMode) {
                minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(getArgAsString(text.getArgs()[1]),
                        config.mainConfig.minecraftToDiscord.chatChannels.sendMeCommand,
                        config.mainConfig.minecraftToDiscord.logChannels.sendMeCommand
                )).searchForAuthor();
            }

            if (text.hasAuthorUUID()) minecraftMessage.setAuthor(text.getAuthorUUID());

            return minecraftMessage;
        }));

        // /say command
        MessageHandler.registerHandler(new TextHandler("chat.type.announcement", (text, config) -> {
            boolean webhookMode = !config.mainConfig.webhook.url.isEmpty();

            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.sayMessage.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.sayMessage.customMessage
                        .replace("%author", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%message", getArgAsString(text.getArgs()[1]));
            }
            MinecraftMessage minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(message,
                    config.mainConfig.minecraftToDiscord.chatChannels.sendSayCommand,
                    config.mainConfig.minecraftToDiscord.logChannels.sendSayCommand
                    )).searchForAuthor();

            if (webhookMode) {
                minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(getArgAsString(text.getArgs()[1]),
                        config.mainConfig.minecraftToDiscord.chatChannels.sendSayCommand,
                        config.mainConfig.minecraftToDiscord.logChannels.sendSayCommand
                )).searchForAuthor();
            }

            if (text.hasAuthorUUID()) minecraftMessage.setAuthor(text.getAuthorUUID());

            return minecraftMessage;
        }));

        // Advancement task
        MessageHandler.registerHandler(new TextHandler("chat.type.advancement.task", (text, config) -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.advancementTask.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.advancementTask.customMessage
                        .replace("%player", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%advancement", getArgAsString(text.getArgs()[1]));
            }
            MinecraftMessage minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(message,
                    config.mainConfig.minecraftToDiscord.chatChannels.advancementMessages,
                    config.mainConfig.minecraftToDiscord.logChannels.advancementMessages
                    ));
            return minecraftMessage;
        }));

        // Advancement challenge
        MessageHandler.registerHandler(new TextHandler("chat.type.advancement.challenge", (text, config) -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.advancementChallenge.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.advancementChallenge.customMessage
                        .replace("%player", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%advancement", getArgAsString(text.getArgs()[1]));
            }
            MinecraftMessage minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(message,
                    config.mainConfig.minecraftToDiscord.chatChannels.challengeMessages,
                    config.mainConfig.minecraftToDiscord.logChannels.challengeMessages
                    ));
            return minecraftMessage;
        }));

        // Advancement goal
        MessageHandler.registerHandler(new TextHandler("chat.type.advancement.goal", (text, config) -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.advancementGoal.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.advancementGoal.customMessage
                        .replace("%player", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%advancement", getArgAsString(text.getArgs()[1]));
            }
            MinecraftMessage minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(message,
                    config.mainConfig.minecraftToDiscord.chatChannels.goalMessages,
                    config.mainConfig.minecraftToDiscord.logChannels.goalMessages
                    ));
            return minecraftMessage;
        }));

        // Admin commands
        MessageHandler.registerHandler(new TextHandler("chat.type.admin", (text, config) -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.adminMessage.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.adminMessage.customMessage
                        .replace("%author", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%message", getArgAsString(text.getArgs()[1]));
            }
            MinecraftMessage minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(message,
                    config.mainConfig.minecraftToDiscord.chatChannels.adminMessages,
                    config.mainConfig.minecraftToDiscord.logChannels.adminMessages
                    ));
            return minecraftMessage;
        }));

        // Player join server with new username
        MessageHandler.registerHandler(new TextHandler("multiplayer.player.joined.renamed", (text, config) -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.playerJoinedRenamed.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.playerJoinedRenamed.customMessage
                        .replace("%new", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%old", adaptUsernameToDiscord(getArgAsString(text.getArgs()[1])));
            }
            MinecraftMessage minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(message,
                    config.mainConfig.minecraftToDiscord.chatChannels.joinAndLeaveMessages,
                    config.mainConfig.minecraftToDiscord.logChannels.joinAndLeaveMessages
                    ));
            return minecraftMessage;
        }));

        // Player join server
        MessageHandler.registerHandler(new TextHandler("multiplayer.player.joined", (text, config) -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.playerJoined.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.playerJoined.customMessage
                        .replace("%player", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])));
            }
            MinecraftMessage minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(message,
                    config.mainConfig.minecraftToDiscord.chatChannels.joinAndLeaveMessages,
                    config.mainConfig.minecraftToDiscord.logChannels.joinAndLeaveMessages
            ));
            return minecraftMessage;
        }));

        // Player leave server
        MessageHandler.registerHandler(new TextHandler("multiplayer.player.left", (text, config) -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.playerLeft.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.playerLeft.customMessage
                        .replace("%player", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])));
            }
            MinecraftMessage minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(message,
                    config.mainConfig.minecraftToDiscord.chatChannels.joinAndLeaveMessages,
                    config.mainConfig.minecraftToDiscord.logChannels.joinAndLeaveMessages
            ));
            return minecraftMessage;
        }));

        // Death messages
        MessageHandler.registerHandler(new TextHandler("death.", (text, config) -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            MinecraftMessage minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(
                    this.config.messageConfig.minecraftToDiscord.deathMsgPrefix
                    + message
                    + this.config.messageConfig.minecraftToDiscord.deathMsgPostfix,
                    config.mainConfig.minecraftToDiscord.chatChannels.deathMessages,
                    config.mainConfig.minecraftToDiscord.logChannels.deathMessages
            ));
            return minecraftMessage;
        }));

        MessageHandler.registerHandler(new CommandHandler("tellraw", (text, config) -> {
            boolean webhookMode = !config.mainConfig.webhook.url.isEmpty();

            String message = text.getMessage();
            String source = adaptUsernameToDiscord(text.getSource());

            String stringMessage = this.config.messageConfig.minecraftToDiscord.atATellRaw
                    .replace("%message", message)
                    .replace("%source", source);

            MinecraftMessage minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(stringMessage,
                    config.mainConfig.minecraftToDiscord.chatChannels.atATellRaw,
                    config.mainConfig.minecraftToDiscord.logChannels.atATellRaw
                    )).searchForAuthor();

            if (webhookMode) {
                minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(message,
                        config.mainConfig.minecraftToDiscord.chatChannels.atATellRaw,
                        config.mainConfig.minecraftToDiscord.logChannels.atATellRaw
                )).searchForAuthor();
            }

            if (text.hasAuthorUUID()) minecraftMessage.setAuthor(text.getAuthorUUID());

            return minecraftMessage;
        }));

        // Old versions achievement
        MessageHandler.registerHandler(new TextHandler("chat.type.achievement", (text, config) -> {
            String message = text.getMessage().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.messageConfig.minecraftToDiscord.achievement.useCustomMessage) {
                message = this.config.messageConfig.minecraftToDiscord.achievement.customMessage
                        .replace("%player", adaptUsernameToDiscord(getArgAsString(text.getArgs()[0])))
                        .replace("%achievement", getArgAsString(text.getArgs()[1]));
            }
            MinecraftMessage minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(message,
                    config.mainConfig.minecraftToDiscord.chatChannels.achievementMessages,
                    config.mainConfig.minecraftToDiscord.logChannels.achievementMessages
                    ));
            return minecraftMessage;
        }));

        // Old versions
        MessageHandler.registerHandler(new StringHandler((message, config) -> {
            String text = message.getMessage().replaceAll("§[b0931825467adcfeklmnor]","");
            MinecraftMessage minecraftMessage = new MinecraftMessage(new MinecraftMessage.MessageSendability(text,
                    config.mainConfig.minecraftToDiscord.chatChannels.playerMessages,
                    config.mainConfig.minecraftToDiscord.logChannels.playerMessages
            ));
            return minecraftMessage;
        }));
    }

    @Override
    public MinecraftMessage handleText(Message text) {
        if (this.api == null || (!this.discordBot.hasChatChannels && !this.discordBot.hasLogChannels && this.config.mainConfig.webhook.url.isEmpty())) return null;
        Message.MessageObjectType objectType = text.getType();
        String message = text.getMessage();
        if (message.equals(this.discordBot.lastMessageD)) return null;
        for (fr.arthurbambou.fdlink.api.discord.handlers.MessageHandler messageHandler : TEXT_HANDLERS) {
            if (messageHandler.match(text)) {
                return messageHandler.handle(text, this.config);
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
}