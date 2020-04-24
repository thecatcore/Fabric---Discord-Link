package fr.arthurbambou.fblink.mixin;

import fr.arthurbambou.fblink.FBLink;
import fr.arthurbambou.fblink.discordstuff.DiscordBot;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    /**
     * This method handles message from the death of tamed entity, team chat, various commands and everything
     * broadcastChatMessage will processes
     * @param text_1
     * @param ci
     */
    @Inject(at = @At("RETURN"), method = "sendSystemMessage")
    public void sendMessage(Text text_1, CallbackInfo ci) {
        FBLink.getDiscordBot().sendMessage(text_1);
    }
}
