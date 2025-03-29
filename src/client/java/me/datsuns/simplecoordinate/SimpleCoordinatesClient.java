package me.datsuns.simplecoordinate;

import me.datsuns.simplecoordinate.config.ModKeyBinding;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.text.Text;
import me.datsuns.simplecoordinate.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
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
		this.DirectionText.clear();
		this.DirectionText.add(Text.translatable("hud.direction.south"));
		this.DirectionText.add(Text.translatable("hud.direction.southwest"));
		this.DirectionText.add(Text.translatable("hud.direction.west"));
		this.DirectionText.add(Text.translatable("hud.direction.northwest"));
		this.DirectionText.add(Text.translatable("hud.direction.north"));
		this.DirectionText.add(Text.translatable("hud.direction.northeast"));
		this.DirectionText.add(Text.translatable("hud.direction.east"));
		this.DirectionText.add(Text.translatable("hud.direction.southeast"));

		AutoConfig.register(ModConfig.class, Toml4jConfigSerializer::new);
		this.ModConfig = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

		this.keyBinding = new ModKeyBinding();
		this.keyBinding.initialize();

		LOGGER.info("Hello Fabric world!");

	}
}