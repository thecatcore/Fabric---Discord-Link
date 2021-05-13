package fr.arthurbambou.fdlink.api.minecraft;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface MinecraftServer {

    String getMotd();

    int getPlayerCount();

    int getMaxPlayerCount();

    List<PlayerEntity> getPlayers();

    void sendMessageToAll(MessagePacket messagePacket);

    String getIp();

    File getIcon();

    PlayerEntity getPlayerFromUsername(String username);

    String getUsernameFromUUID(UUID uuid);
}
