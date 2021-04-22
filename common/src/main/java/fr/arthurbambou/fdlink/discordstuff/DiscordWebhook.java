package fr.arthurbambou.fdlink.discordstuff;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.receive.ReadonlyMessage;
import club.minnced.discord.webhook.send.AllowedMentions;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.config.Config;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.PlayerEntity;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DiscordWebhook implements MessageSender {

    protected final Config config;
    protected WebhookClient webhookClient;
    protected DiscordBot messageReader;

    public DiscordWebhook(String webhookURL, Config config, DiscordBot messageReader) {
        this.config = config;
        WebhookClientBuilder builder = new WebhookClientBuilder(webhookURL)
                .setAllowedMentions(AllowedMentions.none()
                        .withParseEveryone(config.mainConfig.webhook.mentions.everyone)
                        .withParseRoles(config.mainConfig.webhook.mentions.roles)
                        .withParseUsers(config.mainConfig.webhook.mentions.users)
                )
                // Fix found by Tom_The_Geek (Geek202), creator of TomsServerUtils.
                .setHttpClient(new OkHttpClient.Builder()
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    .build());
        this.webhookClient = builder.build();
        this.messageReader = messageReader;
    }

    public @NotNull CompletableFuture<ReadonlyMessage> sendMessage(UUID author, String message) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();

        if (author != null) {
            try {
                if (FDLink.getMessageReceiver() != null && FDLink.getMessageReceiver().getServer() != null) {
                    MinecraftServer minecraftServer = FDLink.getMessageReceiver().getServer();
                    builder.setUsername(minecraftServer.getUsernameFromUUID(author));
                } else {
                    builder.setUsername("Could not get player username");
                }
            } catch (NullPointerException e) {
                builder.setUsername("Could not get player username");
            }
            builder.setAvatarUrl("https://crafatar.com/avatars/" + author.toString() + "?&overlay");
        }

        builder.setContent(message);

        return this.webhookClient.send(builder.build());
    }

    @Override
    public void serverStarting() {
        if (this.config.mainConfig.minecraftToDiscord.chatChannels.serverStartingMessage) this.sendMessage(null, this.config.messageConfig.minecraftToDiscord.serverStarting);
    }

    @Override
    public void serverStarted() {
        if (this.config.mainConfig.minecraftToDiscord.chatChannels.serverStartMessage) this.sendMessage(null, this.config.messageConfig.minecraftToDiscord.serverStarted);
    }

    @Override
    public void serverStopping() {
        if (this.config.mainConfig.minecraftToDiscord.chatChannels.serverStoppingMessage) this.sendMessage(null, this.config.messageConfig.minecraftToDiscord.serverStopping);
        if (this.messageReader != null) this.messageReader.serverStopping();
    }

    @Override
    public void serverStopped() {
        if (this.config.mainConfig.minecraftToDiscord.chatChannels.serverStopMessage) this.sendMessage(null, this.config.messageConfig.minecraftToDiscord.serverStopped)
                .thenRun(new Runnable() {
                    @Override
                    public void run() {
                        DiscordWebhook.this.webhookClient.close();
                    }
                });
        else this.webhookClient.close();
        if (this.messageReader != null) this.messageReader.serverStopped();
    }

    @Override
    public void sendMessage(Message message) {
        if (this.messageReader != null && this.messageReader.minecraftToDiscordHandler != null
                && this.webhookClient != null && this.config != null) {
            MinecraftMessage minecraftMessage = this.messageReader.minecraftToDiscordHandler.handleTexts(message);
            if (minecraftMessage != null) {
                String stringMessage = minecraftMessage.getMessage();
                String[] stringMessages = minecraftMessage.getMessages();
                switch (minecraftMessage.getType()) {
                    case CHAT_COMMAND:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.allowDiscordCommands) {
                            this.sendMessage(
                                    message.hasAuthorUUID() ? message.getAuthorUUID() : getPlayerUUIDFromText(message.getArgs()[0]),
                                    stringMessage);
                        }
                        break;
                    case CHAT:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.playerMessages){
                            this.sendMessage(message.hasAuthorUUID() ? message.getAuthorUUID() : getPlayerUUIDFromText(message.getArgs()[0]), stringMessages[0]);
                        }
                        break;
                    case TEAM_CHAT:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.teamPlayerMessages){
                            this.sendMessage(message.hasAuthorUUID() ? message.getAuthorUUID() : getPlayerUUIDFromText(message.getArgs()[1]), stringMessages[0]);
                        }
                        break;
                    case ME:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.sendMeCommand) {
                            this.sendMessage(message.hasAuthorUUID() ? message.getAuthorUUID() : getPlayerUUIDFromText(message.getArgs()[0]), stringMessage);
                        }
                        break;
                    case SAY:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.sendSayCommand) {
                            this.sendMessage(message.hasAuthorUUID() ? message.getAuthorUUID() : getPlayerUUIDFromText(message.getArgs()[0]), stringMessage);
                        }
                        break;
                    case ADVANCEMENT_TASK:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.advancementMessages) {
                            this.sendMessage(null, stringMessage);
                        }
                        break;
                    case ADVANCEMENT_CHALLENGE:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.challengeMessages) {
                            this.sendMessage(null, stringMessage);
                        }
                        break;
                    case ADVANCEMENT_GOAL:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.goalMessages) {
                            this.sendMessage(null, stringMessage);
                        }
                        break;
                    case ADMIN:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.adminMessages) {
                            this.sendMessage(null, stringMessage);
                        }
                        break;
                    case JOIN_RENAMED:
                    case JOIN:
                    case LEAVE:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.joinAndLeaveMessages) {
                            this.sendMessage(null, stringMessage);
                        }
                        break;
                    case DEATH:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.deathMessages) {
                            this.sendMessage(null, stringMessage);
                        }
                        break;
                    case TELLRAW:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.atATellRaw) {
                            this.sendMessage(message.hasAuthorUUID() ? message.getAuthorUUID() : getPlayerUUIDFromText(message.getArgs()[0]), stringMessage);
                        }
                        break;
                    case ACHIEVEMENT:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.achievementMessages) {
                            this.sendMessage(null, stringMessage);
                        }
                        break;
                    case STRING_OLD:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.playerMessages) {
                            this.sendMessage(null, stringMessage);
                        }
                        break;
                    case CUSTOM:
                        minecraftMessage.getMessageSender().sendMessage(stringMessage, this, this.config);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private UUID getPlayerUUIDFromText(Object arg) {
        String playerName = "";
        if (arg instanceof Message) {
            Message message = (Message) arg;
            if (message.getSibblings().isEmpty()) {
                playerName = message.getMessage();
            } else if (message.getSibblings().size() == 3) {
                playerName = message.getSibblings().get(1).getMessage();
            }
        }

        if (playerName.isEmpty()) {
            playerName = MinecraftToDiscordHandler.getArgAsString(arg);
        }
        try {
            if (!playerName.isEmpty() && FDLink.getMessageReceiver() != null && FDLink.getMessageReceiver().getServer() != null) {
                MinecraftServer minecraftServer = FDLink.getMessageReceiver().getServer();
                PlayerEntity playerEntity = minecraftServer.getPlayerFromUsername(playerName);
                return playerEntity.getUUID();
            }
        } catch (NullPointerException ignored) {
            ignored.printStackTrace();
        }

        return null;
    }
}
