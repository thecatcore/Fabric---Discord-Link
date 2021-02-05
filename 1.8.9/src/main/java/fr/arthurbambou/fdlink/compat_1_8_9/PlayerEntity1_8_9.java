package fr.arthurbambou.fdlink.compat_1_8_9;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class PlayerEntity1_8_9 implements PlayerEntity {

    private final ServerPlayerEntity playerEntity;

    public PlayerEntity1_8_9(ServerPlayerEntity playerEntity) {
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
