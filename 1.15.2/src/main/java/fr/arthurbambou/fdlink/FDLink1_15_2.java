package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.compat_1_15_2.Message1_15_2;
import fr.arthurbambou.fdlink.compat_1_15_2.MessagePacket1_15_2;
import fr.arthurbambou.fdlink.compat_1_15_2.MinecraftServer1_15_2;
import fr.arthurbambou.fdlink.versionhelpers.CrossVersionHandler;
import fr.arthurbambou.fdlink.versionhelpers.MessageSender;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;

public class FDLink1_15_2 implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        FDLink.LOGGER.info("Initializing 1.14-1.15 Compat module");
        if (canLoad(CrossVersionHandler.getMinecraftVersion(), "1.16-Snapshot.20.17.a")) {
            ServerTickEvents.START_SERVER_TICK.register((server -> FDLink.getDiscordBot().serverTick(new MinecraftServer1_15_2(server))));
        }
        if (canLoad(CrossVersionHandler.getMinecraftVersion(), "1.16-Snapshot.20.21.a")) {
            CrossVersionHandler.registerMessageSender(new MessageSender() {
                @Override
                public boolean isCompatibleWithVersion(SemanticVersion semanticVersion) {
                    return canLoad(semanticVersion, "1.16-Snapshot.20.21.a");
                }

                @Override
                public void sendMessageToChat(MinecraftServer server, String message, Style style) {
                    Message literalText = new Message1_15_2(message);
                    if (style != null) {
                        literalText = literalText.setStyle(style);
                    }
                    server.sendMessageToAll(new MessagePacket1_15_2(literalText));
                }
            });
        }
    }

    public static boolean canLoad(SemanticVersion semanticVersion, String higher) {
        try {
            int comparaison = SemanticVersion.parse(higher).compareTo(semanticVersion);
            return comparaison > 0;
        } catch (VersionParsingException versionParsingException) {
            versionParsingException.printStackTrace();
        }
        return false;
    }
}
