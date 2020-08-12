package fr.arthurbambou.fdlink.versionhelpers;

import net.fabricmc.loader.api.SemanticVersion;
import net.minecraft.text.Style;

public interface StyleApplier {

    boolean isCompatibleWithVersion(SemanticVersion semanticVersion);

    Style getStyleWithClickEventURL(String url);
}
