package me.datsuns.simplecoordinate;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class CoordinateRenderer implements HudElement {

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, DeltaTracker deltaTracker) {
        if (!SimpleCoordinatesClient.ModConfig.Visible) {
            return;
        }
        Minecraft c = Minecraft.getInstance();
        Entity e = c.getCameraEntity();
        if (e == null) {
            return;
        }

        String fmt = String.format("X:%4.1f Y:%4.1f Z:%4.1f", e.getX(), e.getY(), e.getZ());
        if (SimpleCoordinatesClient.ModConfig.ShowDirection) {
            float yaw = e.getYRot();
            int index = (int) (Util.yawToDegree(yaw) / 45) & 7;
            fmt += String.format(" (%s)", SimpleCoordinatesClient.DirectionText.get(index).getString());
        }
        if (SimpleCoordinatesClient.ModConfig.ShowAngle) {
            float degree = Util.yawToDegree(e.getYRot());
            float pitch = e.getXRot();
            if (degree > 180 ){
                degree -= 360.0F;
            }
            fmt += String.format(" (%3.1f/%3.1f)", degree, pitch);
        }
        
        int x = SimpleCoordinatesClient.ModConfig.RenderPosX;
        int y = SimpleCoordinatesClient.ModConfig.RenderPosY;
        int color = SimpleCoordinatesClient.ModConfig.TextColor.argb;

        extractor.text(c.font, Component.literal(fmt), x, y, color, false);
    }

}
