package fr.arthurbambou.fdlink.compat_1_16;

import fr.arthurbambou.fdlink.api.minecraft.Message;
import fr.arthurbambou.fdlink.api.minecraft.MessagePacket;
import fr.arthurbambou.fdlink.api.minecraft.MinecraftServer;
import fr.arthurbambou.fdlink.api.minecraft.PlayerEntity;
import net.minecraft.network.MessageType;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.ServerMetadata;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MinecraftServer1_16 implements MinecraftServer {

    private final net.minecraft.server.MinecraftServer minecraftServer;

    public MinecraftServer1_16(net.minecraft.server.MinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
    }

    @Override
    public String getMotd() {
        return this.minecraftServer.getServerMotd();
    }

    @Override
    public int getPlayerCount() {
        return this.minecraftServer.getPlayerManager().getPlayerList().size();
    }

    @Override
    public int getMaxPlayerCount() {
        return this.minecraftServer.getPlayerManager().getMaxPlayerCount();
    }

    @Override
    public List<PlayerEntity> getPlayers() {
        List<PlayerEntity> list = new ArrayList<>();
        for (ServerPlayerEntity playerEntity : this.minecraftServer.getPlayerManager().getPlayerList()) {
            list.add(new PlayerEntity1_16(playerEntity));
        }
        return list;
    }

    @Override
    public void sendMessageToAll(MessagePacket messagePacket) {
        Message message = messagePacket.getMessage();
        MutableText text = null;
        if (message.getType() == Message.MessageObjectType.STRING) {
            text = new LiteralText(message.getMessage());
        } else {
            if (message.getTextType() == Message.TextType.LITERAL) {
                text = new LiteralText(message.getMessage());
            } else if (message.getTextType() == Message.TextType.TRANSLATABLE) {
                text = new TranslatableText(message.getKey(), message.getArgs());
            }
        }
        Style vanillaStyle = Style.EMPTY;
        fr.arthurbambou.fdlink.api.minecraft.style.Style compatStyle = message.getStyle();
        vanillaStyle = vanillaStyle
                .withBold(compatStyle.isBold())
                .withInsertion(compatStyle.getInsertion())
                .withItalic(compatStyle.isItalic())
                /*.withFont(new Identifier(compatStyle.getFont()))*/;
        if (compatStyle.isObfuscated()) vanillaStyle = vanillaStyle.withFormatting(Formatting.OBFUSCATED);
        if (compatStyle.isStrikethrough()) vanillaStyle = vanillaStyle.withFormatting(Formatting.STRIKETHROUGH);
        if (compatStyle.isUnderlined()) vanillaStyle = vanillaStyle.withFormatting(Formatting.UNDERLINE);
        if (compatStyle.getClickEvent() != null) {
            vanillaStyle = vanillaStyle.withClickEvent(new ClickEvent(ClickEvent.Action.byName(compatStyle.getClickEvent().getAction().getName()),
                    compatStyle.getClickEvent().getValue()));
        }
        if (compatStyle.getColor() != null) {
            vanillaStyle = vanillaStyle.withColor(TextColor.fromRgb(compatStyle.getColor().getRgb()));
        }
        this.minecraftServer.getPlayerManager().sendToAll(new GameMessageS2CPacket(text, getMessageType(messagePacket.getMessageType()), messagePacket.getUUID()));
    }

    @Override
    public File getIcon() {
        return this.minecraftServer.getFile("server-icon.png");
    }

    @Override
    public PlayerEntity getPlayerFromUsername(String username) {
        return new PlayerEntity1_16(this.minecraftServer.getPlayerManager().getPlayer(username));
    }

    @Override
    public String getUsernameFromUUID(UUID uuid) {
        return this.minecraftServer.getPlayerManager().getPlayer(uuid).getName().getString();
    }

    private MessageType getMessageType(MessagePacket.MessageType messageType) {
        switch (messageType) {
            case INFO:
                return MessageType.GAME_INFO;
            case SYSTEM:
                return MessageType.SYSTEM;
            default:
                return MessageType.CHAT;
        }
    }

    @Override
    public String getIp() {
        return this.minecraftServer.getServerIp();
    }
}
