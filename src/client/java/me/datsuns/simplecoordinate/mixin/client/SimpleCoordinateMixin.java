package me.datsuns.simplecoordinate.mixin.client;

import me.datsuns.simplecoordinate.SimpleCoordinatesClient;
import me.datsuns.simplecoordinate.Util;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(InGameHud.class)
public class SimpleCoordinateMixin {
    @Inject(at = @At("TAIL"), method = "render")
    public void render(DrawContext context, RenderTickCounter tickCounter, CallbackInfo info) throws Exception {
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
            float yaw = e.getYaw(tickCounter.getTickDelta(true));
            int index = (int) (Util.yawToDegree(yaw) / 45);
            fmt += String.format(" (%s)", SimpleCoordinatesClient.DirectionText.get(index).getString());
        }
        if (SimpleCoordinatesClient.ModConfig.ShowAngle) {
            float degree = Util.yawToDegree(e.getYaw());
            float pitch = e.getPitch();
            if (degree > 180 ){
                degree -= 360.0;
            }
            fmt += String.format(" (%3.1f/%3.1f)", degree, pitch);
        }
        int posX = 5;
        int posY = 5;
        //c.textRenderer.drawWithShadow(matrixStack, fmt, posX, posY, 0xFFFFFF);
        context.drawText(c.textRenderer, fmt, posX, posY, 0xFFFFFF, false);
    }
}