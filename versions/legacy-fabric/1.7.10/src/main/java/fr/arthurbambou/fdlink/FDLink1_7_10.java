package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.api.minecraft.Message;
import fr.arthurbambou.fdlink.api.minecraft.VersionHelper;
import fr.arthurbambou.fdlink.compat_1_7_10.Message1_7_10;
import fr.arthurbambou.fdlink.compat_1_7_10.MessagePacket1_7_10;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.discovery.ModResolutionException;
import net.fabricmc.loader.gui.FabricGuiEntry;

public class FDLink1_7_10 implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (VersionHelper.isVersion("1.7.10")) {
            FDLink.LOGGER.info("Initializing 1.7.10 Compat module");
            VersionHelper.registerMessageSender((server, message, style) -> {
                Message literalText = new Message1_7_10(message);
                if (style != null) {
                    literalText = literalText.setStyle(style);
                }
                server.sendMessageToAll(new MessagePacket1_7_10(literalText));
            });

//            if (FabricLoader.getInstance().isModLoaded("fabric")) {
//                ServerTickEvents.START_SERVER_TICK.register(server -> {
//                    FDLink.getMessageReceiver().serverTick(new MinecraftServer1_8_9(server));
//                });
//
//                ServerLifecycleEvents.SERVER_STARTING.register(server -> {
//                    FDLink.getMessageSender().serverStarting();
//                });
//                ServerLifecycleEvents.SERVER_STARTED.register(server -> {
//                    FDLink.getMessageSender().serverStarted();
//                });
//                ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
//                    FDLink.getMessageSender().serverStopping();
//                });
//                ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
//                    FDLink.getMessageSender().serverStopped();
//                });
//            } else {
//                try {
//                    throw new ModResolutionException("Could not find required mod: fdlink requires fabric");
//                } catch (ModResolutionException e) {
//                    FabricGuiEntry.displayCriticalError(e, true);
//                }
//            }
        }
    }
}
