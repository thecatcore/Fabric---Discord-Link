package fr.arthurbambou.fdlink.versionhelpers.minecraft;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style;

public interface Message {

    String getMessage();

    Style getStyle();

    <T extends Message> T setStyle(Style style);

    MessageObjectType getType();

    TextType getTextType();

    String getKey();

    Object[] getArgs();

    enum MessageObjectType {
        STRING,
        TEXT;
    }

    enum TextType {
        TRANSLATABLE,
        LITERAL,
        NULL;
    }
}
