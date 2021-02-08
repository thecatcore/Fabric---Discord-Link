package fr.arthurbambou.fdlink.compat_1_5_2;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MessagePacket;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.PlayerEntity;
import net.minecraft.entity.ServerPlayer;
import net.minecraft.packet.class_971;
import net.minecraft.server.DedicatedMinecraftServer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MinecraftServer1_5_2 implements MinecraftServer {

    private final net.minecraft.server.DedicatedMinecraftServer minecraftServer;

    public MinecraftServer1_5_2(net.minecraft.server.MinecraftServer minecraftServer) {
        this.minecraftServer = (DedicatedMinecraftServer) minecraftServer;
    }

    @Override
    public String getMotd() {
        return this.minecraftServer.getMotd();
    }

    @Override
    public int getPlayerCount() {
        return this.minecraftServer.method_4158().maxPlayer();
    }

    @Override
    public int getMaxPlayerCount() {
        return this.minecraftServer.method_4158().field_3929.size();
    }

    @Override
    public List<PlayerEntity> getPlayers() {
        List<PlayerEntity> list = new ArrayList<>();
        for (Object playerEntity : this.minecraftServer.method_4158().field_3929) {
            list.add(new PlayerEntity1_5_2((ServerPlayer) playerEntity));
        }
        return list;
    }

    @Override
    public PlayerEntity getPlayerFromUsername(String username) {
        return new PlayerEntity1_5_2(this.minecraftServer.method_4158().method_3359(username));
    }

    @Override
    public String getUsernameFromUUID(UUID uuid) {
        String username = "";
        for (PlayerEntity playerEntity : this.getPlayers()) {
            if (playerEntity.getUUID() == uuid) {
                username = playerEntity.getPlayerName();
                break;
            }
        }
        return username;
    }

    @Override
    public void sendMessageToAll(MessagePacket messagePacket) {
        Message message = messagePacket.getMessage();
//        class_2828 text = null;
//        if (message.getType() == Message.MessageObjectType.STRING) {
//            text = class_2828.method_11702(message.getMessage());
//        } else {
//            if (message.getTextType() == Message.TextType.LITERAL) {
//                text = class_2828.method_11702(message.getMessage());
//            } else if (message.getTextType() == Message.TextType.TRANSLATABLE) {
//                text = class_2828.method_11696(message.getKey(), message.getArgs());
//            }
//        }
//        Style vanillaStyle = new Style();
//        fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style compatStyle = message.getStyle();
//        vanillaStyle = vanillaStyle
//                .setBold(compatStyle.isBold())
//                .setColor(Formatting.byName(TextColor.toFormatting(compatStyle.getColor()).getName()))
//                .setItalic(compatStyle.isItalic())
//                .setUnderline(compatStyle.isUnderlined())
//                .setObfuscated(compatStyle.isObfuscated())
//                .setStrikethrough(compatStyle.isStrikethrough());
//        if (compatStyle.getClickEvent() != null) {
//            vanillaStyle.setClickEvent(new ClickEvent(class_2432.method_9892(compatStyle.getClickEvent().getAction().getName()),
//                    compatStyle.getClickEvent().getValue()));
//        }
        if (message.getMessage() != null) this.minecraftServer.method_4158().method_3328(new class_971(message.getMessage()));
    }

    @Override
    public String getIp() {
        return this.minecraftServer.getIP();
    }

    @Override
    public File getIcon() {
        return this.minecraftServer.getFile("icon.png");
    }
}
