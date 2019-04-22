package fr.arthurbambou.fblink.mixin;

import fr.arthurbambou.fblink.FBLink;
import net.minecraft.client.network.packet.ChatMessageS2CPacket;
import net.minecraft.text.ChatMessageType;
import net.minecraft.text.TextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatMessageS2CPacket.class)
public class MixinChatMessageS2CPacket {

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/text/TextComponent;Lnet/minecraft/text/ChatMessageType;)V")
    public void MixinChatMessageS2CPacket(TextComponent textComponent_1, ChatMessageType chatMessageType_1, CallbackInfo callbackInfo) {
        FBLink.getDiscordBot().sendMessage(textComponent_1.getString());
    }
}
