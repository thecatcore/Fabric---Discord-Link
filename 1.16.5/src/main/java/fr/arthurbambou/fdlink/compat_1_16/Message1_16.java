package fr.arthurbambou.fdlink.compat_1_16;

import fr.arthurbambou.fdlink.versionhelpers.minecraft.Message;
import fr.arthurbambou.fdlink.versionhelpers.minecraft.style.Style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Message1_16 implements Message {

    private String message;
    private Style style = Style.EMPTY;
    private TextType type;
    private String key;
    private Object[] args;
    private List<Message> sibblings = new ArrayList<>();
    private UUID authorUUID;

    public Message1_16(String message) {
        this.message = message;
        this.type = TextType.LITERAL;
    }

    public Message1_16(String key, String message, Object... args) {
        this.key = key;
        this.message = message;
        this.args = args;
        this.type = TextType.TRANSLATABLE;
    }

    @Override
    public Message1_16 setAuthorUUID(UUID uuid) {
        this.authorUUID = uuid;
        return this;
    }

    @Override
    public boolean hasAuthorUUID() {
        return true;
    }

    @Override
    public UUID getAuthorUUID() {
        return this.authorUUID;
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
    public Message1_16 setStyle(Style style) {
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

    @Override
    public List<Message> getSibblings() {
        return this.sibblings;
    }

    @Override
    public Message1_16 addSibbling(Message message) {
        this.sibblings.add(message);
        return this;
    }
}
