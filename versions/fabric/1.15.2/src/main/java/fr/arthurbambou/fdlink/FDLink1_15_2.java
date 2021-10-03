package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.api.minecraft.Message;
import fr.arthurbambou.fdlink.api.minecraft.VersionHelper;
import fr.arthurbambou.fdlink.compat_1_15_2.Message1_15_2;
import fr.arthurbambou.fdlink.compat_1_15_2.MessagePacket1_15_2;
import fr.arthurbambou.fdlink.compat_1_15_2.MinecraftServer1_15_2;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.discovery.ModResolutionException;
import net.fabricmc.loader.gui.FabricGuiEntry;

public class FDLink1_15_2 implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (VersionHelper.isVersion("1.15.2") || VersionHelper.isVersion("1.14.4")) {
            if (!FabricLoader.getInstance().isModLoaded("fabric")) {
                VersionHelper.throwModResolution("Could not find required mod: fdlink requires fabric");
            }
            ServerTickEvents.START_SERVER_TICK.register((server -> FDLink.getMessageReceiver().serverTick(new MinecraftServer1_15_2(server))));
        }
        if ((VersionHelper.compareToMinecraftVersion("1.16-alpha.20.21.a").isOlder()
                && VersionHelper.compareToMinecraftVersion("1.14").isMoreRecentOrEqual()) || VersionHelper.isVersion("1.16-20.w.14")) {
            FDLink.LOGGER.info("Initializing 1.14-1.15 Compat module");
            VersionHelper.registerMessageSender((server, message, style) -> {
                Message literalText = new Message1_15_2(message);
                if (style != null) {
                    literalText = literalText.setStyle(style);
                }
                server.sendMessageToAll(new MessagePacket1_15_2(literalText));
            });
        }
    }
}
