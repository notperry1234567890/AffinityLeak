package com.gamesense.client.clickgui.buttons;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.font.FontUtils;
import com.gamesense.client.clickgui.frame.Buttons;
import com.gamesense.client.clickgui.frame.Component;
import com.gamesense.client.clickgui.frame.Renderer;
import com.gamesense.client.module.modules.hud.HUD;

public class BooleanComponent extends Component {
  private final Setting.Boolean op;
  
  private final Buttons parent;
  
  private boolean hovered;
  
  private int offset;
  
  private int x;
  
  private int y;
  
  public BooleanComponent(Setting.Boolean option, Buttons button, int offset) {
    this.op = option;
    this.parent = button;
    this.x = button.parent.getX() + button.parent.getWidth();
    this.y = button.parent.getY() + button.offset;
    this.offset = offset;
  }
  
  public void renderComponent() {
    Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + 16 + this.offset, Renderer.getTransColor(this.hovered));
    Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, Renderer.getTransColor(false));
    FontUtils.drawStringWithShadow(HUD.customFont.getValue(), this.op.getName(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, this.op.getValue() ? Renderer.getMainColor() : Renderer.getFontColor());
  }
  
  public void setOff(int newOff) {
    this.offset = newOff;
  }
  
  public void updateComponent(int mouseX, int mouseY) {
    this.hovered = isMouseOnButton(mouseX, mouseY);
    this.y = this.parent.parent.getY() + this.offset;
    this.x = this.parent.parent.getX();
  }
  
  public void mouseClicked(int mouseX, int mouseY, int button) {
    if (isMouseOnButton(mouseX, mouseY) && button == 0 && this.parent.open)
      this.op.setValue(!this.op.getValue()); 
  }
  
  public boolean isMouseOnButton(int x, int y) {
    return (x > this.x && x < this.x + 88 && y > this.y && y < this.y + 16);
  }
}
