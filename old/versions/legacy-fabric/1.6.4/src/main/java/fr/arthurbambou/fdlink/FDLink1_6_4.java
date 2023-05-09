package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.api.minecraft.Message;
import fr.arthurbambou.fdlink.api.minecraft.VersionHelper;
import fr.arthurbambou.fdlink.compat_1_6_4.Message1_6_4;
import fr.arthurbambou.fdlink.compat_1_6_4.MessagePacket1_6_4;
import net.fabricmc.api.DedicatedServerModInitializer;

public class FDLink1_6_4 implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (VersionHelper.isVersion("1.6.4")) {
            FDLink.LOGGER.info("Initializing 1.6.4 Compat module");
            VersionHelper.registerMessageSender((server, message, style) -> {
                Message literalText = new Message1_6_4(message);
                if (style != null) {
                    literalText = literalText.setStyle(style);
                }
                server.sendMessageToAll(new MessagePacket1_6_4(literalText));
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
