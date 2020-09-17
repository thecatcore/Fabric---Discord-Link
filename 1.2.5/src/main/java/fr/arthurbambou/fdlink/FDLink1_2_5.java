package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.compat_1_2_5.Message1_2_5;
import fr.arthurbambou.fdlink.compat_1_2_5.MessagePacket1_2_5;
import fr.arthurbambou.fdlink.versionhelpers.CrossVersionHandler;
import fr.arthurbambou.fdlink.versionhelpers.MessageSender;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.SemanticVersion;

public class FDLink1_2_5 implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (canLoad("1.2.5")) {
            FDLink.LOGGER.info("Initializing 1.2.5 Compat module");
            CrossVersionHandler.registerMessageSender(new MessageSender() {
                @Override
                public boolean isCompatibleWithVersion(SemanticVersion semanticVersion) {
                    return canLoad("1.2.5");
                }

                @Override
                public void sendMessageToChat(MinecraftServer server, String message, Style style) {
                    Message literalText = new Message1_2_5(message);
                    if (style != null) {
                        literalText = literalText.setStyle(style);
                    }
                    server.sendMessageToAll(new MessagePacket1_2_5(literalText));
                }
            });
        }
    }

    public static boolean canLoad(String version) {
        try {
            int comparaison = SemanticVersion.parse(version).compareTo(CrossVersionHandler.getMinecraftVersion());
            return comparaison == 0;
        } catch (net.fabricmc.loader.api.VersionParsingException versionParsingException) {
            versionParsingException.printStackTrace();
        }
        return false;
    }
}
