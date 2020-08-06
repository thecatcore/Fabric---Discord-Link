package fr.arthurbambou.fdlink.mixin;

import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TranslatableText.class)
public interface TranslatableTextAccessor {

    @Accessor("args")
    public Object[] getArgs();
}
