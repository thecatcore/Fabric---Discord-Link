package fr.arthurbambou.fdlink.discordstuff;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.receive.ReadonlyMessage;
import club.minnced.discord.webhook.send.AllowedMentions;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import fr.arthurbambou.fdlink.config.Config;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DiscordWebhook implements MessageSender {

    protected final Config config;
    protected WebhookClient webhookClient;
    protected DiscordBot messageReader;

    public DiscordWebhook(String webhookURL, Config config, DiscordBot messageReader) {
        this.config = config;
        WebhookClientBuilder builder = new WebhookClientBuilder(webhookURL);
        builder.setAllowedMentions(AllowedMentions.all().withParseEveryone(false));
        this.webhookClient = builder.build();
        this.messageReader = messageReader;
    }

    public @NotNull CompletableFuture<ReadonlyMessage> sendMessage(String author, String message) {
        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        if (author != null) builder.setUsername(author);
        else builder.setUsername("Server");

        if (author != null) builder.setAvatarUrl("https://minotar.net/avatar/" + author);

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
                            this.sendMessage(MinecraftToDiscordHandler.getArgAsString(message.getArgs()[0]), stringMessage);
                        }
                        break;
                    case CHAT:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.playerMessages){
                            this.sendMessage(MinecraftToDiscordHandler.getArgAsString(message.getArgs()[0]), stringMessages[0]);
                        }
                        break;
                    case ME:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.sendMeCommand) {
                            this.sendMessage(MinecraftToDiscordHandler.getArgAsString(message.getArgs()[0]), stringMessage);
                        }
                        break;
                    case SAY:
                        if (this.config.mainConfig.minecraftToDiscord.chatChannels.sendSayCommand) {
                            this.sendMessage(MinecraftToDiscordHandler.getArgAsString(message.getArgs()[0]), stringMessage);
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
                            this.sendMessage(MinecraftToDiscordHandler.getArgAsString(message.getArgs()[0]), stringMessage);
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
}
