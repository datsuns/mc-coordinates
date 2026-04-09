package me.datsuns.simplecoordinate;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.datsuns.simplecoordinate.config.ColorConfig;
import me.datsuns.simplecoordinate.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

@Environment(EnvType.CLIENT)
public class ModMenuDisplay implements ModMenuApi {
    class YesNoText {
        public final Component yes;
        public final Component no;

        YesNoText() {
            Style yesStyle = Style.EMPTY.withBold(true).withColor(ChatFormatting.GREEN);
            Style noStyle = Style.EMPTY.withBold(true).withColor(ChatFormatting.RED);
            this.yes = Component.literal("").append(Component.translatable("option.true").setStyle(yesStyle));
            this.no = Component.literal("").append(Component.translatable("option.false").setStyle(noStyle));
        }
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            Minecraft mc = Minecraft.getInstance();
            int max_x = mc.getWindow().getGuiScaledWidth();
            int max_y = mc.getWindow().getGuiScaledHeight();
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.translatable("simplecoordinate.option_title"));

            ConfigCategory category = builder.getOrCreateCategory(Component.translatable("category.simplecoordinate"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            YesNoText yesno = new YesNoText();

            category.addEntry(entryBuilder
                    .startIntSlider(optionTitle("option.pos_x.title"), SimpleCoordinatesClient.ModConfig.RenderPosX, 0, max_x)
                    .setDefaultValue(SimpleCoordinatesClient.ModConfig.RenderPosX)
                    .setTextGetter(v -> Component.translatable("option.pos_xy.value", v))
                    .setSaveConsumer(integer -> SimpleCoordinatesClient.ModConfig.RenderPosX = integer)
                    .build());

            category.addEntry(entryBuilder
                    .startIntSlider(optionTitle("option.pos_y.title"), SimpleCoordinatesClient.ModConfig.RenderPosY, 0, max_y)
                    .setDefaultValue(SimpleCoordinatesClient.ModConfig.RenderPosY)
                    .setTextGetter(v -> Component.translatable("option.pos_xy.value", v))
                    .setSaveConsumer(integer -> SimpleCoordinatesClient.ModConfig.RenderPosY = integer)
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
                    .setYesNoTextSupplier(state -> state ? yesno.yes : yesno.no)
                    .setSaveConsumer(newValue -> SimpleCoordinatesClient.ModConfig.Visible = newValue)
                    .build());

            category.addEntry(entryBuilder
                    .startBooleanToggle(optionTitle("option.show_direction.title"), SimpleCoordinatesClient.ModConfig.ShowDirection)
                    .setDefaultValue(SimpleCoordinatesClient.ModConfig.ShowDirection)
                    .setYesNoTextSupplier(state -> state ? yesno.yes : yesno.no)
                    .setSaveConsumer(newValue -> SimpleCoordinatesClient.ModConfig.ShowDirection = newValue)
                    .build());

            category.addEntry(entryBuilder
                    .startBooleanToggle(optionTitle("option.show_angle.title"), SimpleCoordinatesClient.ModConfig.ShowAngle)
                    .setDefaultValue(SimpleCoordinatesClient.ModConfig.ShowAngle)
                    .setYesNoTextSupplier(state -> state ? yesno.yes : yesno.no)
                    .setSaveConsumer(newValue -> SimpleCoordinatesClient.ModConfig.ShowAngle = newValue)
                    .build());

            builder.setSavingRunnable(() -> {
                AutoConfig.getConfigHolder(ModConfig.class).save();
            });

            return builder.build();
        };
    }


    Component optionTitle(String key) {
        Style s = Style.EMPTY.withColor(ChatFormatting.WHITE);
        return Component.literal("").append(Component.translatable(key).setStyle(s));
    }
}
