//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.api.util.config;

import com.gamesense.api.players.enemy.Enemies;
import com.gamesense.api.players.enemy.Enemy;
import com.gamesense.api.players.friends.Friend;
import com.gamesense.api.players.friends.Friends;
import com.gamesense.client.AffinityPlus;
import com.gamesense.client.clickgui.ClickGUI;
import com.gamesense.client.clickgui.frame.Frames;
import com.gamesense.client.command.Command;
import com.gamesense.client.macro.Macro;
import com.gamesense.client.module.Module;
import com.gamesense.client.module.ModuleManager;
import com.gamesense.client.module.modules.misc.AutoGG;
import com.gamesense.client.module.modules.misc.AutoReply;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class SaveConfiguration {
  public static File GameSenseDev;
  
  public static File Modules;
  
  public static File Messages;
  
  public static File Miscellaneous;
  
  public static File Combat;
  
  public static File Exploits;
  
  public static File Hud;
  
  public static File Misc;
  
  public static File Movement;
  
  public static File Render;
  
  Minecraft mc = Minecraft.getMinecraft();
  
  public SaveConfiguration() {
    GameSenseDev = new File(this.mc.gameDir + File.separator + "GameSense");
    if (!GameSenseDev.exists())
      GameSenseDev.mkdirs(); 
    Modules = new File(this.mc.gameDir + File.separator + "GameSense" + File.separator + "Modules");
    if (!Modules.exists())
      Modules.mkdirs(); 
    Messages = new File(this.mc.gameDir + File.separator + "GameSense" + File.separator + "Messages");
    if (!Messages.exists())
      Messages.mkdirs(); 
    Miscellaneous = new File(this.mc.gameDir + File.separator + "GameSense" + File.separator + "Miscellaneous");
    if (!Miscellaneous.exists())
      Miscellaneous.mkdirs(); 
    Combat = new File(this.mc.gameDir + File.separator + "GameSense" + File.separator + "Modules" + File.separator + "Combat");
    if (!Combat.exists())
      Combat.mkdirs(); 
    Exploits = new File(this.mc.gameDir + File.separator + "GameSense" + File.separator + "Modules" + File.separator + "Exploits");
    if (!Exploits.exists())
      Exploits.mkdirs(); 
    Hud = new File(this.mc.gameDir + File.separator + "GameSense" + File.separator + "Modules" + File.separator + "Hud");
    if (!Hud.exists())
      Hud.mkdirs(); 
    Misc = new File(this.mc.gameDir + File.separator + "GameSense" + File.separator + "Modules" + File.separator + "Misc");
    if (!Misc.exists())
      Misc.mkdirs(); 
    Movement = new File(this.mc.gameDir + File.separator + "GameSense" + File.separator + "Modules" + File.separator + "Movement");
    if (!Movement.exists())
      Movement.mkdirs(); 
    Render = new File(this.mc.gameDir + File.separator + "GameSense" + File.separator + "Modules" + File.separator + "Render");
    if (!Render.exists())
      Render.mkdirs(); 
  }
  
  public static void saveGUI() {
    try {
      File file = new File(Miscellaneous.getAbsolutePath(), "ClickGUI.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Frames> var3 = ClickGUI.frames.iterator();
      while (var3.hasNext()) {
        Frames frames = var3.next();
        out.write(frames.category + ":" + frames.getX() + ":" + frames.getY() + ":" + frames.isOpen());
        out.write("\r\n");
      } 
      out.close();
    } catch (Exception exception) {}
  }
  
  public static void saveMacros() {
    try {
      File file = new File(Miscellaneous.getAbsolutePath(), "ClientMacros.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Macro> var3 = (AffinityPlus.getInstance()).macroManager.getMacros().iterator();
      while (var3.hasNext()) {
        Macro m = var3.next();
        out.write(Keyboard.getKeyName(m.getKey()) + ":" + m.getValue().replace(" ", "_"));
        out.write("\r\n");
      } 
      out.close();
    } catch (Exception exception) {}
  }
  
  public static void saveFriends() {
    try {
      File file = new File(Miscellaneous.getAbsolutePath(), "Friends.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Friend> var3 = Friends.getFriends().iterator();
      while (var3.hasNext()) {
        Friend f = var3.next();
        out.write(f.getName());
        out.write("\r\n");
      } 
      out.close();
    } catch (Exception exception) {}
  }
  
  public static void saveEnemies() {
    try {
      File file = new File(Miscellaneous.getAbsolutePath(), "Enemies.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Enemy> var3 = Enemies.getEnemies().iterator();
      while (var3.hasNext()) {
        Enemy e = var3.next();
        out.write(e.getName());
        out.write("\r\n");
      } 
      out.close();
    } catch (Exception exception) {}
  }
  
  public static void savePrefix() {
    try {
      File file = new File(Miscellaneous.getAbsolutePath(), "CommandPrefix.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      out.write(Command.getPrefix());
      out.write("\r\n");
      out.close();
    } catch (Exception exception) {}
  }
  
  public static void saveFont() {
    try {
      File file = new File(Miscellaneous.getAbsolutePath(), "CustomFont.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      out.write(AffinityPlus.fontRenderer.getFontName() + ":" + AffinityPlus.fontRenderer.getFontSize());
      out.write("\r\n");
      out.close();
    } catch (Exception exception) {}
  }
  
  public static void saveAutoGG() {
    try {
      File file = new File(Messages.getAbsolutePath(), "AutoGG.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      for (String s : AutoGG.getAutoGgMessages()) {
        out.write(s);
        out.write("\r\n");
      } 
      out.close();
    } catch (Exception exception) {}
  }
  
  public static void saveAutoReply() {
    try {
      File file = new File(Messages.getAbsolutePath(), "AutoReply.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      out.write(AutoReply.getReply());
      out.close();
    } catch (Exception exception) {}
  }
  
  public static void saveMessages() {
    try {
      File file = new File(Messages.getAbsolutePath(), "ClientMessages.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      out.write(Command.MsgWaterMark + "");
      out.write(",");
      out.write(Command.cf.getName());
      out.close();
    } catch (Exception exception) {}
  }
  
  public static void saveDrawn() {
    try {
      File file = new File(Miscellaneous.getAbsolutePath(), "DrawnModules.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Module> var3 = ModuleManager.getModules().iterator();
      while (var3.hasNext()) {
        Module module = var3.next();
        out.write(module.getName() + ":" + module.isDrawn());
        out.write("\r\n");
      } 
      out.close();
    } catch (Exception exception) {}
  }
  
  public static void saveEnabled() {
    try {
      File file = new File(Miscellaneous.getAbsolutePath(), "EnabledModules.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Module> var3 = ModuleManager.getModules().iterator();
      while (var3.hasNext()) {
        Module module = var3.next();
        if (module.isEnabled()) {
          out.write(module.getName());
          out.write("\r\n");
        } 
      } 
      out.close();
    } catch (Exception exception) {}
  }
  
  public static void saveBinds() {
    try {
      File file = new File(Miscellaneous.getAbsolutePath(), "ModuleBinds.json");
      BufferedWriter out = new BufferedWriter(new FileWriter(file));
      Iterator<Module> var3 = ModuleManager.getModules().iterator();
      while (var3.hasNext()) {
        Module module = var3.next();
        out.write(module.getName() + ":" + Keyboard.getKeyName(module.getBind()));
        out.write("\r\n");
      } 
      out.close();
    } catch (Exception exception) {}
  }
}
