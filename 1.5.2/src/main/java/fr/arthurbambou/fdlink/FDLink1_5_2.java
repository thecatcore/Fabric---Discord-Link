package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.compat_1_5_2.Message1_5_2;
import fr.arthurbambou.fdlink.compat_1_5_2.MessagePacket1_5_2;
import fr.arthurbambou.fdlink.versionhelpers.CrossVersionHandler;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import net.fabricmc.api.DedicatedServerModInitializer;

public class FDLink1_5_2 implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (CrossVersionHandler.isVersion("1.5.2")) {
            FDLink.LOGGER.info("Initializing 1.5.2 Compat module");
            CrossVersionHandler.registerMessageSender((server, message, style) -> {
                Message literalText = new Message1_5_2(message);
                if (style != null) {
                    literalText = literalText.setStyle(style);
                }
                server.sendMessageToAll(new MessagePacket1_5_2(literalText));
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
