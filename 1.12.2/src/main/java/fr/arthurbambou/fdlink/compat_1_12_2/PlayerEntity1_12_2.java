package fr.arthurbambou.fdlink.compat_1_12_2;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerEntity1_12_2 implements PlayerEntity {

    private final ServerPlayerEntity playerEntity;

    public PlayerEntity1_12_2(ServerPlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    public String getPlayerName() {
        return this.playerEntity.method_29611();
    }
}
