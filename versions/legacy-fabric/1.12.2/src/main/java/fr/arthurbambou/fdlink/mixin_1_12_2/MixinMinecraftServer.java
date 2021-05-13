package fr.arthurbambou.fdlink.mixin_1_12_2;

import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.api.minecraft.Message;
import fr.arthurbambou.fdlink.compat_1_12_2.Message1_12_2;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.BaseText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    /**
     * This method handles message from the death of tamed entity, team chat, various commands and everything
     * broadcastChatMessage will processes
     * @param text
     * @param ci
     */
    @Inject(at = @At("HEAD"), method = "sendSystemMessage")
    public void sendMessage(Text text, CallbackInfo ci) {
        if (text instanceof BaseText) FDLink.getMessageSender().sendMessage(getMessageFromText((BaseText) text));
        else FDLink.getMessageSender().sendMessage(new Message1_12_2(text.asString()));
    }

    private static Message getMessageFromText(BaseText text) {
        List<Text> sibblings = text.getSiblings();
        Message message = null;
        if (text instanceof TranslatableText) {
            Object[] args = ((TranslatableText) text).getArgs();
            Object[] argsList = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof BaseText) {
                    argsList[i] = getMessageFromText((BaseText) arg);
                } else {
                    argsList[i] = arg;
                }
            }
            message = new Message1_12_2(((TranslatableText) text).getKey(), text.asString(), argsList);
        }
        else message = new Message1_12_2(text.asString());
        for (Text sib : sibblings) {
            if (sib instanceof BaseText) message.addSibbling(getMessageFromText((BaseText) sib));
            else message.addSibbling(new Message1_12_2(sib.asString()));
        }

        return message;
    }
}
