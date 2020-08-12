package fr.arthurbambou.fdlink.discordstuff.todiscord;

import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.discordstuff.DiscordBot;
import fr.arthurbambou.fdlink.versionhelpers.CrossVersionHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;
import java.util.List;

public final class MinecraftToDiscordHandler {

    private final DiscordApi api;
    private final DiscordBot discordBot;
    private final FDLink.Config config;
    private final List<TextHandler> TEXT_HANDLERS = new ArrayList<>();

    public MinecraftToDiscordHandler(DiscordApi api, DiscordBot discordBot, FDLink.Config config) {
        this.api = api;
        this.discordBot = discordBot;
        this.config = config;

        // Literal Text not using translation key.
        registerTextHandler(new TextHandler(Text.class, text -> this.discordBot.sendToLogChannels(text.getString()
                .replaceAll("§[b0931825467adcfeklmnor]", ""))));

        // Chat messages
        registerTextHandler(new TextHandler("chat.type.text", text -> {
            String playerName = ((LiteralText) CrossVersionHandler.getArgs(text)[0]).getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            String message = ((String) CrossVersionHandler.getArgs(text)[1]).replaceAll("§[b0931825467adcfeklmnor]", "");
            String chatMessage = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            String logMessage = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            String chatPlayerName = "";
            String logPlayerName = "";
            String chatCompleteMessage;
            String logCompleteMessage;
            if (this.config.minecraftToDiscord.chatChannels.allowDiscordCommands && message.startsWith(this.config.minecraftToDiscord.chatChannels.commandPrefix)){
                this.discordBot.sendToChatChannels(message);
            } else if (this.config.minecraftToDiscord.chatChannels.playerMessages || this.config.minecraftToDiscord.logChannels.playerMessages) {
                chatPlayerName = adaptUsernameToDiscord(playerName);
                logPlayerName = adaptUsernameToDiscord(playerName);
                for (FDLink.Config.EmojiEntry emojiEntry : this.config.emojiMap) {
                    message = message.replaceAll(emojiEntry.name, "<" + emojiEntry.id + ">");
                }
                if(!this.config.minecraftToDiscord.chatChannels.minecraftToDiscordTag){
                    chatMessage = message;
                }
                if(!this.config.minecraftToDiscord.logChannels.minecraftToDiscordTag){
                    logMessage = message;
                }
                if (this.config.minecraftToDiscord.chatChannels.minecraftToDiscordTag ||  this.config.minecraftToDiscord.logChannels.minecraftToDiscordTag) {
                    for (User user : this.api.getCachedUsers()) {
                        ServerChannel serverChannel = (ServerChannel) this.api.getServerChannels().toArray()[0];
                        Server server = serverChannel.getServer();
                        message = message
                                .replaceAll("@" + user.getName(), user.getMentionTag())
                                .replaceAll("@" + user.getDisplayName(server), user.getMentionTag())
                                .replaceAll("@" + user.getName().toLowerCase(), user.getMentionTag())
                                .replaceAll("@" + user.getDisplayName(server).toLowerCase(), user.getMentionTag());
                        if (user.getNickname(server).isPresent()) {
                            message = message
                                    .replaceAll("@" + user.getNickname(server).get(), user.getMentionTag())
                                    .replaceAll("@" + user.getNickname(server).get().toLowerCase(), user.getMentionTag());
                        }
                    }
                    if(this.config.minecraftToDiscord.chatChannels.minecraftToDiscordTag){
                        chatMessage = message;
                    }
                    if(this.config.minecraftToDiscord.logChannels.minecraftToDiscordTag){
                        logMessage = message;
                    }
                }
                if (this.config.minecraftToDiscord.messages.playerMessage.useCustomMessage) {
                    chatCompleteMessage = this.config.minecraftToDiscord.messages.playerMessage.customMessage.replace("%player", chatPlayerName).replace("%message", chatMessage);
                    logCompleteMessage = this.config.minecraftToDiscord.messages.playerMessage.customMessage.replace("%player", logPlayerName).replace("%message", logMessage);
                } else {
                    chatCompleteMessage = "<" + chatPlayerName + "> " + chatMessage;
                    logCompleteMessage = "<" + logPlayerName + "> " + logMessage;
                }
                if(this.config.minecraftToDiscord.chatChannels.playerMessages){
                    this.discordBot.sendToChatChannels(chatCompleteMessage);
                }else if(this.config.minecraftToDiscord.logChannels.playerMessages){
                    this.discordBot.sendToLogChannels(logCompleteMessage);
                }
            }
        }));

        // /me command
        registerTextHandler(new TextHandler("chat.type.emote", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.chatChannels.sendMeCommand) {
                this.discordBot.sendToChatChannels(message);
            }
            if (this.config.minecraftToDiscord.logChannels.sendMeCommand) {
                this.discordBot.sendToLogChannels(message);
            }
        }));

        // /say command
        registerTextHandler(new TextHandler("chat.type.announcement", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.chatChannels.sendSayCommand) {
                this.discordBot.sendToChatChannels(message);
            }
            if (this.config.minecraftToDiscord.logChannels.sendSayCommand) {
                this.discordBot.sendToLogChannels(message);
            }
        }));

        // Advancement task
        registerTextHandler(new TextHandler("chat.type.advancement.task", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.chatChannels.advancementMessages || this.config.minecraftToDiscord.logChannels.advancementMessages) {
                if (this.config.minecraftToDiscord.messages.advancementTask.useCustomMessage) {
                    message = this.config.minecraftToDiscord.messages.advancementTask.customMessage
                            .replace("%player", adaptUsernameToDiscord((LiteralText) CrossVersionHandler.getArgs(text)[0]))
                            .replace("%advancement", ((LiteralText)CrossVersionHandler.getArgs(text)[1]).getString());
                }
                if (this.config.minecraftToDiscord.chatChannels.advancementMessages) {
                        this.discordBot.sendToChatChannels(message);
                 }
                if (this.config.minecraftToDiscord.logChannels.advancementMessages) {
                        this.discordBot.sendToLogChannels(message);
                }
            }
        }));

        // Advancement challenge
        registerTextHandler(new TextHandler("chat.type.advancement.challenge", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.chatChannels.challengeMessages || this.config.minecraftToDiscord.logChannels.challengeMessages) {
                if (this.config.minecraftToDiscord.messages.advancementChallenge.useCustomMessage) {
                    message = this.config.minecraftToDiscord.messages.advancementChallenge.customMessage
                            .replace("%player", adaptUsernameToDiscord((LiteralText) CrossVersionHandler.getArgs(text)[0]))
                            .replace("%advancement", ((LiteralText)CrossVersionHandler.getArgs(text)[1]).getString());
                }
                if (this.config.minecraftToDiscord.chatChannels.challengeMessages) {
                    this.discordBot.sendToChatChannels(message);
                }
                if (this.config.minecraftToDiscord.logChannels.challengeMessages) {
                    this.discordBot.sendToLogChannels(message);
                }
            }
        }));

        // Advancement goal
        registerTextHandler(new TextHandler("chat.type.advancement.goal", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.chatChannels.goalMessages || this.config.minecraftToDiscord.logChannels.goalMessages) {
                if (this.config.minecraftToDiscord.messages.advancementGoal.useCustomMessage) {
                    message = this.config.minecraftToDiscord.messages.advancementGoal.customMessage
                            .replace("%player", adaptUsernameToDiscord((LiteralText) CrossVersionHandler.getArgs(text)[0]))
                            .replace("%advancement", ((LiteralText)CrossVersionHandler.getArgs(text)[1]).getString());
                }
                if (this.config.minecraftToDiscord.chatChannels.goalMessages) {
                    this.discordBot.sendToChatChannels(message);
                }
                if (this.config.minecraftToDiscord.logChannels.goalMessages) {
                    this.discordBot.sendToLogChannels(message);
                }
            }
        }));

        // Admin commands
        registerTextHandler(new TextHandler("chat.type.admin", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.chatChannels.adminMessages) {
                this.discordBot.sendToChatChannels(message);
            }
            if (this.config.minecraftToDiscord.logChannels.adminMessages) {
                this.discordBot.sendToLogChannels(message);
            }
        }));

        // Player join server with new username
        registerTextHandler(new TextHandler("multiplayer.player.joined.renamed", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.chatChannels.joinAndLeaveMessages || this.config.minecraftToDiscord.logChannels.joinAndLeaveMessages) {
                if (this.config.minecraftToDiscord.messages.playerJoinedRenamed.useCustomMessage) {
                    message = this.config.minecraftToDiscord.messages.playerJoinedRenamed.customMessage.replace("%new", adaptUsernameToDiscord((LiteralText) CrossVersionHandler.getArgs(text)[0])).replace("%old", adaptUsernameToDiscord((LiteralText) CrossVersionHandler.getArgs(text)[1]));
                }
                if (this.config.minecraftToDiscord.chatChannels.joinAndLeaveMessages) {
                    this.discordBot.sendToChatChannels(message);
                }
                if (this.config.minecraftToDiscord.logChannels.joinAndLeaveMessages) {
                    this.discordBot.sendToLogChannels(message);
                }
            }
        }));

        // Player join server
        registerTextHandler(new TextHandler("multiplayer.player.joined", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.chatChannels.joinAndLeaveMessages || this.config.minecraftToDiscord.logChannels.joinAndLeaveMessages) {
                if (this.config.minecraftToDiscord.messages.playerJoined.useCustomMessage) {
                    message = this.config.minecraftToDiscord.messages.playerJoined.customMessage.replace("%player", adaptUsernameToDiscord((LiteralText) CrossVersionHandler.getArgs(text)[0]));
                }
                if (this.config.minecraftToDiscord.chatChannels.joinAndLeaveMessages) {
                    this.discordBot.sendToChatChannels(message);
                }
                if (this.config.minecraftToDiscord.logChannels.joinAndLeaveMessages) {
                    this.discordBot.sendToLogChannels(message);
                }
            }
        }));

        // Player leave server
        registerTextHandler(new TextHandler("multiplayer.player.left", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.chatChannels.joinAndLeaveMessages || this.config.minecraftToDiscord.logChannels.joinAndLeaveMessages) {
                if (this.config.minecraftToDiscord.messages.playerLeft.useCustomMessage) {
                    message = this.config.minecraftToDiscord.messages.playerLeft.customMessage.replace("%player", adaptUsernameToDiscord((LiteralText) CrossVersionHandler.getArgs(text)[0]));
                }
                if (this.config.minecraftToDiscord.chatChannels.joinAndLeaveMessages) {
                    this.discordBot.sendToChatChannels(message);
                }
                if (this.config.minecraftToDiscord.logChannels.joinAndLeaveMessages) {
                    this.discordBot.sendToLogChannels(message);
                }
            }
        }));

        // Death messages
        registerTextHandler(new TextHandler("death.", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.chatChannels.deathMessages) {
                this.discordBot.sendToChatChannels(this.config.minecraftToDiscord.messages.deathMsgPrefix + message + this.config.minecraftToDiscord.messages.deathMsgPostfix);
            }
            if (this.config.minecraftToDiscord.logChannels.deathMessages) {
                this.discordBot.sendToLogChannels(this.config.minecraftToDiscord.messages.deathMsgPrefix + message + this.config.minecraftToDiscord.messages.deathMsgPostfix);
            }
        }));
    }

    public String adaptUsernameToDiscord(String string) {
        return string.replaceAll("§[b0931825467adcfeklmnor]", "")
                .replaceAll("_", "\\_")
                .replaceAll("`", "\\`")
//                .replaceAll(Character.toString('\\'), "\\\\")
//                .replaceAll(Character.toString('*'),"\\*")
                .replaceAll("~", "\\~");
    }

    public String adaptUsernameToDiscord(LiteralText literalText) {
        return this.adaptUsernameToDiscord(literalText.getString());
    }

    public TextHandler registerTextHandler(TextHandler textHandler) {
        this.TEXT_HANDLERS.add(textHandler);
        return textHandler;
    }

    public void handleTexts(Text text) {
        if (this.api == null || (!this.discordBot.hasChatChannels && !this.discordBot.hasLogChannels)) return;
        if (text.getString().equals(this.discordBot.lastMessageD)) {
            return;
        }
        for (TextHandler textHandler : TEXT_HANDLERS) {
            if (textHandler.match(text)) {
                textHandler.handle(text);
                return;
            }
        }
        if (this.config.minecraftToDiscord.general.enableDebugLogs) {
            if (text instanceof TranslatableText) {
                DiscordBot.LOGGER.error("[FDLink] Unhandled text \"{}\":{}", ((TranslatableText) text).getKey(), text.getString());
            } else {
                DiscordBot.LOGGER.error("[FDLink] Unhandled text \"{}\"", text.getString());
            }
        }
    }

    public static class TextHandler {
        private final Class<?> textType;
        private final MinecraftToDiscordFunction minecraftToDiscordFunction;
        private String key;

        public TextHandler(Class<?> textType, MinecraftToDiscordFunction minecraftToDiscordFunction) {
            this.textType = textType;
            this.minecraftToDiscordFunction = minecraftToDiscordFunction;
        }

        public TextHandler(String key, MinecraftToDiscordFunction minecraftToDiscordFunction) {
            this.textType = TranslatableText.class;
            this.key = key;
            this.minecraftToDiscordFunction = minecraftToDiscordFunction;
        }

        public boolean match(Text text) {
            if (text instanceof TranslatableText) {
                return this.textType.getName().equals(text.getClass().getName()) && ((TranslatableText)text).getKey().startsWith(this.key);
            }
            return this.textType.getName().equals(text.getClass().getName());
        }

        public void handle(Text text) {
            this.minecraftToDiscordFunction.handleText(text);
        }
    }
}