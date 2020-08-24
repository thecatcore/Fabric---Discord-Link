package fr.arthurbambou.fdlink.mixin_1_2_5;

import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.compat_1_2_5.Message1_2_5;
import fr.arthurbambou.fdlink.compat_1_2_5.MinecraftServer1_2_5;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(method = "sendMessage", at = @At("RETURN"))
    private void getMessageFromLogs(String text, CallbackInfo ci) {
        System.out.println(text);
        Message message = new Message1_2_5(text);
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;method_3356()Z"))
    private void serverStarting(CallbackInfo ci) {
        FDLink.getDiscordBot().serverStarting();
    }

    @Inject(method = "run", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/MinecraftServer;method_3356()Z"))
    private void serverStarted(CallbackInfo ci) {
        FDLink.getDiscordBot().serverStarted();
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"method_3359"}
    )
    private void beforeShutdownServer(CallbackInfo info) {
        FDLink.getDiscordBot().serverStopping();
    }

    @Inject(
            at = {@At("TAIL")},
            method = {"method_3359"}
    )
    private void afterShutdownServer(CallbackInfo info) {
        FDLink.getDiscordBot().serverStopped();
    }

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Ljava/lang/System;currentTimeMillis()J", ordinal = 1, remap = false), remap = false)
    private void serverTick(CallbackInfo ci) {
        FDLink.getDiscordBot().serverTick(new MinecraftServer1_2_5((MinecraftServer) (Object)this));
    }
}
