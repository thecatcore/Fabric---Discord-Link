package fr.arthurbambou.fdlink.mixin_1_6_4;

import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.compat_1_6_4.Message1_6_4;
import fr.arthurbambou.fdlink.versionhelpers.CompatText;
import net.minecraft.class_2828;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    /**
     * This method handles message from the death of tamed entity, team chat, various commands and everything
     * broadcastChatMessage will processes
     * @param text
     * @param ci
     */
    @Inject(at = @At("HEAD"), method = "sendMessage")
    public void sendMessage(class_2828 text, CallbackInfo ci) {
        if (!((CompatText)text).getTranslationKey().isEmpty()) FDLink.getMessageSender().sendMessage(new Message1_6_4(((CompatText)text).getTranslationKey(), text.toString(), ((CompatText)text).getArgs()));
        else FDLink.getMessageSender().sendMessage(new Message1_6_4(text.toString()));
    }
}
