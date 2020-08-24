package fr.arthurbambou.fdlink.compat_1_2_5;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerEntity1_2_5 implements PlayerEntity {

    private final ServerPlayerEntity playerEntity;

    public PlayerEntity1_2_5(ServerPlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    public String getPlayerName() {
        return this.playerEntity.name;
    }
}
