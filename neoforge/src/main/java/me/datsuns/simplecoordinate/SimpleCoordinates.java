package me.datsuns.simplecoordinate;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.datsuns.simplecoordinate.config.ModKeyBinding;

@Mod(SimpleCoordinates.MOD_ID)
public class SimpleCoordinates {
    public static final String MOD_ID = "simplecoordinates";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    static final ResourceLocation HUD_LAYER = ResourceLocation.fromNamespaceAndPath(MOD_ID, "coordinates");

    public SimpleCoordinates(IEventBus modEventBus) {
        modEventBus.addListener(ModClientEvents::onClientSetup);
        modEventBus.addListener(ModClientEvents::onRegisterKeyMappings);
        modEventBus.addListener(ModClientEvents::onRegisterLayers);
        NeoForge.EVENT_BUS.addListener(ClientEvents::onClientTick);
    }

    public static class ModClientEvents {
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(SimpleCoordinatesClient::onInitializeClient);
        }

        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            ModKeyBinding.register(event);
        }

        public static void onRegisterLayers(RegisterGuiLayersEvent event) {
            event.registerAboveAll(HUD_LAYER, CoordinateRenderer::render);
        }
    }

    public static class ClientEvents {
        public static void onClientTick(ClientTickEvent.Post event) {
            ModKeyBinding.handleClientTick();
        }
    }
}
