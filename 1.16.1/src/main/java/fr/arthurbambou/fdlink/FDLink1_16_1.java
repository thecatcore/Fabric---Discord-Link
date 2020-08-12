package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.mixin_1_16_1.TranslatableTextAccessor;
import fr.arthurbambou.fdlink.versionhelpers.ArgAccessor;
import fr.arthurbambou.fdlink.versionhelpers.CrossVersionHandler;
import fr.arthurbambou.fdlink.versionhelpers.MessageSender;
import fr.arthurbambou.fdlink.versionhelpers.StyleApplier;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.*;

import java.util.UUID;

public class FDLink1_16_1 implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (canLoad(CrossVersionHandler.getMinecraftVersion(), "1.16-Snapshot.20.17.a")) {
            CrossVersionHandler.registerStyleApplier(new StyleApplier() {
                @Override
                public boolean isCompatibleWithVersion(SemanticVersion semanticVersion) {
                    return canLoad(semanticVersion, "1.16-Snapshot.20.17.a");
                }

                @Override
                public Style getStyleWithClickEventURL(String url) {
                    Style style = Style.EMPTY;
                    style = style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
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
                    MutableText literalText = new LiteralText(message);
                    if (style != null) {
                        literalText = literalText.setStyle(style);
                    }
                    server.getPlayerManager().sendToAll(new GameMessageS2CPacket(literalText, MessageType.CHAT, UUID.randomUUID()));
                }
            });
        }
        CrossVersionHandler.registerArgAccessor(new ArgAccessor() {
            @Override
            public boolean isCompatibleWithVersion(SemanticVersion semanticVersion) {
                return true;
            }

            @Override
            public Object[] getArgs(Text translatableText) {
                return ((TranslatableTextAccessor)translatableText).getArgs();
            }
        });
    }

    public static boolean canLoad(SemanticVersion semanticVersion, String otherVersion) {
        try {
            int comparaison = SemanticVersion.parse(otherVersion).compareTo(semanticVersion);
            return comparaison <= 0;
        } catch (VersionParsingException versionParsingException) {
            versionParsingException.printStackTrace();
        }
        return false;
    }
}
