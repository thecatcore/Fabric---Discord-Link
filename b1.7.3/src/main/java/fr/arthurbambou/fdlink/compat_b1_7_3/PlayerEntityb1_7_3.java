package fr.arthurbambou.fdlink.compat_b1_7_3;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.PlayerEntity;
import net.minecraft.entity.player.ServerPlayer;

public class PlayerEntityb1_7_3 implements PlayerEntity {

    private final ServerPlayer playerEntity;

    public PlayerEntityb1_7_3(ServerPlayer playerEntity) {
        this.playerEntity = playerEntity;
    }

    @Override
    public String getPlayerName() {
        return this.playerEntity.name;
    }
}
