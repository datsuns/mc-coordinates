package me.datsuns.simplecoordinate.config;

import io.github.cottonmc.cotton.config.annotations.ConfigFile;
import me.datsuns.simplecoordinate.SimpleCoordinate;

@ConfigFile(name = "./" + SimpleCoordinate.MOD_ID, extension = ".json")
public class Config {
    public Boolean ShowDirection = false;
}
