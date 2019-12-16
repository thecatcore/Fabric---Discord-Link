package fr.arthurbambou.fblink.mixin;

import fr.arthurbambou.fblink.FBLink;
import net.minecraft.network.MessageType;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class MixinPlayerManager {

    @Inject(at = @At("RETURN"), method = "broadcastChatMessage")
    public void broadcastChatMessage(Text text_1, boolean boolean_1, CallbackInfo ci) {
        FBLink.getDiscordBot().sendMessage(text_1, boolean_1 ? MessageType.SYSTEM : MessageType.CHAT);
    }
}
