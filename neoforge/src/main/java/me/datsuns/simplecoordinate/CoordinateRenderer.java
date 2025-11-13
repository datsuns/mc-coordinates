package me.datsuns.simplecoordinate;

import me.datsuns.simplecoordinate.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;

public final class CoordinateRenderer {
    private CoordinateRenderer() {
    }

    public static void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        ModConfig config = SimpleCoordinatesClient.ModConfig;
        if (config == null || !config.Visible) {
            return;
        }
        Minecraft client = Minecraft.getInstance();
        Entity entity = client.getCameraEntity();
        if (entity == null) {
            return;
        }

        String fmt = String.format("X:%4.1f Y:%4.1f Z:%4.1f", entity.getX(), entity.getY(), entity.getZ());
        if (config.ShowDirection) {
            float yaw = entity.getYRot();
            int index = (int) (Util.yawToDegree(yaw) / 45) & 7;
            fmt += String.format(" (%s)", SimpleCoordinatesClient.DirectionText.get(index).getString());
        }
        if (config.ShowAngle) {
            float degree = Util.yawToDegree(entity.getYRot());
            float pitch = entity.getXRot();
            if (degree > 180) {
                degree -= 360.0F;
            }
            fmt += String.format(" (%3.1f/%3.1f)", degree, pitch);
        }
        int posX = config.RenderPosX;
        int posY = config.RenderPosY;
        int color = config.TextColor.argb;

        guiGraphics.drawString(client.font, fmt, posX, posY, color, false);
    }
}
