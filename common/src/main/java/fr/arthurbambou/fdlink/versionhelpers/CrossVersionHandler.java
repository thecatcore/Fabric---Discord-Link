package fr.arthurbambou.fdlink.versionhelpers;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class CrossVersionHandler {

    private static final List<MessageSender> MESSAGE_SENDERS = new ArrayList<>();
    private static final List<ArgAccessor> ARG_ACCESSORS = new ArrayList<>();

    public static void registerMessageSender(MessageSender messageSender) {
        MESSAGE_SENDERS.add(messageSender);
    }

    public static void registerArgAccessor(ArgAccessor argAccessor) {
        ARG_ACCESSORS.add(argAccessor);
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

    public static Object[] getArgs(Text translatableText) {
        ArgAccessor argAccessor = null;
        for (ArgAccessor argAccessor1 : ARG_ACCESSORS) {
            if (argAccessor1.isCompatibleWithVersion(getMinecraftVersion())) {
                argAccessor = argAccessor1;
                break;
            }
        }
        return argAccessor.getArgs(translatableText);
    }
}
