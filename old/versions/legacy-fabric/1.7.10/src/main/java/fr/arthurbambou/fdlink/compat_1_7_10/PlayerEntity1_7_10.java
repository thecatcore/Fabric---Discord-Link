package fr.arthurbambou.fdlink.compat_1_7_10;

import fr.arthurbambou.fdlink.api.minecraft.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class PlayerEntity1_7_10 implements PlayerEntity {

    private final ServerPlayerEntity playerEntity;

    public PlayerEntity1_7_10(ServerPlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    public String getPlayerName() {
        return this.playerEntity.getGameProfile().getName();
    }

    @Override
    public UUID getUUID() {
        return this.playerEntity.getUuid();
    }
}
