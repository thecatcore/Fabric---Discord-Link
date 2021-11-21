package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.api.minecraft.Message;
import fr.arthurbambou.fdlink.api.minecraft.MessagePacket;
import fr.arthurbambou.fdlink.api.minecraft.VersionHelper;
import fr.arthurbambou.fdlink.compat_1_16.Message1_16;
import fr.arthurbambou.fdlink.compat_1_16.MessagePacket1_16;
import fr.arthurbambou.fdlink.compat_1_16.MinecraftServer1_16;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.BaseText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.List;
import java.util.UUID;

public class FDLink1_16 implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        if (VersionHelper.compareToMinecraftVersion("1.16-alpha.20.17.a").isMoreRecentOrEqual() && !VersionHelper.isVersion("1.16-20.w.14")) {
            FDLink.LOGGER.info("Initializing 1.16 Compat module");

            if (FabricLoader.getInstance().isModLoaded("fabric-console")
                && VersionHelper.compareToModVersion("fabric-console", "1.0.3").isOlderOrEqual()) {
                VersionHelper.throwModResolution("Fabric-Discord Link is incompatible with version 1.0.3 or older of Fabric-Console, please upgrade your version of Fabric-Console to at least 1.0.4");
            }
        }

        if (VersionHelper.compareToMinecraftVersion("1.16.1").isMoreRecentOrEqual() && !VersionHelper.isVersion("1.16-20.w.14")) {
            if (!FabricLoader.getInstance().isModLoaded("fabric")) {
                VersionHelper.throwModResolution("Could not find required mod: fdlink requires fabric");
            }

            ServerTickEvents.START_SERVER_TICK.register((server -> FDLink.getMessageReceiver().serverTick(new MinecraftServer1_16(server))));
        }

        if (VersionHelper.compareToMinecraftVersion("1.16-alpha.20.21.a").isMoreRecentOrEqual() && !VersionHelper.isVersion("1.16-20.w.14")) {
            VersionHelper.registerMessageSender((server, message, style) -> {
                Message literalText = new Message1_16(message);
                if (style != null) {
                    literalText = literalText.setStyle(style);
                }
                server.sendMessageToAll(new MessagePacket1_16(literalText, MessagePacket.MessageType.CHAT, UUID.randomUUID()));
            });
        }

        if (VersionHelper.compareToMinecraftVersion("1.14").isMoreRecentOrEqual()) {
            if ((VersionHelper.compareToMinecraftVersion("1.16.1").isMoreRecentOrEqual()
                    || VersionHelper.isVersion("1.15.2") || VersionHelper.isVersion("1.14.4"))
                    && !VersionHelper.isVersion("1.16-20.w.14")) {

                if (!FabricLoader.getInstance().isModLoaded("fabric")) {
                    VersionHelper.throwModResolution("Could not find required mod: fdlink requires fabric");
                }
                ServerLifecycleEvents.SERVER_STARTING.register(minecraftServer -> FDLink.getMessageSender().serverStarting());
                ServerLifecycleEvents.SERVER_STARTED.register((server -> FDLink.getMessageSender().serverStarted()));
                ServerLifecycleEvents.SERVER_STOPPING.register(minecraftServer -> FDLink.getMessageSender().serverStopping());
                ServerLifecycleEvents.SERVER_STOPPED.register((server -> FDLink.getMessageSender().serverStopped()));
            }
        }
    }

    public static void handleText(Text text, UUID uUID) {
        if (text instanceof BaseText) FDLink.getMessageSender().sendMessage(getMessageFromText((BaseText) text).setAuthorUUID(uUID));
        else FDLink.getMessageSender().sendMessage(new Message1_16(text.getString()).setAuthorUUID(uUID));
    }

    private static Message getMessageFromText(BaseText text) {
        List<Text> sibblings = text.getSiblings();
        Message message = null;
        if (text instanceof TranslatableText) {
            Object[] args = ((TranslatableText) text).getArgs();
            Object[] argsList = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof BaseText) {
                    argsList[i] = getMessageFromText((BaseText) arg);
                } else {
                    argsList[i] = arg;
                }
            }
            message = new Message1_16(((TranslatableText) text).getKey(), text.getString(), argsList);
        }
        else message = new Message1_16(text.getString());
        for (Text sib : sibblings) {
            if (sib instanceof BaseText) message.addSibbling(getMessageFromText((BaseText) sib));
            else message.addSibbling(new Message1_16(sib.getString()));
        }

        return message;
    }
}
