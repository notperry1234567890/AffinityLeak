//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.clickgui;

import com.gamesense.client.clickgui.frame.Component;
import com.gamesense.client.clickgui.frame.Frames;
import com.gamesense.client.module.Module;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.gui.GuiScreen;

public class ClickGUI extends GuiScreen {
  public static ArrayList<Frames> frames;
  
  public ClickGUI() {
    frames = new ArrayList<>();
    int DevFrameX = 10;
    for (Module.Category category : Module.Category.values()) {
      Frames devframe = new Frames(category);
      devframe.setX(DevFrameX);
      frames.add(devframe);
      DevFrameX += devframe.getWidth() + 10;
    } 
  }
  
  public static Frames getFrameByName(String name) {
    Frames pa = null;
    for (Frames frames : getFrames()) {
      if (name.equalsIgnoreCase(String.valueOf(frames.category)))
        pa = frames; 
    } 
    return pa;
  }
  
  public static ArrayList<Frames> getFrames() {
    return frames;
  }
  
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    for (Frames frames : ClickGUI.frames) {
      frames.renderGUIFrame(this.fontRenderer);
      frames.updatePosition(mouseX, mouseY);
      frames.updateMouseWheel();
      for (Component comp : frames.getComponents())
        comp.updateComponent(mouseX, mouseY); 
    } 
  }
  
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    for (Frames frames : ClickGUI.frames) {
      if (frames.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
        frames.setDrag(true);
        frames.dragX = mouseX - frames.getX();
        frames.dragY = mouseY - frames.getY();
      } 
      if (frames.isWithinHeader(mouseX, mouseY) && mouseButton == 1)
        frames.setOpen(!frames.isOpen()); 
      if (frames.isOpen() && !frames.getComponents().isEmpty())
        for (Component component : frames.getComponents())
          component.mouseClicked(mouseX, mouseY, mouseButton);  
    } 
  }
  
  protected void mouseReleased(int mouseX, int mouseY, int state) {
    for (Frames frames : ClickGUI.frames)
      frames.setDrag(false); 
    for (Frames frames : ClickGUI.frames) {
      if (frames.isOpen() && !frames.getComponents().isEmpty())
        for (Component component : frames.getComponents())
          component.mouseReleased(mouseX, mouseY, state);  
    } 
  }
  
  protected void keyTyped(char typedChar, int keyCode) {
    for (Frames frames : ClickGUI.frames) {
      if (frames.isOpen() && !frames.getComponents().isEmpty())
        for (Component component : frames.getComponents())
          component.keyTyped(typedChar, keyCode);  
    } 
    if (keyCode == 1)
      this.mc.displayGuiScreen(null); 
  }
  
  public boolean doesGuiPauseGame() {
    return false;
  }
  
  public void initGui() {}
}
