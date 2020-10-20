package com.gamesense.client.clickgui.frame;

import com.gamesense.api.util.font.FontUtils;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.clickgui.ClickGUI;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.gamesense.client.module.modules.hud.ClickGuiModule;
import com.gamesense.client.module.modules.hud.HUD;
import java.util.ArrayList;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.input.Mouse;

public class Frames {
  private final int width;
  
  private final int barHeight;
  
  public ArrayList<Component> guicomponents;
  
  public Module.Category category;
  
  public int x;
  
  public int y;
  
  public int dragX;
  
  public int dragY;
  
  public boolean open;
  
  boolean font;
  
  private int height;
  
  private boolean isDragging;
  
  public Frames(Module.Category catg) {
    this.guicomponents = new ArrayList<>();
    this.category = catg;
    this.open = true;
    this.isDragging = false;
    this.x = 10;
    this.y = 30;
    this.dragX = 0;
    this.width = 100;
    this.barHeight = 16;
    int tY = this.barHeight;
    for (Module mod : ModuleManager.getModulesInCategory(catg)) {
      Buttons devmodButton = new Buttons(mod, this, tY);
      this.guicomponents.add(devmodButton);
      tY += 16;
    } 
    refresh();
  }
  
  public ArrayList<Component> getComponents() {
    return this.guicomponents;
  }
  
  public int getWidth() {
    return this.width;
  }
  
  public int getX() {
    return this.x;
  }
  
  public void setX(int newX) {
    this.x = newX;
  }
  
  public int getY() {
    return this.y;
  }
  
  public void setY(int newY) {
    this.y = newY;
  }
  
  public void renderGUIFrame(FontRenderer fontRenderer) {
    Renderer.drawRectGradient(this.x, this.y, this.x + this.width, this.y + this.barHeight, Renderer.getMainColor(), Renderer.getTransColor(false));
    if (this.font) {
      AffinityPlus.fontRenderer.drawStringWithShadow(this.category.name(), (this.x + 2), (this.y + 3), Renderer.getFontColor());
    } else {
      FontUtils.drawStringWithShadow(HUD.customFont.getValue(), this.category.name(), this.x + 2, this.y + 3, Renderer.getFontColor());
    } 
    if (this.open && !this.guicomponents.isEmpty())
      for (Component component : this.guicomponents)
        component.renderComponent();  
  }
  
  public void updatePosition(int mouseX, int mouseY) {
    if (this.isDragging) {
      setX(mouseX - this.dragX);
      setY(mouseY - this.dragY);
    } 
  }
  
  public boolean isWithinHeader(int x, int y) {
    return (x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight);
  }
  
  public void setDrag(boolean drag) {
    this.isDragging = drag;
  }
  
  public boolean isOpen() {
    return this.open;
  }
  
  public void setOpen(boolean open) {
    this.open = open;
  }
  
  public void refresh() {
    int off = this.barHeight;
    for (Component comp : this.guicomponents) {
      comp.setOff(off);
      off += comp.getHeight();
    } 
    this.height = off;
  }
  
  public void updateMouseWheel() {
    int scrollWheel = Mouse.getDWheel();
    for (Frames frames : ClickGUI.frames) {
      if (scrollWheel < 0) {
        frames.setY(frames.getY() - ClickGuiModule.scrollSpeed.getValue());
        continue;
      } 
      if (scrollWheel > 0)
        frames.setY(frames.getY() + ClickGuiModule.scrollSpeed.getValue()); 
    } 
  }
}
