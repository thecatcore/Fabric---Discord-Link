package fr.arthurbambou.fdlink.compat_1_8_9;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MessagePacket;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.PlayerEntity;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.TextColor;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MinecraftServer1_8_9 implements MinecraftServer {

    private final net.minecraft.server.MinecraftServer minecraftServer;

    public MinecraftServer1_8_9(net.minecraft.server.MinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
    }

    @Override
    public String getMotd() {
        return this.minecraftServer.getServerMotd();
    }

    @Override
    public int getPlayerCount() {
        return this.minecraftServer.getPlayerManager().getPlayers().size();
    }

    @Override
    public int getMaxPlayerCount() {
        return this.minecraftServer.getPlayerManager().getMaxPlayerCount();
    }

    @Override
    public List<PlayerEntity> getPlayers() {
        List<PlayerEntity> list = new ArrayList<>();
        for (ServerPlayerEntity playerEntity : this.minecraftServer.getPlayerManager().getPlayers()) {
            list.add(new PlayerEntity1_8_9(playerEntity));
        }
        return list;
    }

    @Override
    public PlayerEntity getPlayerFromUsername(String username) {
        return new PlayerEntity1_8_9(this.minecraftServer.getPlayerManager().getPlayer(username));
    }

    @Override
    public String getUsernameFromUUID(UUID uuid) {
        return this.minecraftServer.getPlayerManager().getPlayer(uuid).getGameProfile().getName();
    }

    @Override
    public void sendMessageToAll(MessagePacket messagePacket) {
        Message message = messagePacket.getMessage();
        Text text = null;
        if (message.getType() == Message.MessageObjectType.STRING) {
            text = new LiteralText(message.getMessage());
        } else {
            if (message.getTextType() == Message.TextType.LITERAL) {
                text = new LiteralText(message.getMessage());
            } else if (message.getTextType() == Message.TextType.TRANSLATABLE) {
                text = new TranslatableText(message.getKey(), message.getArgs());
            }
        }
        Style vanillaStyle = new Style();
        fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style compatStyle = message.getStyle();
        vanillaStyle = vanillaStyle
                .setBold(compatStyle.isBold())
                .setColor(Formatting.byName(TextColor.toFormatting(compatStyle.getColor()).getName()))
                .setInsertion(compatStyle.getInsertion())
                .setItalic(compatStyle.isItalic())
                .setUnderline(compatStyle.isUnderlined())
                .setObfuscated(compatStyle.isObfuscated())
                .setStrikethrough(compatStyle.isStrikethrough());
        if (compatStyle.getClickEvent() != null) {
            vanillaStyle.setClickEvent(new ClickEvent(ClickEvent.Action.byName(compatStyle.getClickEvent().getAction().getName()),
                    compatStyle.getClickEvent().getValue()));
        }
        this.minecraftServer.getPlayerManager().sendToAll(text);
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
