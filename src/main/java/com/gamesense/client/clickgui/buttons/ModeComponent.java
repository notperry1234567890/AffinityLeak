package com.gamesense.client.clickgui.buttons;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.font.FontUtils;
import com.gamesense.client.clickgui.frame.Buttons;
import com.gamesense.client.clickgui.frame.Component;
import com.gamesense.client.clickgui.frame.Renderer;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.modules.hud.HUD;
import com.mojang.realmsclient.gui.ChatFormatting;

public class ModeComponent extends Component {
  private final Buttons parent;
  
  private final Setting.Mode set;
  
  private final Module mod;
  
  private boolean hovered;
  
  private int offset;
  
  private int x;
  
  private int y;
  
  private int modeIndex;
  
  public ModeComponent(Setting.Mode set, Buttons button, Module mod, int offset) {
    this.set = set;
    this.parent = button;
    this.mod = mod;
    this.x = button.parent.getX() + button.parent.getWidth();
    this.y = button.parent.getY() + button.offset;
    this.offset = offset;
    this.modeIndex = 0;
  }
  
  public void setOff(int newOff) {
    this.offset = newOff;
  }
  
  public void renderComponent() {
    Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 16, Renderer.getTransColor(this.hovered));
    Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, Renderer.getTransColor(false));
    FontUtils.drawStringWithShadow(HUD.customFont.getValue(), this.set.getName() + " " + ChatFormatting.GRAY + this.set.getValue(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, Renderer.getFontColor());
  }
  
  public void updateComponent(int mouseX, int mouseY) {
    this.hovered = isMouseOnButton(mouseX, mouseY);
    this.y = this.parent.parent.getY() + this.offset;
    this.x = this.parent.parent.getX();
  }
  
  public void mouseClicked(int mouseX, int mouseY, int button) {
    if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open) {
      int maxIndex = this.set.getModes().size() - 1;
      this.modeIndex++;
      if (this.modeIndex > maxIndex)
        this.modeIndex = 0; 
      this.set.setValue(this.set.getModes().get(this.modeIndex));
    } 
  }
  
  public boolean isMouseOnButton(int x, int y) {
    return (x > this.x && x < this.x + 88 && y > this.y && y < this.y + 16);
  }
}
