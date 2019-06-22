package fr.arthurbambou.fblink.mixin;

import fr.arthurbambou.fblink.FBLink;
import net.minecraft.client.network.packet.ChatMessageS2CPacket;
import net.minecraft.network.chat.ChatMessageType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatMessageS2CPacket.class)
public class MixinChatMessageS2CPacket {

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatMessageType;)V")
    public void MixinChatMessageS2CPacket(Component textComponent_1, ChatMessageType chatMessageType_1, CallbackInfo callbackInfo) {
        FBLink.getDiscordBot().sendMessage(textComponent_1.getString());
    }
}
