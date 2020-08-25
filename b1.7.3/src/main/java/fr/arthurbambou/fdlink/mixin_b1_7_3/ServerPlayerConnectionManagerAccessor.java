package fr.arthurbambou.fdlink.mixin_b1_7_3;

import net.minecraft.server.ServerPlayerConnectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayerConnectionManager.class)
public interface ServerPlayerConnectionManagerAccessor {

    @Accessor("maxPlayerCount")
    int getMaxPlayerCount();
}
