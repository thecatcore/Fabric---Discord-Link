package fr.arthurbambou.fdlink.discord;

import fr.arthurbambou.fdlink.api.discord.MessageHandler;
import fr.arthurbambou.fdlink.api.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.api.minecraft.PlayerEntity;
import fr.arthurbambou.fdlink.discordstuff.MinecraftToDiscordHandler;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public enum Commands {
    playercount("Show the number of player on the server.",(minecraftServer, messageCreateEvent, startTime) -> {
        int playerNumber = minecraftServer.getPlayerCount();
        int maxPlayer = minecraftServer.getMaxPlayerCount();
        messageCreateEvent.getChannel().sendMessage("Playercount: " + playerNumber +  "/" + maxPlayer).submit();
        return false;
    }),
    playerlist("Show the list of player on the server.",(minecraftServer, messageCreateEvent, startTime) -> {
        StringBuilder playerlist = new StringBuilder();

        List<PlayerEntity> players = minecraftServer.getPlayers();

        if (players.size() > 0) {
            playerlist.append("Players:").append("\n");

            for (int i = 0; i < players.size() - 1; i++) {
                playerlist.append("- ").append(MessageHandler.adaptUsername(players.get(i).getPlayerName())).append("\n");
            }

            playerlist.append("- ").append(MessageHandler.adaptUsername(players.get(players.size() - 1).getPlayerName()));
        } else {
            playerlist.append("There are no players online.");
        }

        messageCreateEvent.getChannel().sendMessage(playerlist).submit();
        return false;
    }),
    status("Show various information about the server.", (minecraftServer, messageCreateEvent, startTime) -> {
        int playerNumber = minecraftServer.getPlayerCount();
        int maxPlayer = minecraftServer.getMaxPlayerCount();
        int totalUptimeSeconds = (int) (System.currentTimeMillis() - startTime) / 1000;

        final int uptimeD = totalUptimeSeconds / 86400;
        final int uptimeH = (totalUptimeSeconds / 3600) % 24 ;
        final int uptimeM = (totalUptimeSeconds / 60) % 60;
        final int uptimeS = totalUptimeSeconds % 60;

        messageCreateEvent.getChannel().sendMessage(String.format(
                "Playercount : %d/%d,\n" +
                        "Uptime : %dd %dh %dm %ds",
                playerNumber, maxPlayer, uptimeD, uptimeH, uptimeM, uptimeS
                )
        ).submit();
        return false;
    }),
    uptime("Show the uptime of the server.",(minecraftServer, messageCreateEvent, startTime) -> {
        int totalUptimeSeconds = (int) (System.currentTimeMillis() - startTime) / 1000;

        final int uptimeD = totalUptimeSeconds / 86400;
        final int uptimeH = (totalUptimeSeconds / 3600) % 24;
        final int uptimeM = (totalUptimeSeconds / 60) % 60;
        final int uptimeS = totalUptimeSeconds % 60;

        messageCreateEvent.getChannel().sendMessage("Uptime: " + uptimeD + "d " + uptimeH + "h " + uptimeM + "m " + uptimeS + "s").submit();
        return false;
    }),
    commands("Show the list of commands of the bot.",(minecraftServer, messageCreateEvent, startTime) -> {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Commands: ");
        for (Commands command : values()) {
            stringBuilder.append("\n- ").append(command.name().toLowerCase()).append(": ").append(command.getDescription());
        }
        messageCreateEvent.getChannel().sendMessage(stringBuilder.toString()).submit();
        return false;
    });

    private final String description;
    private final CommandFunction function;

    Commands(String description, CommandFunction function) {
        this.description = description;
        this.function = function;
    }

    public String getDescription() {
        return description;
    }

    public boolean execute(MinecraftServer minecraftServer, MessageReceivedEvent messageCreateEvent, long startTime) {
        return this.function.execute(minecraftServer, messageCreateEvent, startTime);
    }

    private interface CommandFunction {

        boolean execute(MinecraftServer minecraftServer, MessageReceivedEvent messageCreateEvent, long startTime);
    }
}
