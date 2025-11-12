package me.datsuns.simplecoordinate;

import me.datsuns.simplecoordinate.config.ModKeyBinding;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import me.datsuns.simplecoordinate.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SimpleCoordinatesClient implements ClientModInitializer {
    public static final String MOD_ID = "SimpleCoordinate";
    public static final Logger LOGGER = LoggerFactory.getLogger("simple-coordinates");
    public static ModConfig ModConfig;
    public static ArrayList<Text> DirectionText = new ArrayList<>();
    private ModKeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        DirectionText.clear();
        DirectionText.add(Text.translatable("hud.direction.south"));
        DirectionText.add(Text.translatable("hud.direction.southwest"));
        DirectionText.add(Text.translatable("hud.direction.west"));
        DirectionText.add(Text.translatable("hud.direction.northwest"));
        DirectionText.add(Text.translatable("hud.direction.north"));
        DirectionText.add(Text.translatable("hud.direction.northeast"));
        DirectionText.add(Text.translatable("hud.direction.east"));
        DirectionText.add(Text.translatable("hud.direction.southeast"));

        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
        ModConfig = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        HudElementRegistry.addLast(Identifier.of("simple-coordinates", "render"), new CoordinateRenderer());

        this.keyBinding = new ModKeyBinding();
        this.keyBinding.initialize();

        LOGGER.info("Hello Fabric world!");

    }
}