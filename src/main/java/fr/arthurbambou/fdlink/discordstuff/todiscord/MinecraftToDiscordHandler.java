package fr.arthurbambou.fdlink.discordstuff.todiscord;

import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.discordstuff.DiscordBot;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import java.util.ArrayList;
import java.util.List;

public class MinecraftToDiscordHandler {

    private DiscordApi api;
    private DiscordBot discordBot;
    private FDLink.Config config;
    private List<TextHandler> TEXT_HANDLERS = new ArrayList<>();

    public MinecraftToDiscordHandler(DiscordApi api, DiscordBot discordBot, FDLink.Config config) {
        this.api = api;
        this.discordBot = discordBot;
        this.config = config;

        // Literal Text not using translation key.
        registerTextHandler(new TextHandler(Text.class, text -> this.discordBot.sendToLogChannels(text.getString()
                .replaceAll("§[b0931825467adcfeklmnor]", ""))));

        // Chat messages
        registerTextHandler(new TextHandler("chat.type.text", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.booleans.playerMessages) {
                String playerName = message.split("> ")[0];
                playerName = playerName.substring(1);
                String fixedName = adaptUsernameToDiscord(playerName);
                message = message.replace("<" + playerName + ">", "<" + fixedName + ">");
                for (FDLink.Config.EmojiEntry emojiEntry : this.config.emojiMap) {
                    message = message.replaceAll(emojiEntry.name, "<" + emojiEntry.id + ">");
                }

                if (this.config.minecraftToDiscord.booleans.minecraftToDiscordTag) {
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
                }
                this.discordBot.sendToAllChannels(message);
            }
        }));

        // /me command
        registerTextHandler(new TextHandler("chat.type.emote", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.booleans.sendMeCommand) {
                this.discordBot.sendToAllChannels(message);
            }
        }));

        // /say command
        registerTextHandler(new TextHandler("chat.type.announcement", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.booleans.sendSayCommand) {
                this.discordBot.sendToAllChannels(message);
            }
        }));

        // Advancement task
        registerTextHandler(new TextHandler("chat.type.advancement.task", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.booleans.advancementMessages) {
                String[] args = message.split(" has made the advancement ");
                message = this.config.minecraftToDiscord.messages.advancementTask
                        .replace("%player", adaptUsernameToDiscord(args[0]))
                        .replace("%advancement", args[1]);
                this.discordBot.sendToAllChannels(message);
            }
        }));

        // Advancement challenge
        registerTextHandler(new TextHandler("chat.type.advancement.challenge", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.booleans.advancementMessages) {
                String[] args = message.split(" has completed the challenge ");
                message = this.config.minecraftToDiscord.messages.advancementChallenge
                        .replace("%player", adaptUsernameToDiscord(args[0]))
                        .replace("%advancement", args[1]);
                this.discordBot.sendToAllChannels(message);
            }
        }));

        // Advancement goal
        registerTextHandler(new TextHandler("chat.type.advancement.goal", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.booleans.advancementMessages) {
                String[] args = message.split(" has reached the goal ");
                message = this.config.minecraftToDiscord.messages.advancementGoal
                        .replace("%player", adaptUsernameToDiscord(args[0]))
                        .replace("%advancement", args[1]);
                this.discordBot.sendToAllChannels(message);
            }
        }));

        // Admin commands
        registerTextHandler(new TextHandler("chat.type.admin", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.booleans.adminMessages) this.discordBot.sendToLogChannels(message);
        }));

        // Player join server with new username
        registerTextHandler(new TextHandler("multiplayer.player.joined.renamed", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.booleans.joinAndLeftMessages) {
                String newName = message.split("formerly known as ")[0];
                newName = newName.substring(0, newName.length() - 2);
                String oldName = message.split("formerly known as ")[1].split(" joined the game")[0];
                oldName = oldName.substring(0, oldName.length() - 1);
                message = this.config.minecraftToDiscord.messages.playerJoinedRenamed.replace("%new", adaptUsernameToDiscord(newName)).replace("%old", adaptUsernameToDiscord(oldName));
                this.discordBot.sendToAllChannels(message);
            }
        }));

        // Player join server
        registerTextHandler(new TextHandler("multiplayer.player.joined", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.booleans.joinAndLeftMessages) {
                String name = message.split(" joined the game")[0];
                message = this.config.minecraftToDiscord.messages.playerJoined.replace("%player", adaptUsernameToDiscord(name));
                this.discordBot.sendToAllChannels(message);
            }
        }));

        // Player leave server
        registerTextHandler(new TextHandler("multiplayer.player.left", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.booleans.joinAndLeftMessages) {
                String name = message.split(" left the game")[0];
                message = this.config.minecraftToDiscord.messages.playerLeft.replace("%player", adaptUsernameToDiscord(name));
                this.discordBot.sendToAllChannels(message);
            }
        }));

        // Death messages
        registerTextHandler(new TextHandler("death.", text -> {
            String message = text.getString().replaceAll("§[b0931825467adcfeklmnor]", "");
            if (this.config.minecraftToDiscord.booleans.deathMessages) {
                this.discordBot.sendToAllChannels(message);
            }
        }));
    }

    public String adaptUsernameToDiscord(String string) {
        return string
                .replaceAll("_", "\\_")
                .replaceAll("`", "\\`")
//                .replaceAll(Character.toString('\\'), "\\\\")
//                .replaceAll(Character.toString('*'),"\\*")
                .replaceAll("~", "\\~");
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
        DiscordBot.LOGGER.error("[FDLink] Unhandled text \"{}\":{}", ((TranslatableText)text).getKey(), text.getString());
    }

    public static class TextHandler {
        private final Class textType;
        private final MinecraftToDiscordFunction minecraftToDiscordFunction;
        private String key;

        public TextHandler(Class textType, MinecraftToDiscordFunction minecraftToDiscordFunction) {
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
