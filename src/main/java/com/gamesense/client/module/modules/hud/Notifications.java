//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.hud;

import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.APColor;
import com.gamesense.api.util.font.FontUtils;
import com.gamesense.client.module.Module;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class Notifications extends Module {
  public static Setting.Boolean disableChat;
  
  static List<TextComponentString> list = new ArrayList<>();
  
  Setting.Integer notX;
  
  Setting.Integer notY;
  
  Setting.Boolean sortUp;
  
  Setting.Boolean sortRight;
  
  int sort;
  
  int notCount;
  
  int waitCounter;
  
  TextFormatting notColor;
  
  public Notifications() {
    super("Notifications", Module.Category.HUD);
  }
  
  public static void addMessage(TextComponentString m) {
    if (list.size() < 3) {
      list.remove(m);
      list.add(m);
    } else {
      list.remove(0);
      list.remove(m);
      list.add(m);
    } 
  }
  
  public void setup() {
    disableChat = registerBoolean("No Chat Msg", "NoChatMsg", true);
    this.sortUp = registerBoolean("Sort Up", "SortUp", false);
    this.sortRight = registerBoolean("Sort Right", "SortRight", false);
    this.notX = registerInteger("X", "X", 0, 0, 1000);
    this.notY = registerInteger("Y", "Y", 50, 0, 1000);
  }
  
  public void onUpdate() {
    if (this.waitCounter < 500) {
      this.waitCounter++;
      return;
    } 
    this.waitCounter = 0;
    if (list.size() > 0)
      list.remove(0); 
  }
  
  public void onRender() {
    if (this.sortUp.getValue()) {
      this.sort = -1;
    } else {
      this.sort = 1;
    } 
    this.notCount = 0;
    for (TextComponentString s : list) {
      this.notCount = list.indexOf(s) + 1;
      this.notColor = s.getStyle().getColor();
      if (this.sortUp.getValue()) {
        if (this.sortRight.getValue()) {
          FontUtils.drawStringWithShadow(HUD.customFont.getValue(), s.getText(), this.notX.getValue() - FontUtils.getStringWidth(HUD.customFont.getValue(), s.getText()), this.notY.getValue() + this.notCount * 10, new APColor(255, 255, 255));
          continue;
        } 
        FontUtils.drawStringWithShadow(HUD.customFont.getValue(), s.getText(), this.notX.getValue(), this.notY.getValue() + this.notCount * 10, new APColor(255, 255, 255));
        continue;
      } 
      if (this.sortRight.getValue()) {
        FontUtils.drawStringWithShadow(HUD.customFont.getValue(), s.getText(), this.notX.getValue() - FontUtils.getStringWidth(HUD.customFont.getValue(), s.getText()), this.notY.getValue() + this.notCount * -10, new APColor(255, 255, 255));
        continue;
      } 
      FontUtils.drawStringWithShadow(HUD.customFont.getValue(), s.getText(), this.notX.getValue(), this.notY.getValue() + this.notCount * -10, new APColor(255, 255, 255));
    } 
  }
}
