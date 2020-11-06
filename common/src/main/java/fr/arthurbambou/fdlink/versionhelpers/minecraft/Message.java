package fr.arthurbambou.fdlink.versionhelpers.minecraft;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style;

public interface Message {

    String getMessage();

    default Style getStyle() {
        return null;
    }

    default <T extends Message> T setStyle(Style style) {
        return (T) this;
    }

    MessageObjectType getType();

    TextType getTextType();

    default String getKey() {
        return "";
    }

    default Object[] getArgs() {
        return new Object[0];
    }

    default String getSource() {
        return "";
    }

    default String getCommandName() {
        return "";
    }

    enum MessageObjectType {
        STRING,
        TEXT;
    }

    enum TextType {
        TRANSLATABLE,
        LITERAL,
        COMMAND,
        UNKNOWN;
    }
}
