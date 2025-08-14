package me.datsuns.simplecoordinate;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.Entity;
import net.minecraft.util.Colors;

public class CoordinateRenderer implements HudElement {
    @Override
    public void render(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        if (!SimpleCoordinatesClient.ModConfig.Visible) {
            return;
        }
        MinecraftClient c = MinecraftClient.getInstance();
        Entity e = c.getCameraEntity();
        if (e == null) {
            return;
        }

        String fmt = String.format("X:%4.1f Y:%4.1f Z:%4.1f", e.getX(), e.getY(), e.getZ());
        if (SimpleCoordinatesClient.ModConfig.ShowDirection) {
            float yaw = e.getYaw(renderTickCounter.getTickProgress(true));
            int index = (int) (Util.yawToDegree(yaw) / 45);
            fmt += String.format(" (%s)", SimpleCoordinatesClient.DirectionText.get(index).getString());
        }
        if (SimpleCoordinatesClient.ModConfig.ShowAngle) {
            float degree = Util.yawToDegree(e.getYaw());
            float pitch = e.getPitch();
            if (degree > 180 ){
                degree -= 360.0F;
            }
            fmt += String.format(" (%3.1f/%3.1f)", degree, pitch);
        }
        int posX = 5;
        int posY = 5;
        //c.textRenderer.drawWithShadow(matrixStack, fmt, posX, posY, 0xFFFFFF);
        drawContext.drawText(c.textRenderer, fmt, posX, posY, Colors.WHITE, false);

    }
}
