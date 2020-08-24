package fr.arthurbambou.fdlink.compat_1_2_5;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style;

public class Message1_2_5 implements Message {

    private String message;

    public Message1_2_5(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Style getStyle() {
        return null;
    }

    @Override
    public Message1_2_5 setStyle(Style style) {

        return this;
    }

    @Override
    public MessageObjectType getType() {
        return MessageObjectType.STRING;
    }

    @Override
    public TextType getTextType() {
        return TextType.NULL;
    }

    @Override
    public String getKey() {
        return "";
    }

    @Override
    public Object[] getArgs() {
        return new Object[0];
    }
}
