package fr.arthurbambou.fdlink.mixin_1_16;

import com.mojang.brigadier.context.CommandContext;
import fr.arthurbambou.fdlink.FDLink;
import fr.arthurbambou.fdlink.compat_1_16.CommandMessage1_16;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TellRawCommand;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TellRawCommand.class)
public class MixinTellRawCommand {

    @Inject(method = "method_13777(Lcom/mojang/brigadier/context/CommandContext;)I", at = @At("RETURN"))
    private static void getTellRawOutput(CommandContext<ServerCommandSource> commandContext, CallbackInfoReturnable<?> ci) {
        if (commandContext.getInput().replace("/tellraw ", "").startsWith("@a")) {
            ServerCommandSource source = commandContext.getSource();
            Text message = TextArgumentType.getTextArgument(commandContext, "message").copy();
            Text author = source.getDisplayName();

            FDLink.getMessageSender().sendMessage(new CommandMessage1_16(author.getString(), message.getString(), "tellraw"));
        }
    }
}
