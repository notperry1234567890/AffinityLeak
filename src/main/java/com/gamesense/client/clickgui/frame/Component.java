//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.clickgui.frame;

import net.minecraft.client.Minecraft;

public abstract class Component {
  protected Minecraft mc = Minecraft.getMinecraft();
  
  public void updateComponent(int mouseX, int mouseY) {}
  
  public void renderComponent() {}
  
  public void setOff(int newOff) {}
  
  public int getHeight() {
    return 0;
  }
  
  public int getParentHeight() {
    return 0;
  }
  
  public void mouseClicked(int mouseX, int mouseY, int button) {}
  
  public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}
  
  public void keyTyped(char typedChar, int key) {}
}
