package fr.arthurbambou.fdlink.versionhelpers.minecraft.style;

import javax.annotation.Nullable;
import java.util.Objects;

public class Style {
    public static final Style EMPTY = new Style((TextColor)null, (Boolean)null, (Boolean)null, (Boolean)null, (Boolean)null, (Boolean)null, (ClickEvent)null, (String)null, (String) null);
    public static final String DEFAULT_FONT_ID = "minecraft:default";

    @Nullable
    private final TextColor color;
    @Nullable
    private final Boolean bold;
    @Nullable
    private final Boolean italic;
    @Nullable
    private final Boolean underlined;
    @Nullable
    private final Boolean strikethrough;
    @Nullable
    private final Boolean obfuscated;
    @Nullable
    private final ClickEvent clickEvent;
    @Nullable
    private final String insertion;
    @Nullable
    private final String font;

    private Style(@Nullable TextColor color, @Nullable Boolean bold, @Nullable Boolean italic, @Nullable Boolean underlined, @Nullable Boolean strikethrough, @Nullable Boolean obfuscated, @Nullable ClickEvent clickEvent, @Nullable String insertion, @Nullable String font) {
        this.color = color;
        this.bold = bold;
        this.italic = italic;
        this.underlined = underlined;
        this.strikethrough = strikethrough;
        this.obfuscated = obfuscated;
        this.clickEvent = clickEvent;
        this.insertion = insertion;
        this.font = font;
    }

    /**
     * Returns the color of this style.
     */
    @Nullable
    public TextColor getColor() {
        return this.color;
    }

    /**
     * Returns whether the style has bold formatting.
     *
     * @see Formatting#BOLD
     */
    public boolean isBold() {
        return this.bold == Boolean.TRUE;
    }

    /**
     * Returns whether the style has italic formatting.
     *
     * @see Formatting#ITALIC
     */
    public boolean isItalic() {
        return this.italic == Boolean.TRUE;
    }

    /**
     * Returns whether the style has strikethrough formatting.
     *
     * @see Formatting#STRIKETHROUGH
     */
    public boolean isStrikethrough() {
        return this.strikethrough == Boolean.TRUE;
    }

    /**
     * Returns whether the style has underline formatting.
     *
     * @see Formatting#UNDERLINE
     */
    public boolean isUnderlined() {
        return this.underlined == Boolean.TRUE;
    }

    /**
     * Returns whether the style has obfuscated formatting.
     *
     * @see Formatting#OBFUSCATED
     */
    public boolean isObfuscated() {
        return this.obfuscated == Boolean.TRUE;
    }

    /**
     * Returns if this is the empty style.
     *
     * @see #EMPTY
     */
    public boolean isEmpty() {
        return this == EMPTY;
    }

    /**
     * Returns the click event of this style.
     */
    @Nullable
    public ClickEvent getClickEvent() {
        return this.clickEvent;
    }

    /**
     * Returns the insertion text of the style.
     *
     * <p>An insertion is inserted when a piece of text clicked while shift key
     * is down in the chat HUD.</p>
     */
    @Nullable
    public String getInsertion() {
        return this.insertion;
    }

    /**
     * Returns the font of this style.
     */
    public String getFont() {
        return this.font != null ? this.font : DEFAULT_FONT_ID;
    }

    /**
     * Returns a new style with the color provided and all other attributes of
     * this style.
     *
     * @param color the new color
     */
    public Style withColor(@Nullable TextColor color) {
        return new Style(color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.insertion, this.font);
    }

    /**
     * Returns a new style with the color provided and all other attributes of
     * this style.
     *
     * @param color the new color
     */
    public Style withColor(@Nullable Formatting color) {
        return this.withColor(color != null ? TextColor.fromFormatting(color) : null);
    }

    /**
     * Returns a new style with the bold attribute provided and all other
     * attributes of this style.
     *
     * @param bold the new bold property
     */
    public Style withBold(@Nullable Boolean bold) {
        return new Style(this.color, bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.insertion, this.font);
    }

    /**
     * Returns a new style with the italic attribute provided and all other
     * attributes of this style.
     *
     * @param italic the new italic property
     */
    public Style withItalic(@Nullable Boolean italic) {
        return new Style(this.color, this.bold, italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.insertion, this.font);
    }

