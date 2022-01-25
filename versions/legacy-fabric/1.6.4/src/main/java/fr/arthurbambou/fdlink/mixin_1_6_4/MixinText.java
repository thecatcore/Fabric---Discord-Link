package fr.arthurbambou.fdlink.mixin_1_6_4;

import fr.arthurbambou.fdlink.api.minecraft.CompatText;
import net.minecraft.class_1687;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(class_1687.class)
public abstract class MixinText implements CompatText {

    @Shadow private String field_6701;

    @Shadow private List field_6702;

    @Shadow public abstract String toString();

    @Override
    public String getMessage() {
        return this.toString();
    }

    public String getTranslationKey() {
        return this.field_6701;
    }

    public List getArgs(){
        return this.field_6702;
    }
}
