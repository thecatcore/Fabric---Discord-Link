package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.api.minecraft.Message;
import fr.arthurbambou.fdlink.api.minecraft.VersionHelper;
import fr.arthurbambou.fdlink.compat_1_8_9.Message1_8_9;
import fr.arthurbambou.fdlink.compat_1_8_9.MessagePacket1_8_9;
import fr.arthurbambou.fdlink.compat_1_8_9.MinecraftServer1_8_9;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class FDLink1_8_9 implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (VersionHelper.isVersion("1.8.9")) {
            FDLink.LOGGER.info("Initializing 1.8.9 Compat module");
            VersionHelper.registerMessageSender((server, message, style) -> {
                Message literalText = new Message1_8_9(message);
                if (style != null) {
                    literalText = literalText.setStyle(style);
                }
                server.sendMessageToAll(new MessagePacket1_8_9(literalText));
            });

//            if (FabricLoader.getInstance().isModLoaded("legacy-fabric-api")) {
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
//                VersionHelper.throwModResolution("Could not find required mod: fdlink requires legacy-fabric-api (https://www.curseforge.com/minecraft/mc-mods/legacy-fabric-api)");
//            }
        }
    }
}
