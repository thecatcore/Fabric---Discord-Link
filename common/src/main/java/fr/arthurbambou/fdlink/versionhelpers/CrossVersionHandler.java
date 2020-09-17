package fr.arthurbambou.fdlink.versionhelpers;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CrossVersionHandler {

    private static final List<MessageSender> MESSAGE_SENDERS = new ArrayList<>();

    private static final Pattern RELEASE_PATTERN = Pattern.compile("\\d+\\.\\d+(\\.\\d+)?");

    public static void registerMessageSender(MessageSender messageSender) {
        MESSAGE_SENDERS.add(messageSender);
    }

    public static SemanticVersion getMinecraftVersion() {
        try {
            return SemanticVersion.parse(FabricLoader.getInstance().getModContainer("minecraft").get().getMetadata().getVersion().getFriendlyString());
        } catch (VersionParsingException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static void sendMessageToChat(MinecraftServer server, String message, Style style) {
        MessageSender messageSender = null;
        for (MessageSender messageSender1 : MESSAGE_SENDERS) {
            if (messageSender1.isCompatibleWithVersion(getMinecraftVersion())) {
                messageSender = messageSender1;
                break;
            }
        }
        assert messageSender != null;
        messageSender.sendMessageToChat(server, message, style);
    }

    public static boolean isRelease(String version) {
        return RELEASE_PATTERN.matcher(version).matches();
    }

    public static boolean isRelease() {
        return isRelease(getMinecraftVersion().getFriendlyString());
    }
}
