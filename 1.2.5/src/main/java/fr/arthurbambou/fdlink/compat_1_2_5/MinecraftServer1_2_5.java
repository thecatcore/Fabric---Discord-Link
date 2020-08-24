package fr.arthurbambou.fdlink.compat_1_2_5;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MessagePacket;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.PlayerEntity;
import net.minecraft.packet.ChatMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.List;

public class MinecraftServer1_2_5 implements MinecraftServer {

    private final net.minecraft.server.MinecraftServer minecraftServer;

    public MinecraftServer1_2_5(net.minecraft.server.MinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
    }

    @Override
    public int getPlayerCount() {
        return this.minecraftServer.playerManager.players.size();
    }

    @Override
    public int getMaxPlayerCount() {
        return this.minecraftServer.playerManager.getMaxPlayerCount();
    }

    @Override
    public List<PlayerEntity> getPlayers() {
        List<PlayerEntity> list = new ArrayList<>();
        for (Object playerEntity : this.minecraftServer.playerManager.players) {
            list.add(new PlayerEntity1_2_5((ServerPlayerEntity) playerEntity));
        }
        return list;
    }

    @Override
    public void sendMessageToAll(MessagePacket messagePacket) {
        Message message = messagePacket.getMessage();
        this.minecraftServer.playerManager.sendToAll(new ChatMessageS2CPacket(message.getMessage()));
    }
}
