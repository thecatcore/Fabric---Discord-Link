package fr.arthurbambou.fdlink.compat_b1_7_3;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style;

public class Messageb1_7_3 implements Message {

    private String message;

    public Messageb1_7_3(String message) {
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
    public Messageb1_7_3 setStyle(Style style) {

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
