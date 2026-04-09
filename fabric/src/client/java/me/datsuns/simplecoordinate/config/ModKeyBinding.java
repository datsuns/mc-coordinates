package me.datsuns.simplecoordinate.config;

import com.mojang.blaze3d.platform.InputConstants;
import me.datsuns.simplecoordinate.SimpleCoordinatesClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class ModKeyBinding {
    private static KeyMapping Visible;

    public void initialize() {
        Visible = KeyMappingHelper.registerKeyMapping(
                new KeyMapping(
                        "key.simplecoordinate.visible",
                        InputConstants.Type.KEYSYM,
                        GLFW.GLFW_KEY_COMMA,
                        KeyMapping.Category.MISC
                )
        );
        SimpleCoordinatesClient.LOGGER.info("register key : {}", Visible);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (Visible.consumeClick()) {
                SimpleCoordinatesClient.ModConfig.Visible = !SimpleCoordinatesClient.ModConfig.Visible;
                //AutoConfig.getConfigHolder(ModConfig.class).save();
            }
        });
    }
}
