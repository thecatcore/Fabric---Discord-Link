package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.compat_b1_7_3.MessagePacketb1_7_3;
import fr.arthurbambou.fdlink.compat_b1_7_3.Messageb1_7_3;
import fr.arthurbambou.fdlink.compat_b1_7_3.MinecraftServerb1_7_3;
import fr.arthurbambou.fdlink.versionhelpers.CrossVersionHandler;
import fr.arthurbambou.fdlink.versionhelpers.MessageSender;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style;
import io.github.minecraftcursedlegacy.api.event.DedicatedServerTickCallback;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.SemanticVersion;

public class FDLinkb1_7_3 implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (canLoad("1.0.0-beta.7.3")) {
            CrossVersionHandler.registerMessageSender(new MessageSender() {
                @Override
                public boolean isCompatibleWithVersion(SemanticVersion semanticVersion) {
                    return canLoad("1.0.0-beta.7.3");
                }

                @Override
                public void sendMessageToChat(MinecraftServer server, String message, Style style) {
                    Message literalText = new Messageb1_7_3(message);
                    if (style != null) {
                        literalText = literalText.setStyle(style);
                    }
                    server.sendMessageToAll(new MessagePacketb1_7_3(literalText));
                }
            });
            DedicatedServerTickCallback.EVENT.register(minecraftServer -> FDLink.getDiscordBot().serverTick(new MinecraftServerb1_7_3(minecraftServer)));
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
