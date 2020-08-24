package fr.arthurbambou.fdlink.mixin_1_16;

import fr.arthurbambou.fdlink.versionhelpers.CrossVersionHandler;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.util.version.VersionParsingException;
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
        try {
            if (mixinClassName.equals("fr.arthurbambou.fdlink.mixin_1_16.MixinMinecraftServer")) {
                return CrossVersionHandler.getMinecraftVersion().compareTo(SemanticVersion.parse("1.16-Snapshot.20.21.a")) >= 0;
            } else {
                return CrossVersionHandler.getMinecraftVersion().compareTo(SemanticVersion.parse("1.14")) >= 0;
            }
        } catch (VersionParsingException versionParsingException) {
            versionParsingException.printStackTrace();
        }
        return false;
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
