package me.datsuns.simplecoordinate;

import me.datsuns.simplecoordinate.config.ColorConfig;
import me.datsuns.simplecoordinate.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

final class ConfigScreenProvider {
    private ConfigScreenProvider() {
    }

    static Screen create(Screen parent) {
        if (SimpleCoordinatesClient.ModConfig == null) {
            SimpleCoordinatesClient.ModConfig = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        }
        Minecraft mc = Minecraft.getInstance();
        int maxX = mc.getWindow().getGuiScaledWidth();
        int maxY = mc.getWindow().getGuiScaledHeight();
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("simplecoordinate.option_title"));

        ConfigCategory category = builder.getOrCreateCategory(Component.translatable("category.simplecoordinate"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        YesNoText yesNo = new YesNoText();

        category.addEntry(entryBuilder
                .startIntSlider(optionTitle("option.pos_x.title"), SimpleCoordinatesClient.ModConfig.RenderPosX, 0, maxX)
                .setDefaultValue(SimpleCoordinatesClient.ModConfig.RenderPosX)
                .setTextGetter(value -> Component.translatable("option.pos_xy.value", value))
                .setSaveConsumer(value -> SimpleCoordinatesClient.ModConfig.RenderPosX = value)
                .build());

        category.addEntry(entryBuilder
                .startIntSlider(optionTitle("option.pos_y.title"), SimpleCoordinatesClient.ModConfig.RenderPosY, 0, maxY)
                .setDefaultValue(SimpleCoordinatesClient.ModConfig.RenderPosY)
                .setTextGetter(value -> Component.translatable("option.pos_xy.value", value))
                .setSaveConsumer(value -> SimpleCoordinatesClient.ModConfig.RenderPosY = value)
                .build());

        category.addEntry(entryBuilder
                .startEnumSelector(optionTitle("option.text_color.title"), ColorConfig.class, SimpleCoordinatesClient.ModConfig.TextColor)
                .setEnumNameProvider(entry -> {
                    ColorConfig color = (ColorConfig) entry;
                    return Component.literal("")
                            .append(Component.translatable(color.translationKey())
                                    .setStyle(Style.EMPTY.withBold(true).withColor(color.argb)));
                })
                .setSaveConsumer(color -> SimpleCoordinatesClient.ModConfig.TextColor = color)
                .build());

        category.addEntry(entryBuilder
                .startBooleanToggle(optionTitle("option.visible.title"), SimpleCoordinatesClient.ModConfig.Visible)
                .setDefaultValue(SimpleCoordinatesClient.ModConfig.Visible)
                .setYesNoTextSupplier(state -> state ? yesNo.yes : yesNo.no)
                .setSaveConsumer(value -> SimpleCoordinatesClient.ModConfig.Visible = value)
                .build());

        category.addEntry(entryBuilder
                .startBooleanToggle(optionTitle("option.show_direction.title"), SimpleCoordinatesClient.ModConfig.ShowDirection)
                .setDefaultValue(SimpleCoordinatesClient.ModConfig.ShowDirection)
                .setYesNoTextSupplier(state -> state ? yesNo.yes : yesNo.no)
                .setSaveConsumer(value -> SimpleCoordinatesClient.ModConfig.ShowDirection = value)
                .build());

        category.addEntry(entryBuilder
                .startBooleanToggle(optionTitle("option.show_angle.title"), SimpleCoordinatesClient.ModConfig.ShowAngle)
                .setDefaultValue(SimpleCoordinatesClient.ModConfig.ShowAngle)
                .setYesNoTextSupplier(state -> state ? yesNo.yes : yesNo.no)
                .setSaveConsumer(value -> SimpleCoordinatesClient.ModConfig.ShowAngle = value)
                .build());

        builder.setSavingRunnable(() -> AutoConfig.getConfigHolder(ModConfig.class).save());
        return builder.build();
    }

    private static Component optionTitle(String key) {
        return Component.literal("")
                .append(Component.translatable(key).setStyle(Style.EMPTY.withColor(ChatFormatting.WHITE)));
    }

    private static class YesNoText {
        final Component yes;
        final Component no;

        YesNoText() {
            Style yesStyle = Style.EMPTY.withBold(true).withColor(ChatFormatting.GREEN);
            Style noStyle = Style.EMPTY.withBold(true).withColor(ChatFormatting.RED);
            this.yes = Component.literal("").append(Component.translatable("option.true").setStyle(yesStyle));
            this.no = Component.literal("").append(Component.translatable("option.false").setStyle(noStyle));
        }
    }
}
