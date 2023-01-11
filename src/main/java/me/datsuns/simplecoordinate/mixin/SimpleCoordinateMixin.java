package me.datsuns.simplecoordinate.mixin;

import me.datsuns.simplecoordinate.SimpleCoordinate;
import me.datsuns.simplecoordinate.Util;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class SimpleCoordinateMixin {
	@Inject(at = @At("TAIL"), method = "render")
	public void render(MatrixStack matrixStack, float tickDelta, CallbackInfo info){
		if( !SimpleCoordinate.Config.Visible ){
			return;
		}
		MinecraftClient c = MinecraftClient.getInstance();
		Entity e = c.getCameraEntity();
		if( e == null ){
			return;
		}

		matrixStack.push();
		String fmt = String.format("X:%4.1f Y:%4.1f Z:%4.1f", e.getX(), e.getY(), e.getZ());
		if( SimpleCoordinate.Config.ShowDirection ){
			float yaw = e.getYaw(tickDelta);
			int index = (int) (Util.yawToDegree(yaw) / 45);
			fmt += String.format(" (%s)", SimpleCoordinate.DirectionText.get(index).getString());
		}
		float posX = 5;
		float posY = 5;
		//c.textRenderer.drawWithShadow(matrixStack, fmt, posX, posY, 0xFFFFFF);
		c.textRenderer.draw(matrixStack, fmt, posX, posY, 0xFFFFFF);

		matrixStack.pop();
	}
}
