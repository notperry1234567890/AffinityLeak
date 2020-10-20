//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.render;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;
import java.util.ArrayList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class Fullbright extends Module {
  float old;
  
  Setting.Mode Mode;
  
  public Fullbright() {
    super("Fullbright", Module.Category.Render);
  }
  
  public void setup() {
    ArrayList<String> modes = new ArrayList<>();
    modes.add("Gamma");
    modes.add("Potion");
    this.Mode = registerMode("Mode", "Mode", modes, "Gamma");
  }
  
  public void onEnable() {
    this.old = mc.gameSettings.gammaSetting;
  }
  
  public void onUpdate() {
    if (this.Mode.getValue().equalsIgnoreCase("Gamma")) {
      mc.gameSettings.gammaSetting = 666.0F;
      mc.player.removePotionEffect(Potion.getPotionById(16));
    } else if (this.Mode.getValue().equalsIgnoreCase("Potion")) {
      PotionEffect potionEffect = new PotionEffect(Potion.getPotionById(16), 123456789, 5);
      potionEffect.setPotionDurationMax(true);
      mc.player.addPotionEffect(potionEffect);
    } 
  }
  
  public void onDisable() {
    mc.gameSettings.gammaSetting = this.old;
    mc.player.removePotionEffect(Potion.getPotionById(16));
  }
}
