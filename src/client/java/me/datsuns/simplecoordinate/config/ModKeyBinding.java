package me.datsuns.simplecoordinate.config;

import me.datsuns.simplecoordinate.SimpleCoordinatesClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ModKeyBinding {
    private static KeyBinding Visible;

    public void initialize() {
        Visible = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key.simplecorrdinate.visible",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_COMMA,
                        KeyBinding.Category.MISC
                )
        );
        SimpleCoordinatesClient.LOGGER.info("register key : {}", Visible);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (Visible.wasPressed()) {
                SimpleCoordinatesClient.ModConfig.Visible = !SimpleCoordinatesClient.ModConfig.Visible;
                //AutoConfig.getConfigHolder(ModConfig.class).save();
            }
        });
    }
}
