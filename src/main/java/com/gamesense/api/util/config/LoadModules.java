package com.gamesense.api.util.config;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class LoadModules {
  public LoadModules() {
    loadCategory(SaveConfiguration.Combat, Module.Category.Combat);
    loadCategory(SaveConfiguration.Exploits, Module.Category.Exploits);
    loadCategory(SaveConfiguration.Hud, Module.Category.HUD);
    loadCategory(SaveConfiguration.Misc, Module.Category.Misc);
    loadCategory(SaveConfiguration.Movement, Module.Category.Movement);
    loadCategory(SaveConfiguration.Render, Module.Category.Render);
  }
  
  private void loadCategory(File config, Module.Category category) {
    loadSettings(config, category, "Value.json", Setting.Type.INT);
    loadSettings(config, category, "Boolean.json", Setting.Type.BOOLEAN);
    loadSettings(config, category, "String.json", Setting.Type.MODE);
    loadSettings(config, category, "Color.json", Setting.Type.COLOR);
  }
  
  private void loadSettings(File config, Module.Category category, String filename, Setting.Type type) {
    try {
      File file = new File(config.getAbsolutePath(), filename);
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String configname = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        String m = curLine.split(":")[2];
        for (Module mm : ModuleManager.getModulesInCategory(category)) {
          if (mm != null && mm.getName().equalsIgnoreCase(m)) {
            Setting mod = (AffinityPlus.getInstance()).settingsManager.getSettingByNameAndMod(configname, mm);
            if (mod.getType() == type || (type == Setting.Type.INT && mod.getType() == Setting.Type.DOUBLE))
              switch (mod.getType()) {
                case INT:
                  ((Setting.Integer)mod).setValue(Integer.parseInt(isOn));
                case DOUBLE:
                  ((Setting.Double)mod).setValue(Double.parseDouble(isOn));
                case BOOLEAN:
                  ((Setting.Boolean)mod).setValue(Boolean.parseBoolean(isOn));
                case MODE:
                  ((Setting.Mode)mod).setValue(isOn);
                case COLOR:
                  ((Setting.ColorSetting)mod).fromInteger(Integer.parseInt(isOn));
              }  
          } 
        } 
      } 
      br.close();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
}
