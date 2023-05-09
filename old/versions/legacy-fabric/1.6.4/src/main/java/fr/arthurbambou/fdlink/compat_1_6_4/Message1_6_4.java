package fr.arthurbambou.fdlink.compat_1_6_4;

import fr.arthurbambou.fdlink.api.minecraft.Message;
import fr.arthurbambou.fdlink.api.minecraft.style.Style;

public class Message1_6_4 implements Message {

    private String message;
    private Style style = Style.EMPTY;
    private TextType type;
    private String key;
    private Object[] args;

    public Message1_6_4(String message) {
        this.message = message;
        this.type = TextType.LITERAL;
    }

    public Message1_6_4(String key, String message, Object... args) {
        this.key = key;
        this.message = message;
        this.args = args;
        this.type = TextType.TRANSLATABLE;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public Style getStyle() {
        return this.style;
    }

    @Override
    public Message1_6_4 setStyle(Style style) {
        this.style = style;
        return this;
    }

    @Override
    public MessageObjectType getType() {
        return MessageObjectType.TEXT;
    }

    @Override
    public TextType getTextType() {
        return this.type;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public Object[] getArgs() {
        return this.args;
    }
}
