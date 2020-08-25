package fr.arthurbambou.fdlink.compat_b1_7_3;

import fr.arthurbambou.fdlink.mixin_b1_7_3.ServerPlayerConnectionManagerAccessor;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MessagePacket;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.PlayerEntity;
import net.minecraft.entity.player.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public class MinecraftServerb1_7_3 implements MinecraftServer {

    private final net.minecraft.server.MinecraftServer minecraftServer;

    public MinecraftServerb1_7_3(net.minecraft.server.MinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
    }

    @Override
    public int getPlayerCount() {
        return this.minecraftServer.field_2842.players.size();
    }

    @Override
    public int getMaxPlayerCount() {
        return ((ServerPlayerConnectionManagerAccessor)this.minecraftServer.field_2842).getMaxPlayerCount();
    }

    @Override
    public List<PlayerEntity> getPlayers() {
        List<PlayerEntity> list = new ArrayList<>();
        for (Object playerEntity : this.minecraftServer.field_2842.players) {
            list.add(new PlayerEntityb1_7_3((ServerPlayer) playerEntity));
        }
        return list;
    }

    @Override
    public void sendMessageToAll(MessagePacket messagePacket) {
        Message message = messagePacket.getMessage();
        this.minecraftServer.field_2842.sendChatMessage(message.getMessage());
    }
}
