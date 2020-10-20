package com.gamesense.api.util;

import java.awt.Color;
import org.lwjgl.opengl.GL11;

public class APColor extends Color {
  public APColor(int rgb) {
    super(rgb);
  }
  
  public APColor(int rgba, boolean hasalpha) {
    super(rgba, hasalpha);
  }
  
  public APColor(int r, int g, int b) {
    super(r, g, b);
  }
  
  public APColor(int r, int g, int b, int a) {
    super(r, g, b, a);
  }
  
  public APColor(Color color) {
    super(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
  }
  
  public APColor(APColor color, int a) {
    super(color.getRed(), color.getGreen(), color.getBlue(), a);
  }
  
  public static APColor fromHSB(float hue, float saturation, float brightness) {
    return new APColor(Color.getHSBColor(hue, saturation, brightness));
  }
  
  public float getHue() {
    return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[0];
  }
  
  public float getSaturation() {
    return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[1];
  }
  
  public float getBrightness() {
    return RGBtoHSB(getRed(), getGreen(), getBlue(), null)[2];
  }
  
  public void glColor() {
    GL11.glColor4f(getRed() / 255.0F, getGreen() / 255.0F, getBlue() / 255.0F, getAlpha() / 255.0F);
  }
}
