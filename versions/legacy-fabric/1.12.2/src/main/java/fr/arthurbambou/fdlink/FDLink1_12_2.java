package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.api.minecraft.Message;
import fr.arthurbambou.fdlink.api.minecraft.VersionHelper;
import fr.arthurbambou.fdlink.compat_1_12_2.Message1_12_2;
import fr.arthurbambou.fdlink.compat_1_12_2.MessagePacket1_12_2;
import fr.arthurbambou.fdlink.compat_1_12_2.MinecraftServer1_12_2;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.discovery.ModResolutionException;
import net.fabricmc.loader.gui.FabricGuiEntry;
import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.legacyfabric.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class FDLink1_12_2 implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (VersionHelper.isVersion("1.12.2")) {
            FDLink.LOGGER.info("Initializing 1.12.2 Compat module");
            VersionHelper.registerMessageSender((server, message, style) -> {
                Message literalText = new Message1_12_2(message);
                if (style != null) {
                    literalText = literalText.setStyle(style);
                }
                server.sendMessageToAll(new MessagePacket1_12_2(literalText));
            });

            if (FabricLoader.getInstance().isModLoaded("legacy-fabric-api")) {
                ServerTickEvents.START_SERVER_TICK.register(server -> {
                    FDLink.getMessageReceiver().serverTick(new MinecraftServer1_12_2(server));
                });

                ServerLifecycleEvents.SERVER_STARTING.register(server -> {
                    FDLink.getMessageSender().serverStarting();
                });
                ServerLifecycleEvents.SERVER_STARTED.register(server -> {
                    FDLink.getMessageSender().serverStarted();
                });
                ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
                    FDLink.getMessageSender().serverStopping();
                });
                ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
                    FDLink.getMessageSender().serverStopped();
                });
            } else {
                VersionHelper.throwModResolution("Could not find required mod: fdlink requires legacy-fabric-api (https://maven.legacyfabric.net/net/legacyfabric/legacy-fabric-api/legacy-fabric-api/1.1.1+1.12.2/legacy-fabric-api-1.1.1+1.12.2.jar)");
            }
        }
    }
}