    /**
     * Returns a new style with the click event provided and all other
     * attributes of this style.
     *
     * @param clickEvent the new click event
     */
    public Style withClickEvent(@Nullable ClickEvent clickEvent) {
        return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, clickEvent, this.insertion, this.font);
    }

    /**
     * Returns a new style with the insertion provided and all other
     * attributes of this style.
     *
     * @param insertion the new insertion string
     */
    public Style withInsertion(@Nullable String insertion) {
        return new Style(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, insertion, this.font);
    }

    /**
     * Returns a new style with the formatting provided and all other
     * attributes of this style.
     *
     * @param formatting the new formatting
     */
    public Style withFormatting(Formatting formatting) {
        TextColor lv = this.color;
        Boolean lv1 = this.bold;
        Boolean lv2 = this.italic;
        Boolean lv3 = this.strikethrough;
        Boolean lv4 = this.underlined;
        Boolean lv5 = this.obfuscated;
        switch(formatting) {
            case OBFUSCATED:
                lv5 = true;
                break;
            case BOLD:
                lv1 = true;
                break;
            case STRIKETHROUGH:
                lv3 = true;
                break;
            case UNDERLINE:
                lv4 = true;
                break;
            case ITALIC:
                lv2 = true;
                break;
            case RESET:
                return EMPTY;
            default:
                lv = TextColor.fromFormatting(formatting);
        }

        return new Style(lv, lv1, lv2, lv4, lv3, lv5, this.clickEvent, this.insertion, this.font);
    }

    /**
     * Returns a new style with the formattings provided and all other
     * attributes of this style.
     *
     * @param formattings an array of new formattings
     */
    public Style withFormatting(Formatting... formattings) {
        TextColor lv = this.color;
        Boolean lv1 = this.bold;
        Boolean lv2 = this.italic;
        Boolean lv3 = this.strikethrough;
        Boolean lv4 = this.underlined;
        Boolean lv5 = this.obfuscated;

        for(Formatting lv6 : formattings) {
            switch(lv6) {
                case OBFUSCATED:
                    lv5 = true;
                    break;
                case BOLD:
                    lv1 = true;
                    break;
                case STRIKETHROUGH:
                    lv3 = true;
                    break;
                case UNDERLINE:
                    lv4 = true;
                    break;
                case ITALIC:
                    lv2 = true;
                    break;
                case RESET:
                    return EMPTY;
                default:
                    lv = TextColor.fromFormatting(lv6);
            }
        }

        return new Style(lv, lv1, lv2, lv4, lv3, lv5, this.clickEvent, this.insertion, this.font);
    }

    /**
     * Returns a new style with the undefined attributes of this style filled
     * by the {@code parent} style.
     *
     * @param parent the parent style
     */
    public Style withParent(Style parent) {
        if (this == EMPTY) {
            return parent;
        } else {
            return parent == EMPTY ? this : new Style(this.color != null ? this.color : parent.color, this.bold != null ? this.bold : parent.bold, this.italic != null ? this.italic : parent.italic, this.underlined != null ? this.underlined : parent.underlined, this.strikethrough != null ? this.strikethrough : parent.strikethrough, this.obfuscated != null ? this.obfuscated : parent.obfuscated, this.clickEvent != null ? this.clickEvent : parent.clickEvent, this.insertion != null ? this.insertion : parent.insertion, this.font != null ? this.font : parent.font);
        }
    }

    public String toString() {
        return "Style{ color=" + this.color + ", bold=" + this.bold + ", italic=" + this.italic + ", underlined=" + this.underlined + ", strikethrough=" + this.strikethrough + ", obfuscated=" + this.obfuscated + ", clickEvent=" + this.getClickEvent() + ", insertion=" + this.getInsertion() + ", font=" + this.getFont() + '}';
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (!(obj instanceof Style)) {
            return false;
        } else {
            Style lv = (Style)obj;
            return this.isBold() == lv.isBold() && Objects.equals(this.getColor(), lv.getColor()) && this.isItalic() == lv.isItalic() && this.isObfuscated() == lv.isObfuscated() && this.isStrikethrough() == lv.isStrikethrough() && this.isUnderlined() == lv.isUnderlined() && Objects.equals(this.getClickEvent(), lv.getClickEvent()) && Objects.equals(this.getInsertion(), lv.getInsertion()) && Objects.equals(this.getFont(), lv.getFont());
        }
    }

    public int hashCode() {
        return Objects.hash(this.color, this.bold, this.italic, this.underlined, this.strikethrough, this.obfuscated, this.clickEvent, this.insertion);
    }
}
