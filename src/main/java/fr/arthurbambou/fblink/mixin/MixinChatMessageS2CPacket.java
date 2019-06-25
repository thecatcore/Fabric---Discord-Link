package fr.arthurbambou.fblink.mixin;

import fr.arthurbambou.fblink.FBLink;
import net.minecraft.client.network.packet.ChatMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.MessageType;

@Mixin(ChatMessageS2CPacket.class)
public class MixinChatMessageS2CPacket {

    @Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/text/Text;Lnet/minecraft/network/MessageType;)V")
    public void MixinChatMessageS2CPacket(Text textComponent_1, MessageType chatMessageType_1, CallbackInfo callbackInfo) {
        FBLink.getDiscordBot().sendMessage(textComponent_1.getString());
    }
}
