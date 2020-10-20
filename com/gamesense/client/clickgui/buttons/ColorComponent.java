package com.gamesense.client.clickgui.buttons;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.APColor;
import com.gamesense.api.util.font.FontUtils;
import com.gamesense.client.clickgui.frame.Buttons;
import com.gamesense.client.clickgui.frame.Component;
import com.gamesense.client.clickgui.frame.Renderer;
import com.gamesense.client.module.modules.hud.ClickGuiModule;
import com.gamesense.client.module.modules.hud.ColorMain;
import com.gamesense.client.module.modules.hud.HUD;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ColorComponent extends Component {
  private final Setting.ColorSetting set;
  
  private final Buttons parent;
  
  private boolean hoveredA;
  
  private boolean hoveredB;
  
  private int offset;
  
  private int x;
  
  private int y;
  
  private boolean dragging;
  
  public ColorComponent(Setting.ColorSetting value, Buttons button, int offset) {
    this.dragging = false;
    this.set = value;
    this.parent = button;
    this.x = button.parent.getX() + button.parent.getWidth();
    this.y = button.parent.getY() + button.offset;
    this.offset = offset;
  }
  
  private static double roundToPlace(double value, int places) {
    if (places < 0)
      throw new IllegalArgumentException(); 
    BigDecimal bd = new BigDecimal(value);
    bd = bd.setScale(places, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
  
  public void renderComponent() {
    double renderWidthR, renderWidthG, renderWidthB;
    Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 80, Renderer.getTransColor(this.hoveredA));
    if (this.set.getRainbow())
      Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1 + 16, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 32, this.set.getValue()); 
    Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 1, Renderer.getTransColor(false));
    FontUtils.drawStringWithShadow(HUD.customFont.getValue(), this.set.getName(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4, Renderer.getFontColor());
    FontUtils.drawStringWithShadow(HUD.customFont.getValue(), ChatFormatting.GRAY + "Rainbow", this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4 + 16, Renderer.getFontColor());
    Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 81, this.parent.parent.getX() + this.parent.parent.getWidth(), this.parent.parent.getY() + this.offset + 96, Renderer.getTransColor(this.hoveredB));
    FontUtils.drawStringWithShadow(HUD.customFont.getValue(), ChatFormatting.GRAY + "Sync Color", this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4 + 80, Renderer.getFontColor());
    if (ColorMain.colorModel.getValue().equalsIgnoreCase("RGB")) {
      renderWidthR = (100 * this.set.getColor().getRed()) / 255.0D;
      renderWidthG = (100 * this.set.getColor().getGreen()) / 255.0D;
      renderWidthB = (100 * this.set.getColor().getBlue()) / 255.0D;
    } else {
      renderWidthR = (100.0F * this.set.getColor().getHue());
      renderWidthG = (100.0F * this.set.getColor().getSaturation());
      renderWidthB = (100.0F * this.set.getColor().getBrightness());
    } 
    Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1 + 32, this.parent.parent.getX() + (int)renderWidthR, this.parent.parent.getY() + this.offset + 48, this.set.getValue());
    Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1 + 48, this.parent.parent.getX() + (int)renderWidthG, this.parent.parent.getY() + this.offset + 64, this.set.getValue());
    Renderer.drawRectStatic(this.parent.parent.getX(), this.parent.parent.getY() + this.offset + 1 + 64, this.parent.parent.getX() + (int)renderWidthB, this.parent.parent.getY() + this.offset + 80, this.set.getValue());
    if (ColorMain.colorModel.getValue().equalsIgnoreCase("RGB")) {
      FontUtils.drawStringWithShadow(HUD.customFont.getValue(), ChatFormatting.GRAY + "Red: " + this.set.getColor().getRed(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4 + 32, Renderer.getFontColor());
      FontUtils.drawStringWithShadow(HUD.customFont.getValue(), ChatFormatting.GRAY + "Green: " + this.set.getColor().getGreen(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4 + 48, Renderer.getFontColor());
      FontUtils.drawStringWithShadow(HUD.customFont.getValue(), ChatFormatting.GRAY + "Blue: " + this.set.getColor().getBlue(), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4 + 64, Renderer.getFontColor());
    } else {
      FontUtils.drawStringWithShadow(HUD.customFont.getValue(), ChatFormatting.GRAY + "Hue: " + (int)(this.set.getColor().getHue() * 360.0F), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4 + 32, Renderer.getFontColor());
      FontUtils.drawStringWithShadow(HUD.customFont.getValue(), ChatFormatting.GRAY + "Saturation: " + (int)(this.set.getColor().getSaturation() * 100.0F), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4 + 48, Renderer.getFontColor());
      FontUtils.drawStringWithShadow(HUD.customFont.getValue(), ChatFormatting.GRAY + "Brightness: " + (int)(this.set.getColor().getBrightness() * 100.0F), this.parent.parent.getX() + 2, this.parent.parent.getY() + this.offset + 4 + 64, Renderer.getFontColor());
    } 
  }
  
  public void setOff(int newOff) {
    this.offset = newOff;
  }
  
  public void updateComponent(int mouseX, int mouseY) {
    boolean hovered = (isMouseOnButtonD(mouseX, mouseY) || isMouseOnButtonI(mouseX, mouseY));
    this.y = this.parent.parent.getY() + this.offset;
    this.x = this.parent.parent.getX();
    double diff = Math.min(100, Math.max(0, mouseX - this.x));
    if (hovered) {
      if (mouseY - this.y <= 80) {
        this.hoveredA = true;
        this.hoveredB = false;
      } else {
        this.hoveredA = false;
        this.hoveredB = true;
      } 
    } else {
      this.hoveredA = false;
      this.hoveredB = false;
    } 
    if (this.dragging) {
      APColor c = this.set.getColor();
      if (ColorMain.colorModel.getValue().equalsIgnoreCase("RGB")) {
        int newValue;
        if (diff == 0.0D) {
          newValue = 0;
        } else {
          newValue = (int)roundToPlace(diff / 100.0D * 255.0D, 2);
        } 
        if (mouseY - this.y >= 32 && mouseY - this.y < 48) {
          this.set.setValue(this.set.getRainbow(), new APColor(newValue, c.getGreen(), c.getBlue()));
        } else if (mouseY - this.y >= 48 && mouseY - this.y < 64) {
          this.set.setValue(this.set.getRainbow(), new APColor(c.getRed(), newValue, c.getBlue()));
        } else if (mouseY - this.y >= 64 && mouseY - this.y < 80) {
          this.set.setValue(this.set.getRainbow(), new APColor(c.getRed(), c.getGreen(), newValue));
        } 
      } else {
        float newValue = (float)(diff / 100.0D);
        if (mouseY - this.y >= 32 && mouseY - this.y < 48) {
          this.set.setValue(this.set.getRainbow(), APColor.fromHSB(newValue, c.getSaturation(), c.getBrightness()));
        } else if (mouseY - this.y >= 48 && mouseY - this.y < 64) {
          this.set.setValue(this.set.getRainbow(), APColor.fromHSB(c.getHue(), newValue, c.getBrightness()));
        } else if (mouseY - this.y >= 64 && mouseY - this.y < 80) {
          this.set.setValue(this.set.getRainbow(), APColor.fromHSB(c.getHue(), c.getSaturation(), newValue));
        } 
      } 
    } 
  }
  
  public void mouseClicked(int mouseX, int mouseY, int button) {
    if ((isMouseOnButtonI(mouseX, mouseY) || isMouseOnButtonD(mouseX, mouseY)) && button == 0 && this.parent.open) {
      this.dragging = true;
      if (mouseY - this.y >= 16 && mouseY - this.y < 32) {
        this.set.setValue(!this.set.getRainbow(), this.set.getColor());
      } else if (mouseY - this.y >= 80 && mouseY - this.y < 96) {
        this.set.setValue(ClickGuiModule.guiColor.getRainbow(), ClickGuiModule.guiColor.getColor());
      } 
    } 
  }
  
  public void mouseReleased(int mouseX, int mouseY, int mouseButton) {
    this.dragging = false;
  }
  
  public boolean isMouseOnButtonD(int x, int y) {
    return (x > this.x && x < this.x + this.parent.parent.getWidth() / 2 + 1 && y > this.y && y < this.y + 96);
  }
  
  public boolean isMouseOnButtonI(int x, int y) {
    return (x > this.x + this.parent.parent.getWidth() / 2 && x < this.x + this.parent.parent.getWidth() && y > this.y && y < this.y + 96);
  }
}
