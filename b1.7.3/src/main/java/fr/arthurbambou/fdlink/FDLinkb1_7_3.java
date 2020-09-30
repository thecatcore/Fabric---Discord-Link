package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.compat_b1_7_3.MessagePacketb1_7_3;
import fr.arthurbambou.fdlink.compat_b1_7_3.Messageb1_7_3;
import fr.arthurbambou.fdlink.compat_b1_7_3.MinecraftServerb1_7_3;
import fr.arthurbambou.fdlink.versionhelpers.CrossVersionHandler;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import io.github.minecraftcursedlegacy.api.event.DedicatedServerTickCallback;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.discovery.ModResolutionException;
import net.fabricmc.loader.gui.FabricGuiEntry;

public class FDLinkb1_7_3 implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (CrossVersionHandler.isVersion("1.0.0-beta.7.3")) {
            FDLink.LOGGER.info("Initializing Beta 1.7.3 Compat module");
            CrossVersionHandler.registerMessageSender((server, message, style) -> {
                Message literalText = new Messageb1_7_3(message);
                if (style != null) {
                    literalText = literalText.setStyle(style);
                }
                server.sendMessageToAll(new MessagePacketb1_7_3(literalText));
            });
            if (!FabricLoader.getInstance().isModLoaded("api")) {
                try {
                    throw new ModResolutionException("Could not find required mod: fdlink requires api");
                } catch (ModResolutionException e) {
                    FabricGuiEntry.displayCriticalError(e, true);
                }
            } else {
                DedicatedServerTickCallback.EVENT.register(minecraftServer -> FDLink.getDiscordBot().serverTick(new MinecraftServerb1_7_3(minecraftServer)));
            }
        }
    }
}
