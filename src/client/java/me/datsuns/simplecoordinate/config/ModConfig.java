package me.datsuns.simplecoordinate.config;

import me.datsuns.simplecoordinate.SimpleCoordinatesClient;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = SimpleCoordinatesClient.MOD_ID)
public class ModConfig implements ConfigData {
    public Boolean Visible = false;
    public Boolean ShowDirection = false;
    public Boolean ShowAngle = false;

    public enum DirectionType {
        Nothing(0),
        String(1),
        Angle(2),
        StringAndAngle(3);

        private final int n;

        DirectionType(int i) {
            this.n = i;
        }
    }
}
