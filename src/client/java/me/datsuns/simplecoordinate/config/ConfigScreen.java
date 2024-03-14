package me.datsuns.simplecoordinate.config;

import me.datsuns.simplecoordinate.SimpleCoordinatesClient;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ConfigScreen extends GameOptionsScreen {
    private final Screen previous;

    public final String TRUE;
    public final String FALSE;

    public static ConfigBuilder Build() {
        return ConfigBuilder.create();
    }

    //@SuppressWarnings("resource")
    public ConfigScreen(Screen previous) {
        super(previous, MinecraftClient.getInstance().options, Text.translatable("simplecoordinate.option_title"));
        this.previous = previous;
        this.TRUE  = "§a" + Text.translatable("option.true").getString();
        this.FALSE = "§c" + Text.translatable("option.false").getString();

    }

    protected Text buildButtonTitle(String key, boolean value) {
        String t = Text.translatable(key).getString() + " : " + (value ? this.TRUE : this.FALSE);
        return Text.of(t);
    }

    protected void init() {
        super.init();
        this.addDrawableChild(new ButtonWidget.Builder(buildButtonTitle("option.visible.title", SimpleCoordinatesClient.ModConfig.Visible), (button) -> {
            SimpleCoordinatesClient.ModConfig.Visible = !SimpleCoordinatesClient.ModConfig.Visible;
            button.setMessage(buildButtonTitle("option.visible.title", SimpleCoordinatesClient.ModConfig.Visible));
            AutoConfig.getConfigHolder(ModConfig.class).save();
        }).position(this.width / 2 - 100, 20).size(200, 20).build());
        this.addDrawableChild(new ButtonWidget.Builder(buildButtonTitle("option.show_direction.title", SimpleCoordinatesClient.ModConfig.ShowDirection), (button) -> {
            SimpleCoordinatesClient.ModConfig.ShowDirection = !SimpleCoordinatesClient.ModConfig.ShowDirection;
            button.setMessage(buildButtonTitle("option.show_direction.title", SimpleCoordinatesClient.ModConfig.ShowDirection));
            AutoConfig.getConfigHolder(ModConfig.class).save();
        }).position(this.width / 2 - 100, 40).size(200, 20).build());

        this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> this.client.setScreen(this.previous))
                .position(this.width / 2 - 100, this.height - 27).size(200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
    }
}
