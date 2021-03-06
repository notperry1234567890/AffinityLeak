package com.gamesense.api;

import com.gamesense.api.util.config.SaveConfiguration;
import com.gamesense.client.AffinityPlus;

public class Stopper extends Thread {
  public static void saveConfig() {
    (AffinityPlus.getInstance()).saveModules.saveModules();
    SaveConfiguration.saveAutoGG();
    SaveConfiguration.saveAutoReply();
    SaveConfiguration.saveBinds();
    SaveConfiguration.saveDrawn();
    SaveConfiguration.saveEnabled();
    SaveConfiguration.saveEnemies();
    SaveConfiguration.saveFont();
    SaveConfiguration.saveFriends();
    SaveConfiguration.saveGUI();
    SaveConfiguration.saveMacros();
    SaveConfiguration.saveMessages();
    SaveConfiguration.savePrefix();
  }
  
  public void run() {
    saveConfig();
  }
}
