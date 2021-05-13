package fr.arthurbambou.fdlink;

import fr.arthurbambou.fdlink.api.minecraft.Message;
import fr.arthurbambou.fdlink.api.minecraft.VersionHelper;
import fr.arthurbambou.fdlink.compat_1_12_2.Message1_12_2;
import fr.arthurbambou.fdlink.compat_1_12_2.MessagePacket1_12_2;
import net.fabricmc.api.DedicatedServerModInitializer;

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
        }
    }
}
