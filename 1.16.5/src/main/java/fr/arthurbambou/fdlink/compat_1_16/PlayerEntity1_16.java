package fr.arthurbambou.fdlink.compat_1_16;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerEntity1_16 implements PlayerEntity {

    private final ServerPlayerEntity playerEntity;

    public PlayerEntity1_16(ServerPlayerEntity playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    public String getPlayerName() {
        return this.playerEntity.getName().getString();
    }
}
