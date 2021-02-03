package fr.arthurbambou.fdlink.mixin_1_5_2;

import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.compat_1_5_2.Message1_5_2;
import net.minecraft.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LogManager.class)
public class MixinLogManager {

    /**
     * This method handles message from the death of tamed entity, team chat, various commands and everything
     * broadcastChatMessage will processes
     * @param text
     * @param ci
     */
    @Inject(at = @At("HEAD"), method = "info")
    public void sendMessage(String text, CallbackInfo ci) {
        FDLink.getMessageSender().sendMessage(new Message1_5_2(text));
    }
}
