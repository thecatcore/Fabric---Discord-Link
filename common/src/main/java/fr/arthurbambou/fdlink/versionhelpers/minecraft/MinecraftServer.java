package fr.arthurbambou.fdlink.versionhelpers.minecraft;

import java.util.List;

public interface MinecraftServer {

    int getPlayerCount();

    int getMaxPlayerCount();

    List<PlayerEntity> getPlayers();

    void sendMessageToAll(MessagePacket messagePacket);
}
