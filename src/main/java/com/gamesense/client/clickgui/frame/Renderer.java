//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.clickgui.frame;

import com.gamesense.api.util.APColor;
import com.gamesense.client.module.modules.hud.ClickGuiModule;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class Renderer {
  public static void drawRectStatic(int leftX, int leftY, int rightX, int rightY, APColor color) {
    Gui.drawRect(leftX, leftY, rightX, rightY, color.getRGB());
  }
  
  public static void drawRectGradient(int leftX, int leftY, int rightX, int rightY, APColor startColor, APColor endColor) {
    float s = (startColor.getRGB() >> 24 & 0xFF) / 255.0F;
    float s1 = (startColor.getRGB() >> 16 & 0xFF) / 255.0F;
    float s2 = (startColor.getRGB() >> 8 & 0xFF) / 255.0F;
    float s3 = (startColor.getRGB() & 0xFF) / 255.0F;
    float e1 = (endColor.getRGB() >> 24 & 0xFF) / 255.0F;
    float e2 = (endColor.getRGB() >> 16 & 0xFF) / 255.0F;
    float e3 = (endColor.getRGB() >> 8 & 0xFF) / 255.0F;
    float e4 = (endColor.getRGB() & 0xFF) / 255.0F;
    GlStateManager.disableTexture2D();
    GlStateManager.enableBlend();
    GlStateManager.disableAlpha();
    GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    GlStateManager.shadeModel(7425);
    Tessellator tessellator = Tessellator.getInstance();
    BufferBuilder bufferbuilder = tessellator.getBuffer();
    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
    bufferbuilder.pos(rightX, leftY, 0.0D).color(s1, s2, s3, s).endVertex();
    bufferbuilder.pos(leftX, leftY, 0.0D).color(s1, s2, s3, s).endVertex();
    bufferbuilder.pos(leftX, rightY, 0.0D).color(e2, e3, e4, e1).endVertex();
    bufferbuilder.pos(rightX, rightY, 0.0D).color(e2, e4, e4, e1).endVertex();
    tessellator.draw();
    GlStateManager.shadeModel(7424);
    GlStateManager.disableBlend();
    GlStateManager.enableAlpha();
    GlStateManager.enableTexture2D();
  }
  
  public static APColor getMainColor() {
    return ClickGuiModule.guiColor.getValue();
  }
  
  public static APColor getTransColor(boolean hovered) {
    APColor transColor = new APColor(195, 195, 195, ClickGuiModule.opacity.getValue() - 50);
    if (ClickGuiModule.backgroundColor.getValue().equalsIgnoreCase("Black")) {
      transColor = new APColor(0, 0, 0, ClickGuiModule.opacity.getValue() - 50);
    } else if (ClickGuiModule.backgroundColor.getValue().equalsIgnoreCase("Silver")) {
      transColor = new APColor(100, 100, 100, ClickGuiModule.opacity.getValue() - 50);
    } 
    if (hovered)
      return new APColor(transColor.darker().darker()); 
    return transColor;
  }
  
  public static APColor getFontColor() {
    return new APColor(255, 255, 255);
  }
}
