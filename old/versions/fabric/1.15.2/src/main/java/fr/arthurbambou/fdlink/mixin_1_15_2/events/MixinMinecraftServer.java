package fr.arthurbambou.fdlink.mixin_1_15_2.events;

import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.compat_1_15_2.MinecraftServer1_15_2;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin({MinecraftServer.class})
public class MixinMinecraftServer {

    @Inject(
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;setupServer()Z"
            )},
            method = {"run"}
    )
    private void beforeSetupServer(CallbackInfo info) {
        FDLink.getMessageSender().serverStarting();
    }

    @Inject(
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;setFavicon(Lnet/minecraft/server/ServerMetadata;)V",
                    ordinal = 0
            )},
            method = {"run"}
    )
    private void afterSetupServer(CallbackInfo info) {
        FDLink.getMessageSender().serverStarted();
    }

    @Inject(
            at = {@At("HEAD")},
            method = {"shutdown"}
    )
    private void beforeShutdownServer(CallbackInfo info) {
        FDLink.getMessageSender().serverStopping();
    }

    @Inject(
            at = {@At("TAIL")},
            method = {"shutdown"}
    )
    private void afterShutdownServer(CallbackInfo info) {
        FDLink.getMessageSender().serverStopped();
    }

    @Inject(
            at = {@At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/MinecraftServer;tickWorlds(Ljava/util/function/BooleanSupplier;)V"
            )},
            method = {"tick"}
    )
    private void onStartTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        FDLink.getMessageReceiver().serverTick(new MinecraftServer1_15_2((MinecraftServer)(Object) this));
    }
}
