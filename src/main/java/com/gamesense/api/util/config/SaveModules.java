package com.gamesense.api.util.config;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.module.Module;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;

public class SaveModules {
  public void saveModules() {
    saveCategory(SaveConfiguration.Combat, Module.Category.Combat);
    saveCategory(SaveConfiguration.Exploits, Module.Category.Exploits);
    saveCategory(SaveConfiguration.Hud, Module.Category.HUD);
    saveCategory(SaveConfiguration.Misc, Module.Category.Misc);
    saveCategory(SaveConfiguration.Movement, Module.Category.Movement);
    saveCategory(SaveConfiguration.Render, Module.Category.Render);
  }
  
  private void saveCategory(File config, Module.Category category) {
    saveSettings(config, category, "Value.json", Setting.Type.INT);
    saveSettings(config, category, "Boolean.json", Setting.Type.BOOLEAN);
    saveSettings(config, category, "String.json", Setting.Type.MODE);
    saveSettings(config, category, "Color.json", Setting.Type.COLOR);
  }
  
  private void saveSettings(File config, Module.Category category, String filename, Setting.Type type) {
    try {
      File file = new File(config.getAbsolutePath(), filename);
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Setting> iter = (AffinityPlus.getInstance()).settingsManager.getSettingsByCategory(category).iterator();
      while (iter.hasNext()) {
        Setting mod = iter.next();
        if (mod.getType() == type || (type == Setting.Type.INT && mod.getType() == Setting.Type.DOUBLE))
          switch (mod.getType()) {
            case INT:
              out.write(mod.getConfigName() + ":" + ((Setting.Integer)mod).getValue() + ":" + mod.getParent().getName() + "\r\n");
            case DOUBLE:
              out.write(mod.getConfigName() + ":" + ((Setting.Double)mod).getValue() + ":" + mod.getParent().getName() + "\r\n");
            case BOOLEAN:
              out.write(mod.getConfigName() + ":" + ((Setting.Boolean)mod).getValue() + ":" + mod.getParent().getName() + "\r\n");
            case MODE:
              out.write(mod.getConfigName() + ":" + ((Setting.Mode)mod).getValue() + ":" + mod.getParent().getName() + "\r\n");
            case COLOR:
              out.write(mod.getConfigName() + ":" + ((Setting.ColorSetting)mod).toInteger() + ":" + mod.getParent().getName() + "\r\n");
          }  
      } 
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
