package fr.arthurbambou.fdlink.mixin_1_12_2;

import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.compat_1_12_2.Message1_12_2;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
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
    public void sendMessage(Text text, CallbackInfo ci) {
        if (text instanceof TranslatableText) FDLink.getDiscordBot().sendMessage(new Message1_12_2(((TranslatableText) text).getKey(), text.asString(), ((TranslatableText) text).getArgs()));
        else FDLink.getDiscordBot().sendMessage(new Message1_12_2(text.asString()));
    }
}
