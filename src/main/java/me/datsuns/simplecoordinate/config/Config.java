package me.datsuns.simplecoordinate.config;

import io.github.cottonmc.cotton.config.annotations.ConfigFile;
import me.datsuns.simplecoordinate.SimpleCoordinate;
import net.minecraft.text.Text;
import java.util.ArrayList;

@ConfigFile(name = "./" + SimpleCoordinate.MOD_ID, extension = ".json")
public class Config {
    public Boolean ShowDirection = false;
    public ArrayList<String> DirectionKeys = new ArrayList<>();
    public ArrayList<String> DirectionText = new ArrayList<>();

    public void configLoaded(){
        this.DirectionText.clear();
        this.DirectionText.add(Text.translatable("hud.direction.south").getString());
        this.DirectionText.add(Text.translatable("hud.direction.southwest").getString());
        this.DirectionText.add(Text.translatable("hud.direction.west").getString());
        this.DirectionText.add(Text.translatable("hud.direction.northwest").getString());
        this.DirectionText.add(Text.translatable("hud.direction.north").getString());
        this.DirectionText.add(Text.translatable("hud.direction.northeast").getString());
        this.DirectionText.add(Text.translatable("hud.direction.east").getString());
        this.DirectionText.add(Text.translatable("hud.direction.southeast").getString());

        this.DirectionKeys.clear();
        this.DirectionKeys.add("hud.direction.south");
        this.DirectionKeys.add("hud.direction.southwest");
        this.DirectionKeys.add("hud.direction.west");
        this.DirectionKeys.add("hud.direction.northwest");
        this.DirectionKeys.add("hud.direction.north");
        this.DirectionKeys.add("hud.direction.northeast");
        this.DirectionKeys.add("hud.direction.east");
        this.DirectionKeys.add("hud.direction.southeast");
    }
}
