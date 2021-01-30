package fr.arthurbambou.fdlink.mixin_1_6_4;

import fr.arthurbambou.fdlink.versionhelpers.CompatText;
import net.minecraft.class_2828;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(class_2828.class)
public abstract class MixinText implements CompatText {

    @Shadow private String field_12739;

    @Shadow private List field_12740;

    @Shadow public abstract String toString();

    @Override
    public String getMessage() {
        return this.toString();
    }

    public String getTranslationKey() {
        return this.field_12739;
    }

    public List getArgs(){
        return this.field_12740;
    }
}
