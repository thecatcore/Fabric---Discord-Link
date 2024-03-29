package fr.arthurbambou.fdlink.mixin_1_16;

import fr.arthurbambou.fdlink.api.minecraft.VersionHelper;
import net.fabricmc.loader.api.FabricLoader;
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
        if (mixinClassName.equals("fr.arthurbambou.fdlink.mixin_1_16.MixinMinecraftServer")) {
            return VersionHelper.compareToMinecraftVersion("1.16-alpha.20.21.a").isMoreRecentOrEqual() && !VersionHelper.isVersion("1.16-20.w.14");
        } else if (mixinClassName.equals("fr.arthurbambou.fdlink.mixin_1_16.events.MixinMinecraftServer")) {
            return VersionHelper.compareToMinecraftVersion("1.16.1").isOlder() &&
                    VersionHelper.compareToMinecraftVersion("1.16-alpha.20.17.a").isMoreRecentOrEqual() && !VersionHelper.isVersion("1.16-20.w.14");
        } else {
            return VersionHelper.compareToMinecraftVersion("1.14").isMoreRecentOrEqual();
        }
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
