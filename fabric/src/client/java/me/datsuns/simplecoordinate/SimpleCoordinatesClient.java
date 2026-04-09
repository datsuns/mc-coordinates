package me.datsuns.simplecoordinate;

import me.datsuns.simplecoordinate.DirectionKeys;
import me.datsuns.simplecoordinate.config.ModKeyBinding;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import me.datsuns.simplecoordinate.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SimpleCoordinatesClient implements ClientModInitializer {
    public static final String MOD_ID = SimpleCoordinates.MOD_ID;
    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleCoordinates.MOD_ID);
    public static ModConfig ModConfig;
    public static ArrayList<Component> DirectionText = new ArrayList<>();
    private ModKeyBinding keyBinding;

    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        DirectionText.clear();
        for (String key : DirectionKeys.DIRECTIONS) {
            DirectionText.add(Component.translatable(key));
        }

        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
        ModConfig = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        HudElementRegistry.addLast(Identifier.parse(SimpleCoordinates.MOD_ID + ":render"), new CoordinateRenderer());

        this.keyBinding = new ModKeyBinding();
        this.keyBinding.initialize();

        LOGGER.info("Hello Fabric world!");

    }
}
