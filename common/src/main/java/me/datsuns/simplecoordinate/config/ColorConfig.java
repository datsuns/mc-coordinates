package me.datsuns.simplecoordinate.config;

public enum ColorConfig {
    WHITE("color_name.white", 0xFFFFFFFF),
    BLACK("color_name.black", 0xFF000000),
    GRAY("color_name.gray", 0xFFAAAAAA),
    DARK_GRAY("color_name.dark_gray", 0xFF555555),
    LIGHT_GRAY("color_name.light_gray", 0xFFD3D3D3),
    ALTERNATE_WHITE("color_name.alternate_white", 0xFFF0F0F0),
    RED("color_name.red", 0xFFFF5555),
    LIGHT_RED("color_name.light_red", 0xFFFF9999),
    GREEN("color_name.green", 0xFF55FF55),
    BLUE("color_name.blue", 0xFF5555FF),
    YELLOW("color_name.yellow", 0xFFFFFF55),
    LIGHT_YELLOW("color_name.light_yellow", 0xFFFFFFAA),
    PURPLE("color_name.purple", 0xFFAA55FF),
    CYAN("color_name.cyan", 0xFF55FFFF),
    LIGHT_PINK("color_name.light_pink", 0xFFFFB6C1);

    private final String translationKey;
    public final int argb;

    ColorConfig(String translationKey, int argb) {
        this.translationKey = translationKey;
        this.argb = argb;
    }

    public String translationKey() {
        return translationKey;
    }
}
