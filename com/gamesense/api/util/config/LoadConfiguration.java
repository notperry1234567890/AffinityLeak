package com.gamesense.api.util.config;

import com.gamesense.api.players.enemy.Enemies;
import com.gamesense.api.players.friends.Friends;
import com.gamesense.api.util.font.CFontRenderer;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.clickgui.ClickGUI;
import com.gamesense.client.clickgui.frame.Frames;
import com.gamesense.client.command.Command;
import com.gamesense.client.macro.Macro;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.gamesense.client.module.modules.misc.AutoGG;
import com.gamesense.client.module.modules.misc.AutoReply;
import com.mojang.realmsclient.gui.ChatFormatting;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;

public class LoadConfiguration {
  public LoadConfiguration() {
    loadAutoGG();
    loadAutoReply();
    loadBinds();
    loadDrawn();
    loadEnabled();
    loadEnemies();
    loadFont();
    loadFriends();
    loadGUI();
    loadMacros();
    loadMessages();
    loadPrefix();
  }
  
  public void loadGUI() {
    try {
      File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ClickGUI.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String name = curLine.split(":")[0];
        String x = curLine.split(":")[1];
        String y = curLine.split(":")[2];
        String e = curLine.split(":")[3];
        int x1 = Integer.parseInt(x);
        int y1 = Integer.parseInt(y);
        boolean open = Boolean.parseBoolean(e);
        Frames frames = ClickGUI.getFrameByName(name);
        if (frames != null) {
          frames.x = x1;
          frames.y = y1;
          frames.open = open;
        } 
      } 
      br.close();
    } catch (Exception var6) {
      var6.printStackTrace();
      SaveConfiguration.saveGUI();
    } 
  }
  
  public void loadMacros() {
    try {
      File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ClientMacros.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String bind = curLine.split(":")[0];
        String value = curLine.split(":")[1];
        (AffinityPlus.getInstance()).macroManager.addMacro(new Macro(Keyboard.getKeyIndex(bind), value.replace("_", " ")));
      } 
      br.close();
    } catch (Exception var6) {
      var6.printStackTrace();
      SaveConfiguration.saveMacros();
    } 
  }
  
  public void loadFriends() {
    try {
      File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "Friends.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      Friends.friends.clear();
      String line;
      while ((line = br.readLine()) != null)
        (AffinityPlus.getInstance()).friends.addFriend(line); 
      br.close();
    } catch (Exception var6) {
      var6.printStackTrace();
      SaveConfiguration.saveFriends();
    } 
  }
  
  public void loadEnemies() {
    try {
      File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "Enemies.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      Enemies.enemies.clear();
      String line;
      while ((line = br.readLine()) != null)
        Enemies.addEnemy(line); 
      br.close();
    } catch (Exception var6) {
      var6.printStackTrace();
      SaveConfiguration.saveEnemies();
    } 
  }
  
  public void loadPrefix() {
    try {
      File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "CommandPrefix.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null)
        Command.setPrefix(line); 
      br.close();
    } catch (Exception var6) {
      var6.printStackTrace();
      SaveConfiguration.savePrefix();
    } 
  }
  
  public void loadFont() {
    try {
      File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "CustomFont.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String name = line.split(":")[0];
        String size = line.split(":")[1];
        int sizeInt = Integer.parseInt(size);
        AffinityPlus.fontRenderer = new CFontRenderer(new Font(name, 0, sizeInt), true, false);
        AffinityPlus.fontRenderer.setFont(new Font(name, 0, sizeInt));
        AffinityPlus.fontRenderer.setAntiAlias(true);
        AffinityPlus.fontRenderer.setFractionalMetrics(false);
        AffinityPlus.fontRenderer.setFontName(name);
        AffinityPlus.fontRenderer.setFontSize(sizeInt);
      } 
      br.close();
    } catch (Exception var6) {
      var6.printStackTrace();
      SaveConfiguration.saveFont();
    } 
  }
  
  public void loadAutoGG() {
    try {
      File file = new File(SaveConfiguration.Messages.getAbsolutePath(), "AutoGG.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null)
        AutoGG.addAutoGgMessage(line); 
      br.close();
    } catch (Exception var6) {
      var6.printStackTrace();
      SaveConfiguration.saveAutoGG();
    } 
  }
  
  public void loadAutoReply() {
    try {
      File file = new File(SaveConfiguration.Messages.getAbsolutePath(), "AutoReply.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null)
        AutoReply.setReply(line); 
      br.close();
    } catch (Exception var6) {
      var6.printStackTrace();
      SaveConfiguration.saveAutoReply();
    } 
  }
  
  public void loadMessages() {
    try {
      File file = new File(SaveConfiguration.Messages.getAbsolutePath(), "ClientMessages.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String watermark = curLine.split(",")[0];
        String color = curLine.split(",")[1];
        boolean w = Boolean.parseBoolean(watermark);
        ChatFormatting c = ChatFormatting.getByName(color);
        Command.cf = c;
        Command.MsgWaterMark = w;
      } 
      br.close();
    } catch (Exception var6) {
      var6.printStackTrace();
      SaveConfiguration.saveMessages();
    } 
  }
  
  public void loadDrawn() {
    try {
      File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "DrawnModules.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String name = curLine.split(":")[0];
        String isOn = curLine.split(":")[1];
        boolean drawn = Boolean.parseBoolean(isOn);
        for (Module m : ModuleManager.getModules()) {
          if (m.getName().equalsIgnoreCase(name))
            m.setDrawn(drawn); 
        } 
      } 
      br.close();
    } catch (Exception var6) {
      var6.printStackTrace();
      SaveConfiguration.saveDrawn();
    } 
  }
  
  public void loadEnabled() {
    try {
      File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "EnabledModules.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        Iterator<Module> var6 = ModuleManager.getModules().iterator();
        while (var6.hasNext()) {
          Module m = var6.next();
          if (m.getName().equals(line))
            m.enable(); 
        } 
      } 
      br.close();
    } catch (Exception var6) {
      var6.printStackTrace();
      SaveConfiguration.saveEnabled();
    } 
  }
  
  public void loadBinds() {
    try {
      File file = new File(SaveConfiguration.Miscellaneous.getAbsolutePath(), "ModuleBinds.json");
      FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
      DataInputStream in = new DataInputStream(fstream);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      String line;
      while ((line = br.readLine()) != null) {
        String curLine = line.trim();
        String name = curLine.split(":")[0];
        String bind = curLine.split(":")[1];
        for (Module m : ModuleManager.getModules()) {
          if (m != null && m.getName().equalsIgnoreCase(name))
            m.setBind(Keyboard.getKeyIndex(bind)); 
        } 
      } 
      br.close();
    } catch (Exception var6) {
      var6.printStackTrace();
      SaveConfiguration.saveBinds();
    } 
  }
}
