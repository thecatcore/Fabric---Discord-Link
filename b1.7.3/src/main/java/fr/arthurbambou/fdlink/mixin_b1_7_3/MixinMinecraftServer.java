package fr.arthurbambou.fdlink.mixin_b1_7_3;

import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.compat_b1_7_3.Messageb1_7_3;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(method = "sendFeedback", at = @At("RETURN"))
    private void getMessageFromLogs(String text, CallbackInfo ci) {
        FDLink.getDiscordBot().sendMessage(new Messageb1_7_3(text));
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;start()Z"))
    private void serverStarting(CallbackInfo ci) {
        FDLink.getDiscordBot().serverStarting();
    }

    @Inject(method = "run", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/MinecraftServer;start()Z"))
    private void serverStarted(CallbackInfo ci) {
        FDLink.getDiscordBot().serverStarted();
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"stopServer"}
    )
    private void beforeShutdownServer(CallbackInfo info) {
        FDLink.getDiscordBot().serverStopping();
    }

    @Inject(
            at = {@At("TAIL")},
            method = {"stopServer"}
    )
    private void afterShutdownServer(CallbackInfo info) {
        FDLink.getDiscordBot().serverStopped();
    }
}
