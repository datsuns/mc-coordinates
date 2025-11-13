package me.datsuns.simplecoordinate;

import me.datsuns.simplecoordinate.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Mod(value = SimpleCoordinates.MOD_ID, dist = Dist.CLIENT)
public class SimpleCoordinatesClient {
    public static final Logger LOGGER = LoggerFactory.getLogger(SimpleCoordinates.MOD_ID);
    public static ModConfig ModConfig;
    public static final List<Component> DirectionText = new ArrayList<>();

    public SimpleCoordinatesClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, (IConfigScreenFactory) (mc, parent) -> ConfigScreenProvider.create(parent));
    }

    public static void onInitializeClient() {
        if (DirectionText.isEmpty()) {
            for (String key : DirectionKeys.DIRECTIONS) {
                DirectionText.add(Component.translatable(key));
            }
        }
        AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
        ModConfig = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
