package fr.arthurbambou.fdlink.mixin_1_16;

import fr.arthurbambou.fdlink.api.minecraft.CompatText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({TranslatableText.class, LiteralText.class})
public class MixinText implements CompatText {

    @Override
    public String getMessage() {
        return ((Text)(Object)this).getString();
    }
}
