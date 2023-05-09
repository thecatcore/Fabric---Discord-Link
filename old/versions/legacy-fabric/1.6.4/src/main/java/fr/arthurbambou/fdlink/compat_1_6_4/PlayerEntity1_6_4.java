package fr.arthurbambou.fdlink.compat_1_6_4;

import fr.arthurbambou.fdlink.api.minecraft.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.UUID;

public class PlayerEntity1_6_4 implements PlayerEntity {

    private final ServerPlayerEntity playerEntity;

    public PlayerEntity1_6_4(ServerPlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    public String getPlayerName() {
        return this.playerEntity.method_3334();
    }

    @Override
    public UUID getUUID() {
        return this.playerEntity.getUuid();
    }
}
