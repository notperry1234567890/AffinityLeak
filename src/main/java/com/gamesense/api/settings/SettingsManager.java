package com.gamesense.api.settings;

import com.gamesense.client.module.Module;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SettingsManager {
  private final List<Setting> settings = new ArrayList<>();
  
  public List<Setting> getSettings() {
    return this.settings;
  }
  
  public void addSetting(Setting setting) {
    this.settings.add(setting);
  }
  
  public Setting getSettingByNameAndMod(String name, Module parent) {
    return this.settings.stream().filter(s -> s.getParent().equals(parent)).filter(s -> s.getConfigName().equalsIgnoreCase(name)).findFirst().orElse(null);
  }
  
  public Setting getSettingByNameAndModConfig(String configname, Module parent) {
    return this.settings.stream().filter(s -> s.getParent().equals(parent)).filter(s -> s.getConfigName().equalsIgnoreCase(configname)).findFirst().orElse(null);
  }
  
  public List<Setting> getSettingsForMod(Module parent) {
    return (List<Setting>)this.settings.stream().filter(s -> s.getParent().equals(parent)).collect(Collectors.toList());
  }
  
  public List<Setting> getSettingsByCategory(Module.Category category) {
    return (List<Setting>)this.settings.stream().filter(s -> s.getCategory().equals(category)).collect(Collectors.toList());
  }
  
  public Setting getSettingByName(String name) {
    for (Setting set : getSettings()) {
      if (set.getName().equalsIgnoreCase(name))
        return set; 
    } 
    System.err.println("[GameSense] Error Setting NOT found: '" + name + "'!");
    return null;
  }
}
