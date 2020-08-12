package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.versionhelpers.CrossVersionHandler;
import fr.arthurbambou.fdlink.versionhelpers.MessageSender;
import fr.arthurbambou.fdlink.versionhelpers.StyleApplier;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;
import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.UUID;

public class FDLink1_15_2 implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        FDLink.LOGGER.info("Initializing 1.14-1.15 Compat module");
        if (canLoad(CrossVersionHandler.getMinecraftVersion(), "1.16-Snapshot.20.17.a")) {
            CrossVersionHandler.registerStyleApplier(new StyleApplier() {
                @Override
                public boolean isCompatibleWithVersion(SemanticVersion semanticVersion) {
                    return canLoad(semanticVersion, "1.16-Snapshot.20.17.a");
                }

                @Override
                public Style getStyleWithClickEventURL(String url) {
                    Style style = new Style();
                    style = style.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
                    return style;
                }
            });
        }
        if (canLoad(CrossVersionHandler.getMinecraftVersion(), "1.16-Snapshot.20.21.a")) {
            CrossVersionHandler.registerMessageSender(new MessageSender() {
                @Override
                public boolean isCompatibleWithVersion(SemanticVersion semanticVersion) {
                    return canLoad(semanticVersion, "1.16-Snapshot.20.21.a");
                }

                @Override
                public void sendMessageToChat(MinecraftServer server, String message, Style style) {
                    Text literalText = new LiteralText(message);
                    if (style != null) {
                        literalText = literalText.setStyle(style);
                    }
                    server.getPlayerManager().sendToAll(literalText);
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
