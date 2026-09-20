package me.datsuns.simplecoordinate.config;

import com.mojang.blaze3d.platform.InputConstants;
import me.datsuns.simplecoordinate.SimpleCoordinatesClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

public final class ModKeyBinding {
    private static KeyMapping visible;

    private ModKeyBinding() {
    }

    public static void register(RegisterKeyMappingsEvent event) {
        visible = new KeyMapping(
                "key.simplecoordinate.visible",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_COMMA,
                KeyMapping.Category.MISC
        );
        event.register(visible);
    }

    public static void handleClientTick() {
        if (visible == null || SimpleCoordinatesClient.ModConfig == null) {
            return;
        }
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) {
            return;
        }
        while (visible.consumeClick()) {
            SimpleCoordinatesClient.ModConfig.Visible = !SimpleCoordinatesClient.ModConfig.Visible;
        }
    }
}
