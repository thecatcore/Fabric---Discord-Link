package fr.arthurbambou.fdlink.mixin_1_12_2;

import fr.arthurbambou.fdlink.api.minecraft.CompatText;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({TranslatableText.class, LiteralText.class})
public class MixinText implements CompatText {
    @Override
    public String getMessage() {
        return ((Text)(Object)this).asString();
    }
}
