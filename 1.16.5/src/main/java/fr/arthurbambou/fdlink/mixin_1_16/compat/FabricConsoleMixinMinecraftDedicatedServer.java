package fr.arthurbambou.fdlink.mixin_1_16.compat;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.compat_1_16.Message1_16;
import net.logandark.fabricconsole.TextToAnsi;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.UserCache;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.net.Proxy;
import java.util.UUID;

@Mixin(value = MinecraftDedicatedServer.class, priority = 1100)
public abstract class FabricConsoleMixinMinecraftDedicatedServer extends MinecraftServer {
    @Shadow
    @Final
    private static Logger LOGGER;

    public FabricConsoleMixinMinecraftDedicatedServer(Thread thread, DynamicRegistryManager.Impl impl, LevelStorage.Session session, SaveProperties saveProperties, ResourcePackManager resourcePackManager, Proxy proxy, DataFixer dataFixer, ServerResourceManager serverResourceManager, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, UserCache userCache, WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory) {
        super(thread, impl, session, saveProperties, resourcePackManager, proxy, dataFixer, serverResourceManager, minecraftSessionService, gameProfileRepository, userCache, worldGenerationProgressListenerFactory);
    }

    @Override
    public void sendSystemMessage(Text message, UUID senderUuid) {
        if (message instanceof TranslatableText) FDLink.getMessageSender().sendMessage(new Message1_16(((TranslatableText) message).getKey(), message.getString(), ((TranslatableText) message).getArgs()));
        else FDLink.getMessageSender().sendMessage(new Message1_16(message.getString()));
        LOGGER.info(TextToAnsi.INSTANCE.textToAnsi(message));
    }
}
