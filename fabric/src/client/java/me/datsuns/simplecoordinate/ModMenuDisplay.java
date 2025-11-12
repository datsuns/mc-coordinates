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
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

@Environment(EnvType.CLIENT)
public class ModMenuDisplay implements ModMenuApi {
    class YesNoText {
        public final Text yes;
        public final Text no;

        YesNoText() {
            Style yesStyle = Style.EMPTY.withBold(true).withColor(Formatting.GREEN);
            Style noStyle = Style.EMPTY.withBold(true).withColor(Formatting.RED);
            this.yes = Text.literal("").append(Text.translatable("option.true").setStyle(yesStyle));
            this.no = Text.literal("").append(Text.translatable("option.false").setStyle(noStyle));
        }
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            MinecraftClient mc = MinecraftClient.getInstance();
            int max_x = mc.getWindow().getScaledWidth();
            int max_y = mc.getWindow().getScaledHeight();
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("simplecoordinate.option_title"));

            ConfigCategory category = builder.getOrCreateCategory(Text.translatable("category.simplecoordinate"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            YesNoText yesno = new YesNoText();

            category.addEntry(entryBuilder
                    .startIntSlider(optionTitle("option.pos_x.title"), SimpleCoordinatesClient.ModConfig.RenderPosX, 0, max_x)
                    .setDefaultValue(SimpleCoordinatesClient.ModConfig.RenderPosX)
                    .setTextGetter(v -> Text.translatable("option.pos_xy.value", v))
                    .setSaveConsumer(integer -> SimpleCoordinatesClient.ModConfig.RenderPosX = integer)
                    .build());

            category.addEntry(entryBuilder
                    .startIntSlider(optionTitle("option.pos_y.title"), SimpleCoordinatesClient.ModConfig.RenderPosY, 0, max_y)
                    .setDefaultValue(SimpleCoordinatesClient.ModConfig.RenderPosY)
                    .setTextGetter(v -> Text.translatable("option.pos_xy.value", v))
                    .setSaveConsumer(integer -> SimpleCoordinatesClient.ModConfig.RenderPosY = integer)
                    .build());

            category.addEntry(entryBuilder
                    .startEnumSelector(optionTitle("option.text_color.title"), ColorConfig.class, SimpleCoordinatesClient.ModConfig.TextColor)
                    .setEnumNameProvider(entry -> {
                        ColorConfig color = (ColorConfig) entry;
                        return Text.literal("")
                                .append(Text.translatable(color.translationKey())
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


    Text optionTitle(String key) {
        Style s = Style.EMPTY.withColor(Formatting.WHITE);
        return Text.literal("").append(Text.translatable(key).setStyle(s));
    }
}
