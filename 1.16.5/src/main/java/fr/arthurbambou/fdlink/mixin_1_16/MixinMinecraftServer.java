package fr.arthurbambou.fdlink.mixin_1_16;

import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.compat_1_16.Message1_16;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.BaseText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    /**
     * This method handles message from the death of tamed entity, team chat, various commands and everything
     * broadcastChatMessage will processes
     * @param text
     * @param uUID
     * @param ci
     */
    @Inject(at = @At("HEAD"), method = "sendSystemMessage")
    public void sendMessage(Text text, UUID uUID, CallbackInfo ci) {
        if (text instanceof BaseText) FDLink.getMessageSender().sendMessage(getMessageFromText((BaseText) text).setAuthorUUID(uUID));
        else FDLink.getMessageSender().sendMessage(new Message1_16(text.getString()).setAuthorUUID(uUID));
    }

    private static Message getMessageFromText(BaseText text) {
        List<Text> sibblings = text.getSiblings();
        System.out.println(text);
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
            System.out.println(args.toString());
            System.out.println(argsList.toString());
            System.out.println("-----");
            message = new Message1_16(((TranslatableText) text).getKey(), text.getString(), argsList);
        }
        else message = new Message1_16(text.getString());
        for (Text sib : sibblings) {
            if (sib instanceof BaseText) message.addSibbling(getMessageFromText((BaseText) sib));
            else message.addSibbling(new Message1_16(sib.getString()));
        }

        return message;
    }
}
