package fr.arthurbambou.fdlink.mixin_1_15_2;

import fr.arthurbambou.fdlink.api.minecraft.VersionHelper;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        boolean inRange = (VersionHelper.compareToMinecraftVersion("1.16-alpha.20.21.a").isOlder() &&
                VersionHelper.compareToMinecraftVersion("1.14").isMoreRecentOrEqual()) || VersionHelper.isVersion("1.16-20.w.14");
        boolean apiSupported = VersionHelper.isVersion("1.15.2") || VersionHelper.isVersion("1.14.4");
        if (mixinClassName.equals("fr.arthurbambou.fdlink.mixin_1_15_2.events.MixinMinecraftServer")) {
            return inRange && !apiSupported;
        }
        return inRange;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
