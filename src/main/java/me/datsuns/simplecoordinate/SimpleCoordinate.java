package me.datsuns.simplecoordinate;

import io.github.cottonmc.cotton.config.ConfigManager;
import me.datsuns.simplecoordinate.config.Config;
import net.fabricmc.api.ModInitializer;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SimpleCoordinate implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "SimpleCorrdinate";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Config Config;

	public static ArrayList<Text> DirectionText = new ArrayList<>();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		this.Config = ConfigManager.loadConfig(Config.class);

		this.DirectionText.clear();
		this.DirectionText.add(Text.translatable("hud.direction.south"));
		this.DirectionText.add(Text.translatable("hud.direction.southwest"));
		this.DirectionText.add(Text.translatable("hud.direction.west"));
		this.DirectionText.add(Text.translatable("hud.direction.northwest"));
		this.DirectionText.add(Text.translatable("hud.direction.north"));
		this.DirectionText.add(Text.translatable("hud.direction.northeast"));
		this.DirectionText.add(Text.translatable("hud.direction.east"));
		this.DirectionText.add(Text.translatable("hud.direction.southeast"));

		LOGGER.info("Hello Fabric world!");
	}
}
