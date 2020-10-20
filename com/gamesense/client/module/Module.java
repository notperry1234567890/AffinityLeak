//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module;

import com.gamesense.api.event.events.RenderEvent;
import com.gamesense.api.settings.Setting;
import com.gamesense.api.util.APColor;
import com.gamesense.client.AffinityPlus;
import java.util.List;
import net.minecraft.client.Minecraft;

public abstract class Module {
  protected static final Minecraft mc = Minecraft.getMinecraft();
  
  String name;
  
  Category category;
  
  int bind;
  
  boolean enabled;
  
  boolean drawn;
  
  public Module(String n, Category c) {
    this.name = n;
    this.category = c;
    this.bind = 0;
    this.enabled = false;
    this.drawn = true;
    setup();
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String n) {
    this.name = n;
  }
  
  public Category getCategory() {
    return this.category;
  }
  
  public void setCategory(Category c) {
    this.category = c;
  }
  
  public int getBind() {
    return this.bind;
  }
  
  public void setBind(int b) {
    this.bind = b;
  }
  
  protected void onEnable() {}
  
  protected void onDisable() {}
  
  public void onUpdate() {}
  
  public void onRender() {}
  
  public void onWorldRender(RenderEvent event) {}
  
  public boolean isEnabled() {
    return this.enabled;
  }
  
  public void setEnabled(boolean e) {
    this.enabled = e;
  }
  
  public void enable() {
    setEnabled(true);
    onEnable();
  }
  
  public void disable() {
    setEnabled(false);
    onDisable();
  }
  
  public void toggle() {
    if (isEnabled()) {
      disable();
    } else if (!isEnabled()) {
      enable();
    } 
  }
  
  public String getHudInfo() {
    return "";
  }
  
  public void setup() {}
  
  public boolean isDrawn() {
    return this.drawn;
  }
  
  public void setDrawn(boolean d) {
    this.drawn = d;
  }
  
  protected Setting.Integer registerInteger(String name, String configname, int value, int min, int max) {
    Setting.Integer s = new Setting.Integer(name, configname, this, getCategory(), value, min, max);
    (AffinityPlus.getInstance()).settingsManager.addSetting((Setting)s);
    return s;
  }
  
  protected Setting.Double registerDouble(String name, String configname, double value, double min, double max) {
    Setting.Double s = new Setting.Double(name, configname, this, getCategory(), value, min, max);
    (AffinityPlus.getInstance()).settingsManager.addSetting((Setting)s);
    return s;
  }
  
  protected Setting.Boolean registerBoolean(String name, String configname, boolean value) {
    Setting.Boolean s = new Setting.Boolean(name, configname, this, getCategory(), value);
    (AffinityPlus.getInstance()).settingsManager.addSetting((Setting)s);
    return s;
  }
  
  protected Setting.Mode registerMode(String name, String configname, List<String> modes, String value) {
    Setting.Mode s = new Setting.Mode(name, configname, this, getCategory(), modes, value);
    (AffinityPlus.getInstance()).settingsManager.addSetting((Setting)s);
    return s;
  }
  
  protected Setting.ColorSetting registerColor(String name, String configname, APColor color) {
    Setting.ColorSetting s = new Setting.ColorSetting(name, configname, this, getCategory(), false, color);
    (AffinityPlus.getInstance()).settingsManager.addSetting((Setting)s);
    return s;
  }
  
  protected Setting.ColorSetting registerColor(String name, String configname) {
    return registerColor(name, configname, new APColor(90, 145, 240));
  }
  
  public enum Category {
    Combat, Exploits, Movement, Misc, Render, HUD;
  }
}
