package fr.arthurbambou.fdlink.api.util;

import fr.arthurbambou.fdlink.api.minecraft.VersionHelper;

public class LogManager {

    public static Logger getLogger(String name) {
        if (VersionHelper.compareToMinecraftVersion("1.18.2-alpha.22.03.a").isMoreRecentOrEqual()) {
            return new Slf4jLogger(name);
        }

        try {
            return new Log4jLogger(name);
        } catch (Error ignored) {}

        return new SysoutLogger(name);
    }
}
