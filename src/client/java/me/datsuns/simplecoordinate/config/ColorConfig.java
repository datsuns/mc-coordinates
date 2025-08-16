package me.datsuns.simplecoordinate.config;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;

import java.util.function.Function;

public enum ColorConfig {
    WHITE(Text.translatable("color_name.white"), Colors.WHITE),
    BLACK(Text.translatable("color_name.black"), Colors.BLACK),
    GRAY(Text.translatable("color_name.gray"), Colors.GRAY),
    DARK_GRAY(Text.translatable("color_name.dark_gray"), Colors.DARK_GRAY),
    LIGHT_GRAY(Text.translatable("color_name.light_gray"), Colors.LIGHT_GRAY),
    ALTERNATE_WHITE(Text.translatable("color_name.alternate_white"), Colors.ALTERNATE_WHITE),
    RED(Text.translatable("color_name.red"), Colors.RED),
    LIGHT_RED(Text.translatable("color_name.light_red"), Colors.LIGHT_RED),
    GREEN(Text.translatable("color_name.green"), Colors.GREEN),
    BLUE(Text.translatable("color_name.blue"), Colors.BLUE),
    YELLOW(Text.translatable("color_name.yellow"), Colors.YELLOW),
    LIGHT_YELLOW(Text.translatable("color_name.light_yellow"), Colors.LIGHT_YELLOW),
    PURPLE(Text.translatable("color_name.purple"), Colors.PURPLE),
    CYAN(Text.translatable("color_name.cyan"), Colors.CYAN),
    LIGHT_PINK(Text.translatable("color_name.light_pink"), Colors.LIGHT_PINK);

    public final MutableText label;
    public final int argb;

    ColorConfig(MutableText label, int argb) {
        this.label = label;
        this.argb = argb;
        Style s = Style.EMPTY.withBold(true).withColor(this.argb);
        this.label.setStyle(s);
    }
}
