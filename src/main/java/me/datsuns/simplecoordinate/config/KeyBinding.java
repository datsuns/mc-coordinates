package me.datsuns.simplecoordinate.config;

import io.github.cottonmc.cotton.config.ConfigManager;
import me.datsuns.simplecoordinate.SimpleCoordinate;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    static net.minecraft.client.option.KeyBinding Visible = new net.minecraft.client.option.KeyBinding("key.simplecorrdinate.visible", GLFW.GLFW_KEY_COMMA, "category.simplecoordinate");

    public static void initialize() {
        KeyBindingHelper.registerKeyBinding(Visible);
    }

    public static void tick(){
        if(Visible.wasPressed()){
            SimpleCoordinate.Config.Visible = !SimpleCoordinate.Config.Visible;
            ConfigManager.saveConfig(SimpleCoordinate.Config);
        }
    }
}
