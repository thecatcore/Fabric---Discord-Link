package fr.arthurbambou.fdlink.mixin_1_16.compat;

import fr.arthurbambou.fdlink.FDLink1_16;
import net.logandark.fabricconsole.TextToAnsi;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(value = MinecraftDedicatedServer.class, priority = 1100)
public abstract class FabricConsoleMixinMinecraftDedicatedServer {
    @Shadow
    @Final
    private static Logger LOGGER;

    public void sendSystemMessage(Text message, UUID senderUuid) {
        FDLink1_16.handleText(message, senderUuid);
        LOGGER.info(TextToAnsi.INSTANCE.textToAnsi(message));
    }
}
