package fr.arthurbambou.fdlink.mixin_1_5_2.events;

import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.compat_1_5_2.MinecraftServer1_5_2;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MinecraftServer.class})
public class MixinMinecraftServer {

    @Inject(
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;method_4172()Z"
            )},
            method = {"run"}
    )
    private void beforeSetupServer(CallbackInfo info) {
        FDLink.getMessageSender().serverStarting();
    }

    @Inject(
            at = {@At(
                    value = "INVOKE",
                    target = "Ljava/lang/System;currentTimeMillis()J",
                    ordinal = 0,
                    remap = false
            )},
            method = {"run"}
    )
    private void afterSetupServer(CallbackInfo info) {
        FDLink.getMessageSender().serverStarted();
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"method_4192"}
    )
    private void beforeShutdownServer(CallbackInfo info) {
        FDLink.getMessageSender().serverStopping();
    }

    @Inject(
            at = {@At("RETURN")},
            method = {"method_4192"}
    )
    private void afterShutdownServer(CallbackInfo info) {
        FDLink.getMessageSender().serverStopped();
    }

    @Inject(
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;method_4203()V"
            )},
            method = {"run"}
    )
    private void onStartTick(CallbackInfo ci) {
        FDLink.getMessageReceiver().serverTick(new MinecraftServer1_5_2((MinecraftServer)(Object) this));
    }
}
