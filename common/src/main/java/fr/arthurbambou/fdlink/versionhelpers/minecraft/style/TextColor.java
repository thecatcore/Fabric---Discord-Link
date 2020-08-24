package fr.arthurbambou.fdlink.versionhelpers.minecraft.style;

import com.google.common.collect.ImmutableMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class TextColor {
    private static final Map<Formatting, TextColor> FORMATTING_TO_COLOR = Stream.of(Formatting.values()).filter(Formatting::isColor).collect(ImmutableMap.toImmutableMap(Function.identity(), (arg) -> new TextColor(arg.getColorValue(), arg.getName())));
    private static final Map<String, TextColor> BY_NAME = FORMATTING_TO_COLOR.values().stream().collect(ImmutableMap.toImmutableMap((arg) -> arg.name, Function.identity()));
    private final int rgb;
    @Nullable
    private final String name;

    private TextColor(int rgb, String name) {
        this.rgb = rgb;
        this.name = name;
    }

    private TextColor(int rgb) {
        this.rgb = rgb;
        this.name = null;
    }

    /**
     * Gets the RGB value of this color.
     *
     * <p>The red bits can be obtained by {@code (rgb >> 16) & 0xFF}, green bits
     * by {@code (rgb >> 8) & 0xFF}, blue bits by {@code rgb & 0xFF}.</p>
     */
    @Environment(EnvType.CLIENT)
    public int getRgb() {
        return this.rgb;
    }

    /**
     * Gets the name of this color, used for converting the color to JSON format.
     */
    public String getName() {
        return this.name != null ? this.name : this.getHexCode();
    }

    private String getHexCode() {
        return String.format("#%06X", this.rgb);
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object != null && this.getClass() == object.getClass()) {
            TextColor lv = (TextColor)object;
            return this.rgb == lv.rgb;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.rgb, this.name);
    }

    public String toString() {
        return this.name != null ? this.name : this.getHexCode();
    }

    /**
     * Obtains a text color from a formatting.
     *
     * @param formatting the formatting
     */
    @Nullable
    public static TextColor fromFormatting(Formatting formatting) {
        return FORMATTING_TO_COLOR.get(formatting);
    }

    public static Formatting toFormatting(TextColor textColor) {
        Formatting formatting = Formatting.STRIKETHROUGH;
        for (Map.Entry<Formatting, TextColor> entry : FORMATTING_TO_COLOR.entrySet()) {
            if (entry.getValue().equals(textColor)) {
                formatting = entry.getKey();
                break;
            }
        }
        return formatting;
    }

    /**
     * Obtains a text color from an RGB value.
     *
     * @param rgb the RGB color
     */
    public static TextColor fromRgb(int rgb) {
        return new TextColor(rgb);
    }

    /**
     * Parses a color by its name.
     *
     * @param name the name
     */
    @Nullable
    public static TextColor parse(String name) {
        if (name.startsWith("#")) {
            try {
                int i = Integer.parseInt(name.substring(1), 16);
                return fromRgb(i);
            } catch (NumberFormatException var2) {
                return null;
            }
        } else {
            return BY_NAME.get(name);
        }
    }
}
