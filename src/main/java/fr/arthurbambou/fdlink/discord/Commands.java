package fr.arthurbambou.fdlink.discord;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Util;
import org.javacord.api.event.message.MessageCreateEvent;

public enum Commands {
    playercount("Show the number of player on the server.",(minecraftServer, messageCreateEvent, startTime) -> {
        int playerNumber = minecraftServer.getPlayerManager().getPlayerList().size();
        int maxPlayer = minecraftServer.getPlayerManager().getMaxPlayerCount();
        messageCreateEvent.getChannel().sendMessage("Playercount: " + playerNumber +  "/" + maxPlayer);
        return false;
    }),
    playerlist("Show the list of player on the server.",(minecraftServer, messageCreateEvent, startTime) -> {
        StringBuilder playerlist = new StringBuilder();
        for (PlayerEntity playerEntity : minecraftServer.getPlayerManager().getPlayerList()) {
            playerlist.append(playerEntity.getName().getString()).append("\n");
        }
        if (playerlist.toString().endsWith("\n")) {
            int a = playerlist.lastIndexOf("\n");
            playerlist = new StringBuilder(playerlist.substring(0, a));
        }
        messageCreateEvent.getChannel().sendMessage("\n Players: \n" + playerlist);
        return false;
    }),
    status("Show various information about the server.", (minecraftServer, messageCreateEvent, startTime) -> {
        int playerNumber = minecraftServer.getPlayerManager().getPlayerList().size();
        int maxPlayer = minecraftServer.getPlayerManager().getMaxPlayerCount();
        int totalUptimeSeconds = (int) (Util.getMeasuringTimeMs() - startTime) / 1000;

        final int uptimeH = totalUptimeSeconds / 3600 ;
        final int uptimeM = (totalUptimeSeconds % 3600) / 60;
        final int uptimeS = totalUptimeSeconds % 60;

        messageCreateEvent.getChannel().sendMessage(String.format(
                "Playercount : %d/%d,\n" +
                        "Uptime : %dh %dm %ds",
                playerNumber, maxPlayer, uptimeH, uptimeM, uptimeS
                )
        );
        return false;
    }),
    uptime("Show the uptime of the server.",(minecraftServer, messageCreateEvent, startTime) -> {
        int totalUptimeSeconds = (int) (Util.getMeasuringTimeMs() - startTime) / 1000;

        final int uptimeH = totalUptimeSeconds / 3600 ;
        final int uptimeM = (totalUptimeSeconds % 3600) / 60;
        final int uptimeS = totalUptimeSeconds % 60;

        messageCreateEvent.getChannel().sendMessage("Uptime: " + uptimeH + "h " + uptimeM + "m " + uptimeS + "s");
        return false;
    }),
    commands("Show the list of commands of the bot.",(minecraftServer, messageCreateEvent, startTime) -> {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Commands: ");
        for (Commands command : values()) {
            stringBuilder.append("\n- ").append(command.name().toLowerCase()).append(": ").append(command.getDescription());
        }
        messageCreateEvent.getChannel().sendMessage(stringBuilder.toString());
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

    public boolean execute(MinecraftServer minecraftServer, MessageCreateEvent messageCreateEvent, long startTime) {
        return this.function.execute(minecraftServer, messageCreateEvent, startTime);
    }

    private interface CommandFunction {

        boolean execute(MinecraftServer minecraftServer, MessageCreateEvent messageCreateEvent, long startTime);
    }
}
