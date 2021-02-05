package fr.arthurbambou.fdlink.compat_1_5_2;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.PlayerEntity;
import net.minecraft.entity.ServerPlayer;

import java.util.UUID;

public class PlayerEntity1_5_2 implements PlayerEntity {

    private final ServerPlayer playerEntity;

    public PlayerEntity1_5_2(ServerPlayer playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    public String getPlayerName() {
        return this.playerEntity.getName();
    }

    @Override
    public UUID getUUID() {
        return UUID.randomUUID();
    }
}
