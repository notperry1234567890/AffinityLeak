//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.util.font;

import com.gamesense.api.util.APColor;
import com.gamesense.client.AffinityPlus;
import net.minecraft.client.Minecraft;

public class FontUtils {
  private static final Minecraft mc = Minecraft.getMinecraft();
  
  public static float drawStringWithShadow(boolean customFont, String text, int x, int y, APColor color) {
    if (customFont)
      return AffinityPlus.fontRenderer.drawStringWithShadow(text, x, y, color); 
    return mc.fontRenderer.drawStringWithShadow(text, x, y, color.getRGB());
  }
  
  public static int getStringWidth(boolean customFont, String str) {
    if (customFont)
      return AffinityPlus.fontRenderer.getStringWidth(str); 
    return mc.fontRenderer.getStringWidth(str);
  }
  
  public static int getFontHeight(boolean customFont) {
    if (customFont)
      return AffinityPlus.fontRenderer.getHeight(); 
    return mc.fontRenderer.FONT_HEIGHT;
  }
}
