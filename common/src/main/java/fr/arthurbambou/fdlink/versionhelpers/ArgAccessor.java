package fr.arthurbambou.fdlink.versionhelpers;

import net.fabricmc.loader.api.SemanticVersion;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public interface ArgAccessor {

    boolean isCompatibleWithVersion(SemanticVersion semanticVersion);

    Object[] getArgs(Text translatableText);
}
