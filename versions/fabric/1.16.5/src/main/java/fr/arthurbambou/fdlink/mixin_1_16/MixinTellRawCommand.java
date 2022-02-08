package fr.arthurbambou.fdlink.mixin_1_16;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.compat_1_16.CommandMessage1_16;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TellRawCommand;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

@Mixin(TellRawCommand.class)
public class MixinTellRawCommand {

//    @Inject(method = "method_13777(Lcom/mojang/brigadier/context/CommandContext;)I", at = @At("RETURN"))
//    private static void getTellRawOutput(CommandContext<ServerCommandSource> commandContext, CallbackInfoReturnable<?> ci) {
//        if (commandContext.getInput().replace("/tellraw ", "").startsWith("@a")) {
//            ServerCommandSource source = commandContext.getSource();
//            Text message = TextArgumentType.getTextArgument(commandContext, "message").copy();
//            Text author = source.getDisplayName();
//
//            FDLink.getMessageSender().sendMessage(new CommandMessage1_16(author.getString(), message.getString(), "tellraw"));
//        }
//    }

    /**
     * @author Cat Core
     * @reason Injecting in lambda doesn't seems to work
     */
    @Overwrite
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder) CommandManager.literal("tellraw").requires((serverCommandSource) -> {
            return serverCommandSource.hasPermissionLevel(2);
        })).then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.argument("message", TextArgumentType.text()).executes((commandContext) -> {
            int i = 0;

            if (commandContext.getInput().replace("/tellraw ", "").startsWith("@a") || commandContext.getInput().replace("tellraw ", "").startsWith("@a")) {
                try {
                    ServerCommandSource source = commandContext.getSource();
                    Text message = TextArgumentType.getTextArgument(commandContext, "message");
                    Text author = source.getDisplayName();

                    Text text = Texts.parse(source, message, null, 0);

                    FDLink.getMessageSender().sendMessage(new CommandMessage1_16(author.getString(), text.getString(), "tellraw"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for(Iterator var2 = EntityArgumentType.getPlayers(commandContext, "targets").iterator(); var2.hasNext(); ++i) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)var2.next();
                serverPlayerEntity.sendSystemMessage(Texts.parse((ServerCommandSource)commandContext.getSource(), TextArgumentType.getTextArgument(commandContext, "message"), serverPlayerEntity, 0), Util.NIL_UUID);
            }

            return i;
        }))));
    }
}
