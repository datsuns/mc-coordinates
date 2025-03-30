package me.datsuns.simplecoordinate;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.datsuns.simplecoordinate.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.translatable("simplecoordinate.option_title"));

            ConfigCategory category = builder.getOrCreateCategory(Text.translatable("category.simplecoordinate"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            YesNoText yesno = new YesNoText();

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
