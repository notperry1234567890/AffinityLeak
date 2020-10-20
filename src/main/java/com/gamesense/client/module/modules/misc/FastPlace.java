//Deobfuscated with https://github.com/PetoPetko/Minecraft-Deobfuscator3000 using mappings "1.12 stable mappings"!

package com.gamesense.client.module.modules.misc;

import com.gamesense.api.settings.Setting;
import com.gamesense.client.module.Module;
import net.minecraft.init.Items;

public class FastPlace extends Module {
  Setting.Boolean exp;
  
  Setting.Boolean crystals;
  
  Setting.Boolean everything;
  
  public FastPlace() {
    super("FastPlace", Module.Category.Misc);
  }
  
  public void setup() {
    this.exp = registerBoolean("Exp", "Exp", false);
    this.crystals = registerBoolean("Crystals", "Crystals", false);
    this.everything = registerBoolean("Eveything", "Everything", false);
  }
  
  public void onUpdate() {
    if ((this.exp.getValue() && mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE) || mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE)
      mc.rightClickDelayTimer = 0; 
    if ((this.crystals.getValue() && mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL)
      mc.rightClickDelayTimer = 0; 
    if (this.everything.getValue())
      mc.rightClickDelayTimer = 0; 
    mc.playerController.blockHitDelay = 0;
  }
}
