package me.datsuns.simplecoordinate;

public class Util {
    public static float yawToDegree(float yaw){
        return ((yaw % 360) + 360) % 360;
    }
}
