package fr.arthurbambou.fdlink.compat_1_6_4;

import fr.arthurbambou.fdlink.api.minecraft.Message;
import fr.arthurbambou.fdlink.api.minecraft.MessagePacket;
import fr.arthurbambou.fdlink.api.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.api.minecraft.PlayerEntity;
import net.minecraft.class_1687;
import net.minecraft.server.network.ServerPlayerEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MinecraftServer1_6_4 implements MinecraftServer {

    private final net.minecraft.server.MinecraftServer minecraftServer;

    public MinecraftServer1_6_4(net.minecraft.server.MinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
    }

    @Override
    public String getMotd() {
        return this.minecraftServer.getServerMotd();
    }

    @Override
    public int getPlayerCount() {
        return this.minecraftServer.getCurrentPlayerCount();
    }

    @Override
    public int getMaxPlayerCount() {
        return this.minecraftServer.getMaxPlayerCount();
    }

    @Override
    public List<PlayerEntity> getPlayers() {
        List<PlayerEntity> list = new ArrayList<>();
        for (Object playerEntity : this.minecraftServer.getPlayerManager().players) {
            list.add(new PlayerEntity1_6_4((ServerPlayerEntity) playerEntity));
        }
        return list;
    }

    @Override
    public PlayerEntity getPlayerFromUsername(String username) {
        return new PlayerEntity1_6_4(this.minecraftServer.getPlayerManager().getPlayer(username));
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
        class_1687 text = null;
        if (message.getType() == Message.MessageObjectType.STRING) {
            text = class_1687.method_6026(message.getMessage());
        } else {
            if (message.getTextType() == Message.TextType.LITERAL) {
                text = class_1687.method_6026(message.getMessage());
            } else if (message.getTextType() == Message.TextType.TRANSLATABLE) {
                text = class_1687.method_6020(message.getKey(), message.getArgs());
            }
        }
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
        if (text != null) this.minecraftServer.getPlayerManager().method_6061(text);
    }

    @Override
    public String getIp() {
        return this.minecraftServer.getServerIp();
    }

    @Override
    public File getIcon() {
        return this.minecraftServer.getFile("icon.png");
    }
}
