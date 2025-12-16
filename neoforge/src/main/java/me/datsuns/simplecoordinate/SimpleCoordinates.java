package me.datsuns.simplecoordinate;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.datsuns.simplecoordinate.config.ModKeyBinding;

@Mod(SimpleCoordinates.MOD_ID)
public class SimpleCoordinates {
    public static final String MOD_ID = "simplecoordinates";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public SimpleCoordinates(IEventBus modEventBus) {
        modEventBus.addListener(ModClientEvents::onClientSetup);
        modEventBus.addListener(ModClientEvents::onRegisterKeyMappings);
        NeoForge.EVENT_BUS.addListener(ModClientEvents::onRenderGui);
        NeoForge.EVENT_BUS.addListener(ClientEvents::onClientTick);
    }

    public static class ModClientEvents {
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(SimpleCoordinatesClient::onInitializeClient);
        }

        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            ModKeyBinding.register(event);
        }

        public static void onRenderGui(RenderGuiEvent.Post event) {
            CoordinateRenderer.render(event.getGuiGraphics(), event.getPartialTick());
        }
    }

    public static class ClientEvents {
        public static void onClientTick(ClientTickEvent.Post event) {
            ModKeyBinding.handleClientTick();
        }
    }
}
