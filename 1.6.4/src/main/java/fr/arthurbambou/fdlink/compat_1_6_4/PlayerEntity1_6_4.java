package fr.arthurbambou.fdlink.compat_1_6_4;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerEntity1_6_4 implements PlayerEntity {

    private final ServerPlayerEntity playerEntity;

    public PlayerEntity1_6_4(ServerPlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    public String getPlayerName() {
        return this.playerEntity.getName();
    }
}
