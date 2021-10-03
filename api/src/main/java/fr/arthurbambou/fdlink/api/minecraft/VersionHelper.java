package fr.arthurbambou.fdlink.api.minecraft;

import fr.arthurbambou.fdlink.api.minecraft.style.Style;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.api.VersionParsingException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class VersionHelper {
    private static final List<MessageSender> MESSAGE_SENDERS = new ArrayList<>();

    private static final Pattern RELEASE_PATTERN = Pattern.compile("\\d+\\.\\d+(\\.\\d+)?");

    public static void registerMessageSender(MessageSender messageSender) {
        MESSAGE_SENDERS.add(messageSender);
    }

    private static SemanticVersion getModVersion(String modId) {
        return (SemanticVersion) FabricLoader.getInstance().getModContainer(modId).get().getMetadata().getVersion();
    }

    public static VersionComparison compareToMinecraftVersion(String version) {
        return compareToModVersion("minecraft", version);
    }

    public static VersionComparison compareToModVersion(String modId, String version) {
        return compareTo(getModVersion(modId), version);
    }

    public static VersionComparison compareTo(String comparedTo, String compared) {
        try {
            return compareTo(SemanticVersion.parse(comparedTo), compared);
        } catch (VersionParsingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static VersionComparison compareTo(SemanticVersion comparedTo, String compared) {
        try {
            return compareTo(comparedTo, SemanticVersion.parse(compared));
        } catch (VersionParsingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static VersionComparison compareTo(SemanticVersion comparedTo, SemanticVersion compared) {
        return new VersionComparison(comparedTo, compared);
    }

    public static void sendMessageToChat(MinecraftServer server, String message, Style style) {
        MessageSender messageSender = MESSAGE_SENDERS.get(0);
        messageSender.sendMessageToChat(server, message, style);
    }

    public static boolean isRelease(String version) {
        return RELEASE_PATTERN.matcher(version).matches();
    }

    public static boolean isRelease() {
        return isRelease(getModVersion("minecraft").getFriendlyString());
    }

    public static boolean isVersion(String version) {
        return compareToMinecraftVersion(version).isEqual();
    }

    public static void throwModResolution(String message) {
        String exceptionName;
        String guiName;
        if (compareToModVersion("fabricloader", "0.12.0").isMoreRecentOrEqual()) {
            exceptionName = "net.fabricmc.loader.impl.discovery.ModResolutionException";
            guiName = "net.fabricmc.loader.impl.gui.FabricGuiEntry";
        } else {
            exceptionName = "net.fabricmc.loader.discovery.ModResolutionException";
            guiName = "net.fabricmc.loader.gui.FabricGuiEntry";
        }
        try {
            Constructor<?> constructor = Class.forName(exceptionName).getDeclaredConstructor(String.class);
            throw (Throwable) constructor.newInstance(message);
        } catch (Throwable e) {
            try {
                Method method = Class.forName(guiName).getMethod("displayCriticalError", Throwable.class, boolean.class);
                method.invoke(null, e, true);
            } catch (Throwable ex) {
                ex.printStackTrace();
            }
        }
    }

    public static class VersionComparison {
        private SemanticVersion version1;
        private SemanticVersion version2;

        protected VersionComparison(SemanticVersion version1, SemanticVersion version2) {
            this.version1 = version1;
            this.version2 = version2;
        }

        private int compare() {
            return version1.compareTo(version2);
        }

        public boolean isMoreRecent() {
            return this.compare() > 0;
        }

        public boolean isMoreRecentOrEqual() {
            return this.compare() >= 0;
        }

        public boolean isEqual() {
            return this.compare() == 0;
        }

        public boolean isOlder() {
            return this.compare() < 0;
        }

        public boolean isOlderOrEqual() {
            return this.compare() <= 0;
        }
    }
}
